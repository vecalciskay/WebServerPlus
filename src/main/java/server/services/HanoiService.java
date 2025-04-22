package server.services;

import config.ServerConfiguration;
import operations.hanoi.Hanoi;
import operations.http.MimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.ClientService;

import javax.json.Json;
import javax.json.JsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HanoiService extends ClientService {
    private static final Logger logger = LogManager.getRootLogger();

    private final int start;
    private final int end;
    private final int numberRings;
    private final int numberMove;

    /**
     * Constructor del servicio de Hanoi. Se requiere los datos de la solicitud y
     * aquí se construirá lo necesario para la solicitud.
     * @param id El número de cliente para el servidor
     * @param cmd El comando hanoi solamente
     * @param args Los argumentos para nuestro comando
     * @param input La entrada que vino del servidor
     * @param output La salida donde se debe escribir el resultado
     */
    public HanoiService(int id, String cmd, String args, List<String> input, OutputStream output) {
        super(id, input, output);

        String[] argsArray = args.split("_");

        int torreOrigen = Integer.parseInt(argsArray[0]);
        int torreDestino = Integer.parseInt(argsArray[1]);
        int numAnillos = Integer.parseInt(argsArray[2]);
        int movimiento = Integer.parseInt(argsArray[3]);

        start = torreOrigen;
        end = torreDestino;
        numberRings = numAnillos;
        numberMove = movimiento;
    }
    @Override
    public void start() {
        ServerConfiguration config = ServerConfiguration.getOrCreate();

        logger.info("{} Comienza a servir el hanoi", getName());
        Hanoi h = new Hanoi(numberRings, start);
        h.solve(start, end, numberMove);

        byte[] bodyBytes = buildBody(h);
        byte[] headerBytes = buildHeaders(MimeUtils.getMime("json"), bodyBytes.length);

        byte[] response = new byte[headerBytes.length + bodyBytes.length];

        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(bodyBytes, 0, response, headerBytes.length, bodyBytes.length);
        try {
            output.write(response);
            output.flush();

            logger.info("{} Se paso el JSON correspondiente de {} bytes", getName(), bodyBytes.length);
        } catch (IOException e) {
            logger.error("{} Hubo un error de IO", getName(), e);
        }
    }

    private byte[] buildBody(Hanoi h) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(h.getJson());
        jsonWriter.close();

        return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
    }
}
