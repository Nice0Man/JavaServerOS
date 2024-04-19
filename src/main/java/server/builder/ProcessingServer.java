package server.builder;

import lombok.NoArgsConstructor;
import lombok.Setter;
import server.http.annotations.Get;
import server.logger.Logger;
import server.RequestHandler;
import editor.CSVApp;
import server.models.HttpHeader;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

@Setter
@NoArgsConstructor
public class ProcessingServer implements Runnable {
    private static final String CRLF = "\r\n";

    private String dataFilePath;
    private RequestHandler requestHandler;
    private Logger logger;
    private CSVApp csvApp;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;


    @Override
    public void run() {
        try {
            readInputHeaders();
//            sendResponse();
        } catch (Throwable t) {
            /*do nothing*/
        } finally {
            try {
                socket.close();
            } catch (Throwable t) {
                /*do nothing*/
            }
        }
        System.err.println("Client processing finished!");
    }

    private void readInputHeaders() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String s = br.readLine();
            if (s == null || s.trim().isEmpty()) {
                break;
            }
        }
    }

//    @ServerLogging("Write Response")
//    private void writeResponse() throws Throwable {
//        String response =
//                "HTTP/1.1 200 OK\r\n" +
//                        "Server: YarServer/2009-09-09\r\n" +
//                        "Content-Type: text/html\r\n" +
//                        "Content-Length: " + "<html><body><h1>Hello from Habrahabr</h1></body></html>".length() + "\r\n" +
//                        "Connection: close\r\n\r\n";
//        String result = response + "<html><body><h1>Hello from Habrahabr</h1></body></html>";
//        outputStream.write(result.getBytes());
//        outputStream.flush();
//    }

    private void handleGetMethod(String path, Map<HttpHeader, String> headers) throws IOException {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Get.class)) {
                Get annotation = method.getAnnotation(Get.class);
                if (annotation.value().equals(path)) {
                    try {
                        method.invoke(this);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.sendResponse(404, "Not Found", "text/plain", "Resource not found: " + path);
    }
    private void sendResponse(int statusCode, String statusText, String contentType, String responseBody) throws IOException {
        String response = HttpHeader.HTTP_VERSION.createHeader(statusCode + " " + statusText) + CRLF +
                HttpHeader.CONTENT_TYPE.createHeader(contentType) + CRLF +
                HttpHeader.CONTENT_LENGTH.createHeader(String.valueOf(responseBody.getBytes().length)) + CRLF +
                HttpHeader.CONNECTION.createHeader("close") + CRLF +
                CRLF +
                responseBody;
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
