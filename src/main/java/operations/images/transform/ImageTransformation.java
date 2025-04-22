package operations.images.transform;

import operations.images.Imagen;

public abstract class ImageTransformation implements IImageTransformation {
    protected Imagen target;

    public ImageTransformation(Imagen img) {
        target = img;
    }

    public Imagen getTarget() {
        return target;
    }
}
