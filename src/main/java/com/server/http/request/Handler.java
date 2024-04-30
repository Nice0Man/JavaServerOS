package com.server.http.request;

import com.server.http.enums.HTTP_METHOD;
import com.server.http.enums.HTTP_STATUS_CODE;
import com.server.http.request.annotations.Template;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import com.server.http.response.Json;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Handler implements Runnable {
    private final Socket socket;
    private static final Logger logger = LogManager.getLogger(Handler.class);

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage());
            handleServerError(socket);
        }
    }

    private void processRequest() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        String line = in.readLine();
        if (line != null) {
            logger.info(line);
            String[] parts = line.split("\\s+");
            String requestMethod = parts[0];
            String uri = parts[1];
            HTTP_METHOD httpMethod = HTTP_METHOD.getMethod(requestMethod);
            String requestBody = "";
            if (httpMethod == HTTP_METHOD.POST || httpMethod == HTTP_METHOD.PUT) {
                requestBody = readRequestBody(in);
                // Process request body as needed...
                System.out.println(STR."Request body: \{requestBody}");
            }
            AbstractResponse response = RequestHandler.handleRequest(socket, uri, httpMethod, requestBody);
            handleResponse(response);
        } else {
            logger.error("Received null request line");
        }
    }

    private static void handleResponse(AbstractResponse response) throws IOException {
        if (response instanceof Html) {
            if (response.getBytes() != null)
                handleHtmlResponse((Html) response);
            else {
                String errorString = "<p>TEMPLATE FILE NOT FOUND!</p>";
                response.setBytes(errorString.getBytes());
                response.setStatusCode(HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR_500);
                handleHtmlResponse((Html) response);
            }
        } else if (response instanceof Json) {
            handleJsonResponse((Json) response);
        }
    }

    private static void handleJsonResponse(Json response) throws IOException {
        response.send();
        response.closeSocket();
    }

    private static void handleHtmlResponse(Html response) throws IOException {
        response.send();
        response.closeSocket();
    }

    private String readRequestBody(BufferedReader reader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        boolean headerPassed = false;
        int contentLength = -1; // По умолчанию не задано

        // Пропускаем заголовки запроса и ищем параметр Content-Length
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                headerPassed = true;
                break;
            }
            // Если строка содержит Content-Length, извлекаем его значение
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        // Считываем тело запроса с учетом Content-Length
        if (headerPassed && contentLength > 0) {
            int bytesRead = 0;
            char[] buffer = new char[1024]; // Буфер для чтения байтов
            while (bytesRead < contentLength) {
                int bytesToRead = Math.min(contentLength - bytesRead, buffer.length);
                int bytesReadNow = reader.read(buffer, 0, bytesToRead);
                if (bytesReadNow == -1) {
                    break; // Если достигнут конец потока
                }
                String bodyLine = new String(buffer, 0, bytesReadNow);
                requestBody.append(bodyLine);
                bytesRead += bytesReadNow;
            }
        }
        return requestBody.toString();
    }

    private void handleServerError(Socket socket) throws IOException {
        sendErrorResponse(socket, HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR_500);
    }

    private void sendErrorResponse(Socket socket, HTTP_STATUS_CODE httpStatusCode) throws IOException {
        AbstractResponse errorResponse = new Html(socket, httpStatusCode);
        errorResponse.send();
        errorResponse.closeSocket();
    }
}
