package gui;

import config.ServerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.WebServer;

import javax.swing.*;
import java.awt.*;

/**
 * Este es el frame que puede manejar el servidor Web.
 */
public class WebServerControl extends JFrame {
    private static final Logger logger = LogManager.getRootLogger();
    private final WebServer server;

    public static void main(String[] args) {
        new WebServerControl();
    }

    /**
     * Constructor de control de servidor. Crear el frame, crea el servidor y el panel.
     */
    public WebServerControl() {
        logger.info("Frame comienza a mostrarse");
        ServerConfiguration config = ServerConfiguration.getOrCreate();
        server = new WebServer(config.getPort());

        init();
    }

    /**
     * Este método inicializa todos los componentes del frame.
     */
    private void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        logger.info("Frame creando panel para colocarlo al centro");
        createPanel();

        logger.info("Frame creando menu para la barra de menus");
        createMenu();

        logger.info("Frame mostrándose");
        pack();
        setVisible(true);
    }

    /**
     * El panel es un componente aparte. Esto se crea como un objeto aparte y se
     * apunta un observador al servidor.
     */
    private void createPanel() {
        this.getContentPane().setLayout(new BorderLayout());
        WebServerPanel serverPanel = new WebServerPanel(server);
        this.getContentPane().add(serverPanel, BorderLayout.CENTER);
    }

    /**
     * Este método crea todos los items del menu de la aplicación.
     */
    private void createMenu() {

        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);

        JMenu menu = new JMenu("Control");
        bar.add(menu);

        JMenuItem item = new JMenuItem("Comenzar");
        item.addActionListener(e -> menuControl_comenzar(e.getActionCommand()));
        menu.add(item);

        item = new JMenuItem("Detener");
        item.addActionListener(e -> menuControl_detener(e.getActionCommand()));
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Salir");
        item.addActionListener(e -> menuControl_salir(e.getActionCommand()));
        menu.add(item);
    }

    /**
     * Manejador del item salir del menu Control
     * @param cmd
     */
    private void menuControl_salir(String cmd) {
        if (server.isRunning()) {
            logger.warn("{} - No se puede salir sin detener primero", server.getName());
            return;
        }
        logger.info("Ejecutando comando salir ({})", cmd);
        System.exit(0);
    }

    /**
     * Manejador del item detener del menu Control
     * @param cmd
     */
    private void menuControl_detener(String cmd) {
        logger.info("Ejecutando comando detener ({})", cmd);
        server.stop();
    }

    /**
     * Manejador del item comenzar del menu Control.
     * @param cmd
     */
    private void menuControl_comenzar(String cmd) {
        logger.info("Ejecutando comando comenzar ({})", cmd);
        Thread t = new Thread(server::start);
        t.start();
    }
}
