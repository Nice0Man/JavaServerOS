package server.endpoints;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

import at.favre.lib.crypto.bcrypt.BCrypt;
import server.http.models.HttpHeader;
import server.http.status.HTTP_STATUS_CODE;
import server.util.Config;

/**
 * Handles HTTP basic authentication
 *
 * @author tomiwa
 *
 */
public class Authenticator {
    private static final String CRLF = "\r\n";
    private static HttpHeader headers;

    /**
     * Validate if authorization credentials are valid
     *
     * @param s	       The incoming connection object
     * @param base64   The authorization header value
     * 				   extracted from the connection
     *                 object
     * @return         Returns true if authentication
     *                 is successful and false if
     *                 otherwise
     */
    public static boolean authenticate(Socket s, String base64) {
        String username = new String(Base64.getDecoder().decode(base64)).split(":")[0];
        String password = new String(Base64.getDecoder().decode(base64)).split(":")[1];
        if(username.equals(Config.username) && BCrypt.verifyer().verify(password.toCharArray(), Config.password).verified) {
            return true;
        } else {
            try {
                failedAuthenticationView(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Builds the view for a failed basic authentication
     *
     * @param socket The connection to send the response to
     * @throws IOException
     */
    public static void failedAuthenticationView(Socket socket) throws IOException {
        failedAuthenticationHeaders(401);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        for(String entry : headers.getHeaders()) {
            outputStream.writeBytes(entry);
        }
        outputStream.writeBytes(CRLF);
        outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body><h1> 401 " + HTTP_STATUS_CODE.UNAUTHORIZED_401 + "</h1></body></html>");
        outputStream.writeBytes(CRLF);
        outputStream.flush();
    }

    /**
     * Build the headers for a failed basic authentication
     *
     * @param statusCode The HTTP response code
     */
    public static void failedAuthenticationHeaders(int statusCode) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        headers = new HttpHeader.Builder()
                .addHTTP(String.valueOf(HTTP_STATUS_CODE.get(statusCode)))
                .addRow("Content-Type: ", "text/html")
                .addRow("Date: ", formatter.format(new Date(System.currentTimeMillis())))
                .addRow("Server: ", "Simple Java Web Server")
                .addRow("Connection: ", "close")
                .build();
    }

    /**
     *
     * @param socket The incoming request
     * @throws IOException
     */
    public static void responseView(Socket socket) throws IOException {
        responseHeaders(401);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        for(String entry : headers.getHeaders()) {
            outputStream.writeBytes(entry );
        }
        outputStream.writeBytes(CRLF);
        outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body></body></html>");
        outputStream.writeBytes(CRLF);
        outputStream.flush();
    }

    /**
     * Builds a HTTP header that requests for basic authentication
     *
     * @param statusCode The HTTP response code
     */
    public static void responseHeaders(int statusCode) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        headers = new HttpHeader.Builder()
                .addHTTP(String.valueOf(HTTP_STATUS_CODE.get(statusCode)))
                .addRow("Content-Type: ", "text/html")
                .addRow("Date: ", formatter.format(new Date(System.currentTimeMillis())))
                .addRow("Server: ", "Simple Java Web Server")
                .addRow("Connection: ", "close")
                .addRow("WWW-Authenticate: ", "Basic")
                .build();
    }
}
