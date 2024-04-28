import com.server.app.ServerRunnableApplication;
import com.server.app.annotation.ServerApplication;

import com.server.http.enums.RESPONSE_TYPE;
import com.server.http.request.annotations.*;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import com.server.http.response.Json;
import editor.CSVApp;
import editor.CSVEditor;
import editor.commands.CommandHistory;

import java.io.IOException;
import java.util.Arrays;

import static com.server.http.enums.HTTP_METHOD.*;
import static com.server.http.enums.RESPONSE_TYPE.HTML;
import static com.server.http.enums.RESPONSE_TYPE.JSON;

@HttpMethods
@ServerApplication
public class App {
    private static final CSVApp csvApp = new CSVApp();

    public static void main(String[] args) {
        ServerRunnableApplication.run(App.class, args);
    }

    @Template(path = "index.html")
    @EndpointMapping(uri = "/")
    public static AbstractResponse helloWorld() {
        return Html.renderTemplate();
    }


    @Template(path = "mainView.html")
    @EndpointMapping(uri = "/csv", type = HTML)
    public static AbstractResponse addRecord() {
        return Html.renderTemplate();
    }

    @EndpointMapping(uri = "/csv/all", method = GET, type = JSON)
    public static AbstractResponse getAllRecords() throws IOException {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
        csvApp.read();
        return Json.sendResponse(csvApp.convertDataToJson(csvApp.getActiveEditor().getDataList()));
    }


    @EndpointMapping(uri = "/csv/{id}", method = GET , type = JSON)
    public AbstractResponse getRecordById(@RequestParam("id") String id) throws IOException {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
        csvApp.read();
        int number = Integer.parseInt(id);
        String[] string = csvApp.getActiveEditor().getDataList().get(number);
        return Json.sendResponse(Arrays.toString(string));
    }


    @EndpointMapping(uri = "/csv/{id}", method = PUT, type = HTML)
    public AbstractResponse updateRecord(
            @RequestParam("id") String id,
            @BodyParam("body") String body
    ) throws IOException {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
        int number = Integer.parseInt(id);
        csvApp.update(number, new String[]{body});
        return Html.renderTemplate(STR."<p>Row \{id} was updated! </p>");
    }


    @EndpointMapping(uri = "/csv/{id}", method = DELETE, type = JSON)
    public AbstractResponse deleteRecord(@RequestParam("id") String id) throws IOException {

        String json = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        return Json.sendResponse(json);
    }
}
