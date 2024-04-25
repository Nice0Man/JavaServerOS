package com.server.http.response;

import com.server.http.enums.CONTENT_TYPE;
import com.server.http.enums.HTTP_STATUS_CODE;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


@Data
@EqualsAndHashCode(callSuper = true)
public class Html extends AbstractResponse {
    private HTTP_STATUS_CODE statusCode;

    public Html(HTTP_STATUS_CODE code){
        this.statusCode = code;
    }

    public Html(Socket socket, HTTP_STATUS_CODE statusCode) throws IOException {
        super(socket, statusCode, CONTENT_TYPE.TEXT_HTML);
    }

    public Html(Html other) throws IOException {
        super(other.getSocket(), other.getStatusCode(), other.getHeaders());
    }

    public static AbstractResponse renderTemplate(){
        return new Html(HTTP_STATUS_CODE.OK_200);
    }

    public void sendStringResponse(DataOutputStream outputStream, String content) throws IOException {
        setHeaders(CONTENT_TYPE.TEXT_HTML);
        writeHeaders(outputStream);
        writeBody(outputStream, content.getBytes());
        flush(outputStream);
    }

    public void sendByteResponse(DataOutputStream outputStream, byte[] data) throws IOException {
        setHeaders(CONTENT_TYPE.TEXT_HTML);
        writeHeaders(outputStream);
        writeBody(outputStream, data);
        flush(outputStream);;
    }

    public void sendError(DataOutputStream outputStream,HTTP_STATUS_CODE httpStatusCode) throws IOException {
        sendStringResponse(outputStream, STR."<h1> \{httpStatusCode}</h1>");
    }
}
