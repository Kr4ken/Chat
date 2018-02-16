package com.ezhov.commands.server;

import com.ezhov.controller.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterServerChatCommand extends ServerChatCommand {
    private static Logger LOGGER = Logger.getLogger(RegisterServerChatCommand.class.getName());

    public RegisterServerChatCommand() {
        command = "/register";
        info = "Command for registration. Use when you wanna start to chat or change name";
    }

    public static final Object lock = new Object();

    private Boolean isValidName(ChatServer server, String name) {
        LinkedBlockingDeque<ChatClientController> clients = server.getClients();
        Boolean nameExist = clients.stream().anyMatch(chatClient -> name.equals(chatClient.getClientName()));
        Boolean systemEquals = name.equals(server.getSystemUserName());
        return !systemEquals && !nameExist;
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        action(null, null, params);
    }

    @Override
    public void action(ChatClientController client, ChatServer server, List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        LOGGER.log(Level.INFO,"Execute register command withs params");
        if (params.size() != 1 || params.get(0) == null || params.get(0).equals(""))
            throw new IncorrectCommandFormat("Incorrect params for command " + command);
        // This command have one param and this is name
        String name = params.get(0);
        LOGGER.log(Level.INFO,String.format("register name: %s ",name));
        synchronized (lock) {
            if (isValidName(server, name)) {
                LOGGER.log(Level.INFO,"Name valid");
                client.setClientName(name);
                ChatMessage answerMessage = new ChatMessage(String.format("%s %s", command, name), server.getSystemUserName());
                client.sendMessage(answerMessage);
                for (ChatMessage message : server.getLastMessages()) {
                    client.sendMessage(message);
                }
                LOGGER.log(Level.INFO,String.format("Client %s registred and send history",name));
            } else {
                LOGGER.log(Level.INFO,"Name is invalid");
                ChatMessage answerMessage = new ChatMessage("Name " + name + " already in use. Choose another one.", server.getSystemUserName());
                client.sendMessage(answerMessage);
            }
        }
    }
}
