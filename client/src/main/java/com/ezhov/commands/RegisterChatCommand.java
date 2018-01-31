package com.ezhov.commands;

import com.ezhov.client.ChatClient;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import java.util.List;

public class RegisterChatCommand  extends ChatCommand{

    protected ChatClient client;

    public RegisterChatCommand(ChatClient client) {
        this.client = client;
        command = "/register";
    }

    @Override
    public String action(List<String> params) throws IncorrectCommandFormat,IncorrectMessageException {
        if(params.size() != 1 || params.get(0) == null || params.get(0).equals(""))
            throw new IncorrectCommandFormat("Incorrect params for command " + command);
        // This command have one param and this is name
        String name = params.get(0);
        client.setName(name);
        return null;
    }
}
