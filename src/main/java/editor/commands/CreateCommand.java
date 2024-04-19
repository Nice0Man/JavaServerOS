package editor.commands;

import editor.CSVApp;
import editor.CSVEditor;

// Конкретная команда для добавления записи в CSV файл
public class CreateCommand extends Command {
    private final String[] strings;

    public CreateCommand(CSVApp csvApp, CSVEditor csvEditor, String[] strings) {
        super(csvApp, csvEditor);
        this.strings = strings;
    }

    @Override
    public boolean execute(){
        saveBackup();
        csvEditor.createData(strings);
        return true;
    }
}
