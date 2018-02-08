package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientTerminal;
import com.ezhov.client.ClientSettings;
import com.ezhov.connector.ConnectorSettings;

import java.io.IOException;

public class ClientApp {
    // Args <name> <hostName> <portNumber>
    public static void main(String args[]) throws IOException {
        ClientSettings settings = ClientSettings.getDefault();
        ConnectorSettings connectorSettings = settings.getConnectorSettings();
        if (args.length > 0) {
            settings.setName(args[0]);
        }
        if (args.length > 1) {
            connectorSettings.setHostName(args[1]);
        }
        if (args.length > 2) {
            connectorSettings.setPortNumber(Integer.parseInt(args[2]));
        }
        ChatClient client = new ChatClientTerminal(settings);
        client.start();
    }
}