package com.ezhov.settings;

public class ListenerSettings {
    private Integer portNumber;

    public ListenerSettings(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public static ListenerSettings getDefault() {
        return new ListenerSettings(8989);
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }
}
