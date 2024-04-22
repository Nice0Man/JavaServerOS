package com.server.http.request;

import com.server.http.request.annotations.EndpointMapping;
import com.server.http.models.HTTP_METHOD;
import com.server.http.request.annotations.RequestParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EndpointMapperTest {

    @BeforeEach
    void setUp() {
        EndpointMapper.setEndpointMap(new HashMap<>());
    }

    @Test
    void test_map_endpoints() {
        // Test mapping of endpoints
        // Setup
        Class<?> testClass = TestEndpoints.class;

        // Exercise
        EndpointMapper.mapEndpoints(testClass);

        // Verify
        assertFalse(EndpointMapper.getEndpointMap().isEmpty());
    }

    @Test
    void test_handle_request_with_valid_endpoint() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Test handling of requests with a valid endpoint
        // Setup
        EndpointMapper.mapEndpoints(TestEndpoints.class);
        String requestUri = "/csv/123";
        HTTP_METHOD httpMethod = HTTP_METHOD.GET;
        EndpointMapper.handleRequest(requestUri, httpMethod);
    }

    @Test
    void test_handle_request_with_invalid_endpoint() {
        // Test handling of requests with an invalid endpoint
        // Setup
        EndpointMapper.mapEndpoints(TestEndpoints.class);
        String requestUri = "/invalid";
        HTTP_METHOD httpMethod = HTTP_METHOD.GET;

        // Exercise and Verify
        NoSuchMethodException exception = assertThrows(NoSuchMethodException.class, () -> EndpointMapper.handleRequest(requestUri, httpMethod));
        assertEquals(STR."Endpoint not found for request URI: \{requestUri}", exception.getMessage());
    }

    static class TestEndpoints {
//        @RequestParam("id")
        @EndpointMapping(uri = "/csv/{id}", method = HTTP_METHOD.GET)
        public static void getRecordById(@RequestParam("id") String id) {
            // Logic for retrieving a specific record from the CSV file by its identifier and sending a response
            System.out.println(STR."GET \{id} from - Retrieving a record from the CSV file by identifier");
        }
    }
}
