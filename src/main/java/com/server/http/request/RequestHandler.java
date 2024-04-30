package com.server.http.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.http.enums.HTTP_METHOD;
import com.server.http.enums.HTTP_STATUS_CODE;
import com.server.http.response.AbstractResponse;
import lombok.Data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

@Data
public class RequestHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected static class JsonParser {
        public static <T> T parse(String json, String key, Class<T> valueType) throws IOException {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode valueNode = rootNode.get(key);
            if (valueNode != null) {
                return objectMapper.treeToValue(valueNode, valueType);
            } else {
                throw new IllegalArgumentException(STR."Key '\{key}' not found in JSON");
            }
        }
    }

    public static AbstractResponse handleRequest(Socket socket, String uri, HTTP_METHOD httpMethod, String requestBody)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {

        return switch (httpMethod) {
            case GET -> handleGetRequest(socket, uri);
            case POST -> handlePostRequest(socket, uri, requestBody);
            case PUT -> handlePutRequest(socket, uri, requestBody);
            case DELETE -> handleDeleteRequest(socket, uri);
        };
    }

    public static AbstractResponse handleGetRequest(Socket socket, String uri)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки GET запроса
        AbstractResponse handledRequest = EndpointMapper.handleRequest(uri, HTTP_METHOD.GET, null);
        if (handledRequest != null) {
            if (handledRequest.getStatusCode() == null)
                handledRequest.setStatusCode(HTTP_STATUS_CODE.OK_200);
            handledRequest.setSocket(socket);
        }
        return handledRequest;
    }

    public static AbstractResponse handlePostRequest(Socket socket, String uri, String requestBody)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки POST запроса
        AbstractResponse handledRequest = EndpointMapper.handleRequest(uri, HTTP_METHOD.POST, requestBody);
        if (handledRequest != null) {
            if (handledRequest.getStatusCode() == null)
                handledRequest.setStatusCode(HTTP_STATUS_CODE.OK_200);
            handledRequest.setSocket(socket);
        }
        return handledRequest;
    }

    public static AbstractResponse handlePutRequest(Socket socket, String uri, String requestBody)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки PUT запроса
        AbstractResponse handledRequest = EndpointMapper.handleRequest(uri, HTTP_METHOD.PUT, requestBody);
        if (handledRequest != null) {
            if (handledRequest.getStatusCode() == null)
                handledRequest.setStatusCode(HTTP_STATUS_CODE.OK_200);
            handledRequest.setSocket(socket);
        }
        return handledRequest;
    }

    public static AbstractResponse handleDeleteRequest(Socket socket, String uri)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки DELETE запроса
        AbstractResponse handledRequest = EndpointMapper.handleRequest(uri, HTTP_METHOD.DELETE, null);
        if (handledRequest != null) {
            if (handledRequest.getStatusCode() == null)
                handledRequest.setStatusCode(HTTP_STATUS_CODE.OK_200);
            handledRequest.setSocket(socket);
        }
        return handledRequest;
    }
}
