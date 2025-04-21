package server.services;

import config.ServerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ClientService;
import server.HttpErrorBuilder;
import server.HttpError;
import operations.http.MimeUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpServiceClient extends ClientService {

    private static final Logger logger = LogManager.getRootLogger();
    private static final int MAX_NUMBER_CHARS_EXTENSION = 4;
    private final String pathAndQuery;
    private final String httpCommand;
    private String pathOnly;
    private String argsOnly;

    public HttpServiceClient(int id, String operation, String path, List<String> input, OutputStream output) {
        super(id, input, output);
        httpCommand = operation;
        pathAndQuery = path;
        pathOnly = "";
        argsOnly = "";
    }

    @Override
    public void start() {
        ServerConfiguration config = ServerConfiguration.getOrCreate();

        logger.info("{} Solamente sabemos GET, operacion pedida es {}", getName(), httpCommand );

        String regex = "([a-zA-Z0-9_\\-\\.\\/]+)(\\?.+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pathAndQuery);
        if (matcher.find()) {
            pathOnly = matcher.group(1);
            if (matcher.groupCount() > 2) {
                argsOnly = matcher.group(2);
            }
        }

        if (pathOnly.isEmpty()) {
            logger.warn("{} estaba vacia la solicitud, no devuelve nada", getName());
            return;
        }

        if (!argsOnly.isEmpty()) {
            logger.warn("{} Los args son {} y no serán tomados en cuenta", getName(), argsOnly);
        }

        String completePath = config.getWwwroot() + pathOnly;
        logger.info("{} Busca el archivo {}", getName(), completePath);

        sendFileThroughSocketAndClose(completePath);
    }

    private void sendFileThroughSocketAndClose(String completePath) {

        Path path = Path.of(completePath);

        String mime;
        int lastDot = completePath.lastIndexOf('.',
                completePath.length() - MAX_NUMBER_CHARS_EXTENSION + 1);
        if (lastDot < 0) {
            mime = MimeUtils.getDefault();
        } else {
            String extension = completePath.substring(lastDot);
            mime = MimeUtils.getMime(extension);
        }

        try {
            if (Files.exists(path)) {
                if (Files.isDirectory(path)) {
                    logger.warn("{} El GET se solicito a una carpeta, no devuelve nada", getName());
                    return;
                }
                logger.info("{} Archivo existe, devolviendo completo", getName());

                byte[] allBytes = Files.readAllBytes(path);
                byte[] headerBytes = buildHeaders(mime, allBytes.length);

                byte[] response = new byte[headerBytes.length + allBytes.length];

                System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
                System.arraycopy(allBytes, 0, response, headerBytes.length, allBytes.length);

                output.write(response);
                output.flush();

                logger.info("{} Se paso un archivo {} de {} bytes", getName(), mime, allBytes.length);

            } else {
                logger.warn("{} El archivo solicitado no existe, entonces devuelve 404", getName());

                HttpError error404 = HttpErrorBuilder.build404(completePath);
                byte[] response = error404.getResponse();

                output.write(response);
                output.flush();

                logger.info("{} Se paso un mensaje de error 404", getName());
            }
        } catch (IOException e) {
            logger.error("{} Hubo un error de IO", getName(), e);
        }
    }
}
