package com.ezhov;

import com.ezhov.connector.ConnectorSettings;
import com.ezhov.server.ChatServer;
import com.ezhov.server.ChatServerSettings;

public class ServerApp {
    public static void main(String args[]) {
        ChatServerSettings settings = ChatServerSettings.getDefault();
        ConnectorSettings connectorSettings = settings.getConnectorSettings();
        // Args <portNumber>
        if(args.length > 0){
            connectorSettings.setPortNumber(Integer.parseInt(args[0]));
        }
        ChatServer server = new ChatServer(settings);
        server.run();
    }
}