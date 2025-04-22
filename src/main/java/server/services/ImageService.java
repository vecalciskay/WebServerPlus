package server.services;

import config.ServerConfiguration;
import operations.http.MimeUtils;
import operations.images.Imagen;
import operations.images.transform.builders.ImageTransformBuilder;
import operations.images.transform.ImageTransformation;
import operations.images.enums.ImageTransformationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ClientService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ImageService extends ClientService {
    private static final Logger logger = LogManager.getRootLogger();
    private final String transformAndArguments;
    private final ImageTransformation transformation;

    /**
     * The command has the image word. The args have all the information about the image transformation
     * that is requested.
     * @param id client id
     * @param cmd image command
     * @param args the image transformation requested along with the arguments
     * @param input the lines
     * @param output the output stream
     */
    public ImageService(int id, String cmd, String args, List<String> input, OutputStream output) {
        super(id, input, output);

        transformAndArguments = args;
        ImageTransformBuilder builder = ImageTransformBuilder.get(transformAndArguments);
        transformation = builder.build();
    }
    @Override
    public void start() {
        transformation.transform();

        byte[] imageBytes = transformation.getTarget().getBytes();
        byte[] headerBytes = buildHeaders(MimeUtils.getMime("png"), imageBytes.length);

        byte[] response = new byte[headerBytes.length + imageBytes.length];

        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(imageBytes, 0, response, headerBytes.length, imageBytes.length);
        try {
            output.write(response);
            output.flush();

            logger.info("{} Se paso una imagen transformada de {} bytes", getName(), imageBytes.length);
        } catch (IOException e) {
            logger.error("{} Hubo un error de IO", getName(), e);
        }
    }
}
