package com.ezhov.connector;

import java.io.IOException;

public interface ChatListener {
    void start() throws IOException;

    void stop() throws IOException;

    ChatConnector getClient() throws IOException;

    Boolean checkStatus();
}
