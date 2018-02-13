package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;

public interface ChatConnector {
    void connect() throws IOException;

    void disconnect() throws IOException;

    void sendMessage(ChatMessage message) throws IOException, IncorrectMessageException;

    ChatMessage readMessage() throws IOException, IncorrectMessageException;

    Boolean checkStatus();
}
