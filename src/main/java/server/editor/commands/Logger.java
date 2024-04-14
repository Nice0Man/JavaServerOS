package server.editor.commands;

import java.io.FileWriter;
import java.io.IOException;

// Класс для логгирования операций в файл
class Logger {
    public void log(String message) {
        try (FileWriter writer = new FileWriter("logfile.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
