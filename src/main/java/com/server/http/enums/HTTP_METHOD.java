package com.server.http.enums;

public enum HTTP_METHOD {
    GET,
    POST,
    PUT,
    DELETE;

    public static HTTP_METHOD getMethod(String requestMethod) {
        for (HTTP_METHOD method : values()) {
            if (method.name().equalsIgnoreCase(requestMethod)) {
                return method;
            }
        }
        throw new RuntimeException(STR."Requested method: \{requestMethod} doesn't exist!");
    }
}
