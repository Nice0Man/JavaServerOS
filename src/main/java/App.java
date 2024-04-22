import com.server.app.ServerRunnableApplication;
import com.server.app.annotation.ServerApplication;

import com.server.http.request.annotations.EndpointMapping;
import com.server.http.request.annotations.HttpMethods;
import com.server.http.request.annotations.RequestParam;
import com.server.http.request.annotations.Template;
import com.server.http.response.Response;
import com.server.http.status.HTTP_STATUS_CODE;

import static com.server.http.models.HTTP_METHOD.*;

@HttpMethods
@ServerApplication
public class App {
    public static void main(String[] args) {
        ServerRunnableApplication.run(App.class, args);
    }

    @Template(path = "index.html")
    @EndpointMapping(uri = "/", method = GET)
    public static Response helloWorld(){
        return new Response(HTTP_STATUS_CODE.OK_200);
    }

    @Template(path = "getAllRecords.html")
    @EndpointMapping(uri = "/csv", method = GET)
    public static Response getAllRecords() {
        // Логика для получения всех записей из CSV файла и отправки ответа
        System.out.println("GET /csv - Получение всех записей из CSV файла");
        return new Response(HTTP_STATUS_CODE.OK_200);
    }

    @Template(path = "getRecordById.html")
    @EndpointMapping(uri = "/csv/{id}", method = GET)
    public static Response getRecordById(@RequestParam("id") String id) {
        // Logic for retrieving a specific record from the CSV file by its identifier and sending a response
        System.out.println(STR."GET /csv/\{id} - Retrieving a record from the CSV file by identifier");
        return new Response(HTTP_STATUS_CODE.OK_200);
    }

    @Template(path = "addRecord.html")
    @EndpointMapping(uri = "/csv/add", method = POST)
    public static Response addRecord() {
        // Логика для добавления новой записи в CSV файл и отправки ответа
        System.out.println("POST /csv/add - Добавление новой записи в CSV файл");
        return new Response(HTTP_STATUS_CODE.OK_200);
    }

    @Template(path = "updateRecord.html")
    @EndpointMapping(uri = "/csv/update/{id}", method = PUT)
    public static Response updateRecord(@RequestParam("id") String id) {
        // Логика для обновления существующей записи в CSV файле по ее идентификатору и отправки ответа
        System.out.println(STR."PUT /csv/update\{id} - Обновление записи в CSV файле");
        return new Response(HTTP_STATUS_CODE.OK_200);
    }

    @Template(path = "deleteRecord.html")
    @EndpointMapping(uri = "/csv/delete/{id}", method = DELETE)
    public static Response deleteRecord(@RequestParam("id") String id) {
        // Логика для удаления записи из CSV файла по ее идентификатору и отправки ответа
        System.out.println(STR."DELETE /csv/delete/\{id} - Удаление записи из CSV файла");
        return new Response(HTTP_STATUS_CODE.OK_200);
    }
}
