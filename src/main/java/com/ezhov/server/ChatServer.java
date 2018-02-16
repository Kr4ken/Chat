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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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
    // Add/Remove clients operation occasion not so often, but spreading message for client list is so more common operation
    // For the right  work of iterators withiout any additional sync section, used copy on write collection
    protected CopyOnWriteArrayList<ChatClientController> clients;
    protected Map<String,ServerChatCommand> commands;

    protected String name;
    protected Integer lastMessageCount;

    ChatServerSettings chatServerSettings;

    private static Logger LOGGER = Logger.getLogger(ChatServer.class.getName());

    public ChatServer(ChatServerSettings chatServerSettings) {
        LOGGER.log(Level.INFO,"Server constructor");
        commands = new HashMap<>();
        isStarted = new AtomicBoolean(false);
        messages = new LinkedBlockingDeque<>();
        clients = new CopyOnWriteArrayList<>();
        this.chatServerSettings = chatServerSettings;
        name = chatServerSettings.getSystemName();
        lastMessageCount = chatServerSettings.getLastMessageCount();
        chatListener = new SocketChatListener(chatServerSettings.getListenerSettings());
    }

    protected void initCommands() {
        ServerChatCommand command = new RegisterServerChatCommand();
        commands.put(command.getCommand(),command);
        command =new CountCommandServer();
        commands.put(command.getCommand(),command);
        command = new HelpServerChatCommand();
        commands.put(command.getCommand(),command);
        command = new CloseCommandServer();
        commands.put(command.getCommand(),command);
        LOGGER.log(Level.INFO,"Command inits");
    }

    public void run() {
        LOGGER.log(Level.INFO,"Server start");
        initCommands();
        try {
            isStarted.set(true);
            chatListener.start();
            Thread listener = new Thread(this::clientListen);
            listener.start();
            LOGGER.log(Level.INFO,"Listener start");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,"Occured error during established server connection",ex);
            stop();
        }
    }

    public void stop() {
        isStarted.set(false);
        try {
            chatListener.stop();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,"Error wheh trying stop listener",ex);
        }
        LOGGER.log(Level.INFO,"Server stop");
    }

    private void clearMessages(){
       while(messages.size() > chatServerSettings.getMaxMessages())
           messages.removeFirst();
    }

    public void addMessage(ChatMessage chatMessage) {
        LOGGER.log(Level.INFO,String.format("Add message in list : %s:%s",   chatMessage.getClient(),chatMessage.getMessage()));
        messages.add(chatMessage);
        clients.stream()
                // Don't send not registred user and sender
                .filter(client -> client.getClientName() != null && !client.getClientName().equals(chatMessage.getClient()))
                .forEach(client -> client.sendMessage(chatMessage));
        clearMessages();
    }

    public void removeClient(ChatClientController client) {
        LOGGER.log(Level.INFO,String.format("Remove client from client list : %s" , client.getClientName()));
        clients.remove(client);
    }

    public void addClient(ChatClientController client) {
        LOGGER.log(Level.INFO,String.format("Add new client in list : %s" , client.getClientName()));
        clients.add(client);
    }

    public List<ChatMessage> getLastMessages() {
        // Return only lastMessageCount messages
        return messages.stream().skip(chatServerSettings.getMaxMessages() - lastMessageCount).collect(Collectors.toList());
    }

    public Map<String,ServerChatCommand> getCommands() {
        return commands;
    }

    public void executeCommand(String command, List<String> params) {
        ServerChatCommand serverChatCommand=  commands.getOrDefault(command,null);
        if (serverChatCommand != null) {
            LOGGER.log(Level.INFO,"Command found. Execute");
            try {
                serverChatCommand.action(params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {
                LOGGER.log(Level.SEVERE, "Error executing command.", ex);
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
                LOGGER.log(Level.SEVERE, "Occured error during established server connection",ex);
            }
        }
    }

    public void executeCommand(ChatClientController client, String command, List<String> params) {
        ServerChatCommand serverChatCommand=  commands.getOrDefault(command,null);
        if (serverChatCommand != null) {
            LOGGER.log(Level.INFO,"Command found. Execute");
            try {
                serverChatCommand.action(client, this, params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {
                LOGGER.log(Level.SEVERE, "Error executing command. " , ex);
            }
        }
        // Command not found
        else {
            try {
                ChatMessage alertMessage = new ChatMessage("Command " + command + " not found", getSystemUserName());
                client.sendMessage(alertMessage);
            } catch (IncorrectMessageException ex) {
                LOGGER.log(Level.SEVERE, "Error send message to Client" ,ex);
            }
        }
    }

    public String getSystemUserName() {
        return name;
    }

    public CopyOnWriteArrayList<ChatClientController> getClients() {
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
