package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;

import java.io.IOException;

public interface ChatConnector {
    void connect() throws IOException;
    void disconnect() throws IOException;
    void sendMessage(ChatMessage message) throws IOException;
    ChatMessage readMessage() throws IOException;
}
