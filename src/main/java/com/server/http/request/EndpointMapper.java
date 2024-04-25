package com.server.http.request;

import com.server.http.enums.HTTP_STATUS_CODE;
import com.server.http.request.annotations.EndpointMapping;
import com.server.http.enums.HTTP_METHOD;
import com.server.http.request.annotations.RequestParam;
import com.server.http.request.annotations.Template;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import com.server.http.response.Json;
import lombok.Data;
import lombok.Setter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Data
public class EndpointMapper {
    private Socket socket;
    private static volatile EndpointMapper instance;
    private DataOutputStream dataOutputStream;
    private static Map<String, Method> endpointMap = new HashMap<>();

    private EndpointMapper() {
        endpointMap = new HashMap<>();
    }

    public static EndpointMapper getInstance() {
        if (instance == null) {
            synchronized (EndpointMapper.class) {
                if (instance == null) {
                    instance = new EndpointMapper();
                }
            }
        }
        return instance;
    }

    public static void mapEndpoints(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            EndpointMapping methodAnnotation = method.getAnnotation(EndpointMapping.class);
            if (methodAnnotation != null) {
                String uri = methodAnnotation.uri();
                HTTP_METHOD httpMethod = methodAnnotation.method();
                String regex = convertUriToRegex(uri);
                endpointMap.put(STR."\{httpMethod.name()} \{regex}", method);
            }
        }
    }

    private static void setResponseParameters(AbstractResponse response, Socket socket){
        response.setSocket(socket);
        response.setStatusCode(HTTP_STATUS_CODE.OK_200);
    }

    public static void handleRequest(Socket socket, String requestUri, HTTP_METHOD httpMethod)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        Method method = findMatchingMethod(requestUri, httpMethod);
        if (method != null) {
            Object[] args = extractArgsFromUri(requestUri, method);
            AbstractResponse response = (AbstractResponse) method.invoke(null, args);
            if (response != null) {
                setResponseParameters(response, socket);
                EndpointMapping annotation = method.getAnnotation(EndpointMapping.class);
                switch (annotation.type()){
                    case HTML -> {
                        Template templateAnnotation = method.getAnnotation(Template.class);
                        Html htmlResponse = new Html((Html) response);
                        handleHtmlResponse(htmlResponse, templateAnnotation.path());
                        socket.close();
                    }
                    case JSON -> {
                        Json jsonResponse = new Json((Json) response);
                        handleJsonResponse(jsonResponse);
                        socket.close();
                    }
                    default -> {
                        return;
                    }
                }
            }
        } else {
            throw new NoSuchMethodException(STR."Endpoint not found for request URI: \{requestUri}");
        }
    }

    private static void handleJsonResponse(Json response) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(response.getSocket().getOutputStream());
        try {
            response.sendResponse(outputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void handleHtmlResponse(Html response, String filePath) throws IOException {
        File file = new File(STR."src/main/resources/template/\{filePath}");
        DataOutputStream outputStream = new DataOutputStream(response.getSocket().getOutputStream());
        if (file.exists()) {
            try {
                byte[] bytes = readFileBytes(file);
                response.sendByteResponse(outputStream, bytes);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        } else {
           response.sendError(outputStream, HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR_500);
        }
    }

    /**
     * Читает содержимое файла и возвращает его в виде массива байтов
     */
    private static byte[] readFileBytes(File file) throws IOException {
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        try (InputStream inputStream = new FileInputStream(file)) {
            int offset = 0;
            while (offset < length) {
                int count = inputStream.read(bytes, offset, (length - offset));
                if (count >= 0) {
                    offset += count;
                } else {
                    throw new IOException("Unexpected end of file");
                }
            }
        }
        return bytes;
    }

    private static Method findMatchingMethod(String requestUri, HTTP_METHOD httpMethod) throws NoSuchMethodException {
        for (Map.Entry<String, Method> entry : endpointMap.entrySet()) {
            String endpointRegex = entry.getKey();
            Method method = entry.getValue();
            if (matchesEndpoint(requestUri, endpointRegex, httpMethod)) {
                return method;
            }
        }
        return null;
    }

    private static boolean matchesEndpoint(String requestUri, String endpointRegex, HTTP_METHOD httpMethod) {
        String[] parts = endpointRegex.split("\\s+");
        if (parts.length < 2) {
            return false;
        }
        String methodPart = parts[0];
        String regexPart = parts[1];

        if (!httpMethod.name().equalsIgnoreCase(methodPart)) {
            return false;
        }

        Pattern pattern = Pattern.compile(regexPart);
        Matcher matcher = pattern.matcher(requestUri);
        return matcher.matches();
    }

    private static String convertUriToRegex(String uri) {
        // Escape special characters in URI and replace parameters with regex patterns
        return uri.replaceAll("\\{([^}]*)}", "(\\\\w+|\\\\d+)");
    }

    private static Object[] extractArgsFromUri(String requestUri, Method method) {
        Parameter[] parameters = method.getParameters();
        List<Object> args = new ArrayList<>();
        String[] uriParts = requestUri.split("/");
        String[] mappedUriParts = method.getAnnotation(EndpointMapping.class).uri().split("/");
        for (Parameter parameter : parameters) {
            RequestParam paramAnnotation = parameter.getAnnotation(RequestParam.class);
            if (paramAnnotation != null) {
                String paramName = paramAnnotation.value();
                for (int j = 0; j < mappedUriParts.length; j++) {
                    if (mappedUriParts[j].startsWith("{") && mappedUriParts[j].endsWith("}")) {
                        String mappedParamName = mappedUriParts[j].substring(1, mappedUriParts[j].length() - 1);
                        if (mappedParamName.equals(paramName)) {
                            args.add(uriParts[j]);
                            break;
                        }
                    }
                }
            }
        }
        return args.toArray();
    }
}

