import com.server.app.ServerRunnableApplication;
import com.server.app.annotation.ServerApplication;

import com.server.http.enums.RESPONSE_TYPE;
import com.server.http.request.annotations.EndpointMapping;
import com.server.http.request.annotations.HttpMethods;
import com.server.http.request.annotations.RequestParam;
import com.server.http.request.annotations.Template;
import com.server.http.response.AbstractResponse;
import com.server.http.response.Html;
import com.server.http.response.Json;

import java.io.IOException;

import static com.server.http.enums.HTTP_METHOD.*;
import static com.server.http.enums.RESPONSE_TYPE.HTML;
import static com.server.http.enums.RESPONSE_TYPE.JSON;

@HttpMethods
@ServerApplication
public class App {
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
        // Логика для добавления новой записи в CSV файл и отправки ответа
        return Html.renderTemplate();
    }


    @EndpointMapping(uri = "/csv/all", method = GET, type = JSON)
    public static AbstractResponse getAllRecords() throws IOException {

        String json = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        return Json.sendResponse(json);
    }


    @EndpointMapping(uri = "/csv/{id}", method = GET , type = JSON)
    public static AbstractResponse getRecordById(@RequestParam("id") String id) throws IOException {

        String json = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        return Json.sendResponse(json);
    }


    @EndpointMapping(uri = "/csv/{id}", method = PUT, type = JSON)
    public static AbstractResponse updateRecord(@RequestParam("id") String id) throws IOException {

        String json = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        return Json.sendResponse(json);
    }


    @EndpointMapping(uri = "/csv/{id}", method = DELETE, type = JSON)
    public static AbstractResponse deleteRecord(@RequestParam("id") String id) throws IOException {

        String json = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        return Json.sendResponse(json);
    }
}
