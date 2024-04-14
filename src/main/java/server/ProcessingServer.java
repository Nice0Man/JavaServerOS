package server;

import util.ConfigReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessingServer {
    private static final String CONFIG_FILE_PATH = "src/main/resources/test-config.properties";
    private static final int DEFAULT_PORT = 8080;
    private final int port;

    public ProcessingServer() {
        this.port = readPortFromConfig();
    }

    private int readPortFromConfig() {
        ConfigReader.setCONFIG_FILE(CONFIG_FILE_PATH);
        String portStr = ConfigReader.getProperty("server.port");
        try {
            assert portStr != null;
            return Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number in config file. Using default port.");
            return DEFAULT_PORT;
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Создание нового обработчика клиента и запуск его в отдельном потоке
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
