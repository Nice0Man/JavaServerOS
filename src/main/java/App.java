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

import static com.server.http.enums.HTTP_METHOD.*;
import static com.server.http.enums.HTTP_STATUS_CODE.*;

@HttpMethods
@ServerApplication
public class App {
    private static final CSVApp csvApp = new CSVApp();

    static {
        CSVEditor editor = new CSVEditor();
        csvApp.setActiveEditor(editor);
        CommandHistory history = new CommandHistory();
        csvApp.setHistory(history);
    }

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
        csvApp.read();
        return Json.sendResponse(csvApp.convertDataToJson(csvApp.getActiveEditor().getDataList()), OK_200);
    }


    @EndpointMapping(uri = "/csv/{number}", method = GET)
    public static AbstractResponse getRecordById(@RequestParam("number") String number) {
        try {
            csvApp.get(Integer.parseInt(number));
            return Json.sendResponse(csvApp.convertDataToJson(csvApp.getActiveEditor().getData()), OK_200);
        } catch (IllegalArgumentException | IOException e){
            return Json.sendResponse(STR."\{e.getMessage()}", BAD_REQUEST_400);
        }
    }

    @EndpointMapping(uri = "/csv", method = POST)
    public static AbstractResponse addRecord(
            @BodyParam("data") String body
    ) {
        try {
            csvApp.create(new String[]{body});
            return Json.sendResponse(STR."Data: \{body} was created!", CREATED_201);
        } catch (IllegalArgumentException | IOException e){
            return Json.sendResponse(STR."\{e.getMessage()}", BAD_REQUEST_400);
        }
    }

    @EndpointMapping(uri = "/csv/{number}", method = PUT)
    public static AbstractResponse updateRecord(
            @RequestParam("number") String number,
            @BodyParam("data") String body
    ) {
        try {
            csvApp.update(Integer.parseInt(number), new String[]{body});
            return Json.sendResponse(STR."Row \{number} was updated!", CREATED_201);
        } catch (IllegalArgumentException | IOException e){
            return Json.sendResponse(STR."\{e.getMessage()}", BAD_REQUEST_400);
        }
    }


    @EndpointMapping(uri = "/csv/{number}", method = DELETE)
    public static AbstractResponse deleteRecord(@RequestParam("number") String number) {
        try {
            csvApp.delete(Integer.parseInt(number));
            return Json.sendResponse(STR."Row \{number} was updated!", CREATED_201);
        } catch (IllegalArgumentException | IOException e){
            return Json.sendResponse(STR."\{e.getMessage()}", BAD_REQUEST_400);
        }
    }
}
