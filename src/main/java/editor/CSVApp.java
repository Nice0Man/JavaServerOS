package editor;


import editor.commands.*;
import lombok.Data;
import server.editor.commands.*;

import java.util.List;

@Data
public class CSVApp {
    List<CSVEditor> editors;
    CSVEditor activeEditor;
    CommandHistory history;

    public void print(){
        activeEditor.getDataList().forEach(row -> {
            for (String value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        });
    }
    public void create(String[] strings){
        executeCommand(new CreateCommand(this, activeEditor, strings));
    }

    public void read(){
        executeCommand(new ReadCommand(this, activeEditor));
    }

    public void update(int rowIndex, String[] strings){
        executeCommand(new UpdateCommand(this, activeEditor, rowIndex, strings ));
    }

    public void delete(int rowIndex){
        executeCommand(new DeleteCommand(this, activeEditor, rowIndex));
    }



    private void executeCommand(Command command){
        if (command.execute()){
            history.push(command);
        }
    }

    void undo(){
        Command command = history.pop();
        if (command != null){
            command.undo();
        }
    }
}
