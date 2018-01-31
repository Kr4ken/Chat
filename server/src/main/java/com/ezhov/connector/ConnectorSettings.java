package com.ezhov.connector;

public class ConnectorSettings {
    private Integer portNumber;
    private String hostName;


    public ConnectorSettings(Integer portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
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
