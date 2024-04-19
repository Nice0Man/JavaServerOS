package editor.commands;


import editor.CSVApp;
import editor.CSVEditor;

// Конкретная команда для чтения данных из CSV файла
public class ReadCommand extends Command {
    public ReadCommand(CSVApp csvApp, CSVEditor activeEditor) {
        super(csvApp, activeEditor);
    }

    @Override
    public boolean execute() {
        csvEditor.readData();
        return false;
    }
}
