package operations.hanoi;

import javax.json.*;
import java.util.Stack;

public class Torre {
    private final Stack<Anillo> anillos;

    public Torre(int n) {
        anillos = new Stack<Anillo>();
        for (int i = n; i > 0; i--) {
            anillos.push(new Anillo(i));
        }
    }

    public Torre() {
        anillos = new Stack<Anillo>();
    }

    public Stack<Anillo> getAnillos() {
        return anillos;
    }

    public JsonObject getJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Anillo a : anillos) {
            arrayBuilder.add(a.getJson());
        }
        JsonArray anillosArray = arrayBuilder.build();
        builder.add("anillos", anillosArray);

        return builder.build();
    }

    public Anillo sacar() {
        return anillos.pop();
    }

    public void colocar(Anillo a) {
        anillos.push(a);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("|-");
        for (Anillo obj : anillos) {
            result.append(obj);
        }
        return result.toString();
    }
}
