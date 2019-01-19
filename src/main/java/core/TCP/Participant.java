package core.TCP;

import app.eventbus.EventBus;
import app.eventbus.IEventBus;
import app.eventbus.StatusEvent;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.util.EventObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class Participant implements Runnable {
    private int port = 8001;
    private InetAddress ipAdress;
    private String name;
    private int timeout = 5000;

    private IEventBus eventBus = EventBus.getInstance();

    private BlockingQueue<String> inputData = new LinkedBlockingQueue<>(100);
    private BlockingQueue<String> outputData = new LinkedBlockingQueue<>(100);

    private Thread readerThread;
    private Thread writerThread;
    private Reader reader;
    private Writer writer;

    public Participant(InetAddress ipAdress, int port) {
        this.port = port;
        this.ipAdress = ipAdress;
        this.name = "TCPParticipant_" + ipAdress + ":" + port;
    }

    public abstract void connect();

    public abstract void close();

    public abstract Boolean checkState();

    @Override
    public abstract void run();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

