package server;

import lombok.Data;
import server.models.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

@Data
public class RequestHandler {
    private static final String CRLF = "\r\n";

//    public RequestHandler(Socket socket, ProcessingServer server) throws IOException {
//        this.socket = socket;
//        this.reader = new BufferedReadeXr(new InputStreamReader(socket.getInputStream()));
//        this.outputStream = socket.getOutputStream();
//        this.server = server;
//    }

    public void handleRequest(BufferedReader reader, OutputStream outputStream, Socket socket) {
        try {
            String requestLine = reader.readLine();
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // Read request headers
            StringBuffer headerBuilder = new StringBuffer();
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                headerBuilder.append(headerLine).append(CRLF);
            }
            Map<HttpHeader, String> headers = HttpHeader.parseHeaders(headerBuilder.toString().split(CRLF));

            // Process request based on the HTTP method
            switch (method) {
                case "GET":
//                    server.handleGetRequest(path, outputStream);
                    break;
                case "POST":
//                    server.handlePostRequest(path, reader, outputStream);
                    break;
                case "PUT":
//                    server.handlePutRequest(path, reader, outputStream);
                    break;
                case "DELETE":
//                    server.handleDeleteRequest(path, reader, outputStream);
                    break;
                default:
                    sendResponse(405, "Method Not Allowed", "text/plain", "Unsupported method: " + method);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendResponse(int statusCode, String statusText, String contentType, String responseBody) throws IOException {
        String response = HttpHeader.HTTP_VERSION.createHeader(statusCode + " " + statusText) + CRLF +
                HttpHeader.CONTENT_TYPE.createHeader(contentType) + CRLF +
                HttpHeader.CONTENT_LENGTH.createHeader(String.valueOf(responseBody.getBytes().length)) + CRLF +
                HttpHeader.CONNECTION.createHeader("close") + CRLF +
                CRLF +
                responseBody;
    }
}

