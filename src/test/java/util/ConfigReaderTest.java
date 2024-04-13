package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigReaderTest {

    @Test
    public void test_get_property_valid_key_returns_value() {
        ConfigReader.setCONFIG_FILE("src/test/resources/test-config.properties");

        String key = "test.key";
        String expectedValue = "testValue";
        String actualValue = ConfigReader.getProperty(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void test_get_property_invalid_key_returns_null() {
        ConfigReader.setCONFIG_FILE("src/test/resources/test-config.properties");

        String key = "invalid.key";
        String actualValue = ConfigReader.getProperty(key);
        assertNull(actualValue);
    }

    @Test
    void test_set_config_file_valid_path() {
        String expectedValue = "test-config-path";
        ConfigReader.setCONFIG_FILE("test-config-path");
        assertEquals(expectedValue,ConfigReader.getCONFIG_FILE());
    }

    @Test
    void test_set_config_file_invalid_path() {
        ConfigReader.setCONFIG_FILE("src/test/wrong-path/test-config.properties");
        assertNull(ConfigReader.getProperty("some.key"));
    }
}
