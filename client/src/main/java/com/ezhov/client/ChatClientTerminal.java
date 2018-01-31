package com.ezhov.client;

import com.ezhov.commands.ChatCommand;
import com.ezhov.commands.CloseCommand;
import com.ezhov.commands.RegisterChatCommand;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.connector.SocketChatConnector;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClientTerminal extends ChatClient {

    private ClientSettings clientSettings;
    private String startInfoMessage;


    protected void initCommandsList() {
        commands = new LinkedList<>();
        commands.add(new RegisterChatCommand(this));
        commands.add(new CloseCommand(this));
    }

    public ChatClientTerminal(ClientSettings clientSettings){
        super();
        connectorSettings = clientSettings.getConnectorSettings();
        connector = new SocketChatConnector(connectorSettings);
        inputStream = System.in;
        printStream = System.out;
        scanner = new Scanner(inputStream);
        name = clientSettings.getName();
        startInfoMessage = "Welcome to chat.\nTo start chatting type \"/register <Nickname>\" to registration, and continue conversation.\nTo end chatting write \"/close\"\n";
    }

//    public ChatClientTerminal(String name) {
//        this();
//        this.name = name;
//    }
//
//    public ChatClientTerminal() {
//        super();
//        connectorSettings = new ConnectorSettings(8989, "127.0.0.1");
//        connector = new SocketChatConnector(connectorSettings);
//        inputStream = System.in;
//        printStream = System.out;
//        scanner = new Scanner(inputStream);
//    }

    public void connect() {
        try {
            connector.connect();
            if(name != null || !name.equals("")){
                ChatMessage registerMessage = new ChatMessage("/register "+name,name);
                connector.sendMessage(registerMessage);
            }
        } catch (IOException | IncorrectMessageException ex) {
            Logger.getLogger(ChatClientTerminal.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
            reconnect();
        }
    }


    public void start() {
        printStream.println(startInfoMessage);
        isStarted = true;
        connect();
        readerThread.start();
        writerThread.start();
    }

    public void stop() {
        isStarted = false;
        try {
            connector.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(ChatClientTerminal.class.getName()).log(Level.SEVERE, "Occured error on disconnecting server " + ex);
        }

    }


}
