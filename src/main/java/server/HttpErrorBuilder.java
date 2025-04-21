package server;

import server.errors.Error404;
import server.services.HttpServiceClient;

public class HttpErrorBuilder {
    public static HttpError build404(String path) {
        return new Error404(path);
    }
}
