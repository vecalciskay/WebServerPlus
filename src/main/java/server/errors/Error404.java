package server.errors;

import operations.http.MimeUtils;
import server.HttpError;

import java.nio.charset.StandardCharsets;

public class Error404 extends HttpError {
    public Error404(String path) {
        super(path);
    }

    @Override
    public byte[] getResponse() {
        String mime = MimeUtils.getMime(".html");
        byte[] allBytes = build404File();
        byte[] headerBytes = buildHeaders(mime, allBytes.length);

        byte[] response = new byte[headerBytes.length + allBytes.length];

        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(allBytes, 0, response, headerBytes.length, allBytes.length);
        
        return response;
    }

    private byte[] build404File() {
        StringBuilder file404 = new StringBuilder();
        file404.append("<html>\r\n");
        file404.append("<h1>404 - Archivo no encontrado</h1>\r\n");
        file404.append("<p>No se ha podido encontrar el archivo, vuelve atras</p>\r\n");
        file404.append("</html>\r\n");

        return file404.toString().getBytes(StandardCharsets.UTF_8);
    }
}
