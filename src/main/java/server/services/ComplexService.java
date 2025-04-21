package server.services;

import server.ClientService;

import java.io.OutputStream;
import java.util.List;

public class ComplexService extends ClientService {
    public ComplexService(int id, List<String> input, OutputStream output) {
        super(id, input, output);
    }
    @Override
    public void start() {

    }
}
