package operations.images;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * La forma en que un pixel toma u color es la siguiente
 * int color =  4 Bytes
 * 00000000 00000000 00000000 00000000
 *
 * Por ejemplo: 255
 * 00000000 00000000 00000000 11111111
 * 00        00         00     FF
 * Esto representa el color AZUL
 *
 * Para representar el color verde
 * 00000000 00000000 11111111 00000000
 *
 * Para representar el color rojo (16711680)
 * 00000000 11111111 00000000 00000000
 * */
public class Imagen {
    private static final Logger logger = LogManager.getRootLogger();
    private String ruta;
    private int alto;
    private int ancho;
    private int[][] pixeles;
    private PropertyChangeSupport observado;

    public Imagen(int w, int h) {
        ancho = w;
        alto = h;
        pixeles = new int[w][h];
        observado = new PropertyChangeSupport(this);
    }

    public Imagen(String path) {
        observado = new PropertyChangeSupport(this);
        readFromFile(path);
    }

    public void readFromFile(String path) {
        BufferedImage bi = null;
        try {
            File f = new File(path);
            bi = ImageIO.read(f);
        } catch (IOException e) {
            logger.error("Imagen No pudo leer la imagen en la direccion {}", path);
        }
        this.setImage(bi);
    }

    public void addObservador(PropertyChangeListener observador) {
        observado.addPropertyChangeListener(observador);
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
    }

    public int[][] getPixeles() {
        return pixeles;
    }

    public void dibujar(Graphics g, int x0, int y0) {
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                Color c = new Color(pixeles[i][j]);
                g.setColor(c);
                g.drawLine(i,j,i,j);
            }
        }
    }

    public void setImage(BufferedImage image) {
        this.ancho = image.getWidth();
        this.alto = image.getHeight();

        pixeles = new int[ancho][alto];

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                pixeles[i][j] = image.getRGB(i,j);
            }
        }
        observado.firePropertyChange("IMAGEN", true, false);
    }

    public void notificarCambios() {
        observado.firePropertyChange("IMAGEN", true, false);
    }

    /**
     * Obtiene el color en un punto de la imagen
     * @param x La coordenada x (número de columna)
     * @param y La coordenada y (número de fila)
     * @return El color en forma de entero RGB
     */
    public int get(int x, int y) {
        return pixeles[x][y];
    }

    /**
     * Coloca el color indicado en la posicion x, y de la imagen
     * @param x La coordenada x (número de columna)
     * @param y La coordenada y (número de fila)
     * @param color El color como entero RGB
     */
    public void set(int x, int y, int color) {
        pixeles[x][y] = color;
    }

    public byte[] getBytes() {
        BufferedImage bi = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = (WritableRaster)bi.getRaster();

        int[] rasterPixels = transformarPuntos();
        raster.setPixels(0, 0, ancho, alto, rasterPixels);

        byte[] result;                              // <- lo que necesitas devolver
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bi, "png", baos);    // escribe la imagen en memoria
            baos.flush();                      // asegura que todo esté volcado

            result = baos.toByteArray();       // aquí tienes los bytes del PNG
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo codificar la imagen", e);
        }

        return result;
    }

    private int[] transformarPuntos() {
        int[] raster = new int[3*ancho*alto];

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                int red = (pixeles[i][j] & 0x00ff0000) >> 16;
                int green = (pixeles[i][j] >> 8) & 0x000000ff;
                int blue = pixeles[i][j] & 0x000000ff;

                int base = 3*(j*ancho + i);
                raster[base] = red;
                raster[base+1] = green;
                raster[base+2] = blue;
            }
        }
        return raster;
    }
}
