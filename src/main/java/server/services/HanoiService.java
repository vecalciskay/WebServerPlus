package server.services;

import server.ClientService;

import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class HanoiService extends ClientService {
    public HanoiService(int id, List<String> input, OutputStream output) {
        super(id, input, output);
    }
    @Override
    public void start() {

    }
}
