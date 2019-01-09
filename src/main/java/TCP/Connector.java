package TCP;

import java.net.InetAddress;

public class Connector {
    private Server server;
    private Client client;

    private int port;
    private InetAddress ipAdress;

    private Thread senderThread;
    private Thread recvThread;

    private long timeout;

    private String name;

    public Connector(InetAddress ipAdress, int port) {
        this.name = "Connector_" + ipAdress + ":" + port;
        this.server = new Server(ipAdress, port);
        this.client = new Client(ipAdress, port);

        senderThread = new Thread(client);
        recvThread = new Thread(server);
    }

    public void connect() {
        senderThread.start();
        recvThread.start();
    }

    public void reconnect(){
        this.server = new Server(ipAdress, port);
        this.client = new Client(ipAdress, port);

        recvThread = new Thread(server);
        senderThread = new Thread(client);
        connect();
    }

    public void close() {
        client.close();
        server.close();
    }

    public void send(String message){
        client.send(message, 5000);
    }

    public void sendServer(String message){
        server.send(message, 5000);
    }

    public String recieve() {
        return client.get();
    }

    public String recieveServer() {
        return server.get();
    }
}
