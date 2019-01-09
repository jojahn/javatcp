package TCP.test;

import java.io.*;

import java.net.*;
import java.util.concurrent.TimeUnit;

public class eyeClient

{

    /*public void execute() {
        try {
            if (inputData.size() > 0) {
                System.out.println("size ");
            }
            System.out.println("    executing ");
            if(clientSocket == null){
                System.out.println(" /// no client");
                connect();
            }

            if(clientSocket != null && inputData.size() > 0) {
                System.out.println("msg exists");
                try {
                    String message = null;
                    message = inputData.poll(timeout, TimeUnit.MILLISECONDS);
                    write(clientSocket, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("    reading ");
            if(clientSocket != null) {
                String message = read(clientSocket);
                try {
                    outputData.offer(message, timeout, TimeUnit.SECONDS);
                    System.out.println("---> outputData = " + outputData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(name + " read " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) throws IOException

    {

        System.out.println("TCP CLIENT");

        System.out.println("Enter the host name to start");

        DataInputStream inp=new DataInputStream(System.in);

        String str=inp.readLine();

        Socket clientsoc = new Socket(str, 9);

        PrintWriter out = new PrintWriter(clientsoc.getOutputStream(), true);

        BufferedReader in = new BufferedReader(new

                InputStreamReader(clientsoc.getInputStream()));

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        String userinput;

        try

        {

            while (true)

            {

                System.out.println("Sever Says : ” + in.readLine()");

                userinput = stdin.readLine();

                out.println(userinput);

            }

        }

        catch(Exception e)

        {

            System.exit(0);

        }

    }

}