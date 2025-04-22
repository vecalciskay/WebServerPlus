package operations.images.transform.builders;


import operations.images.Imagen;
import operations.images.enums.ImageTransformationType;
import operations.images.transform.ImageTransformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ImageTransformBuilder {
    private static final Logger logger = LogManager.getRootLogger();
    protected Imagen targetToTransform;
    protected ImageTransformationType command;
    protected String arguments;

    public static ImageTransformBuilder get(String cmdAndArgs) {

        String possibleOperations = Arrays.stream(ImageTransformationType.values())
                .map(Enum::name)          // or e -> e.toString()
                .collect(Collectors.joining("|"));
        String regexPath = "(" + possibleOperations + ")/(.+)";
        Pattern pattern = Pattern.compile(regexPath);
        Matcher matcher = pattern.matcher(cmdAndArgs);
        if (matcher.find()) {
            String transformation = matcher.group(1);
            String transformationArguments = matcher.group(2);

            logger.info("ImageTransformBuilder La solicitud fue {} con argumentos: {}",
                    transformation, transformationArguments);

            if (transformation.equals(ImageTransformationType.gris.toString()))
                return new GrisTransformBuilder(transformationArguments);
        }
        return new NoTransformBuilder(cmdAndArgs);
    }

    public abstract ImageTransformation build();
}
