package server.services;

import server.ClientService;

import java.io.OutputStream;
import java.util.List;

public class ImageService extends ClientService {
    public ImageService(int id, List<String> input, OutputStream output) {
        super(id, input, output);
    }
    @Override
    public void start() {

    }
}
