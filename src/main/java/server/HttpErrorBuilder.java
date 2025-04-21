package server;

import server.errors.Error404;
import server.services.HttpServiceClient;

public class HttpErrorBuilder {
    public static HttpError build404(HttpServiceClient httpServiceClient) {
        return new Error404();
    }
}
