package com.server.http.request;

import com.server.http.enums.HTTP_METHOD;
import com.server.http.enums.HTTP_STATUS_CODE;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        processRequest();
    }

    private void processRequest() {
        String uri = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String line = in.readLine();
            if (line != null) {
                logger.info(line);
                String[] parts = line.split("\\s+");
                String requestMethod = parts[0];
                uri = parts[1];
                HTTP_METHOD httpMethod = HTTP_METHOD.getMethod(requestMethod);
                String requestBody = "";
                if (httpMethod == HTTP_METHOD.POST || httpMethod == HTTP_METHOD.PUT) {
                    requestBody = readRequestBody(in);
                    // Process request body as needed...
                    System.out.println(STR."Request body: \{requestBody}");
                }
                AbstractResponse response = RequestHandler.handleRequest(socket, uri, httpMethod, requestBody);
                response.send();
                socket.close();
            } else {
                logger.error("Received null request line");
            }
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error processing request for URI {}: {}", uri, e.getMessage());
            try {
                sendErrorResponse(socket);
            } catch (IOException ioException) {
                logger.error("Error sending error response: {}", ioException.getMessage());
            }
        }
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

    private void sendErrorResponse(Socket socket) throws IOException {
        AbstractResponse errorResponse = new Html(socket, HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR_500);
        errorResponse.send();
        socket.close();
    }
}
