package server;

import server.errors.Error404;

public class HttpErrorBuilder {
    public static HttpError build404(String path) {
        return new Error404(path);
    }
}
