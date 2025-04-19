package operations.hanoi;

import javax.json.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Hanoi {
    private Torre[] torres;
    private PropertyChangeSupport observado;
    private int numeroAnillos;

    public Hanoi(int n) {
        this(n, 0);
    }

    /**
     * El numero de la torre que tiene los anillos debe ser un número entre 0, 1, y 2.
     * @param n
     * @param numeroTorreConAnillos
     */
    public Hanoi(int n, int numeroTorreConAnillos) {
        numeroAnillos = n;
        reset(numeroTorreConAnillos);
        observado = new PropertyChangeSupport(this);
    }

    public void reset(int numeroTorreConAnillos) {
        torres = new Torre[3];
        for (int i = 0; i < torres.length; i++) {
            torres[i] = (i == numeroTorreConAnillos ? new Torre(numeroAnillos) : new Torre());
        }
    }

    public void addObserver(PropertyChangeListener observador) {
        observado.addPropertyChangeListener(observador);
    }

    /**
     * Este metodo ejecuta la solución de una estructura de Hanoi. Las siguientes
     * condiciones se ajustan
     * @param de
     * @param a
     * @param n
     * @param hastaMovimiento
     */
    public void solve(int de, int a, int n, int hastaMovimiento) {
        reset(de);
        solveRecursivo(de, a, n, 0, hastaMovimiento);
    }

    private void solveRecursivo(int de, int a, int n,
                                int numeroActualMovimientos,
                                int hastaMovimiento) {
        if (n == 1) {
            moverAnillo(de, a);
            notificarCambios();
            numeroActualMovimientos++;
            if (numeroActualMovimientos == hastaMovimiento)
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
}
