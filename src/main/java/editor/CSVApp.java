package editor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import editor.commands.*;
import lombok.Data;
import editor.commands.*;

import java.io.IOException;
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

    public String convertDataToJson(List<String[]> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
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
