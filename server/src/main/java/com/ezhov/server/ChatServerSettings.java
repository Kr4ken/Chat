package com.ezhov.server;

import com.ezhov.connector.ConnectorSettings;

public class ChatServerSettings {
    private ConnectorSettings connectorSettings;
    private String systemName;
    private Integer lastMessageCount;

    private ChatServerSettings() {
    }

    ;

    public ChatServerSettings(ConnectorSettings connectorSettings, String systemName, Integer lastMessageCount) {
        this.connectorSettings = connectorSettings;
        this.systemName = systemName;
        this.lastMessageCount = lastMessageCount;
    }

    public static ChatServerSettings getDefault() {
        ChatServerSettings result = new ChatServerSettings();
        result.connectorSettings = ConnectorSettings.getDefault();
        result.systemName = "SYSTEM";
        result.lastMessageCount = 100;
        return result;
    }

    public ConnectorSettings getConnectorSettings() {
        return connectorSettings;
    }

    public void setConnectorSettings(ConnectorSettings connectorSettings) {
        this.connectorSettings = connectorSettings;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getLastMessageCount() {
        return lastMessageCount;
    }

    public void setLastMessageCount(Integer lastMessageCount) {
        this.lastMessageCount = lastMessageCount;
    }
}
