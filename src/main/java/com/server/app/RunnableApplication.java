package com.server.app;

import com.server.http.request.Handler;
import com.server.http.request.Listener;
import com.server.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class RunnableApplication implements Serializable {
    private static Logger logger;

    public static void setUp(Object object, String[] args){
        logger = LogManager.getLogger(RunnableApplication.class);
        try {
            Config.getInstance();
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        }
        logger.info("Loading configurations from src/main/resources/config.properties");

        setPort(args);
        logger.info("Server is starting ...");

        // Shutdown cleanup
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.warn("Getting ready to shutdown ...");
            }
        });
    }

    public static void setPort(String[] args) {
        if (args.length == 1 && Integer.parseInt(args[0]) >= Config.PORT_MIN && Integer.parseInt(args[0]) <= Config.PORT_MAX) {
            Config.PORT = Integer.parseInt(args[0]);
            logger.info("Changed PORT: {}", args[0]);
        }
    }

    public static void run(Object object, String[] args) {
        setUp(object, args);
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
}
