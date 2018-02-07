package com.ezhov.client;

import com.ezhov.commands.CloseCommand;
import com.ezhov.commands.RegisterChatCommand;
import com.ezhov.connector.SocketChatConnector;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClientTerminal extends ChatClient {

    private ClientSettings clientSettings;
    private String startInfoMessage;


    public ChatClientTerminal(ClientSettings clientSettings) {
        super();
        this.clientSettings = clientSettings;
        connectorSettings = clientSettings.getConnectorSettings();
        connector = new SocketChatConnector(connectorSettings);
        inputStream = System.in;
        printStream = System.out;
        scanner = new Scanner(inputStream);
        name = clientSettings.getName();
        startInfoMessage = "Welcome to chat.\nTo start chatting type \"/register <Nickname>\" to registration, and continue conversation.\nTo end chatting write \"/close\"\n";
    }

    @Override
    protected void initCommandsList() {
        super.initCommandsList();
        commands.add(new RegisterChatCommand(this));
        commands.add(new CloseCommand(this));
    }

    @Override
    public void connect() {
        super.connect();
        try {
            // First connnect
            // Register
            if (name != null || !name.equals("")) {
                ChatMessage registerMessage = new ChatMessage("/register " + name, name);
                connector.sendMessage(registerMessage);
            }
        } catch (IOException | IncorrectMessageException ex) {
            Logger.getLogger(ChatClientTerminal.class.getName()).log(Level.SEVERE, "Error during initial registration\n" + ex);
        }
    }

    @Override
    public void start() {
        printStream.println(startInfoMessage);
        super.start();
    }

    @Override
    public  void stop(){
        try {
            // Disconnect
                ChatMessage registerMessage = new ChatMessage("/close", name);
                connector.sendMessage(registerMessage);
        } catch (IOException | IncorrectMessageException ex) {
            Logger.getLogger(ChatClientTerminal.class.getName()).log(Level.SEVERE, "Error during disconnect\n" + ex);
        }
        super.stop();
    }
}
