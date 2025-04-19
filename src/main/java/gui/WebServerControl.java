package gui;

import config.ServerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.WebServer;

import javax.swing.*;
import java.awt.*;

public class WebServerControl extends JFrame {
    private static final Logger logger = LogManager.getRootLogger();
    private final WebServer server;

    public static void main(String[] args) {
        new WebServerControl();
    }

    public WebServerControl() {
        logger.info("Frame comienza a mostrarse");
        ServerConfiguration config = ServerConfiguration.getOrCreate();
        server = new WebServer(config.getPort());

        init();
    }

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

    private void createPanel() {
        this.getContentPane().setLayout(new BorderLayout());
        WebServerPanel serverPanel = new WebServerPanel(server);
        this.getContentPane().add(serverPanel, BorderLayout.CENTER);
    }

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
    }

    private void menuControl_detener(String cmd) {
        logger.info("Ejecutando comando detener ({})", cmd);
        server.stop();
    }

    private void menuControl_comenzar(String cmd) {
        logger.info("Ejecutando comando comenzar ({})", cmd);
        server.start();
    }
}
