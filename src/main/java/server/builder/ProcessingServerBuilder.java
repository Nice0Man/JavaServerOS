package server.builder;

import server.logger.Logger;
import server.RequestHandler;
import editor.CSVApp;

import java.net.Socket;

public class ProcessingServerBuilder implements ServerBuilder {

    private final ProcessingServer processingServer;

    public ProcessingServerBuilder() throws Throwable {
        this.processingServer = new ProcessingServer();
    }

    public ServerBuilder dataFilePath(String dataFilePath) {
        this.processingServer.setDataFilePath(dataFilePath);
        return this;
    }

    public ServerBuilder requestHandler(RequestHandler requestHandler) {
        this.processingServer.setRequestHandler(requestHandler);
        return this;
    }

    public ServerBuilder clientSocket(Socket socket) {
        this.processingServer.setSocket(socket);
        return this;
    }

    public ServerBuilder logger(Logger logger) {
        this.processingServer.setLogger(logger);
        return this;
    }
    public ServerBuilder csv(CSVApp csvApp){
        this.processingServer.setCsvApp(csvApp);
        return this;
    }

    public ProcessingServer build() {
        return this.processingServer;
    }
}
