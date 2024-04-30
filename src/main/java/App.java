import com.server.app.ServerRunnableApplication;
import com.server.app.annotation.ServerApplication;

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
import static com.server.http.enums.HTTP_STATUS_CODE.CREATED_201;
import static com.server.http.enums.HTTP_STATUS_CODE.OK_200;

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
    @EndpointMapping(uri = "/csv")
    public static AbstractResponse addRecord() {
        return Html.renderTemplate();
    }

    @EndpointMapping(uri = "/csv/all", method = GET)
    public static AbstractResponse getAllRecords() throws IOException {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
        csvApp.read();
        return Json.sendResponse(csvApp.convertDataToJson(csvApp.getActiveEditor().getDataList()), OK_200);
    }


    @EndpointMapping(uri = "/csv/{number}", method = GET)
    public AbstractResponse getRecordById(@RequestParam("number") String number) throws IOException {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
        csvApp.read();
        String[] string = csvApp.getActiveEditor().getDataList().get(Integer.parseInt(number));
        return Json.sendResponse(Arrays.toString(string), OK_200);
    }


    @EndpointMapping(uri = "/csv/{number}", method = PUT)
    public AbstractResponse updateRecord(
            @RequestParam("number") String number,
            @BodyParam("body") String body
    ) throws IOException {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
        csvApp.update(Integer.parseInt(number), new String[]{body});
        return Html.renderTemplate(STR."<p>Row \{number} was updated! </p>", CREATED_201);
    }


    @EndpointMapping(uri = "/csv/{number}", method = DELETE)
    public AbstractResponse deleteRecord(@RequestParam("number") String number) throws IOException {
        String json = "{\"number\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        return Html.renderTemplate(STR."<p>Row \{number} was deleted! </p>", OK_200);
    }
}
