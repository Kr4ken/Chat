package com.ezhov.tests;

import com.ezhov.domain.ChatMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMock {

    private Socket server;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerMock(Integer portNumber) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Thread serverThread = new Thread() {
                @Override
                public void run() {
                    try {
                        server = serverSocket.accept();
                        System.out.println("Client accepted");
                        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
                    } catch (IOException ex) {
                        System.out.println("Server mock Error");
                    } finally {
                        try {
                            serverSocket.close();
                        } catch (Exception e) {
                        }

                    }
                }
            };
            serverThread.start();
        } catch (IOException ex) {

        }
    }

    public void sendMessage(ChatMessage message) {
        try {
            out.write(message.getFormatMessage() + "\n");
            out.flush();
        } catch (Exception e) {

        }
    }

    public ChatMessage readMessage() {
        try {
            String message = in.readLine();
            return ChatMessage.fromFormatString(message);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        server.close();
        in.close();
        out.close();
    }

}
