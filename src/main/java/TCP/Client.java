package TCP;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
    private int port = 8001;
    private InetAddress ipAdress;
    private Socket clientSocket;
    private String name;
    private int timeout = 5000;

    private BlockingQueue<String> inputData = new LinkedBlockingQueue<>(100);
    private BlockingQueue<String> outputData = new LinkedBlockingQueue<>(100);

    private Thread readerThread;
    private Thread writerThread;
    private Reader reader;
    private Writer writer;

    public Client(InetAddress ipAdress, int port) {
        this.port = port;
        this.ipAdress = ipAdress;
        this.name = "Client_" + ipAdress + ":" + port;
    }

    public void connect() {
        System.out.println(name + " connecting ...");
        try {
            clientSocket = new Socket(ipAdress, port);

            reader = new Reader(outputData, clientSocket);
            readerThread = new Thread(reader);

            writer = new Writer(inputData, clientSocket);
            writerThread = new Thread(writer);

            System.out.println(name + " connected ");
        } catch (Exception e) {
            System.out.println(name + " connection failed ");
        }
    }

    public void close() {
        try {
            reader.stop();
            writer.stop();
            clientSocket.close();
            System.out.println(name + " closed ");
        } catch (IOException e) {
            System.out.println(name + " could not close ");
            e.printStackTrace();
        }
    }

    public Boolean checkState() {
        Thread.State rState = readerThread.getState();
        Thread.State wState = writerThread.getState();
        // System.out.println(" reader state = " + reader.getState());
        // System.out.println(" writer state = " + writer.getState());

        return rState == Thread.State.RUNNABLE && wState == Thread.State.RUNNABLE;
    }

    @Override
    public void run() {
        connect();

        while(!clientSocket.isConnected()) {

        }
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
