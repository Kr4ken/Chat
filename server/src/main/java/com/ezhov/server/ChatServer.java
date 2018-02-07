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

public class ChatServer {

    protected Boolean isStarted;
    protected ChatListener chatListener;
    protected ConnectorSettings settings;
    protected List<ChatMessage> messages;
    protected List<ChatClientController> clients;
    protected List<ChatCommand> commands;

    protected String name;
    protected Integer lastMessageCount;

    ChatServerSettings chatServerSettings;

    public ChatServer(ChatServerSettings chatServerSettings) {
        System.out.println("Server constructor!");
        commands = new LinkedList<>();
        isStarted = false;
        messages = new LinkedList<>();
        clients = new LinkedList<>();
        this.chatServerSettings = chatServerSettings;
        name = chatServerSettings.getSystemName();
        lastMessageCount = chatServerSettings.getLastMessageCount();
        chatListener = new SocketChatListener(chatServerSettings.getConnectorSettings());
    }

    protected void initCommands() {
        commands.add(new RegisterChatCommand());
        commands.add(new CountCommand());
        commands.add(new HelpChatCommand());
        commands.add(new CloseCommand());
    }

    public void run() {
        System.out.println("Server start");
        initCommands();
        try {
            chatListener.connect();
            isStarted = true;
            Thread listener = new Thread(this::clientListen);
            listener.start();
            System.out.println("Listener start");
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
        }
    }

    public void stop() {
        isStarted = false;
        System.out.println("Server stop");
    }

    public synchronized void addMessage(ChatMessage chatMessage) {
        System.out.println("Add message in list :" + chatMessage.getClient() + ":" + chatMessage.getMessage());
        messages.add(chatMessage);
        for (ChatClientController client : clients) {
            //Don't send message to yourself
            if (!client.getClientName().equals(chatMessage.getClient()))
                client.sendMessage(chatMessage);
        }
    }

    public synchronized void removeClient(ChatClientController client) {
        System.out.println("Remove client from client list :" + client.getClientName());
        clients.remove(client);
    }

    public synchronized void addClient(ChatClientController client) {
        System.out.println("Add new client in list :" + client.getClientName());
        clients.add(client);
    }

    public List<ChatMessage> getLastMessages() {
        return messages.stream().limit(lastMessageCount).collect(Collectors.toList());
    }

    public List<ChatCommand> getCommands() {
        return commands;
    }

    public void executeCommand(String command, List<String> params) {
        Optional<ChatCommand> chatCommand = commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if (chatCommand.isPresent()) {
            try {
                chatCommand.get().action(params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {

            }
        }
    }

    protected void clientListen() {
        while (isStarted) {
            try {
                ChatConnector connector = chatListener.waitClient();
                ChatClientController chatClient = new ChatClientController(this, connector);
                addClient(chatClient);
                chatClient.start();
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
            }


        }
    }

    public void executeCommand(ChatClientController client, String command, List<String> params) {
        Optional<ChatCommand> chatCommand = commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if (chatCommand.isPresent()) {
            System.out.println("Command found execute ");
            try {
                chatCommand.get().action(client, this, params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {

            }
        }
        // Command not found
        else {
            try {
                ChatMessage alertMessage = new ChatMessage("Command " + command + " not found", getSystemUserName());
                client.sendMessage(alertMessage);
            } catch (Exception e) {

            }
        }
    }

    public String getSystemUserName() {
        return name;
    }

    public List<ChatClientController> getClients() {
        return clients;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public ConnectorSettings getSettings() {
        return settings;
    }

    public void setSettings(ConnectorSettings settings) {
        this.settings = settings;
    }

    public Boolean getStarted() {
        return isStarted;
    }
}
