package com.ezhov.commands;

import com.ezhov.domain.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;

public class HelpChatCommand extends ChatCommand {
    public HelpChatCommand() {
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
        for (ChatCommand chatCommand : server.getCommands()) {
            ChatMessage answerMessage = new ChatMessage(String.format("> %s - %s", chatCommand.getCommand(), chatCommand.getInfo()), server.getSystemUserName());
            client.sendMessage(answerMessage);
        }
    }
}
