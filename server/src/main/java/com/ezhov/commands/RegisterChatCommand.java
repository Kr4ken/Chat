package com.ezhov.commands;

import com.ezhov.domain.ChatClient;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public class RegisterChatCommand  extends ChatCommand{
     public RegisterChatCommand(){
        command = "/register";
        commentary = "Command for registration. Use when you wanna connect to chat or change name";
    }

    private Boolean isValidName(ChatServer server, String name)
    {
        Boolean nameExist = server.getClients().stream().anyMatch(chatClient ->name.equals(chatClient.getName()));
        Boolean systemEquals = name.equals(server.getSystemUserName());
        return !systemEquals && !nameExist;
    }
    @Override
    public String action(List<String> params) throws IncorrectCommandFormat,IncorrectMessageException {
         return null;
    }

    @Override
    public String action(ChatClient client,ChatServer server, List<String> params) throws IncorrectCommandFormat,IncorrectMessageException {
         System.out.println("Execute register command withs params");
        if(params.size() != 1 || params.get(0) == null || params.get(0).equals(""))
            throw new IncorrectCommandFormat("Incorrect params for command " + command);
        // This command have one param and this is name
        String name  = params.get(0);
        System.out.println("register name = " + name);
        if(isValidName(server,name)){
            System.out.println("Name is valid");
            client.setName(name);
            System.out.println("Client name set " + client.getName());
            ChatMessage answerMessage = new ChatMessage(String.format("%s %s",command,name), server.getSystemUserName());
            client.sendMessage(answerMessage);
            System.out.println("Client send message " + answerMessage.getFormatMessage());
            for(ChatMessage message:server.getLastMessages()){
                client.sendMessage(message);
            }
            System.out.println("Client send history " );
        }
        else{
            System.out.println("Name is invalid");
            ChatMessage answerMessage = new ChatMessage("Name " + name + " already in use. Choose another one.",server.getSystemUserName());
            client.sendMessage(answerMessage);
        }
        return null;
    }
}
