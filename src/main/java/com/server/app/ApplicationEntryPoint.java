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
import java.util.concurrent.*;


public class ApplicationEntryPoint implements Serializable {
    private static Logger logger;

    private static void setUp(String[] args){
        logger = LogManager.getLogger(ApplicationEntryPoint.class);
        try {
            Config.getInstance();
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        }
        logger.info("Loading configurations from src/main/resources/config.properties");

        setPort(args);
        logger.info("Server is starting ...");

        // Shutdown cleanup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.warn("Getting ready to shutdown ...")));
    }

    public static void setPort(String[] args) {
        if (args.length == 1 && Integer.parseInt(args[0]) >= Config.PORT_MIN && Integer.parseInt(args[0]) <= Config.PORT_MAX) {
            Config.PORT = Integer.parseInt(args[0]);
            logger.info("Changed PORT: {}", args[0]);
        }
    }

    public static void runApp(String[] args) throws IOException {
        setUp(args);
        ExecutorService requestHandler = Executors.newFixedThreadPool(Config.WORKER_THREADS);

        @SuppressWarnings("resource")
        ServerSocket serverSocket = new ServerSocket(Config.PORT);
        //noinspection InfiniteLoopStatement
        while (true) {
            Socket socket = serverSocket.accept();
            Listener.getInstance().addRequestToQueue(socket);

            // После добавления задачи в очередь, запускаем ее обработку в Handler
            Future<?> future = requestHandler.submit(new Handler(Listener.getInstance().handleRequest()));

            // Устанавливаем таймаут выполнения в 10 секунд
            try {
                future.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                // Если время выполнения превышает 10 секунд, отменяем задачу и завершаем поток
                future.cancel(true);
                logger.warn("Request processing timeout exceeded.");
            }
        }
    }
}
