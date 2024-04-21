package server;

import lombok.Data;
import lombok.Setter;
import server.http.status.HTTP_STATUS_CODE;
import server.logger.Logger;
import server.http.models.HttpHeader;

import java.io.*;
import java.net.Socket;
import java.util.Map;

@Data
public class RequestHandler {
/*    private static final String CRLF = "\r\n";
    @Setter
    private Logger LOGGER;

    private final BufferedReader reader;
    private final OutputStream outputStream;
    private InputStream inputStream;
    private final Socket socket;


    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public void handleRequest() throws Throwable {
        try {
            String requestLine = reader.readLine();
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // Read request headers
            StringBuilder headerBuilder = new StringBuilder();
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                headerBuilder.append(headerLine).append(CRLF);
            }
            Map<HttpHeader, String> headers = HttpHeader.parseHeaders(headerBuilder.toString().split(CRLF));

            // Process request based on the HTTP method
            switch (method) {
                case "GET":
                    System.out.println("Method: " + method);
                    handleGetMethod(path, headers);
                    break;
                case "POST":
                    System.out.println("Method: " + method);
                    handlePostMethod(path, reader, headers);
                    break;
                case "PUT":
                    System.out.println("Method: " + method);
                    handlePutMethod(path, reader, headers);
                    break;
                case "DELETE":
                    System.out.println("Method: " + method);
                    handleDeleteMethod(path, reader, headers);
                    break;
                default:
                    System.err.println("Method: " + method + " not allowed!");
                    handleError(HTTP_STATUS_CODE.METHOD_NOT_ALLOWED_405, "text/plain", "Unsupported method: " + method);
                    break; // Added missing break statement
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetMethod(String path, Map<HttpHeader, String> headers) throws IOException {
        // Placeholder for handling GET requests
        sendResponse(HTTP_STATUS_CODE.OK_200, "text/html", "<html><body><h1>Hello from JavaServer!</h1></body></html>");
    }

    private void handlePostMethod(String path, BufferedReader reader, Map<HttpHeader, String> headers) throws IOException {
        // Placeholder for handling POST requests
        sendResponse(HTTP_STATUS_CODE.OK_200, "text/plain", "POST request received for path: " + path);
    }

    private void handlePutMethod(String path, BufferedReader reader, Map<HttpHeader, String> headers) throws IOException {
        // Placeholder for handling PUT requests
        sendResponse(HTTP_STATUS_CODE.OK_200, "text/plain", "PUT request received for path: " + path);
    }

    private void handleDeleteMethod(String path, BufferedReader reader, Map<HttpHeader, String> headers) throws IOException {
        // Placeholder for handling DELETE requests
        sendResponse(HTTP_STATUS_CODE.OK_200, "text/plain", "DELETE request received for path: " + path);
    }

    private void handleError(HTTP_STATUS_CODE statusCode, String contentType, String responseBody) throws IOException {
        sendResponse(statusCode, contentType, responseBody);
    }

    protected void sendResponse(HTTP_STATUS_CODE statusCode, String responseBody) throws IOException{
            String contentType = "text/html";
            sendResponse(statusCode,contentType, responseBody);
        }

    public void sendResponse(HTTP_STATUS_CODE statusCode, String contentType, String responseBody) throws IOException {
        String response = HttpHeader.HTTP_VERSION.createHeader(statusCode.getCode() + " " + statusCode.getText()) + CRLF +
                HttpHeader.SERVER.createHeader("JavaServer/2024") + CRLF +
                HttpHeader.CONTENT_TYPE.createHeader(contentType) + CRLF +
                HttpHeader.CONTENT_LENGTH.createHeader(String.valueOf(responseBody.length())) + CRLF +
                HttpHeader.CONNECTION.createHeader("close") + CRLF +
                CRLF +
                responseBody;
        outputStream.write(response.getBytes());
        outputStream.flush();
    }*/
}

