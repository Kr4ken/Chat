package com.ezhov.connector;

public class ConnectorSettings {
    private Integer portNumber;
    private String hostName;


    public ConnectorSettings(Integer portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public static ConnectorSettings getDefault() {
        ConnectorSettings connectorSettings = new ConnectorSettings(8989, "127.0.0.1");
        return connectorSettings;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
