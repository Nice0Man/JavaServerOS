package com.server.http.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.http.status.HTTP_STATUS_CODE;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Json extends AbstractResponse {
    private final Map<String, String> jsonData;

    public Json(Socket socket, HTTP_STATUS_CODE statusCode, Map<String, String> jsonData) {
        super(socket, statusCode, CONTENT_TYPE.TEXT_HTML);
        this.jsonData = jsonData;
    }

    public void sendResponse() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonData);

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeBytes("HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getText() + "\r\n");
        outputStream.writeBytes("Content-Length: " + json.length() + "\r\n");
        outputStream.writeBytes("Content-Type: application/json\r\n");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes(json);
        outputStream.flush();
        outputStream.close();
    }
}
