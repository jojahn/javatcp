package core.TCP;

import java.net.InetAddress;

public class Connector {
    private Server server;
    private Client client;

    private int port;
    private InetAddress ipAdress;

    private Thread clientThread;
    private Thread serverThread;

    private long timeout;

    private String name;

    public Connector(InetAddress ipAdress, int port) {
        this.name = "Connector_" + ipAdress + ":" + port;
        this.server = new Server(ipAdress, port);
        this.client = new Client(ipAdress, port);

        clientThread = new Thread(client);
        serverThread = new Thread(server);
    }

    public void fullConnect() {
        clientThread.start();
        serverThread.start();
    }

    public void fullReconnect(){
        fullClose();
        this.server = new Server(ipAdress, port);
        this.client = new Client(ipAdress, port);

        serverThread = new Thread(server);
        clientThread = new Thread(client);
        fullConnect();
    }

    public void fullClose() {
        client.close();
        server.close();
    }

    public void connect() {
        clientThread.start();
    }

    public void listen() {
        serverThread.start();
    }

    public void close() {
        client.close();
    }

    public void stopListen(){
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
