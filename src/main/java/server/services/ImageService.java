package server.services;

import server.ClientService;

import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ImageService extends ClientService {
    public ImageService(int id, List<String> input, OutputStream output) {
        clientId = id;
    }
    @Override
    public void start() {

    }
}
