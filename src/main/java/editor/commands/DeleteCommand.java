package editor.commands;

import editor.CSVApp;
import editor.CSVEditor;

import java.io.IOException;

// Конкретная команда для удаления записи из CSV файла
public class DeleteCommand extends Command {
    private final int rowIndex;

    public DeleteCommand(CSVApp csvApp, CSVEditor activeEditor, int rowIndex) {
        super(csvApp, activeEditor);
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean execute() throws IOException {
        saveBackup();
        csvEditor.deleteData(rowIndex);
        return true;
    }
}
