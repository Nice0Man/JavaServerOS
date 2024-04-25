package com.server.http.response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Data
public class Response {
    private byte[] bytes;
    private boolean isFile;
    private final HTTP_STATUS_CODE statusCode;
    private Socket socket;
    private StringBuilder output;
    private HttpHeader headers;
    private static final String CRLF = "\r\n";
    private static final Logger logger = LogManager.getLogger(Response.class);

    public Response(HTTP_STATUS_CODE httpStatusCode) {
        this.statusCode = httpStatusCode;
    }

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

    public Response(Socket socket, StringBuilder output, HttpHeader headers, HTTP_STATUS_CODE statusCode, boolean isFile, byte[] bytes) {
        this.socket = socket;
        this.output = output;
        this.headers = headers;
        this.statusCode = statusCode;
        this.isFile = isFile;
        this.bytes = bytes;
    }

    public Response(Response response){
        this(
                response.getSocket(),
                response.getOutput(),
                response.getHeaders(),
                response.getStatusCode(),
                response.isFile(),
                response.getBytes()
        );
    }

    /**
     * Sets HTTP response header
     */

    public void responseHeaders(String contentType) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        headers = new HttpHeader.Builder()
                .addHTTP(String.valueOf(this.statusCode.getCode()))
                .addRow("Content-Type", contentType)
                .addRow("Date", formatter.format(new Date(System.currentTimeMillis())))
                .addRow("Server", "Simple Java Web Server")
                .addRow("Connection", "close")
                .build();
    }


    /**
     * Selects the appropriate response view based on the status code
     */

    public void responseView() {
        responseHeaders("text/html");

//        add other types
        try {
            DataOutputStream outputStream = new DataOutputStream(this.socket.getOutputStream());
            switch (this.statusCode) {
                case OK_200, ACCEPTED_202:
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
            e.printStackTrace(System.err);
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
            outputStream.writeBytes(entry);
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