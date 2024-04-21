package server.http.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class HttpHeader {
    private final ArrayList<String> headers;
    private static final String CRLF = "\r\n";
    private HttpHeader(ArrayList<String> headers) {
        this.headers = headers;
    }

    // Getter method to retrieve a header value by key
    public String getHeader(int index) {
        return headers.get(index);
    }

    // Builder class for HttpHeader
    public static class Builder {
        private final ArrayList<String> headers;

        public Builder() {
            this.headers = new ArrayList<>();
        }

        // Method to add a header
        public Builder addRow(String key, String value) {
            headers.add(key + ": " + value + CRLF);
            return this;
        }
        public Builder addHTTP(String response){
            headers.add("HTTP/1.1 " + response + CRLF);
            return this;
        }
        // Method to add multiple headers
        public Builder addHeaders(ArrayList<String> headers) {
            this.headers.addAll(headers);
            return this;
        }

        // Method to build the HttpHeader instance
        public HttpHeader build() {
            return new HttpHeader(headers);
        }
    }
}
