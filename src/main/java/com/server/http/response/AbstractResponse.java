package com.server.http.response;

import com.server.http.models.HttpHeader;
import com.server.http.status.HTTP_STATUS_CODE;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

public abstract class AbstractResponse {
    protected static final String CRLF = "\r\n";
    protected final Socket socket;
    protected final HTTP_STATUS_CODE statusCode;
    protected final HttpHeader headers;

    public AbstractResponse(Socket socket, HTTP_STATUS_CODE statusCode, CONTENT_TYPE... contentTypes) {
        this.socket = socket;
        this.statusCode = statusCode;
        this.headers = setHeaders(contentTypes);
    }

    protected abstract Response sendResponse(HTTP_STATUS_CODE code) throws IOException;

    protected HttpHeader setHeaders(CONTENT_TYPE... contentTypes) {
        StringBuilder contentTypeBuilder = new StringBuilder();
        for (CONTENT_TYPE contentType : contentTypes) {
            contentTypeBuilder.append(contentType.getType()).append(", ");
        }
        String contentTypeValue = contentTypeBuilder.toString().replaceAll(", $", "");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        return new HttpHeader.Builder()
                .addHTTP(String.valueOf(this.statusCode.getCode()))
                .addRow("Content-Type", contentTypeValue)
                .addRow("Date", formatter.format(new Date(System.currentTimeMillis())))
                .addRow("Server", "Simple Java Web Server")
                .addRow("Connection", "close")
                .build();
    }

    protected void writeHeaders(DataOutputStream outputStream) throws IOException {
        for (String header : headers.getHeaders()) {
            outputStream.writeBytes(header);
        }
        outputStream.writeBytes(CRLF);
    }

    protected void writeBody(DataOutputStream outputStream, String body) throws IOException {
        outputStream.writeBytes(body);
        outputStream.writeBytes(CRLF);
    }

    protected void flush(DataOutputStream outputStream) throws IOException {
        outputStream.flush();
    }

    protected void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
