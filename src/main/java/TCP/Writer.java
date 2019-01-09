package TCP;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Writer implements Runnable {

    private BlockingQueue<String> inputQueue;
    private Socket socket;
    private Boolean isRunning;

    protected Writer(BlockingQueue<String> inputQueue, Socket socket) {
        this.inputQueue = inputQueue;
        this.socket = socket;
        this.isRunning = false;
    };

    public void stop() {
        isRunning = false;
    }

    private void write(Socket socket, String message) throws IOException {
        PrintWriter printWriter =
                new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    @Override
    public void run() {
        isRunning = true;
        while(isRunning) {
            System.out.println(" Writer: writing...");
            try {
                String message = inputQueue.take();
                System.out.println(" Writer: writing "+ message);
                write(socket, message);
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
        System.out.println(" Writer: DONE");
    }
}
