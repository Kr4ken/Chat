package com.ezhov.connector;

import java.io.IOException;

public interface ChatListener {
    void connect() throws IOException;
    void disconnect() throws IOException;
    ChatConnector waitClient() throws IOException;
}
