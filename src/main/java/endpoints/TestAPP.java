package endpoints;

import com.server.app.ServerRunnableApplication;
import com.server.app.annotation.ServerApplication;
import com.server.http.request.annotations.EndpointMapping;
import com.server.http.request.annotations.HttpMethods;
import com.server.http.request.annotations.RequestParam;

import static com.server.http.request.annotations.HTTP_METHOD.*;

@HttpMethods
@ServerApplication
public class TestAPP {
    public static void main(String[] args) {
        ServerRunnableApplication.run(TestAPP.class, args);
    }


    @EndpointMapping(uri = "/csv", method = GET)
    public static void getAllRecords() {
        // Логика для получения всех записей из CSV файла и отправки ответа
        System.out.println("GET /csv - Получение всех записей из CSV файла");
    }

    @RequestParam("id")
    @EndpointMapping(uri = "/csv/{id}", method = GET)
    public static void getRecordById(@RequestParam("id") String id) {
        // Logic for retrieving a specific record from the CSV file by its identifier and sending a response
        System.out.println(STR."GET /csv/\{id} - Retrieving a record from the CSV file by identifier");
    }

    @EndpointMapping(uri = "/csv", method = POST)
    public static void addRecord() {
        // Логика для добавления новой записи в CSV файл и отправки ответа
        System.out.println("POST /csv - Добавление новой записи в CSV файл");
    }

    @EndpointMapping(uri = "/csv/{id}", method = PUT)
    public static void updateRecord(@RequestParam("id") String id) {
        // Логика для обновления существующей записи в CSV файле по ее идентификатору и отправки ответа
        System.out.println(STR."PUT /csv/\{id} - Обновление записи в CSV файле");
    }

    @EndpointMapping(uri = "/csv/{id}", method = DELETE)
    public static void deleteRecord(@RequestParam("id") String id) {
        // Логика для удаления записи из CSV файла по ее идентификатору и отправки ответа
        System.out.println(STR."DELETE /csv/\{id} - Удаление записи из CSV файла");
    }
}
