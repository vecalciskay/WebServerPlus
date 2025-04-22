package operations.images.transform;

import operations.images.Imagen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoImageTransformation extends ImageTransformation {
    private static final Logger logger = LogManager.getRootLogger();

    public NoImageTransformation(Imagen src) {
        super(src);
    }

    @Override
    public void transform() {
        logger.info("NoImageTransofrmation No se hace ninguna transformación");
    }
}
