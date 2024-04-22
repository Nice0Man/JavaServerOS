package com.server.http.request;

import com.server.http.request.annotations.EndpointMapping;
import com.server.http.models.HTTP_METHOD;
import com.server.http.request.annotations.RequestParam;
import com.server.http.request.annotations.Template;
import com.server.http.response.Response;
import lombok.Data;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static Response handleRequest(Socket socket, String requestUri, HTTP_METHOD httpMethod) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = findMatchingMethod(requestUri, httpMethod);
        if (method != null) {
            Object[] args = extractArgsFromUri(requestUri, method);
            Response response = (Response)method.invoke(null, args);
            return handleResponse(socket, method, response);
        } else {
            throw new NoSuchMethodException(STR."Endpoint not found for request URI: \{requestUri}");
        }
    }

    private static Response handleResponse(Socket socket, Method method, Response response) {
        Template annotation = method.getAnnotation(Template.class);
        if (annotation != null){
            String filePath = annotation.path();
            File file = new File(STR."src/main/resources/template/\{filePath}");
            if (file.exists()) {
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                InputStream i = null;
                try {
                    i = new FileInputStream(file);
                    int offset = 0;
                    while (offset < length) {
                        int count = i.read(bytes, offset, (length - offset));
                        offset += count;
                    }
                    i.close();
                    return new Response(response.getStatusCode(), socket, bytes, true);
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
        return new Response(response.getStatusCode(), socket, response.getOutput(),false);
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
