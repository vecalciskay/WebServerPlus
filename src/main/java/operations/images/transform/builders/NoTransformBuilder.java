package operations.images.transform.builders;

import operations.images.transform.ImageTransformation;
import operations.images.transform.NoImageTransformation;

public class NoTransformBuilder extends ImageTransformBuilder {
    public NoTransformBuilder() {

    }

    @Override
    public ImageTransformation build() {
        return new NoImageTransformation(targetToTransform);
    }
}
