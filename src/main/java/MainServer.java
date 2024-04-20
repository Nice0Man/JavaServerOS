import server.ClientHandler;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainServer {
    public static final int PORT = 8080;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final String DATA_FILE_PATH = "src/main/resources/data.csv";
    private static final int THREAD_POOL_SIZE = 10;
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream(CONFIG_FILE_PATH)){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(MainServer.class.getName());
        }catch (Exception ignore){
            ignore.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        // Create the ProcessingServer instance using the builder
        ServerSocket serverSocket = new ServerSocket(PORT);
        LOGGER.log(Level.INFO, "Server parameters:");
        LOGGER.log(Level.INFO, "Data file path: " + DATA_FILE_PATH);
        LOGGER.log(Level.INFO, "Thread pool size: " + THREAD_POOL_SIZE);
        LOGGER.log(Level.INFO, "Server socket port: " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            Thread thread = new Thread(() -> {
                try {
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clientHandler.run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
            thread.start();
        }
    }
}
