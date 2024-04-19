package server.models;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum HttpHeader {
    HTTP_VERSION("HTTP/1.1"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CONNECTION("Connection");

    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    // Helper method to create HTTP header string with given value
    public String createHeader(String value) {
        return this.value + ": " + value;
    }

    // Helper method to parse HTTP header from header line
    public static Map<HttpHeader, String> parseHeaders(String[] headerLines) {
        Map<HttpHeader, String> headers = new HashMap<>();
        for (String headerLine : headerLines) {
            String[] parts = headerLine.split(": ", 2);
            if (parts.length == 2) {
                for (HttpHeader header : values()) {
                    if (header.getValue().equalsIgnoreCase(parts[0])) {
                        headers.put(header, parts[1]);
                        break;
                    }
                }
            }
        }
        return headers;
    }
}

