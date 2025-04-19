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
}
