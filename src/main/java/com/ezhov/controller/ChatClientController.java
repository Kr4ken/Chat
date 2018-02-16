package com.ezhov.controller;

import com.ezhov.connector.ChatConnector;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class ChatClientController {

    private static Logger LOGGER = Logger.getLogger(ChatClientController.class.getName());

    private final String commandPatternString = "^\\/\\w+\\ *(\\w+\\ *)*";
    private String clientName;
    private ChatConnector connector;
    private ChatServer server;
    private Boolean isStarted;
    private Thread readerThread;
    private Pattern commandPattern;

    public ChatClientController(ChatServer server, ChatConnector connector) {
        isStarted = false;
        this.server = server;
        this.connector = connector;
        readerThread = new Thread(this::readMessage);
        commandPattern = Pattern.compile(commandPatternString);
    }
//
//    private Boolean isCommand(String message) {
//        return commandPattern.matcher(message).matches();
//    }
//
//    private String getCommandFromMessage(String message) {
//        if (isCommand(message)) {
//            return message.split(" ")[0];
//        }
//        return null;
//    }
//
//    private List<String> getParamsFromMessage(String message) {
//        if (isCommand(message)) {
//            String[] params = message.split(" ");
//            params = Arrays.copyOfRange(params, 1, params.length);
//            return Arrays.asList(params);
//        }
//        return null;
//    }

    private void readMessage() {
        while (isStarted) {
            try {
                ChatMessage message = connector.readMessage();
                LOGGER.log(Level.INFO,String.format("ChatClientController get new message %s : %s" ,message.getClient(),message.getMessage()));
                // If command then do without registration(
                if (message.isCommand()) {
                    LOGGER.log(Level.INFO,"Message is command. Trying execute");
                    server.executeCommand(this,message.getCommandFromMessage(),message.getParamsFromMessage());
                } else {
                    // If user registred
                    if (isAllowed(message)) {
                        LOGGER.log(Level.INFO,"User register. All Ok");
                        server.addMessage(message);
                    } else {
                        LOGGER.log(Level.INFO,"User not register. Need registration");
                        ChatMessage errorMessage = new ChatMessage("You not authorized. Use /register to register yourserlf, or /help to show all command list", server.getSystemUserName());
                        sendMessage(errorMessage);
                    }
                }
            } catch (IOException | IncorrectMessageException ex) {
                LOGGER.log(Level.SEVERE, "Occured error during read server message", ex);
                stop();
            }
        }
    }

    private Boolean isAllowed(ChatMessage message) {
        return clientName != null && clientName.equals(message.getClient());
    }

    public void sendMessage(ChatMessage message) {
        try {
            // Check client auth
            if (clientName != null || message.getClient().equals(server.getSystemUserName())) {
                LOGGER.log(Level.INFO,String.format("Send message to %s  Message: %s",clientName, message.getMessage()));
                connector.sendMessage(message);
            }
        } catch (IOException | IncorrectMessageException ex) {
            LOGGER.log(Level.SEVERE, "Occured error during send server message",ex);
        }

    }

    public void start() {
        isStarted = true;
        LOGGER.log(Level.INFO,"Client readerStart");
        try {
            connector.connect();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error when starting client chatting",ex);
            stop();
        }
        readerThread.start();
    }

    public void stop() {
        isStarted = false;
        LOGGER.log(Level.INFO,"Client reader stop");
        server.removeClient(this);
        try {
            connector.disconnect();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error when stop client chatting",ex);
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ChatConnector getConnector() {
        return connector;
    }

    public void setConnector(ChatConnector connector) {
        this.connector = connector;
    }
}
