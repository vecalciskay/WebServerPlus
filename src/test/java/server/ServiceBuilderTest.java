package server;

import org.junit.jupiter.api.Test;
import server.services.ComplexService;
import server.services.HanoiService;
import server.services.HttpServiceClient;
import server.services.ImageService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceBuilderTest {
    @Test
    void ComandoGetSimpleHtmlTest() {
        String lineaGet = "GET /index.html HTTP/1.1";
        List<String> inputLines = new ArrayList<>();
        inputLines.add(lineaGet);
        OutputStream output = new ByteArrayOutputStream(50);

        ClientService service = ClientServiceBuilder.buildService(1, inputLines, output);

        boolean isHttpService = service instanceof HttpServiceClient;

        boolean expected = true;

        assertEquals(expected, isHttpService);
    }

    @Test
    void ComandoGetHanoiTest() {
        String lineaGet = "GET /__op__/hanoi/1_3_3_4 HTTP/1.1";
        List<String> inputLines = new ArrayList<>();
        inputLines.add(lineaGet);
        OutputStream output = new ByteArrayOutputStream(50);

        ClientService service = ClientServiceBuilder.buildService(1, inputLines, output);

        boolean isCorrectService = service instanceof HanoiService;

        boolean expected = true;

        assertEquals(expected, isCorrectService);
    }

    @Test
    void ComandoGetImageTest() {
        String lineaGet = "GET /__op__/images/gris/foto1.png HTTP/1.1";
        List<String> inputLines = new ArrayList<>();
        inputLines.add(lineaGet);
        OutputStream output = new ByteArrayOutputStream(50);

        ClientService service = ClientServiceBuilder.buildService(1, inputLines, output);

        boolean isCorrectService = service instanceof ImageService;

        boolean expected = true;

        assertEquals(expected, isCorrectService);
    }

    @Test
    void ComandoGetComplexTest() {
        String lineaGet = "GET /__op__/complex/add/3.4_5.2/2.1_8.4 HTTP/1.1";
        List<String> inputLines = new ArrayList<>();
        inputLines.add(lineaGet);
        OutputStream output = new ByteArrayOutputStream(50);

        ClientService service = ClientServiceBuilder.buildService(1, inputLines, output);

        boolean isCorrectService = service instanceof ComplexService;

        boolean expected = true;

        assertEquals(expected, isCorrectService);
    }
}
