import app.cli.CLIApplication;
import app.gui.GUIApplication;

import javafx.application.Application;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Main {
    private static boolean useGUI = false;
    private static Integer port = -1;
    private static InetAddress inetAddress;
    private static Boolean isServer;
    private static List<String> argCommands;

    public static void main(String[] args) {
        System.out.println("//// JAVA TCP ////");
        String[] arr = {"--CLIENT", "--IP", "127.0.0.1", "-p", "8090"};
        boolean argsValid = parseArguments(arr);

        if(!argsValid) {
            System.out.println("//// ARGUMENTS INVALID ////");
            exit(-1);
        }

        if(useGUI) {
            startGUI(args);
        } else {
            startCLI();
        }
    }

    private static void exit(int status) {
        System.exit(status);
    }

    private static boolean parseArguments(String[] arguments) {
        boolean inetValid = false;
        boolean portValid = false;
        boolean isServerValid = false;

        for(int i = 0; i < arguments.length; i++) {
            String argument = arguments[i].toLowerCase();

            switch (argument) {
                case "--gui": {
                    useGUI = true;
                    break;
                }
                case "-g": {
                    useGUI = true;
                    break;
                }
                case "--ip": {
                    inetValid = parseInetAddress(arguments, i);
                    break;
                }
                case "-i": {
                    inetValid = parseInetAddress(arguments, i);
                    break;
                }
                case "--port": {
                    portValid = parsePort(arguments, i);
                    break;
                }
                case "-p": {
                    portValid = parsePort(arguments, i);
                    break;
                }
                case "--server": {
                    isServer = true;
                    break;
                }
                case "-s": {
                    isServer = true;
                    break;
                }
                case "--client": {
                    isServer = false;
                    break;
                }
                case "-c": {
                    isServer = false;
                    break;
                }
            }
        }

        return true;
    }

    private static boolean parseInetAddress(String[] arguments, int i) {
        if(i == arguments.length - 1) {
            return false;
        }
        try {
            String address = arguments[i + 1];
            inetAddress = InetAddress.getByName(address);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private static boolean parsePort(String[] arguments, int i) {
        if(i == arguments.length - 1) {
            return false;
        }
        try {
            port = Integer.parseInt(arguments[i + 1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void startGUI(String[] args) {
        Application.launch(GUIApplication.class, args);
    }

    private static void startCLI() {
        CLIApplication App;
        if(isServer != null && inetAddress != null && port != null) {
            App = new CLIApplication(inetAddress, port, isServer);
        } else if (inetAddress != null && port != null){
            App = new CLIApplication(inetAddress, port);
        } else if (isServer != null && inetAddress == null && port == null) {
            App = new CLIApplication(isServer);
        } else {
            App = new CLIApplication();
        }
        App.start();
        System.out.println("//// _CLI_ STARTED ////");
    }
}
