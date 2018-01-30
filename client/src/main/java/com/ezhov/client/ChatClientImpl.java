package com.ezhov.client;

import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.connector.SocketChatConnector;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClientImpl implements ChatClient {
    private ChatConnector connector;
    private List<ChatMessage> messages;
    private String currentMessage;
    private Boolean isStarted;
    private String name;
    private Scanner scanner;

    private ConnectorSettings connectorSettings;

    public ChatClientImpl(String name) {
        this();
        this.name = name;
    }

    public ChatClientImpl() {
        connectorSettings = new ConnectorSettings(8989,"127.0.0.1");
        connector = new SocketChatConnector(connectorSettings);
        messages = new LinkedList<>();
        currentMessage = "";
        isStarted = false;
        scanner = new Scanner(System.in);
    }

    public void start() {
        try {
            connector.connect();
            isStarted = true;
            Thread readerThread = new Thread() {
                public void run() {
                    try {

                        while (isStarted) {
                            ChatMessage mess = connector.readMessage();
                            messages.add(mess);
                            System.out.println(mess.getFormatMessage());
                        }
                    } catch (IOException | IncorrectMessageException ex) {
                        isStarted = false;
                        Logger.getLogger(ChatClientImpl.class.getName()).log(Level.WARNING, "Occured error during read server message" + ex);
                    }
                }
            };
            Thread writerThread = new Thread() {
                public void run() {
                    try {
                        while (isStarted) {
                            currentMessage = scanner.nextLine();
                            currentMessage += "\n";
                            ChatMessage mess = new ChatMessage(currentMessage, name);
                            connector.sendMessage(mess);
                            currentMessage = "";
                        }
                    } catch (IOException | IncorrectMessageException ex) {
                        isStarted = false;
                        Logger.getLogger(ChatClientImpl.class.getName()).log(Level.WARNING, "Occured error during read server message" + ex);
                    }
                }
            };
            readerThread.start();
            writerThread.start();
        } catch (IOException ex) {
            Logger.getLogger(ChatClientImpl.class.getName()).log(Level.WARNING, "Occured error during established server connection" + ex);
        }

    }

    public void end() {
        isStarted = false;
    }


}
