package server;

import java.io.OutputStream;
import java.util.List;

public abstract class ClientService implements IClientService {
    protected int clientId;
    protected List<String> inputRead;
    protected OutputStream output;
    protected List<String> headers;

    public ClientService(int id, List<String> lines, OutputStream out) {
        clientId = id;
        inputRead = lines;
        output = out;
        headers = inputRead.subList(1,inputRead.size());
    }

    public int getClientId() {
        return clientId;
    }
    
    public String getName() {
        String className = this.getClass().getSimpleName();
        return className + " " + clientId;
    }
}
