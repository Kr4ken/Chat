package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;

public abstract class ChatConnector {

    protected ConnectorSettings settings;

    ChatConnector(ConnectorSettings settings){
       this.settings = settings;
    }
    abstract public void connect() throws IOException;
    abstract public void disconnect() throws IOException;
    abstract public void sendMessage(ChatMessage message) throws IOException,IncorrectMessageException;
    abstract public ChatMessage readMessage() throws IOException,IncorrectMessageException;
}
