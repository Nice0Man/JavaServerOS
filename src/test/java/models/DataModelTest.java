package models;

import org.junit.jupiter.api.Test;
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
        LocalDateTime newTime = LocalDateTime.of(2024, 4, 12, 10, 30, 0);
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
        assertTrue(data.canEqual(new DataModel(1, "Test Text", LocalDateTime.now())));
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
}
