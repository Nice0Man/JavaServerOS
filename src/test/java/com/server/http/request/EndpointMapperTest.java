package com.server.http.request;

import com.server.http.enums.HTTP_METHOD;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class EndpointMapperTest {
    @Test
    void getInstance() {
    }

    @Test
    void mapEndpoints() {
    }

    @Test
    void handleRequest() {
    }

    @Test
    void getSocket() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
        String uri = "http://localhost:8080/csv/test/{id}";

        String res = convertUriToRegex(uri);
        System.out.println(res);
    }
    private static String convertUriToRegex(String uri) {
        // Escape special characters in URI and replace parameters with regex patterns
        return uri.replaceAll("\\{([^}]*)}", "\\\\d+");
    }
    private static boolean matchesEndpoint(String requestUri, String endpointRegex, HTTP_METHOD httpMethod) {
        String[] parts = endpointRegex.split("\\s+");
        if (parts.length < 2) {
            return false;
        }
        String methodPart = parts[0];
        String regexPart = parts[1];
        if (!httpMethod.name().equalsIgnoreCase(methodPart)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regexPart);
        System.out.println(pattern);
        Matcher matcher = pattern.matcher(requestUri);
        System.out.println(matcher.matches());
        return matcher.matches();
    }

    @Test
    void testMatchesEndpoint() {
        // Define sample requestUri and endpointRegex
        String uriPattern = "GET /example/test/{id}";
        uriPattern = convertUriToRegex(uriPattern);
        System.out.println(uriPattern);

        String requestUri = "/example/test/113";

        // Test when the HTTP_METHOD matches and the requestUri matches the regex
        assertTrue(matchesEndpoint(requestUri, uriPattern, HTTP_METHOD.GET));

        // Test when the HTTP_METHOD matches but the requestUri does not match the regex
        assertFalse(matchesEndpoint("/different", convertUriToRegex(uriPattern), HTTP_METHOD.GET));

        // Test when the HTTP_METHOD does not match
        assertFalse(matchesEndpoint(requestUri, convertUriToRegex(uriPattern), HTTP_METHOD.POST));

        // Test when endpointRegex does not contain method and regex parts
        assertFalse(matchesEndpoint(requestUri, "GET", HTTP_METHOD.GET));
    }

    @Test
    void setSocket() {
    }
}
