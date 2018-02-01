package com.ezhov.server;

import com.ezhov.commands.ChatCommand;
import com.ezhov.domain.ChatClientController;
import com.ezhov.domain.ChatMessage;

import java.util.List;

public interface ChatServer {
    void run();
    void stop();
    void addMessage(ChatMessage chatMessage);
    String getSystemUserName();
    List<ChatClientController> getClients();
    List<ChatMessage> getLastMessages();
    List<ChatCommand> getCommands();
    void executeCommand(String command,List<String> params);
    void executeCommand(ChatClientController client, String command,List<String> params);
}
