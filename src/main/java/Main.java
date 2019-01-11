import app.gui.GUIApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Main {
    private static boolean useGUI = false;
    private static int port = -1;
    private static InetAddress inetAddress;
    private static List<String> argCommands;

    public static void main(String[] args) {
        String[] arr = {"", "--INET", "127.0.0.1", "-p", "8090"};
        boolean argsValid = parseArguments(arr);

        if(!argsValid) {
            exit(-1);
        }

        if(useGUI) {
            startGUI();
        } else if (port != -1 && inetAddress != null) {
            startCLI();
        }
    }

    private static void exit(int status) {
        System.exit(status);
    }

    private static boolean parseArguments(String[] arguments) {
        boolean inetValid = false;
        boolean portValid = false;

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
                case "--inet": {
                    inetValid = parseInetAddress(arguments, i);
                }
                case "-i": {
                    inetValid = parseInetAddress(arguments, i);
                }
                case "--port": {
                    portValid = parsePort(arguments, i);
                }
                case "-p": {
                    portValid = parsePort(arguments, i);
                }
            }
        }

        return inetValid && portValid || (useGUI);
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

    private static void startGUI() {
        GUIApplication application = new GUIApplication();
        System.out.println("//// GUI STARTED ////");
    }

    private static void startCLI() {
        System.out.println("//// _CLI_ STARTED ////");
    }
}
