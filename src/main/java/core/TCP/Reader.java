package core.TCP;

import app.eventbus.EventBus;
import app.eventbus.IEventBus;
import app.eventbus.TCPEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.EventObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class Reader implements Runnable {

    private String name = "Reader";
    private BlockingQueue outputQueue;
    private Socket socket;
    private Boolean isRunning;

    private IEventBus eventBus = EventBus.getInstance();

    protected Reader(BlockingQueue outputQueue, Socket socket) {
        this.outputQueue = outputQueue;
        this.socket = socket;
        this.isRunning = false;
    };

    private String read(Socket socket) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        char[] buffer = new char[200];
        int sumChars = bufferedReader.read(buffer, 0, 200);
        String message = new String(buffer, 0, sumChars);
        return message;
    }

    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;
        while(isRunning) {
            System.out.println("--> " + name + " reading ..., " + outputQueue);
            try {
                String message = read(socket);
                outputQueue.offer(message, 1000, TimeUnit.MILLISECONDS);
                publish(message);
                System.out.println("--> " + name + " read " + message);
            } catch (Exception e) {
                isRunning = false;
            }
        }
        System.out.println("--> " + name + " DONE");
    }

    private void publish(String message) {
        eventBus.publish(new TCPEvent(message, TCPEvent.RECIEVE_TAG));
    }
}
