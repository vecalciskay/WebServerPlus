package operations.hanoi;

import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HanoiTest {
    @Test
    void JsonFormatSimpleTest() {

        Hanoi h = new Hanoi(3);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(h.getJson());
        jsonWriter.close();
        String result = stringWriter.toString();

        String expected = "{\"torres\":[{\"anillos\":[{\"tamano\":3},{\"tamano\":2},{\"tamano\":1}]},{\"anillos\":[]},{\"anillos\":[]}]}";
        assertEquals(expected, result);
    }

    @Test
    void resolverHanoi() {
        Hanoi h = new Hanoi(3, 0);
        h.solve(0,2,0);

        String expected = "|-\n|-\n|-3-2-1-\n";
        String result = h.toString();

        assertEquals(expected, result);
    }

    @Test
    void resolverHanoiHastaMovimiento() {
        Hanoi h = new Hanoi(3, 1);
        h.solve(1,0,4);

        String expected = "|-3-\n|-\n|-2-1-\n";
        String result = h.toString();

        assertEquals(expected, result);
    }
}
