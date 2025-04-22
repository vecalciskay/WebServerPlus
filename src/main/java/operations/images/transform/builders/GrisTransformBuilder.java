package operations.images.transform.builders;

import config.ServerConfiguration;
import operations.images.Imagen;
import operations.images.enums.ImageTransformationType;
import operations.images.transform.Gris;
import operations.images.transform.ImageTransformation;

public class GrisTransformBuilder extends ImageTransformBuilder {
    public GrisTransformBuilder(String args) {
        command = ImageTransformationType.gris;
        arguments = args;

        ServerConfiguration config = ServerConfiguration.getOrCreate();
        String path = config.getWwwroot();
        if (!arguments.startsWith("/"))
            path += "/" + arguments;
        else
            path += arguments;

        targetToTransform = new Imagen(path);
    }

    @Override
    public ImageTransformation build() {
        return new Gris(targetToTransform);
    }
}
