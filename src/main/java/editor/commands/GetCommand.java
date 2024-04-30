package editor.commands;

import editor.CSVApp;
import editor.CSVEditor;

import java.io.IOException;

public class GetCommand extends Command{
    private final int rowIndex;

    public GetCommand(CSVApp csvApp, CSVEditor csvEditor, int rowIndex) {
        super(csvApp, csvEditor);
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean execute() throws IOException {
        csvEditor.get(rowIndex);
        return false;
    }
}
