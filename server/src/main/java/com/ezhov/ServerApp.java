package com.ezhov;

import com.ezhov.server.ChatServer;
import com.ezhov.server.ChatServerSettings;

public class ServerApp {
    public static void main(String args[]) {
        ChatServer server = new ChatServer(ChatServerSettings.getDefault());
        server.run();
    }
}