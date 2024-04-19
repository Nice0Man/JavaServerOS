package server.builder;

import editor.CSVApp;
import server.logger.Logger;
import server.RequestHandler;

import java.net.Socket;

public interface ServerBuilder {

    public ServerBuilder dataFilePath(String dataFilePath);

    public ServerBuilder requestHandler(RequestHandler requestHandler);

    public ServerBuilder logger(Logger logger);

    public ServerBuilder clientSocket(Socket socket);

    public ServerBuilder csv(CSVApp csvApp);

    public ProcessingServer build();
}
