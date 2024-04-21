package com.server.http.request;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.server.http.response.Response;
import com.server.http.status.HTTP_STATUS_CODE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.server.util.Config;


/**
 * Processes request on its in thread and returns a response to the client
 *
 * @author tomiwa
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
        processRequest(this.socket);
    }

    /**
     * Build the response header and body
     *
     * @param s The incoming connection object
     */
    public void processRequest(Socket s) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String line = in.readLine();
            logger.info(line);
            String requestMethod = line.split("\\s+")[0];
            if(requestMethod.equalsIgnoreCase(Config.METHODS.GET.toString())) {
                String uri = line.split("\\s+")[1];
                if(uri.equals("/")) {
                    File file = new File(".");
                    directoryListing(uri, file);
                } else {
                    File file = new File(uri.substring(1));
                    if(file.exists()) {
                        int length = (int) file.length();
                        byte[] bytes = new byte[length];
                        InputStream i = new FileInputStream(file);
                        int offset = 0;
                        while (offset < length) {
                            int count = i.read(bytes, offset, (length - offset));
                            offset += count;
                        }
                        i.close();
                        logger.info("{}: (200) {}", uri, HTTP_STATUS_CODE.OK_200);
                        response = new Response(HTTP_STATUS_CODE.OK_200, this.socket, bytes, true);
                        response.responseView();
                    } else if (file.isDirectory()) {
                        directoryListing(uri, file);
                    } else {
                        logger.error("{}: (404) {}", uri, HTTP_STATUS_CODE.NOT_FOUND_404);
                        response = new Response(HTTP_STATUS_CODE.NOT_FOUND_404, this.socket);
                        response.responseView();
                    }
                }
            } else {
                logger.error("{}: (405) {}", requestMethod.toUpperCase(), HTTP_STATUS_CODE.METHOD_NOT_ALLOWED_405);
                response = new Response(HTTP_STATUS_CODE.METHOD_NOT_ALLOWED_405, this.socket);
                response.responseView();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * List files and directories contained in the requested URI
     *
     * @param uri  The URI in the incoming request
     * @param file The requested file or directory
     */
    public void directoryListing(String uri, File file) {
        StringBuilder output = new StringBuilder("<html><head><title>Index of " + uri);
        output.append("</title></head><body><h1>Index of " + uri);
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
