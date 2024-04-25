package com.server.http.response;

import com.server.http.status.HTTP_STATUS_CODE;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Html extends AbstractResponse {
    private DataOutputStream dataOutputStream;


    public Html(Socket socket, HTTP_STATUS_CODE statusCode, String filePath) {
        super(socket, statusCode, CONTENT_TYPE.TEXT_HTML);
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    protected AbstractResponse sendResponse(HTTP_STATUS_CODE code) throws IOException {

        return null;
    }

    public void sendStringResponse(String content) throws IOException {
        writeHeaders(dataOutputStream);
        writeBody(dataOutputStream, content);
        flush(dataOutputStream);
        closeSocket();
    }

    public void sendByteResponse(byte[] data) throws IOException {
        writeHeaders(dataOutputStream);
        writeBody(dataOutputStream, new String(data));
        flush(dataOutputStream);
        closeSocket();
    }
}
