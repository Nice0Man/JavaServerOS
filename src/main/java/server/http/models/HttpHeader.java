package server.http.models;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum HttpHeader {
    HTTP_VERSION("HTTP/1.1 "),
    SERVER("Server: "),
    CONTENT_TYPE("Content-Type: "),
    CONTENT_LENGTH("Content-Length: "),
    CONNECTION("Connection: ");

    private final String name;

    HttpHeader(String name) {
        this.name = name;
    }

    public String createHeader(String value) {
        return name + value;
    }

    public static Map<HttpHeader, String> parseHeaders(String[] headerLines) {
        Map<HttpHeader, String> headers = new HashMap<>();
        for (String headerLine : headerLines) {
            String[] parts = headerLine.split(": ", 2);
            if (parts.length == 2) {
                for (HttpHeader header : values()) {
                    if (header.getName().equalsIgnoreCase(parts[0])) {
                        headers.put(header, parts[1]);
                        break;
                    }
                }
            }
        }
        return headers;
    }
}
