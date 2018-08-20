package ca.cliky.cliky.services;

public class Response {
    public static final String BODY = "body";
    public static final String CODE = "networkResponseCode";
    public static final String EMPTY_RESPONSE = "";
    private String body;
    private int code;

    public Response(int code, String body) {
        code = code;
        body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
