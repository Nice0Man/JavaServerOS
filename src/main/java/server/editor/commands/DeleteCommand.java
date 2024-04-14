package server.editor.commands;

import server.editor.CSVApp;
import server.editor.CSVEditor;

// Конкретная команда для удаления записи из CSV файла
public class DeleteCommand extends Command {
    private final int rowIndex;

    public DeleteCommand(CSVApp csvApp, CSVEditor activeEditor, int rowIndex) {
        super(csvApp, activeEditor);
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean execute() {
        saveBackup();
        csvEditor.deleteData(rowIndex);
        return true;
    }
}
