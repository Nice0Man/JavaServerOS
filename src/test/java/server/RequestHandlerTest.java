package server;

import org.junit.jupiter.api.Test;
import server.http.status.HttpStatusCode;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

    @Test
    void sendResponse() throws IOException {
        // Create a ByteArrayOutputStream to capture the response
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create a mock Socket with ByteArrayInputStream for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream("GET /test HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
        MockSocket socket = new MockSocket(inputStream, outputStream);

        // Create a RequestHandler instance
        RequestHandler requestHandler = new RequestHandler(socket);

        // Call sendResponse
        requestHandler.sendResponse(HttpStatusCode.OK_200, "text/plain", "Test response");

        // Check the output
        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("JavaServer"));
        assertTrue(response.contains("Content-Type: text/plain"));
        assertTrue(response.contains("Content-Length: 13")); // Length of "Test response"
        assertTrue(response.contains("Test response"));
    }

    @Test
    void testSendResponse() {
        // Implement test logic for testSendResponse method
    }

    // Helper class for mock Socket
    private static class MockSocket extends Socket {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public MockSocket(InputStream inputStream, OutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return outputStream;
        }
    }
}