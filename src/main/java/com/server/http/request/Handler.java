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
            logger.info(line);
            String requestMethod = line.split("\\s+")[0];
            uri = line.split("\\s+")[1];
            response = EndpointMapper.handleRequest(this.socket, uri, HTTP_METHOD.getMethod(requestMethod));
            response.responseView();
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

    private void sendErrorResponse(HTTP_STATUS_CODE statusCode) throws IOException {
        Response response = new Response(statusCode, socket);
        response.responseView();
    }


    public void sendTemplate(String template, HTTP_STATUS_CODE statusCode){
        boolean isHtmlFile = template.contains(".html");
        if (!isHtmlFile) {
            sendResponseToClient(this.socket,statusCode,template, false);
        }
        File file = new File(STR."src/main/resources/template/\{template}");
        if(file.exists()) {
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            InputStream i = null;
            try {
                i = new FileInputStream(file);
                int offset = 0;
                while (offset < length) {
                    int count = i.read(bytes, offset, (length - offset));
                    offset += count;
                }
                i.close();
                response = new Response(HTTP_STATUS_CODE.OK_200, this.socket, bytes, true);
                response.responseView();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private void sendResponseToClient(Socket socket, HTTP_STATUS_CODE statusCode, String string, boolean isFile){
        Response response = new Response(statusCode, socket, new StringBuilder(string), isFile);
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
}
