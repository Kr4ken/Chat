package com.ezhov.client;

import com.ezhov.connector.ConnectorSettings;

public class ClientSettings {
    private ConnectorSettings connectorSettings;
    private String name;

    private ClientSettings(){}

    public static ClientSettings getDefault(){
        ClientSettings clientSettings = new ClientSettings();
        clientSettings.connectorSettings = ConnectorSettings.getDefault();
        clientSettings.name = "default";
       return clientSettings;
    }

    public ClientSettings(ConnectorSettings connectorSettings, String name) {
        this.connectorSettings = connectorSettings;
        this.name = name;
    }

    public ConnectorSettings getConnectorSettings() {
        return connectorSettings;
    }

    public void setConnectorSettings(ConnectorSettings connectorSettings) {
        this.connectorSettings = connectorSettings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
