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
    private final String jsonData;
    private ObjectMapper objectMapper;

    public Json(String data){
        objectMapper = new ObjectMapper();
        jsonData = data;
    }

    public Json(Socket socket, HTTP_STATUS_CODE statusCode, String jsonData) throws IOException {
        super(socket, statusCode, CONTENT_TYPE.TEXT_HTML);
        this.jsonData = jsonData;
    }

    public Json(Json other) throws IOException {
        super(other.getSocket(), other.getStatusCode(), other.getHeaders());
        jsonData = other.jsonData;
    }

    public static AbstractResponse sendResponse(String data) throws IOException {
        return new Json(data);
    }

    public void sendResponse(DataOutputStream outputStream) throws IOException {
        setHeaders(CONTENT_TYPE.APPLICATION_JSON);
        writeHeaders(outputStream);
        writeBody(outputStream, jsonData.getBytes());
        flush(outputStream);
    }

    /**
     *
     */
    @Override
    public void send() {
        try {
            DataOutputStream outputStream = new DataOutputStream(getSocket().getOutputStream());
            if (getJsonData() != null) {
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
