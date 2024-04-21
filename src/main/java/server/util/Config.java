package server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for reading settings from a configuration file.
 */
public final class Config {
    public static String username = null;
    public static String password = null;
    public static int PORT;
    public static final int PORT_MIN = 1;
    public static final int PORT_MAX = 65535;
    public static int WORKER_THREADS;
    public static int CONNECTION_QUEUE;


    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final Properties properties = new Properties();
    private static Config instance;

    public static  enum METHODS {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH
    }

    private Config() throws IOException {
        loadConfig();
    }

    public static Config getInstance() throws IOException {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private static void loadConfig() throws IOException, FileNotFoundException {
        try (FileInputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(inputStream);
            // Set values for the static fields
            username = getProperty("username");
            password = getProperty("password");
            PORT = Integer.parseInt(getProperty("port"));
            WORKER_THREADS = Integer.parseInt(getProperty("worker_threads"));
            CONNECTION_QUEUE = Integer.parseInt(getProperty("connection_queue"));
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
