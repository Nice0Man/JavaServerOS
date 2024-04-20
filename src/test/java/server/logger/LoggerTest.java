package server.logger;

import org.junit.jupiter.api.Test;
import server.ClientHandler;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {
    private static final String filePath = "src/test/resources/test-files/test.log";

//    @Test
//    void test_log() throws IOException {
//        // Create an instance of the Logger class
//        Logger logger = new Logger(filePath);
//        // Call the log method, passing an object for logging
//        logger.log(new ClientHandler());
//        // Check if the log file has been created
//        File logFile = new File(logger.getFilePath());
//        assertTrue(logFile.exists());
//    }
//
//    @Test
//    void test_get_file_path() {
//        // Create an instance of the Logger class
//        Logger logger = new Logger();
//        // Check that the file path to the log file is not empty
//        assertNotNull(logger.getFilePath());
//    }
//
//    @Test
//    void test_set_file_path() {
//        // Create an instance of the Logger class
//        Logger logger = new Logger();
//        // Set a new file path for the log file
//        logger.setFilePath(filePath);
//        // Check that the new file path for the log file is set
//        assertEquals(filePath, logger.getFilePath());
//    }
//
//    @Test
//    void test_log_io_exception() {
//        // Create an instance of the Logger class
//        Logger logger = new Logger();
//        // Set an invalid file path for the log file, which will cause an IOException
//        logger.setFilePath("/"); // Invalid file path
//        // Call the log method and expect an IOException
//        assertThrows(IOException.class, () -> logger.log(new ProcessingServer()));
//    }
//
//    @Test
//    void test_equals() {
//        // Create two instances of the Logger class with the same file path for the log file
//        Logger logger1 = new Logger();
//        logger1.setFilePath(filePath);
//        Logger logger2 = new Logger();
//        logger2.setFilePath(filePath);
//        // Check that the instances are equal
//        assertEquals(logger1, logger2);
//    }
//
//    @Test
//    void test_can_equal() {
//        // Create an instance of the Logger class
//        Logger logger = new Logger();
//        // Create an object of another class
//        Object obj = new Object();
//        // Check that an instance of the Logger class can only be equal to another instance of the Logger class
//        assertFalse(logger.canEqual(obj));
//    }
//
//    @Test
//    void test_hash_code() {
//        // Create two instances of the Logger class with the same file path for the log file
//        Logger logger1 = new Logger();
//        logger1.setFilePath(filePath);
//        Logger logger2 = new Logger();
//        logger2.setFilePath(filePath);
//        // Check that the hash codes of the instances are equal
//        assertEquals(logger1.hashCode(), logger2.hashCode());
//    }
//
//    @Test
//    void test_to_string() {
//        // Create an instance of the Logger class
//        Logger logger = new Logger();
//        // Check that the toString method returns a non-empty string
//        assertNotNull(logger.toString());
//    }
}
