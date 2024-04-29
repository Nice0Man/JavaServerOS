package com.server.http.response;

import com.server.http.enums.CONTENT_TYPE;
import com.server.http.models.HttpHeader;
import com.server.http.enums.HTTP_STATUS_CODE;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor(force = true)
public abstract class AbstractResponse {
    private Socket socket;
    private HTTP_STATUS_CODE statusCode;

    protected static final String CRLF = "\r\n";
    protected HttpHeader headers;
    protected String body;

    protected AbstractResponse(Socket socket, HTTP_STATUS_CODE statusCode, CONTENT_TYPE... contentTypes) throws IOException {
        this.socket = socket;
        this.statusCode = statusCode;
        setHeaders(contentTypes);
    }

    protected AbstractResponse(Socket socket, HTTP_STATUS_CODE statusCode, HttpHeader headers) throws IOException {
        this.socket = socket;
        this.statusCode = statusCode;
        if (headers != null){
            this.headers = headers;
        } else {
            setHeaders(CONTENT_TYPE.TEXT_HTML,CONTENT_TYPE.APPLICATION_JSON);
        }
    }

    protected void setHeaders(CONTENT_TYPE... contentTypes) {
        StringBuilder contentTypeBuilder = new StringBuilder();
        for (CONTENT_TYPE contentType : contentTypes) {
            if (contentType != null) contentTypeBuilder.append(contentType.getType()).append(",");
        }
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        this.headers = new HttpHeader.Builder()
                .addHTTP(String.valueOf(this.statusCode.getCode()))
                .addRow("Content-Type", contentTypeBuilder.toString())
                .addRow("Date", formatter.format(new Date(System.currentTimeMillis())))
                .addRow("Server", "Simple Java Web Server")
                .addRow("Connection", "close")
                .build();
    }

    protected String setBody(){
        return null;
    }

    protected void writeHeaders(DataOutputStream outputStream) throws IOException {
        for (String header : headers.getHeaders()) {
            outputStream.writeBytes(header);
        }
        outputStream.writeBytes(CRLF);
    }

    protected void writeBody(DataOutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
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

    public abstract void send();
}
