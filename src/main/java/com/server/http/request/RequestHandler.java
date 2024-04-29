package com.server.http.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.http.enums.HTTP_METHOD;
import com.server.http.request.annotations.EndpointMapping;
import com.server.http.request.annotations.BodyParam;
import com.server.http.request.annotations.RequestParam;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import lombok.Data;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class RequestHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected static class JsonParser {
        public static <T> T parse(String json, Class<T> valueType) throws IOException {
            return objectMapper.readValue(json, valueType);
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
        return null;
    }

    public static AbstractResponse handlePostRequest(Socket socket, String uri, String requestBody)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки POST запроса
        return null;
    }

    public static AbstractResponse handlePutRequest(Socket socket, String uri, String requestBody)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки PUT запроса
        return null;
    }

    public static AbstractResponse handleDeleteRequest(Socket socket, String uri)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        // Логика обработки DELETE запроса
        return null;
    }
}
