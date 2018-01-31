package com.ezhov.server;

import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.ChatListener;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.connector.SocketChatListener;
import com.ezhov.domain.ChatClient;
import com.ezhov.domain.ChatMessage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServerImpl implements ChatServer {

    Boolean isStarted;
    ChatListener chatListener;
    ConnectorSettings settings;
    List<ChatMessage> messages;
    List<ChatClient> clients;
    final String name = "SYSTEM";

    @Override
    public String getSystemUserName(){
       return name;
    }


    public ChatServerImpl() {
        System.out.println("Server constructor!");
        settings = new ConnectorSettings(8989, "127.0.0.1");
        chatListener = new SocketChatListener(settings);
        isStarted = false;
        messages = new LinkedList<>();
        clients = new LinkedList<>();
    }

    @Override
    public synchronized void addMessage(ChatMessage chatMessage) {
        System.out.println("Add message in list :" + chatMessage.getClient() + ":" + chatMessage.getMessage());
        messages.add(chatMessage);
        for (ChatClient client: clients) {
          client.sendMessage(chatMessage);
        }
    }

    public synchronized void addClient(ChatClient client){
        System.out.println("Add new client in list :" + client.getName());
        clients.add(client);
    }

    private void clientListen() {
        while (isStarted) {
            try {
                ChatConnector connector = chatListener.waitClient();
                ChatClient chatClient = new ChatClient(this,connector);
                addClient(chatClient);
                chatClient.start();
            } catch (IOException ex) {
                Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
            }


        }
    }


    @Override
    public void run() {
        System.out.println("Server start");
        try {
            chatListener.connect();
            isStarted = true;
            Thread listener = new Thread(){
                @Override
                public void run(){
                    clientListen();
                }
            };
            listener.start();
            System.out.println("Listener start");
        } catch (IOException ex) {
            Logger.getLogger(ChatServerImpl.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
        }
    }

    @Override
    public void stop() {
        isStarted = false;
        System.out.println("Server stop");
    }
}
