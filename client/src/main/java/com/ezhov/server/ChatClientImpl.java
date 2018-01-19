package com.ezhov.server;

import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.SocketChatConnector;
import com.ezhov.domain.ChatMessage;

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

    public ChatClientImpl() {
        connector = new SocketChatConnector();
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
                    } catch (IOException ex) {
                        isStarted = false;
                        Logger.getLogger(ChatClientImpl.class.getName()).log(Level.WARNING, "Occured error during read server message" + ex);
                    }
                }
            };
            Thread writerThread = new Thread() {
                public void run() {
                    try {
                        while (isStarted) {
                            currentMessage = scanner.next();
                            ChatMessage mess = new ChatMessage(currentMessage, name);
                            connector.sendMessage(mess);
                            currentMessage = "";
                        }
                    } catch (IOException ex) {
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
