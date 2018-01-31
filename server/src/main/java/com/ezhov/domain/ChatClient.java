package com.ezhov.domain;

import com.ezhov.connector.ChatConnector;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class ChatClient {
    private String name;
    private ChatConnector connector;
    private ChatServer server;

    private Boolean isStarted;

    private Thread readerThread;

    private final String commandPatternString  = "^\\/\\w+\\ *(\\w+\\ *)*";
    private Pattern commandPattern;

    public ChatClient(ChatServer server, ChatConnector connector) {
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

    private Boolean isCommand(String message){
        return commandPattern.matcher(message).matches();
    }

    private String getCommandFromMessage(String message){
        if(isCommand(message)){
            //
            return message.split(" ")[0];
        }
        return null;
    }

    private List<String> getParamsFromMessage(String message){
        if(isCommand(message)){
//            List<String> params =  Arrays.asList(message.split(" "));
            String[] params =  message.split(" ");
            params = Arrays.copyOfRange(params,1,params.length);
            return Arrays.asList(params);
        }
        return null;
    }

    private void readMessage() {
        while (isStarted) {
            try {
                ChatMessage message = connector.readMessage();
                System.out.println("ChatClient get new message " + message.getClient() + ":" + message.getMessage());
                // If command then do without registration(
                if(isCommand(message.getMessage())){
                    System.out.println("Message is command. Trying execute");
                    server.executeCommand(this,getCommandFromMessage(message.getMessage()), getParamsFromMessage(message.getMessage()));
                }
                else {
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
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
            }
        }
    }

    private Boolean isAllowed(ChatMessage message){
        return name != null &&  name.equals(message.getClient());
    }

    public void sendMessage(ChatMessage message) {
        try {
            System.out.println("Send message to " + name + " Message:" + message.getMessage());
            connector.sendMessage(message);
        } catch (IOException | IncorrectMessageException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
        }

    }

    public void start() {
        isStarted = true;
        System.out.println("Client readerStart");
        try {
            connector.connect();
        } catch (IOException ex) {
        }
        readerThread.start();
    }

    public void stop() {
        isStarted = false;
        System.out.println("Client reader stop");
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
