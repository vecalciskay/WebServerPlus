package server;

import server.services.NoService;

import java.net.Socket;

public class ClientServiceBuilder {
    public static ClientService buildService(Socket clt) {
        return new NoService();
    }
}
