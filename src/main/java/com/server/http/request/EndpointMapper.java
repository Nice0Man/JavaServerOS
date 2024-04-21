package com.server.http.request;

import com.server.http.request.annotations.EndpointMapping;
import com.server.http.request.annotations.HTTP_METHOD;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EndpointMapper {
    private static final Map<String, Method> endpointMap = new HashMap<>();

    public static void mapEndpoints(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            EndpointMapping annotation = method.getAnnotation(EndpointMapping.class);
            if (annotation != null) {
                String uri = annotation.uri();
                HTTP_METHOD httpMethod = annotation.method();
                endpointMap.put(httpMethod.name() + " " + uri, method);
            }
        }
    }

    public static void handleRequest(String requestUri, HTTP_METHOD httpMethod) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String key = httpMethod.name() + " " + requestUri;
        Method method = endpointMap.get(key);
        if (method != null) {
            method.invoke(null);
        } else {
            throw new NoSuchMethodException("Endpoint not found: " + key);
        }
    }
}
