package server.http.status;

import com.server.http.status.HTTP_STATUS_CODE;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPSTATUSCODETest {

    @Test
    void test_get_code() {
        HTTP_STATUS_CODE statusCode = HTTP_STATUS_CODE.OK_200;
        assertEquals(200, statusCode.getCode());
    }

    @Test
    void test_get_text() {
        HTTP_STATUS_CODE statusCode = HTTP_STATUS_CODE.OK_200;
        assertEquals("OK", statusCode.getText());
    }

    @Test
    void test_values() {
        HTTP_STATUS_CODE[] values = HTTP_STATUS_CODE.values();
        assertEquals(59, values.length);
    }

    @Test
    void test_value_of() {
        HTTP_STATUS_CODE statusCode = HTTP_STATUS_CODE.valueOf("OK_200");
        assertEquals(HTTP_STATUS_CODE.OK_200, statusCode);
    }
}
