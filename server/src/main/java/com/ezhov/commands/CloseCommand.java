package com.ezhov.commands;

import com.ezhov.domain.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;

public class CloseCommand extends ChatCommand {

    public CloseCommand() {
        command = "/close";
        info = "Close connection";
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
    }

    @Override
    public void action(ChatClientController client, ChatServer server, List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        System.out.println("Execute close command");
        ChatMessage closeMessage = new ChatMessage(command, server.getSystemUserName());
        client.sendMessage(closeMessage);
        client.stop();
        server.removeClient(client);
    }
}
