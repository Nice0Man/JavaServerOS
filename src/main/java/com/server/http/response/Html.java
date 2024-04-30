package com.server.http.response;

import com.server.http.enums.CONTENT_TYPE;
import com.server.http.enums.HTTP_STATUS_CODE;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.server.http.enums.HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR_500;


@Data
@EqualsAndHashCode(callSuper = true)
public class Html extends AbstractResponse {
    public Html(HTTP_STATUS_CODE code){
        this.statusCode = code;
    }

    public Html(Socket socket, HTTP_STATUS_CODE statusCode) throws IOException {
        super(socket, statusCode, CONTENT_TYPE.TEXT_HTML);
    }

    public Html(Html other) throws IOException {
        super(other.getSocket(), other.getStatusCode(), other.getHeaders(), other.getBytes());
    }

    public Html(HTTP_STATUS_CODE code, String s) {
        this.statusCode = code;
        this.bytes = s.getBytes();
    }

    public static AbstractResponse renderTemplate(){
        return new Html(HTTP_STATUS_CODE.OK_200);
    }

    public static AbstractResponse renderTemplate(String s) throws IOException {
        return new Html(HTTP_STATUS_CODE.OK_200, s);
    }

    public static AbstractResponse renderTemplate(String s, HTTP_STATUS_CODE httpStatusCode) throws IOException {
        return new Html(httpStatusCode, s);
    }

    @Override
    protected void sendResponse(DataOutputStream outputStream) throws IOException {
        setHeaders(CONTENT_TYPE.TEXT_HTML);
        writeHeaders(outputStream);
        writeBody(outputStream, bytes);
        flush(outputStream);;
    }
    /**
     *
     */
    @Override
    public void send() {
        try {
            DataOutputStream outputStream = new DataOutputStream(getSocket().getOutputStream());
            if (getBytes() != null) {
                sendResponse(outputStream);
            } else {
                setStatusCode(INTERNAL_SERVER_ERROR_500);
                String template = STR."<html><body><h1>\{INTERNAL_SERVER_ERROR_500}</h1></body></html>";
                setBytes(template.getBytes());
                sendResponse(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
