package server;

import java.nio.charset.StandardCharsets;

public abstract class HttpError {
    protected String path;

    public HttpError(String p) {
        path = p;
    }

    public abstract byte[] getResponse();

    protected byte[] buildHeaders(String mime, int length) {
        StringBuilder headers = new StringBuilder();
        String line = "HTTP/1.1 200 OK\r\n";
        headers.append(line);
        line = "Content-Type: " + mime + "\r\n";
        headers.append(line);
        line = "Content-Length: " + length + "\r\n\r\n";
        headers.append(line);

        return headers.toString().getBytes(StandardCharsets.UTF_8);
    }
}
