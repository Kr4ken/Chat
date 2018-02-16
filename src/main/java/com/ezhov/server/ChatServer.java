package com.ezhov.server;

import com.ezhov.commands.server.*;
import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.ChatListener;
import com.ezhov.connector.SocketChatListener;
import com.ezhov.controller.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.settings.ChatServerSettings;
import com.ezhov.settings.ListenerSettings;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatServer {

    protected AtomicBoolean isStarted;
    protected ChatListener chatListener;
    protected ListenerSettings settings;
    protected LinkedBlockingDeque<ChatMessage> messages;
    protected LinkedBlockingDeque<ChatClientController> clients;
    protected List<ServerChatCommand> commands;

    protected String name;
    protected Integer lastMessageCount;

    ChatServerSettings chatServerSettings;

    public ChatServer(ChatServerSettings chatServerSettings) {
        System.out.println("Server constructor!");
        commands = new LinkedList<>();
        isStarted = new AtomicBoolean(false);
        messages = new LinkedBlockingDeque<>();
        clients = new LinkedBlockingDeque<>();
        this.chatServerSettings = chatServerSettings;
        name = chatServerSettings.getSystemName();
        lastMessageCount = chatServerSettings.getLastMessageCount();
        chatListener = new SocketChatListener(chatServerSettings.getListenerSettings());
    }

    protected void initCommands() {
        commands.add(new RegisterServerChatCommand());
        commands.add(new CountCommandServer());
        commands.add(new HelpServerChatCommand());
        commands.add(new CloseCommandServer());
    }

    public void run() {
        System.out.println("Server start");
        initCommands();
        try {
            isStarted.set(true);
            chatListener.start();
            Thread listener = new Thread(this::clientListen);
            listener.start();
            System.out.println("Listener start");
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
            stop();
        }
    }

    public void stop() {
        isStarted.set(false);
        try {
            chatListener.stop();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Error wheh trying stop listener " + ex);
        }
        System.out.println("Server stop");
    }

    private void clearMessages(){
       while(messages.size() > chatServerSettings.getMaxMessages())
           messages.removeFirst();
    }

    public void addMessage(ChatMessage chatMessage) {
        System.out.println("Add message in list :" + chatMessage.getClient() + ":" + chatMessage.getMessage());
        messages.add(chatMessage);
        clients.stream()
                // Don't send not registred user and sender
                .filter(client -> client.getClientName() != null && !client.getClientName().equals(chatMessage.getClient()))
                .forEach(client -> client.sendMessage(chatMessage));
        clearMessages();
    }

    public void removeClient(ChatClientController client) {
        System.out.println("Remove client from client list :" + client.getClientName());
        clients.remove(client);
    }

    public void addClient(ChatClientController client) {
        System.out.println("Add new client in list :" + client.getClientName());
        clients.add(client);
    }

    public List<ChatMessage> getLastMessages() {
        return messages.stream().limit(lastMessageCount).collect(Collectors.toList());
    }

    public List<ServerChatCommand> getCommands() {
        return commands;
    }

    public void executeCommand(String command, List<String> params) {
        Optional<ServerChatCommand> chatCommand = commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if (chatCommand.isPresent()) {
            try {
                chatCommand.get().action(params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Error executing command. " + ex);
            }
        }
    }

    protected void clientListen() {
        while (isStarted.get()) {
            try {
                ChatConnector connector = chatListener.getClient();
                ChatClientController chatClient = new ChatClientController(this, connector);
                addClient(chatClient);
                chatClient.start();
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
            }
        }
    }

    public void executeCommand(ChatClientController client, String command, List<String> params) {
        Optional<ServerChatCommand> chatCommand = commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if (chatCommand.isPresent()) {
            System.out.println("Command found execute ");
            try {
                chatCommand.get().action(client, this, params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Error executing command. " + ex);
            }
        }
        // Command not found
        else {
            try {
                ChatMessage alertMessage = new ChatMessage("Command " + command + " not found", getSystemUserName());
                client.sendMessage(alertMessage);
            } catch (Exception e) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Error send message to Client" + e);
            }
        }
    }

    public String getSystemUserName() {
        return name;
    }

    public LinkedBlockingDeque<ChatClientController> getClients() {
        return clients;
    }

    public LinkedBlockingDeque<ChatMessage> getMessages() {
        return messages;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public ListenerSettings getSettings() {
        return settings;
    }

    public void setSettings(ListenerSettings settings) {
        this.settings = settings;
    }

    public Boolean getStarted() {
        return isStarted.get();
    }
}
