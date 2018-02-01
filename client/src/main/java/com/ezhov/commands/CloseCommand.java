package com.ezhov.commands;

import com.ezhov.client.ChatClient;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.util.List;

public class CloseCommand extends ChatCommand{

    public CloseCommand(ChatClient client) {
        super(client);
        command = "/close";
        info = "Close all connections. And close chat";
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat,IncorrectMessageException {
        client.stop();
        System.exit(0);
    }
}
