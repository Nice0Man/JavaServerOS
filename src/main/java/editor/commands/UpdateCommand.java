package editor.commands;

import editor.CSVApp;
import editor.CSVEditor;

import java.io.IOException;

// Конкретная команда для обновления записи в CSV файле
public class UpdateCommand extends Command {
    private final int rowIndex;
    private final String[] strings;

    public UpdateCommand(CSVApp csvApp, CSVEditor activeEditor, int rowIndex, String[] strings) {
        super(csvApp, activeEditor);
        this.rowIndex = rowIndex;
        this.strings = strings;
    }

    @Override
    public boolean execute() throws IOException {
        saveBackup();
        csvEditor.updateData(rowIndex, strings);
        return true;
    }
}
