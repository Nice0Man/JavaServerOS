package com.server.http.request;

import com.server.http.enums.HTTP_METHOD;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import com.server.http.enums.HTTP_STATUS_CODE;
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

    @Override
    public void run() {
        processRequest();
    }

    /**
     * Build the response header and body
     *
     */
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
                System.out.println(httpMethod);
                switch (httpMethod) {
                    case GET, POST, PUT, DELETE -> EndpointMapper.handleRequest(this.socket, uri, httpMethod);
                    default -> handleUnsupportedMethod();
                }
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


    public static String readRequestBody(Socket socket) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        boolean headerPassed = false;

        // Пропускаем заголовки запроса
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                headerPassed = true;
                break;
            }
        }

        // Считываем тело запроса
        if (headerPassed) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }
}
