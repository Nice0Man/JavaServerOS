package editor;


import com.fasterxml.jackson.databind.ObjectMapper;
import editor.commands.*;
import lombok.Data;


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
                System.out.print(STR."\{value} ");
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

    public String convertDataToJson(String[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public void create(String[] strings) throws IOException {
        executeCommand(new CreateCommand(this, activeEditor, strings));
    }

    public void read() throws IOException {
        executeCommand(new ReadCommand(this, activeEditor));
    }

    public void update(int rowIndex, String[] strings) throws IOException {
        executeCommand(new UpdateCommand(this, activeEditor, rowIndex, strings ));
    }

    public void delete(int rowIndex) throws IOException {
        executeCommand(new DeleteCommand(this, activeEditor, rowIndex));
    }

    public void get(int rowIndex) throws IOException {
        executeCommand(new GetCommand(this, activeEditor, rowIndex));
    }

    private void executeCommand(Command command) throws IOException {
        if (command.execute()){
            history.push(command);
        }
    }

    void undo() throws IOException {
        Command command = history.pop();
        if (command != null){
            command.undo();
        }
    }
}
