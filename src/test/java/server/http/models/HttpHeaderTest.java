package server.http.models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpHeaderTest {

    @Test
    void testFullHeader() {
        // Arrange
        HttpHeader.Builder builder = new HttpHeader.Builder();
        HttpHeader httpHeader = builder
                .addHTTP("200 OK")
                .addRow("Date", "Sun, 10 Apr 2022 12:00:00 GMT")
                .addRow("Server", "Apache/2.4.29 (Ubuntu)")
                .addRow("Last-Modified", "Mon, 01 Jan 2022 12:00:00 GMT")
                .addRow("Content-Type", "text/html")
                .addRow("Content-Length", "127")
                .build();
        // Assert
        ArrayList<String> headers = httpHeader.getHeaders();

        StringBuilder headerString = new StringBuilder();
        headers.forEach(headerString::append);
        String expectedOutput = headerString.toString();
        String actualOutput = "HTTP/1.1 200 OK\r\n" +
                "Date: Sun, 10 Apr 2022 12:00:00 GMT\r\n" +
                "Server: Apache/2.4.29 (Ubuntu)\r\n" +
                "Last-Modified: Mon, 01 Jan 2022 12:00:00 GMT\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 127\r\n";
        assertEquals(expectedOutput, actualOutput);
    }
}
