package com.ezhov.server;

import com.ezhov.commands.ChatCommand;
import com.ezhov.connector.ChatListener;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.domain.ChatClientController;
import com.ezhov.domain.ChatMessage;

import java.util.List;

public abstract class ChatServer {

    protected Boolean isStarted;
    protected ChatListener chatListener;
    protected ConnectorSettings settings;
    protected List<ChatMessage> messages;
    protected List<ChatClientController> clients;
    protected List<ChatCommand> commands;

    protected final String name = "SYSTEM";
    protected final Integer lastMessageCount = 100;

    abstract public void run();
    abstract public void stop();
    abstract public void addMessage(ChatMessage chatMessage);
    abstract public List<ChatMessage> getLastMessages();
    abstract public List<ChatCommand> getCommands();
    abstract public void executeCommand(String command,List<String> params);
    abstract public void executeCommand(ChatClientController client, String command,List<String> params);

    public String getSystemUserName(){
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
}
