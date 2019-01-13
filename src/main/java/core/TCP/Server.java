package core.TCP;

import app.eventbus.EventBus;
import app.eventbus.IEventBus;
import app.eventbus.StatusEvent;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    private String name;
    private int port = 8001;
    private InetAddress ipAdress;
    private Socket clientSocket;
    private ServerSocket serverSocket;

    private IEventBus eventBus = EventBus.getInstance();

    private BlockingQueue<String> inputData = new LinkedBlockingQueue<>(100);
    private BlockingQueue<String> outputData = new LinkedBlockingQueue<>(100);

    private Thread readerThread;
    private Thread writerThread;
    private Reader reader;
    private Writer writer;


    public Server(InetAddress ipAdress, int port) {
        this.port = port;
        this.ipAdress = ipAdress;
        this.name = "   Server_" + ipAdress + ":" + port;
    }

    public void connect() {
        try {
            this.serverSocket = new ServerSocket(port, 1, this.ipAdress);
            eventBus.publish(new StatusEvent(StatusEvent.IS_LISTENING, "Listening on (" + ipAdress + ":" + port +")"));

            this.clientSocket = waitForClient(serverSocket);

            reader = new Reader(outputData, clientSocket);
            readerThread = new Thread(reader);

            writer = new Writer(inputData, clientSocket);
            writerThread = new Thread(writer);

            eventBus.publish(new StatusEvent(StatusEvent.SET_CONNECTED, "Connection from (" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ")"));
            System.out.println(name + " connected ");
        } catch (Exception e) {
            eventBus.publish(new StatusEvent(StatusEvent.SET_DISCONNECTED, "Listening failed on (" + ipAdress + ":" + port +")"));
            System.out.println(name + " connection failed ");
        }
    }

    public void close() {
        try {
            if(reader != null) {
                reader.stop();
            }
            if(writer != null) {
                writer.stop();
            }
            if(clientSocket != null) {
                clientSocket.close();
            }
            eventBus.publish(new StatusEvent(StatusEvent.SET_DISCONNECTED, "Disconnected"));
            System.out.println(name + " closed ");
        } catch (IOException e) {
            System.out.println(name + " could not close ");
            e.printStackTrace();
        }
    }

    /*private void listen() {
        System.out.println(name + " listening on " + port);
        try {
            this.clientSocket = waitForClient(serverSocket);

            while(true) {
                System.out.println("    executing (server)");
                outputData = read(clientSocket);
                System.out.println(name + " read " + outputData);
                write(clientSocket, "MSG_RECV["+outputData+"]");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private Socket waitForClient(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }

    @Override
    public void run() {
        connect();
        readerThread.start();
        writerThread.start();

        System.out.println(name + " Threads running");

        try {
            writerThread.join();
            readerThread.join();
            System.out.println(name + " DONE");
        } catch (InterruptedException e) {
            System.out.println(name + " CRASHED");
        }
    }

    public void send(String message){
        try {
            inputData.put(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message, long timeout){
        try {
            inputData.offer(message, timeout, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(long timeout) {
        String message = null;
        try {
            message = outputData.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public String get() {
        String message = null;
        try {
            message = outputData.take();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
