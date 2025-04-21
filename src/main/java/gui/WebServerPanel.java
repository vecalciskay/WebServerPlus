package gui;

import server.WebServer;
import server.WebServerEvent;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WebServerPanel extends JPanel implements PropertyChangeListener {
    private final WebServer modelo;
    private final JLabel status;
    private final JTextArea lastMessage;

    public WebServerPanel(WebServer srv) {
        modelo = srv;
        modelo.addObservador(this);
        status = new JLabel();
        status.setFont(new Font("Arial", Font.BOLD, 20));
        lastMessage = new JTextArea();
        lastMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        lastMessage.setEditable(false);

        this.setLayout(new BorderLayout(10,10));

        this.add(status, BorderLayout.NORTH);
        this.add(lastMessage, BorderLayout.CENTER);

        updateMessages("...");
    }

    private void updateMessages(String lastMessage) {
        updateStatus();
        updateLastMessage(lastMessage);
    }

    private void updateLastMessage(String msg) {
        lastMessage.setText(msg);
    }

    private void updateStatus() {
        String msg = "Servidor " + modelo.getName() + ": " + (modelo.isRunning() ? "CORRIENDO" : "PARADO");
        status.setText(msg);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WebServerEvent.NEW_CLIENT.toString()))
            updateMessages(evt.getNewValue().toString());
        if (evt.getPropertyName().equals(WebServerEvent.START_STOP.toString()))
            updateMessages("...");
    }
}
