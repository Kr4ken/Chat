package com.ezhov.connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketChatListener extends ChatListener {

    private ServerSocket server;

    public SocketChatListener(ConnectorSettings settings){
        super(settings);
    }

    @Override
    public void connect() throws IOException {
        server = new ServerSocket(settings.getPortNumber());
    }

    @Override
    public void disconnect() throws IOException{
        server.close();
    }

    @Override
    public ChatConnector waitClient() throws IOException  {
        Socket client = server.accept();
        ChatConnector chatConnector = new SocketChatConnector(client);
        return chatConnector;
    }
}
