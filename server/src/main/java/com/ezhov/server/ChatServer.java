package com.ezhov.server;

import com.ezhov.commands.ChatCommand;
import com.ezhov.domain.ChatClient;
import com.ezhov.domain.ChatMessage;

import java.util.List;

public interface ChatServer {
    void run();
    void stop();
    void addMessage(ChatMessage chatMessage);
    String getSystemUserName();
    List<ChatClient> getClients();
    List<ChatMessage> getLastMessages();
    List<ChatCommand> getCommands();
    void executeCommand(String command,List<String> params);
    void executeCommand(ChatClient client, String command,List<String> params);
}
