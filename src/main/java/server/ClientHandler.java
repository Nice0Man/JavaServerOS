package server;

import lombok.Setter;
import lombok.Getter;
import java.util.logging.Logger;
import server.http.status.HttpStatusCode;

import java.io.*;
import java.net.Socket;
import java.util.logging.LogManager;


@Getter
@Setter
public class ClientHandler implements Runnable {
    private static final String CRLF = "\r\n";
    private static final String SIMPLE_HTML_RESPONSE = "<html><head><title>Simple HTML Response</title></head><body><h1>Hello, World!</h1></body></html>";



    private final Socket socket;
    private final BufferedReader reader;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final RequestHandler requestHandler;


    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.requestHandler = new RequestHandler(socket);
    }

    @Override
    public void run() {
        try {
            requestHandler.handleRequest();
            requestHandler.sendResponse(HttpStatusCode.OK_200, SIMPLE_HTML_RESPONSE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
