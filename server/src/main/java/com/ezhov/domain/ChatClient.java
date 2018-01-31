package com.ezhov.domain;

import com.ezhov.connector.ChatConnector;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
    private String name;
    private ChatConnector connector;
    private ChatServer server;

    private Boolean isStarted;

    private Thread readerThread;
    private Thread writerThread;

    public ChatClient(ChatServer server, ChatConnector connector) {
        isStarted = false;
        this.server = server;
        this.connector = connector;
        readerThread = new Thread() {
            public void run() {
                readMessage();
            }
        };
//        writerThread = new Thread() {
//            public void run() {
//                sendMessage();
//            }
//        };
    }

    private void readMessage() {
        while (isStarted) {
            try {
                ChatMessage message = connector.readMessage();
                server.addMessage(message);
            } catch (IOException | IncorrectMessageException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
            }
        }
    }

    public void sendMessage(ChatMessage message) {
        try {
            connector.sendMessage(message);
        } catch (IOException | IncorrectMessageException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
        }

    }

    public void start() {
        isStarted = true;
        try {
            connector.connect();
        } catch (IOException ex) {
        }
        readerThread.start();
    }

    public void stop() {
        isStarted = false;
        try {
            connector.disconnect();
        } catch (IOException ex) {
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatConnector getConnector() {
        return connector;
    }

    public void setConnector(ChatConnector connector) {
        this.connector = connector;
    }
}
