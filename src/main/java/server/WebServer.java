package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebServer {
    private static final Logger logger = LogManager.getRootLogger();
    private static final int MILISECONDS_TIMEOUT_LOOP = 2000;
    private static int clientId;
    private final int port;
    private boolean running;
    private final PropertyChangeSupport observado;

    public WebServer(int p) {
        port = p;
        observado = new PropertyChangeSupport(this);
        clientId = 0;
    }

    public String getName() {
        return "SRV:" + port;
    }

    public boolean isRunning() {
        return running;
    }

    public void addObservador(PropertyChangeListener observador) {
        observado.addPropertyChangeListener(observador);
        logger.info("{} - Observador añadido al servidor web", getName());
    }

    public void notifyStartStop() {
        logger.info("{} - Notificando start/stop a observadores", getName());
        observado.firePropertyChange(WebServerEvent.START_STOP.toString(), true, false);
    }

    public void notifyNewClient(List<String> inputLines) {
        if (inputLines == null || inputLines.isEmpty()) {
            logger.error("{} - No se pudo leer nada de las lineas", getName());
            return;
        }
        logger.info("{} - Notificando de nuevo cliente a observadores", getName());
        observado.firePropertyChange(WebServerEvent.NEW_CLIENT.toString(), "", inputLines.getFirst());
    }

    public void start() {
        try (ServerSocket srv = new ServerSocket(port)) {
            logger.info("{} - Servidor creado, comienza a escuchar ahora", getName());

            running = true;
            notifyStartStop();
            while (running) {
                srv.setSoTimeout(MILISECONDS_TIMEOUT_LOOP);
                try {
                    Socket clt = srv.accept();
                    logger.info("{} - Un cliente se acaba de conectar, comienza a dar el servicio", getName());

                    Thread t = new Thread(() -> {
                        try {
                            InputStream input = clt.getInputStream();
                            OutputStream output = clt.getOutputStream();
                            List<String> linesRead = readAllLines(input);
                            notifyNewClient(linesRead);

                            int id = ++clientId;
                            ClientService client = ClientServiceBuilder.buildService(id, linesRead, output);
                            client.start();

                            input.close();
                            output.close();
                        } catch(Exception e) {
                            logger.info("{} - Hubo un error dando servicio al cliente", getName(), e);
                        }
                    });
                    t.start();
                } catch (SocketTimeoutException e1) {
                    logger.info("{} - Timeout de socket, solamente para que pueda chequear la variable de corriendo", getName());
                }
            }

            logger.info("{} - Servidor parado, ya no escucha nada", getName());
        } catch (IOException e) {
            throw new RuntimeException("No pudo crear socket servidor en puerto " + port, e);
        }
    }

    public List<String> readAllLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);

        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        // readLine() returns null when the peer closes the stream
        int oneChar;
        while (reader.ready()) {
            oneChar = reader.read();
            if (oneChar == -1)
                break;
            char c = (char)oneChar;
            line.append(c);
        }

        String[] arrayLines = line.toString().split("\r\n");
        lines = Arrays.asList(arrayLines);
        return lines;
    }

    public void stop() {
        logger.info("{} - Recibida la orden para parar el servidor", getName());
        running = false;
        notifyStartStop();
    }
}
