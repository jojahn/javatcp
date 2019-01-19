package core.TCP;

import java.net.InetAddress;

public class SimpleClient extends Participant {

    public SimpleClient(InetAddress ipAdress, int port) {
        super(ipAdress, port);
        setName("Client_" + ipAdress + ":" + port);
    }

    @Override
    public void connect() {

    }

    @Override
    public void close() {

    }

    @Override
    public Boolean checkState() {
        return null;
    }

    @Override
    public void run() {

    }
}
