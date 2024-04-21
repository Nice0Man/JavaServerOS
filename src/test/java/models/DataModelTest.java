package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import com.server.http.models.DataModel;

import java.io.IOException;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DataModelTest {

    @Test
    void test_get_id() {
        DataModel data = new DataModel(1, "Test Text", LocalDateTime.now());
        assertEquals(1, data.getId());
    }

    @Test
    void test_get_text() {
        DataModel data = new DataModel(1, "Test Text", LocalDateTime.now());
        assertEquals("Test Text", data.getText());
    }

    @Test
    void test_get_time() {
        LocalDateTime now = LocalDateTime.now();
        DataModel data = new DataModel(1, "Test Text", now);
        assertEquals(now, data.getTime());
    }

    @Test
    void test_set_id() {
        DataModel data = new DataModel(1, "Test Text", LocalDateTime.now());
        data.setId(2);
        assertEquals(2, data.getId());
    }

    @Test
    void test_set_text() {
        DataModel data = new DataModel(1, "Test Text", LocalDateTime.now());
        data.setText("New Text");
        assertEquals("New Text", data.getText());
    }

    @Test
    void test_set_time() {
        LocalDateTime now = LocalDateTime.now();
        DataModel data = new DataModel(1, "Test Text", now);
        LocalDateTime newTime = LocalDateTime.of(2024, 4, 12, 10, 30, 30);
        data.setTime(newTime);
        assertEquals(newTime, data.getTime());
    }

    @Test
    void test_equals() {
        LocalDateTime now = LocalDateTime.now();
        DataModel data1 = new DataModel(1, "Test Text", now);
        DataModel data2 = new DataModel(1, "Test Text", now);
        assertEquals(data1, data2);
    }

    @Test
    void test_can_equal() {
        DataModel data = new DataModel(1, "Test Text", LocalDateTime.now());
        assertEquals(data, new DataModel(1, "Test Text", LocalDateTime.now()));
    }

    @Test
    void test_hash_code() {
        LocalDateTime now = LocalDateTime.now();
        DataModel data1 = new DataModel(1, "Test Text", now);
        DataModel data2 = new DataModel(1, "Test Text", now);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    void test_to_string() {
        LocalDateTime now = LocalDateTime.now();
        DataModel data = new DataModel(1, "Test Text", now);
        assertEquals("DataModel(id=1, text=Test Text, time=" + now + ")", data.toString());
    }

    @Test
    void test_json_to_class_conversion() {
        // Пример JSON строки
        String json = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
        // Создание объекта ObjectMapper для преобразования JSON в объект Java
        ObjectMapper objectMapper = new ObjectMapper();
        // Добавление модуля поддержки Java 8 Date/Time API
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Преобразование JSON в объект DataModel
            DataModel dataModel = objectMapper.readValue(json, DataModel.class);
            // Проверка правильности преобразования
            assertEquals(1, dataModel.getId());
            assertEquals("Sample text", dataModel.getText());
            assertEquals(LocalDateTime.parse("2022-04-09T12:30:00"), dataModel.getTime());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void test_class_to_json_conversion() {
        // Создание объекта DataModel
        DataModel dataModel = new DataModel(1, "Sample text", LocalDateTime.parse("2022-04-09T12:30:00"));
        // Создание объекта ObjectMapper для преобразования объекта Java в JSON
        ObjectMapper objectMapper = new ObjectMapper();
        // Добавление модуля поддержки Java 8 Date/Time API
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Преобразование объекта DataModel в JSON
            String json = objectMapper.writeValueAsString(dataModel);
            // Проверка правильности преобразования
            String expectedJson = "{\"id\":1,\"text\":\"Sample text\",\"time\":\"2022-04-09T12:30:00\"}";
            assertEquals(expectedJson, json);
        } catch (JsonProcessingException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
