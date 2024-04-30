package editor.commands;


import editor.CSVApp;
import editor.CSVEditor;

import java.io.IOException;

// Конкретная команда для чтения данных из CSV файла
public class ReadCommand extends Command {
    public ReadCommand(CSVApp csvApp, CSVEditor activeEditor) {
        super(csvApp, activeEditor);
    }

    @Override
    public boolean execute() throws IOException {
        csvEditor.readData();
        return false;
    }
}
