package com.server.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.server.http.models.HttpHeader;
import com.server.http.status.HTTP_STATUS_CODE;

/**
 * Builds HTTP responses
 *
 * @author Nice0Man
 *
 */
public class Response {
    private byte[] bytes;
    private boolean isFile;
    private final HTTP_STATUS_CODE statusCode;
    private final Socket socket;
    private StringBuilder output;
    private HttpHeader headers;
    private static final String CRLF = "\r\n";
    private static final Logger logger = LogManager.getLogger(Response.class);

    public Response(HTTP_STATUS_CODE statusCode, Socket socket) {
        this.statusCode = statusCode;
        this.socket = socket;
    }

    public Response(HTTP_STATUS_CODE statusCode, Socket socket, StringBuilder output, boolean isFile) {
        this.statusCode = statusCode;
        this.socket = socket;
        this.output = output;
        this.isFile = isFile;
    }

    public Response(HTTP_STATUS_CODE statusCode, Socket socket, byte[] bytes, boolean isFile) {
        this.statusCode = statusCode;
        this.socket = socket;
        this.bytes = bytes;
        this.isFile = isFile;
    }

    /**
     * Sets HTTP response header
     */
    public void responseHeaders() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        headers = new HttpHeader.Builder()
                .addHTTP(String.valueOf(this.statusCode.getCode()))
                .addRow("Content-Type: ", "text/html")
                .addRow("Date: ", formatter.format(new Date(System.currentTimeMillis())))
                .addRow("Server: ", "Simple Java Web Server")
                .addRow("Connection: ", "close")
                .build();
    }

    /**
     * Selects the appropriate response view based on the status code
     */
    public void responseView() {
        responseHeaders();
        try {
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
            switch (this.statusCode) {
                case OK_200:
                    if(isFile) {
                        responseBytes(outputStream, this.bytes);
                    } else {
                        responseBytes(outputStream, this.output.toString());
                    }
                    break;
                case UNAUTHORIZED_401:
                    responseBytes(outputStream, "<h1> " + this.statusCode + "</h1>");
                    outputStream.flush();
                    break;
                case NOT_FOUND_404:
                case METHOD_NOT_ALLOWED_405:
                    responseBytes(outputStream, "<h1> " + this.statusCode + "</h1>");
                    break;
                default:
                    break;
            }
            logger.info("Response sent to {}", this.socket.getRemoteSocketAddress());
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Builds the response view
     *
     * @param outputStream HTTP response object
     * @param bytes        The message to added in the
     *                     response body
     * @throws IOException
     */
    public void responseBytes(DataOutputStream outputStream, byte[] bytes) throws IOException {
        for(String entry : headers.getHeaders()) {
            outputStream.writeBytes(entry );
        }
        outputStream.writeBytes(CRLF);
        outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body>");
        outputStream.write(bytes);
        outputStream.writeBytes("</body></html>");
        outputStream.writeBytes(CRLF);
        outputStream.flush();
    }

    /**
     * Builds the response view
     *
     * @param outputStream HTTP response object
     * @param response     The message to be added in the
     * 					   response body
     * @throws IOException
     */
    public void responseBytes(DataOutputStream outputStream, String response) throws IOException {
        for(String entry : headers.getHeaders()) {
            outputStream.writeBytes(entry);
            System.out.print(entry);
        }
        outputStream.writeBytes(CRLF);
        outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body>");
        outputStream.writeBytes(response);
        outputStream.writeBytes("</body></html>");
        outputStream.writeBytes(CRLF);
        outputStream.flush();
    }
}