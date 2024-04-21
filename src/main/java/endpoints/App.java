package endpoints;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.server.http.request.Handler;
import com.server.http.request.Listener;
import com.server.util.Config;

public class App implements Serializable {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {

        // Load configurations
        try {
            Config.getInstance();
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        }
        logger.info("Loading configurations from {}", Config.class);

        setPort(args);
        logger.info("Server is starting ...");


        // Shutdown cleanup
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.warn("Getting ready to shutdown ...");
            }
        });

        ExecutorService requestHandler = Executors.newFixedThreadPool(Config.WORKER_THREADS);

        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(Config.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                Listener.getInstance().addRequestToQueue(socket);
                requestHandler.execute(new Handler(Listener.getInstance().handleRequest()));
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
    /**
     * Make a copy of the incoming request object
     *
     * @param socket The incoming request
     * @return The copy of the request object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Socket cloneSocket(Socket socket) throws IOException, ClassNotFoundException {
        return new Socket(socket.getInetAddress().getHostName(), socket.getLocalPort());
    }

    /**
     * Sets the port the web server should listen on
     *
     * @param args
     */
    public static void setPort(String[] args) {
        if (args.length == 1 && Integer.parseInt(args[0]) >= Config.PORT_MIN && Integer.parseInt(args[0]) <= Config.PORT_MAX) {
            Config.PORT = Integer.parseInt(args[0]);
            logger.info("Changed PORT: {}", args[0]);
        }
    }
}