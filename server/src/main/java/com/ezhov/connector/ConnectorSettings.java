package com.ezhov.connector;

public class ConnectorSettings {
    private Integer portNumber;

    public ConnectorSettings(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public static ConnectorSettings getDefault() {
        return new ConnectorSettings(8989);
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }
}
