package com.ezhov.commands;

import com.ezhov.domain.ChatClient;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;

public class HelpChatCommand extends ChatCommand {
    public HelpChatCommand(){
        command = "/help";
        commentary = " List all avaliable commands";
    }

    @Override
    public String action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        return null;
    }

    @Override
    public String action(ChatClient client, ChatServer server, List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        System.out.println("Execute help command withs params");
        for(ChatCommand chatCommand:server.getCommands()){
            ChatMessage answerMessage = new ChatMessage(String.format(">%s - %s",chatCommand.getCommand(),chatCommand.getCommentary()), server.getSystemUserName());
            client.sendMessage(answerMessage);
        }
        return null;
    }
}
