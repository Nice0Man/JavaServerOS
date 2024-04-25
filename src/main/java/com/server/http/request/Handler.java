package com.server.http.request;

import com.server.http.models.HTTP_METHOD;
import com.server.http.response.Response;
import com.server.http.status.HTTP_STATUS_CODE;
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
    private Response response;
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
                String requestData = readRequestBody(socket);
                HTTP_METHOD httpMethod = HTTP_METHOD.getMethod(requestMethod);

                switch (httpMethod) {
                    case GET, POST, PUT, DELETE:
                        response = EndpointMapper.handleRequest(this.socket, uri, httpMethod, requestData);
                        break;
                    default:
                        handleUnsupportedMethod();
                        return;
                }

                response.responseView();
            } else {
                logger.error("Received null request line");
            }
        } catch (NoSuchMethodException e) {
            logger.error("{}: (404) {}", uri, HTTP_STATUS_CODE.NOT_FOUND_404);
            response = new Response(HTTP_STATUS_CODE.NOT_FOUND_404, this.socket);
            response.responseView();
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
            sendErrorResponse(HTTP_STATUS_CODE.METHOD_NOT_ALLOWED_405);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void sendErrorResponse(HTTP_STATUS_CODE statusCode) throws IOException {
        Response response = new Response(statusCode, socket);
        response.responseView();
    }
    /**
     * List files and directories contained in the requested URI
     *
     * @param uri  The URI in the incoming request
     * @param file The requested file or directory
     */
    public void directoryListing(String uri, File file) {
        StringBuilder output = new StringBuilder(STR."<html><head><title>Index of \{uri}");
        output.append(STR."</title></head><body><h1>Index of \{uri}");
        output.append("</h1><hr><pre>");
        File[] files = file.listFiles();
        logger.info(files);
        if (files != null) {
            for (File f : files) {
                output.append(" <a href=\"" + f.getPath() + "\">" + f.getPath() + "</a>\n");
            }
        }
        output.append("<hr></pre></body></html>");
        logger.info("{}: (200) {}", uri, HTTP_STATUS_CODE.OK_200);
        response = new Response(HTTP_STATUS_CODE.OK_200, this.socket, output, false);
        response.responseView();
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
