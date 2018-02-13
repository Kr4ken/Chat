package com.ezhov.connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketChatListener implements ChatListener {

    private ServerSocket server;
    private ListenerSettings settings;

    public SocketChatListener(ListenerSettings settings) {
        this.settings = settings;
    }

    public void start() throws IOException {
        if(!checkStatus())
            server = new ServerSocket(settings.getPortNumber());
    }

    public void stop() throws IOException {
        if(checkStatus())
            server.close();
    }

    public ChatConnector getClient() throws IOException {
        Socket client = server.accept();
        ChatConnector chatConnector = new SocketChatConnector(client);
        return chatConnector;
    }

    public Boolean checkStatus() {
        return server != null && !server.isClosed();
    }
}
