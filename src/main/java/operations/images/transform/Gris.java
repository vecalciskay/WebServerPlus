package operations.images.transform;

import operations.images.Imagen;

public class Gris extends ImageTransformation {
    public Gris(Imagen img) {
        super(img);
    }
    @Override
    public void transform() {
        for (int i = 0; i < target.getAncho(); i++) {
            for (int j = 0; j < target.getAlto(); j++) {
                // Si r=50, g= 80, b= 200
                // x
                // r = x, g=x, b= x
                int r = (0x00FF0000 & target.get(i,j)) >> 16;
                int g = (0x0000FF00 & target.get(i,j)) >> 8;
                int b = (0x000000FF & target.get(i,j));

                int x = (r+g+b) / 3;
                target.set(i,j, x | (x << 8) | (x << 16));
            }
        }
        target.notificarCambios();
    }
}
