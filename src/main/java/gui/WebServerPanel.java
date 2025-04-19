package gui;

import server.WebServer;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WebServerPanel extends JPanel implements PropertyChangeListener {
    private final WebServer modelo;

    public WebServerPanel(WebServer srv) {
        modelo = srv;
        modelo.addObservador(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
