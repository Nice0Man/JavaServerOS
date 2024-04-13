package util;

import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for reading settings from a configuration file.
 */
public final class ConfigReader {

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private ConfigReader() {
    }

    /** Default path to the configuration file. */
    @Getter
    @Setter
    private static String CONFIG_FILE = "src/main/resources/config.properties";

    /**
     * Retrieves the value of a property from the configuration file.
     *
     * @param key the key of the property
     * @return the value of the property, or null if an error occurs while reading the file
     */
    public static String getProperty(String key) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
            return null;
        }
    }
}
