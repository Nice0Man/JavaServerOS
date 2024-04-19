package server.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для логирования методов с аннотацией ServerLogging.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logger {
    private String filePath = "server.log"; // Путь к файлу журнала
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
                        String logEntry = timestamp + " - " + "Метод: " + method.getName() + ", Аннотация: " + annotation.value();
                        writer.println(logEntry);
                    }
                }
            }
        }
    }
}
