package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientTerminal;
import com.ezhov.settings.ClientSettings;
import com.ezhov.settings.ConnectorSettings;

public class ClientApp {

    // Args <Name> <HostName> <portNumber>
    public static void main(String args[]) {
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