package com.ezhov;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String args[]){
        try {
            System.out.println("Server start");
            ServerSocket serverSocket = new ServerSocket(8989);
            Socket socket = serverSocket.accept();
            System.out.println("Client connect");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true){
                String formatMessage = in.readLine();
                System.out.println("Get message " + formatMessage);
                formatMessage = "[SYSTEM] Message accepted\n";
                out.write(formatMessage);
                out.flush();
                System.out.println("Send message " + formatMessage);
            }
        }
        catch (IOException ex){

        }
    }
}