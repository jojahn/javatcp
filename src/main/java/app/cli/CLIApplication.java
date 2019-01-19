package app.cli;

import core.TCP.Connector;

import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class CLIApplication implements Runnable {
    private static String HALT_STRING = "HALT";

    private String name = "JAVA-TCP-CLI";
    private Boolean isServer = null;
    private boolean isRunning = false;
    private Long timeout = 1000L;
    private Scanner scanner;

    private InetAddress inetAddress = null;
    private Integer port = null;
    private Connector connector;

    private List<String> yeses;
    private List<String> noes;

    public CLIApplication() {

    }

    public CLIApplication(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public CLIApplication(InetAddress inetAddress, int port, boolean isServer) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.isServer = isServer;
    }

    public CLIApplication(boolean isServer) {
        this.isServer = isServer;
    }

    public void start() {
        scanner = new Scanner(System.in);
        buildInput();
        waitForInit();
        connector = new Connector(inetAddress, port);
        if(isServer) {
            connector.listen();
        } else {
            connector.connect();
        }
        run();
    }

    private void buildInput() {
        yeses = new ArrayList<>();
        yeses.add("yes");
        yeses.add("y");

        noes = new ArrayList<>();
        noes.add("no");
        noes.add("n");
    }

    private void waitForInit(){
        boolean isServerValid = isServer != null;
        boolean inetValid = inetAddress != null;
        boolean portValid = port != null;
        boolean initValid = isServerValid && inetValid && portValid;

        while(!initValid) {
            while (!isServerValid) {
                System.out.println(name + " SETUP Run as Server? (Y/N)");
                try {
                    String result = scanner.next();
                    if (yeses.contains(result.toLowerCase())) {
                        isServer = true;
                        isServerValid = true;
                        System.out.println(name + " SETUP " + result + " Run as Server? YES");
                    } else if (noes.contains(result.toLowerCase())) {
                        isServer = false;
                        isServerValid = true;
                        System.out.println(name + " SETUP " + result + " Run as Server? NO");
                    } else {
                        throw new IllegalArgumentException();
                    }
                } catch (Exception e) {
                    System.out.println(name + " SETUP (ERROR: Bad Input)");
                }
            }

            while (!inetValid) {
                System.out.println(name + " SETUP Enter IP Address (127.0.0.1)");
                try {
                    String result = scanner.next();
                    inetAddress = InetAddress.getLocalHost();
                    inetValid = true;
                } catch (Exception e) {
                    inetValid = false;
                    System.out.println(name + " SETUP (ERROR: Bad Input)");
                }
            }

            while (!portValid) {
                System.out.println(name + " SETUP Enter port number (0 < port < 65535)");
                try {
                    String result = scanner.next();
                    int portNumber = Integer.parseInt(result);
                    if (portNumber < 0) {
                        throw new IllegalArgumentException();
                    }
                    port = portNumber;
                    portValid = true;
                } catch (Exception e) {
                    System.out.println(name + " SETUP (ERROR: Bad Input)");
                }
            }

            // Check if setup correct
            System.out.println(name + " on " + inetAddress + ":" + port + " as " + ((isServer) ? "Server" : "Client") + " Accept? (Y/N)");
            try {
                String result = scanner.next();
                if (yeses.contains(result.toLowerCase())) {
                    initValid = true;
                } else if (noes.contains(result.toLowerCase())) {
                    initValid = false;
                } else {
                    initValid = true;
                }
            } catch (Exception e) {
                System.out.println(name + " SETUP (ERROR: Bad Input)");
            }
        }

        System.out.println(name + " SETUP Completed");
    }

    @Override
    public void run() {

        isRunning = true;
        while(isRunning) {

            System.out.println(name + "_" + inetAddress + ":" + port + " STEP ");
            String message = (isServer) ? connector.recieveServer(timeout) : connector.recieve(timeout);
            System.out.println(name + " Received " + message);
            System.out.println(name + " Enter message (HALT for Stop)");

            try {
                message = scanner.next();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

            if(message.equals(HALT_STRING)) {
                connector.close();
                scanner.close();
                System.exit(1);
            }

            if(isServer) {
                connector.sendServer(message, timeout);
            } else {
                connector.send(message, timeout);
            }
        }
    }
}
