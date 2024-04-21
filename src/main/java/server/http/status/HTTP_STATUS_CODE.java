package server.http.status;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum HTTP_STATUS_CODE {
    // Informational responses (100–199)
    CONTINUE_100(100, "Continue"),
    SWITCHING_PROTOCOLS_101(101, "Switching Protocols"),
    PROCESSING_102(102, "Processing"),

    // Successful responses (200–299)
    OK_200(200, "OK"),
    CREATED_201(201, "Created"),
    ACCEPTED_202(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION_203(203, "Non-Authoritative Information"),
    NO_CONTENT_204(204, "No Content"),
    RESET_CONTENT_205(205, "Reset Content"),
    PARTIAL_CONTENT_206(206, "Partial Content"),
    MULTI_STATUS_207(207, "Multi-Status"),

    // Redirection messages (300–399)
    MULTIPLE_CHOICES_300(300, "Multiple Choices"),
    MOVED_PERMANENTLY_301(301, "Moved Permanently"),
    FOUND_302(302, "Found"),
    SEE_OTHER_303(303, "See Other"),
    NOT_MODIFIED_304(304, "Not Modified"),
    USE_PROXY_305(305, "Use Proxy"),
    SWITCH_PROXY_306(306, "Switch Proxy"),
    TEMPORARY_REDIRECT_307(307, "Temporary Redirect"),
    PERMANENT_REDIRECT_308(308, "Permanent Redirect"),

    // Client error responses (400–499)
    BAD_REQUEST_400(400, "Bad Request"),
    UNAUTHORIZED_401(401, "Unauthorized"),
    PAYMENT_REQUIRED_402(402, "Payment Required"),
    FORBIDDEN_403(403, "Forbidden"),
    NOT_FOUND_404(404, "Not Found"),
    METHOD_NOT_ALLOWED_405(405, "Method Not Allowed"),
    NOT_ACCEPTABLE_406(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED_407(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT_408(408, "Request Timeout"),
    CONFLICT_409(409, "Conflict"),
    GONE_410(410, "Gone"),
    LENGTH_REQUIRED_411(411, "Length Required"),
    PRECONDITION_FAILED_412(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE_413(413, "Payload Too Large"),
    URI_TOO_LONG_414(414, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE_415(415, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE_416(416, "Range Not Satisfiable"),
    EXPECTATION_FAILED_417(417, "Expectation Failed"),
    IM_A_TEAPOT_418(418, "I'm a teapot"),
    MISDIRECTED_REQUEST_421(421, "Misdirected Request"),
    UNPROCESSABLE_ENTITY_422(422, "Unprocessable Entity"),
    LOCKED_423(423, "Locked"),
    FAILED_DEPENDENCY_424(424, "Failed Dependency"),
    UPGRADE_REQUIRED_426(426, "Upgrade Required"),
    PRECONDITION_REQUIRED_428(428, "Precondition Required"),
    TOO_MANY_REQUESTS_429(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE_431(431, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS_451(451, "Unavailable For Legal Reasons"),

    // Server error responses (500–599)
    INTERNAL_SERVER_ERROR_500(500, "Internal Server Error"),
    NOT_IMPLEMENTED_501(501, "Not Implemented"),
    BAD_GATEWAY_502(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE_503(503, "Service Unavailable"),
    GATEWAY_TIMEOUT_504(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED_505(505, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES_506(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE_507(507, "Insufficient Storage"),
    LOOP_DETECTED_508(508, "Loop Detected"),
    NOT_EXTENDED_510(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED_511(511, "Network Authentication Required");

    private final int code;
    private final String text;

    // Static map to store status codes and their corresponding texts
    private static final Map<Integer, String> codeToTextMap = new HashMap<>();

    // Initialize the map statically
    static {
        for (HTTP_STATUS_CODE status : HTTP_STATUS_CODE.values()) {
            codeToTextMap.put(status.getCode(), status.getText());
        }
    }
    // Constructor and other methods...

    public static String get(int statusCode) {
        return codeToTextMap.getOrDefault(statusCode, "Unknown Status");
    }

    HTTP_STATUS_CODE(int code, String text) {
        this.code = code;
        this.text = text;
    }
    @Override
    public String toString() {
        return code + " " + text;
    }
}
