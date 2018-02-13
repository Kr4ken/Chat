package com.ezhov.commands.server;

import com.ezhov.commands.server.ChatCommand;
import com.ezhov.controller.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;

public class CountCommand extends ChatCommand {
    public CountCommand() {
        command = "/count";
        info = "Show exist count clients in chat";
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
    }

    @Override
    public void action(ChatClientController client, ChatServer server, List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        System.out.println("Execute count command");
        ChatMessage countMessage = new ChatMessage(String.format("%d Clients in chat now", server.getClients().size()), server.getSystemUserName());
        client.sendMessage(countMessage);
    }
}
