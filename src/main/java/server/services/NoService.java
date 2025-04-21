package server.services;

import server.ClientService;

import java.io.OutputStream;
import java.util.List;

public class NoService extends ClientService {
    public NoService(int id, List<String> input, OutputStream output) {
        super(id, input, output);
    }

    @Override
    public void start() {

    }
}
