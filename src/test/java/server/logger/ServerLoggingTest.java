package server.logger;

import org.junit.jupiter.api.Test;
import server.builder.ProcessingServer;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class ServerLoggingTest {

    @Test
    void testServerLoggingAnnotation() {
        ProcessingServer exampleServer = new ProcessingServer();

        // Получаем все методы класса ExampleServer
        Method[] methods = exampleServer.getClass().getDeclaredMethods();

        for (Method method : methods) {
            // Проверяем, есть ли аннотация ServerLogging у текущего метода
            if (method.isAnnotationPresent(ServerLogging.class)) {
                // Получаем аннотацию ServerLogging
                ServerLogging annotation = method.getAnnotation(ServerLogging.class);
                assertNotNull(annotation);
                // Проверяем, что значение аннотации не пустое
                assertNotNull(annotation.value());
                // Выводим информацию о методе и его аннотации
                System.out.println("Метод: " + method.getName() + ", Аннотация: " + annotation.value());
            }
        }
    }
}
