package server.http.request;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.util.Config;


/**
 * Handles the connection queue
 *
 * @author tomiwa
 *
 */
public class Listener {

    @Getter
    private static final Listener instance = new Listener();
    private static final BlockingQueue<Socket> connectionQueue = new ArrayBlockingQueue<Socket>(Config.CONNECTION_QUEUE);
    private static final Logger logger = LogManager.getLogger(Listener.class);

    /**
     * Adds request to connection buffer
     *
     * @param s Incoming connection
     */
    public void addRequestToQueue(Socket s) {
        try {
            logger.info("New connection from {} added to connection queue", s.getRemoteSocketAddress());
            connectionQueue.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes request from the connection buffer
     *
     * @return The connection object or null if connection buffer is empty
     */
    public Socket handleRequest() {
        try {
            Socket s = connectionQueue.take();
            logger.info("Procesing {}", s.getRemoteSocketAddress());
            return s;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
