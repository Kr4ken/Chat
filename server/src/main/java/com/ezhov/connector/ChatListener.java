package com.ezhov.connector;

import java.io.IOException;

public abstract class ChatListener {
    protected ConnectorSettings settings;

    ChatListener(ConnectorSettings settings) {
        this.settings = settings;
    }
    abstract public void connect() throws IOException;

    abstract public void disconnect() throws IOException;

    abstract public ChatConnector waitClient() throws IOException;
}
