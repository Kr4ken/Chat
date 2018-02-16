package com.ezhov.commands.server;

import com.ezhov.controller.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;

public class HelpServerChatCommand extends ServerChatCommand {
    public HelpServerChatCommand() {
        command = "/help";
        info = "List all avaliable commands";
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        action(null, null, params);
    }

    @Override
    public void action(ChatClientController client, ChatServer server, List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        System.out.println("Execute help command");
        for (ServerChatCommand serverChatCommand : server.getCommands()) {
            ChatMessage answerMessage = new ChatMessage(String.format("> %s - %s", serverChatCommand.getCommand(), serverChatCommand.getInfo()), server.getSystemUserName());
            client.sendMessage(answerMessage);
        }
    }
}
