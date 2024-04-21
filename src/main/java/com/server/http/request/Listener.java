package com.server.http.request;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.server.util.Config;


/**
 * Handles the connection queue
 *
 * @author Nice0Man
 *
 */
public class Listener {
    @Getter
    private static final Listener instance = new Listener();
    private static final BlockingQueue<Socket> connectionQueue = new ArrayBlockingQueue<>(Config.CONNECTION_QUEUE);
    private static final Logger logger = LogManager.getLogger(Listener.class);

    /**
     * Adds request to connection buffer
     *
     * @param socket Incoming connection
     */
    public void addRequestToQueue(Socket socket) {
        try {
            logger.info("New connection from {} added to connection queue", socket.getRemoteSocketAddress());
            connectionQueue.put(socket);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Removes request from the connection buffer
     *
     * @return The connection object or null if connection buffer is empty
     */
    public Socket handleRequest() {
        try {
            Socket taken = connectionQueue.take();
            logger.info("Processing {}", taken.getRemoteSocketAddress());
            return taken;
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
