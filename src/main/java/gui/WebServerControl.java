package gui;

import config.ServerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.WebServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebServerControl extends JFrame {
    private static final Logger logger = LogManager.getRootLogger();
    private WebServer server;
    private WebServerPanel serverPanel;

    public static void main(String[] args) {
        WebServerControl srv = new WebServerControl();
    }

    public WebServerControl() {
        ServerConfiguration config = ServerConfiguration.getOrCreate();
        server = new WebServer(config.getPort());

        init();
    }

    private void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        createPanel();
        createMenu();

        pack();
        setVisible(true);
    }

    private void createPanel() {
        this.getContentPane().setLayout(new BorderLayout());
        serverPanel = new WebServerPanel(server);
        this.getContentPane().add(serverPanel, BorderLayout.CENTER);
    }

    private void createMenu() {

        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);

        JMenu menu = new JMenu("Control");
        bar.add(menu);

        JMenuItem item = new JMenuItem("Comenzar");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuControl_comenzar();
            }
        });
        menu.add(item);

        item = new JMenuItem("Detener");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuControl_detener();
            }
        });
        menu.add(item);
    }

    private void menuControl_detener() {
        server.stop();
    }

    private void menuControl_comenzar() {
        server.start();
    }
}
