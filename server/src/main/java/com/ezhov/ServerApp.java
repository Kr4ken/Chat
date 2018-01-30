package com.ezhov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String args[]) {
        try {
            System.out.println("Server start");
            ServerSocket serverSocket = new ServerSocket(8989);
            Socket socket = serverSocket.accept();
            System.out.println("Client connect");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Thread spamerThread = new Thread() {
                public void run() {
                    try {
                        String spammerMessage = "[12:33:22|SYSTEM] I'm a spammer message\n";
                        while (true) {
                            out.write(spammerMessage);
                            out.flush();
                            System.out.println("Send message " + spammerMessage);
                            Thread.sleep(1000);
                        }
                    } catch (IOException | InterruptedException ex) {
                        System.out.println("Spammer thread error " + ex);
                    }
                }
            };
            spamerThread.start();
            while (true) {
                String formatMessage = in.readLine();
                System.out.println("Get message " + formatMessage);
                formatMessage = "[12:33:22|SYSTEM] Message accepted\n";

                out.write(formatMessage);
                out.flush();
                System.out.println("Send message " + formatMessage);
            }
        } catch (IOException ex) {

        }
    }
}