package com.ezhov.server;

import com.ezhov.commands.*;
import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.ChatListener;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.connector.SocketChatListener;
import com.ezhov.domain.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatServerImpl implements ChatServer {

    Boolean isStarted;
    ChatListener chatListener;
    ConnectorSettings settings;
    List<ChatMessage> messages;
    List<ChatClientController> clients;
    List<ChatCommand> commands;
    final String name = "SYSTEM";
    final Integer lastMessageCount = 100;

    private void initCommands(){
        commands =  new LinkedList<>();
        commands.add(new RegisterChatCommand());
        commands.add(new CountCommand());
        commands.add(new HelpChatCommand());
        commands.add(new CloseCommand());
    }

    @Override
    public String getSystemUserName(){
       return name;
    }

    @Override
    public List<ChatClientController> getClients() {
        return clients;
    }

    @Override
    public void executeCommand(String command, List<String> params) {
        Optional<ChatCommand> chatCommand =  commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if(chatCommand.isPresent()){
            try {
                chatCommand.get().action(params);
            }
            catch (IncorrectMessageException | IncorrectCommandFormat ex){

            }
        }
    }

    @Override
    public List<ChatMessage> getLastMessages() {
        return messages.stream().limit(lastMessageCount).collect(Collectors.toList());
    }

    @Override
    public List<ChatCommand> getCommands() {
        return commands;
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
        for (ChatClientController client: clients) {
           //Don't send message to yourself
            if(!client.getClientName().equals(chatMessage.getClient()))
                client.sendMessage(chatMessage);
        }
    }

    @Override
    public void executeCommand(ChatClientController client, String command, List<String> params) {
        Optional<ChatCommand> chatCommand =  commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if(chatCommand.isPresent()){
            System.out.println("Command found execute ");
            try {
                chatCommand.get().action(client,this,params);
            }
            catch (IncorrectMessageException | IncorrectCommandFormat ex){

            }
        }
        // Command not found
        else {
            try {
                ChatMessage alertMessage = new ChatMessage("Command " + command + " not found", getSystemUserName());
                client.sendMessage(alertMessage);
            } catch (Exception e){

            }
        }
    }

    public synchronized void addClient(ChatClientController client){
        System.out.println("Add new client in list :" + client.getClientName());
        clients.add(client);
    }

    private void clientListen() {
        while (isStarted) {
            try {
                ChatConnector connector = chatListener.waitClient();
                ChatClientController chatClient = new ChatClientController(this,connector);
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
        initCommands();
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
