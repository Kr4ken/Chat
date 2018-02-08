package com.ezhov;

import com.ezhov.connector.ConnectorSettings;
import com.ezhov.server.ChatServer;
import com.ezhov.server.ChatServerSettings;

public class ServerApp {
    // Args <portNumber>
    public static void main(String args[]) {
        ChatServerSettings settings = ChatServerSettings.getDefault();
        ConnectorSettings connectorSettings = settings.getConnectorSettings();
        if(args.length > 0){
            connectorSettings.setPortNumber(Integer.parseInt(args[0]));
        }
        ChatServer server = new ChatServer(settings);
        server.run();
    }
}