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
        client.send(message);
    }
    public void send(String message, long timeout) { client.send(message, timeout); }
    public void sendServer(String message){
        server.send(message);
    }
    public void sendServer(String message, long timeout){
        server.send(message, timeout);
    }
    public String recieve() {
        return client.get();
    }
    public String recieve(long timeout) {
        return client.get(timeout);
    }
    public String recieveServer() {
        return server.get();
    }
    public String recieveServer(long timeout) {
        return server.get(timeout);
    }

    public void testConnect() {
        clientThread.start();
        serverThread.start();
    }
    public void testReconnect(){
        testClose();
        this.server = new Server(ipAdress, port);
        this.client = new Client(ipAdress, port);

        serverThread = new Thread(server);
        clientThread = new Thread(client);
        testConnect();
    }
    public void testClose() {
        client.close();
        server.close();
    }
}
