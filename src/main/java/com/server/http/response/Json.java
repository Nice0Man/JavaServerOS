package com.server.http.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.http.enums.CONTENT_TYPE;
import com.server.http.enums.HTTP_STATUS_CODE;

import lombok.Getter;
import lombok.Setter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class Json extends AbstractResponse {
//    private final String jsonData;
    private ObjectMapper objectMapper;

    public Json(String data){
        objectMapper = new ObjectMapper();
        bytes = data.getBytes();
    }

    public Json(Socket socket, HTTP_STATUS_CODE statusCode, String jsonData) throws IOException {
        super(socket, statusCode, CONTENT_TYPE.TEXT_HTML);
        this.bytes = jsonData.getBytes();
    }

    public Json(Json other) throws IOException {
        super(other.getSocket(), other.getStatusCode(), other.getHeaders());
        bytes = other.bytes;
    }

    public Json(String data, HTTP_STATUS_CODE httpStatusCode) {
        this(data);
        this.setStatusCode(httpStatusCode);
    }

    public static AbstractResponse sendResponse(String data) throws IOException {
        return new Json(data);
    }

    public static AbstractResponse sendResponse(String data, HTTP_STATUS_CODE httpStatusCode) {
        return new Json(data, httpStatusCode);
    }

    @Override
    public void sendResponse(DataOutputStream outputStream) throws IOException {
        setHeaders(CONTENT_TYPE.APPLICATION_JSON);
        writeHeaders(outputStream);
        writeBody(outputStream, bytes);
        flush(outputStream);
    }

    /**
     *
     */
    @Override
    public void send() {
        try {
            DataOutputStream outputStream = new DataOutputStream(getSocket().getOutputStream());
            if (bytes != null) {
                sendResponse(outputStream);
            } else {
                setStatusCode(HTTP_STATUS_CODE.INTERNAL_SERVER_ERROR_500);
                sendResponse(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
