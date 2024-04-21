package com.server.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.MissingResourceException;

/**
 * Класс для логирования методов с аннотацией ServerLogging.
 */
public class Logger extends java.util.logging.Logger {
    private String filePath = "server.log"; // Путь к файлу журнала

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected Logger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    /**
     * Метод для логирования методов объекта с аннотацией ServerLogging.
     *
     * @param exampleServer объект, методы которого будут логироваться
     */
    public void log(Object exampleServer)  throws IOException{
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Method[] methods = exampleServer.getClass().getDeclaredMethods();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(ServerLogging.class)) {
                    ServerLogging annotation = method.getAnnotation(ServerLogging.class);
                    if (annotation.value() != null && !annotation.value().isEmpty()) {
                        String logEntry = timestamp + " - " +
                                        "Annotation: " + annotation.value() +
                                        ", Method: " + method.getName() +
                                        ", Parameters: " + Arrays.toString(method.getParameters());
                        writer.println(logEntry);
                    }
                }
            }
        }
    }
}
