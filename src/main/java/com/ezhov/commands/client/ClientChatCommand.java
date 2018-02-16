package com.ezhov.commands.client;

import com.ezhov.client.ChatClient;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.util.List;

public abstract class ClientChatCommand {
    protected String command;
    protected String info;
    protected ChatClient client;

    public ClientChatCommand(ChatClient client) {
        this.client = client;
    }

    abstract public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException;

    public String getCommand() {
        return command;
    }

    public String getInfo() {
        return info;
    }
}
