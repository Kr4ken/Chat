package com.ezhov.commands;

import com.ezhov.client.ChatClient;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.util.List;

public class RegisterChatCommand extends ChatCommand {


    public RegisterChatCommand(ChatClient client) {
        super(client);
        command = "/register";
        info = "Client side command to registration";
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        if (params.size() != 1 || params.get(0) == null || params.get(0).equals(""))
            throw new IncorrectCommandFormat("Incorrect params for command " + command);
        // This command have one param and this is name
        String name = params.get(0);
        client.setName(name);
    }
}
