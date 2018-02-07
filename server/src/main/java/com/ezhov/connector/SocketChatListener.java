package com.ezhov.connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketChatListener implements ChatListener {

    private ServerSocket server;
    private ConnectorSettings settings;

    public SocketChatListener(ConnectorSettings settings) {
        this.settings = settings;
    }

    @Override
    public void start() throws IOException {
        if(!checkStatus())
            server = new ServerSocket(settings.getPortNumber());
    }

    @Override
    public void stop() throws IOException {
        if(checkStatus())
            server.close();
    }

    @Override
    public ChatConnector getClient() throws IOException {
        Socket client = server.accept();
        ChatConnector chatConnector = new SocketChatConnector(client);
        return chatConnector;
    }

    @Override
    public Boolean checkStatus() {
        return server != null && !server.isClosed();
    }
}
