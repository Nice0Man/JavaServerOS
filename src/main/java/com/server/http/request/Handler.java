package com.server.http.request;

import com.server.http.enums.HTTP_METHOD;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import com.server.http.enums.HTTP_STATUS_CODE;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;


/**
 * Processes request on its in thread and returns a response to the client
 *
 * @author Nice0Man
 *
 */
public class Handler implements Runnable {
    private final Socket socket;
    private AbstractResponse response;
    private static final Logger logger = LogManager.getLogger(Handler.class);

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @SneakyThrows
    @Override
    public void run() {
        processRequest();
    }

    /**
     * Build the response header and body
     *
     */
    private void processRequest() throws IOException {
        String uri = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String line = in.readLine();
            System.out.println(line);
            if (line != null) {
                logger.info(line);
                String[] parts = line.split("\\s+");
                String requestMethod = parts[0];
                uri = parts[1];
                HTTP_METHOD httpMethod = HTTP_METHOD.getMethod(requestMethod);
                switch (httpMethod) {
                    case GET, POST, PUT, DELETE -> EndpointMapper.handleRequest(this.socket, uri, httpMethod);
                    default -> handleUnsupportedMethod();
                }
//                System.out.println(readRequestBody());
                socket.close();
            } else {
                logger.error("Received null request line");
            }
        } catch (NoSuchMethodException e) {
            logger.error("{}: (404) {}", uri, HTTP_STATUS_CODE.NOT_FOUND_404);
//            response = new Html(this.socket, HTTP_STATUS_CODE.NOT_FOUND_404);
        } catch (IllegalAccessException | InvocationTargetException | RuntimeException e) {
            logger.error("Error processing request: {}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Handles unsupported HTTP methods
     */
    private void handleUnsupportedMethod() {
        logger.error("Unsupported HTTP method");
        try {
            response = sendErrorResponse();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private AbstractResponse sendErrorResponse() throws IOException {
        return new Html(socket, HTTP_STATUS_CODE.METHOD_NOT_ALLOWED_405);
    }

    private String readRequestBody() throws IOException {
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        boolean headerPassed = false;

        // Пропускаем заголовки запроса
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.isEmpty()) {
                headerPassed = true;
                break;
            }
        }

        // Считываем тело запроса
        if (headerPassed) {
            while (reader.ready()) { // Проверяем доступность данных для чтения
                line = reader.readLine();
                System.out.println(STR."Body line: \{line}");
                if (line != null) {
                    requestBody.append(line);
                } else {
                    break; // Выход из цикла, если достигнут конец потока
                }
            }
        }
        return requestBody.toString();
    }
}
