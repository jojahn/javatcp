import core.TCP.Connector;

import java.net.*;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("starting");

        try {
            int firstPort = 8400;
            int secondPort = 8002;
            InetAddress localhost = InetAddress.getByName("localhost");


            Connector c = new Connector(localhost, firstPort);
            c.testConnect();

            c.send("Hello, World!");
            String message = c.recieveServer();
            System.out.println("main@server: " + message);
            c.sendServer("///" + message + "///");
            message = c.recieve();
            System.out.println("main@client: " + message);

            /*c.send("Test Message System");
            message = c.recieve();
            System.out.println("main: " + message);*/
            c.testClose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("main: ended");
    }
}
