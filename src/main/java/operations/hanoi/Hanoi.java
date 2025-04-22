package operations.hanoi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.json.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Hanoi {
    private static final Logger logger = LogManager.getRootLogger();
    private Torre[] torres;
    private final PropertyChangeSupport observado;
    private final int numeroAnillos;

    public Hanoi(int n) {
        this(n, 0);
    }

    /**
     * El número de la torre que tiene los anillos debe ser un número entre 0, 1, y 2.
     * @param n el numero de anillos que se va a colocar en la torre
     * @param numeroTorreConAnillos la torre donde se colocaran los anillos inicialmente
     */
    public Hanoi(int n, int numeroTorreConAnillos) {
        numeroAnillos = n;
        reset(numeroTorreConAnillos);
        observado = new PropertyChangeSupport(this);
    }

    public void reset(int numeroTorreConAnillos) {
        torres = new Torre[3];
        for (int i = 0; i < torres.length; i++) {
            torres[i] = (i == numeroTorreConAnillos ? new Torre(numeroAnillos,i) : new Torre(i));
        }
    }

    public void addObserver(PropertyChangeListener observador) {
        observado.addPropertyChangeListener(observador);
    }

    /**
     * Este metodo ejecuta la solución de una estructura de Hanoi. Las siguientes
     * condiciones se ajustan
     * @param de El número de torre de inicio de juego
     * @param a El número de torre a donde se deben llevar los anillos
     * @param hastaMovimiento El número de movimiento hasta el cual se realizará el hanoi. Si es 0, se hacen todos
     */
    public void solve(int de, int a, int hastaMovimiento) {
        reset(de);
        int[] numeroActualMovimientos = new int[1];
        logger.info("Hanoi Resuelve hanoi de {} a {} para {} anillos hasta movimiento {}",
                de, a, numeroAnillos, hastaMovimiento);
        solveRecursivo(de, a, numeroAnillos, numeroActualMovimientos, hastaMovimiento);
    }

    private void solveRecursivo(int de, int a, int n,
                                int[] numeroActualMovimientos,
                                int hastaMovimiento) {
        if (hastaMovimiento > 0 &&
                numeroActualMovimientos[0] == hastaMovimiento)
            return;

        if (n == 1) {
            logger.info("Hanoi Movimiento de {} a {}", de, a);
            moverAnillo(de, a);
            notificarCambios();
            numeroActualMovimientos[0]++;
            return;
        }
        int pp = 3 - de - a;
        solveRecursivo(de, pp, n-1, numeroActualMovimientos, hastaMovimiento);
        solveRecursivo(de, a, 1, numeroActualMovimientos, hastaMovimiento);
        solveRecursivo(pp, a, n-1, numeroActualMovimientos, hastaMovimiento);
    }

    private void notificarCambios() {
        observado.firePropertyChange("HANOI", true, false);
    }

    public void moverAnillo(int de, int a) {
        torres[a].colocar(torres[de].sacar());
    }

    /**
     * Obtiene el JSON completo, como objeto, del Hanoi, con sus torres y a su vez con sus anillos.
     * @return el json como objeto. Este objeto se puede imprimir fácilmente.
     */
    public JsonObject getJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonObject torre1 = torres[0].getJson();
        JsonObject torre2 = torres[1].getJson();
        JsonObject torre3 = torres[2].getJson();

        arrayBuilder.add(torre1);
        arrayBuilder.add(torre2);
        arrayBuilder.add(torre3);
        JsonArray torresArray = arrayBuilder.build();

        builder.add("torres", torresArray);

        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (Torre torre : torres) {
            result.append(torre.toString()).append("\n");
        }

        return result.toString();
    }
}
