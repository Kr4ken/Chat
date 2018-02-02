package com.ezhov.domain;

import com.ezhov.connector.ChatConnector;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class ChatClientController {

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
        readerThread = new Thread() {
            public void run() {
                readMessage();
            }
        };
        commandPattern = Pattern.compile(commandPatternString);
    }

    private Boolean isCommand(String message) {
        return commandPattern.matcher(message).matches();
    }

    private String getCommandFromMessage(String message) {
        if (isCommand(message)) {
            return message.split(" ")[0];
        }
        return null;
    }

    private List<String> getParamsFromMessage(String message) {
        if (isCommand(message)) {
            String[] params = message.split(" ");
            params = Arrays.copyOfRange(params, 1, params.length);
            return Arrays.asList(params);
        }
        return null;
    }

    private void readMessage() {
        while (isStarted) {
            try {
                ChatMessage message = connector.readMessage();
                System.out.println("ChatClientController get new message " + message.getClient() + " : " + message.getMessage());
                // If command then do without registration(
                if (isCommand(message.getMessage())) {
                    System.out.println("Message is command. Trying execute");
                    server.executeCommand(this, getCommandFromMessage(message.getMessage()), getParamsFromMessage(message.getMessage()));
                } else {
                    // If user registred
                    if (isAllowed(message)) {
                        System.out.println("User register. All Ok");
                        server.addMessage(message);
                    } else {
                        System.out.println("User not register. Need registration");
                        ChatMessage errorMessage = new ChatMessage("You not authorized. Use /register to register yourserlf, or /help to show all command list", server.getSystemUserName());
                        sendMessage(errorMessage);
                    }
                }
            } catch (IOException | IncorrectMessageException ex) {
                Logger.getLogger(ChatClientController.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
                stop();
            }
        }
    }

    private Boolean isAllowed(ChatMessage message) {
        return clientName != null && clientName.equals(message.getClient());
    }

    public void sendMessage(ChatMessage message) {
        try {
            System.out.println("Send message to " + clientName + " Message:" + message.getMessage());
            connector.sendMessage(message);
        } catch (IOException | IncorrectMessageException ex) {
            Logger.getLogger(ChatClientController.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
        }

    }

    public void start() {
        isStarted = true;
        System.out.println("Client readerStart");
        try {
            connector.connect();
        } catch (IOException ex) {
            stop();
        }
        readerThread.start();
    }

    public void stop() {
        isStarted = false;
        System.out.println("Client reader stop");
        server.getClients().remove(this);
        try {
            connector.disconnect();
        } catch (IOException ex) {
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
