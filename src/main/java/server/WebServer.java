package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {
    private static final Logger logger = LogManager.getRootLogger();
    private final int port;
    private boolean running;
    private final PropertyChangeSupport observado;

    public WebServer(int p) {
        port = p;
        observado = new PropertyChangeSupport(this);
    }

    public String getName() {
        return "SRV:" + port;
    }

    public boolean isRunning() {
        return running;
    }

    public void addObservador(PropertyChangeListener observador) {
        observado.addPropertyChangeListener(observador);
        logger.info(getName() + " - Observador añadido al servidor web");
    }

    public void notifyStartStop() {
        logger.info("{} - Notificando start/stop a observadores", getName());
        observado.firePropertyChange("STARTSTOP", true, false);
    }

    public void notifyNewClient() {
        logger.info("{} - Notificando de nuevo cliente a observadores", getName());
        observado.firePropertyChange("CLIENT", true, false);
    }

    public void start() {
        try (ServerSocket srv = new ServerSocket(port)) {
            logger.info(getName() + " - Servidor creado, comienza a escuchar ahora");

            running = true;
            notifyStartStop();
            while (running) {
                srv.setSoTimeout(1000);
                try {
                    Socket clt = srv.accept();
                    logger.info("{} - Un cliente se acaba de conectar, comienza a dar el servicio", getName());
                    notifyNewClient();
                    Thread t = new Thread(() -> {
                        ClientService client = ClientServiceBuilder.buildService(clt);
                        client.start();
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

    public void stop() {
        logger.info("{} - Recibida la orden para parar el servidor", getName());
        running = false;
        notifyStartStop();
    }
}
