package com.server.http.models;

import lombok.Getter;

@Getter
public enum RequestType {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String text;

    RequestType(String text) {
        this.text = text;
    }
}
