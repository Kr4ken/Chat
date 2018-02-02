package com.ezhov;

import com.ezhov.server.ChatServer;
import com.ezhov.server.ChatServerImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String args[]) {
        ChatServer server = new ChatServerImpl();
        server.run();
    }
}