import server.RequestHandler;
import server.builder.ProcessingServer;
import server.builder.ProcessingServerBuilder;
import editor.CSVApp;
import server.logger.Logger;

import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static final int PORT = 8080;
    private static final String CONFIG_FILE_PATH = "src/main/resources/test-config.properties";

    public static void main(String[] args) throws Throwable {
        // Create the ProcessingServer instance using the builder
        String dataFilePath = "src/main/resources/data.csv";
        Logger logger = new Logger("src/main/resources/server.log");
        int threadPoolSize = 10;
        RequestHandler requestHandler = new RequestHandler();
        ServerSocket serverSocket = new ServerSocket(8080);
        CSVApp csvApp = new CSVApp();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("# Client connected: " + clientSocket.getInetAddress().getHostAddress());
            Thread thread = new Thread(() -> {
                try {
                    ProcessingServer clientHandler = new ProcessingServerBuilder()
                            .dataFilePath(dataFilePath)
                            .logger(logger)
                            .clientSocket(clientSocket)
                            .requestHandler(requestHandler)
                            .csv(csvApp)
                            .build();
                    clientHandler.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
