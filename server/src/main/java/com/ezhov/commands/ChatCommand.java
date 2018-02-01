package com.ezhov.commands;

import com.ezhov.domain.ChatClientController;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;

public abstract class ChatCommand {
    protected String command;
    protected String info;
    abstract public void action(List<String> params) throws IncorrectCommandFormat,IncorrectMessageException;
    abstract public void action(ChatClientController client, ChatServer server, List<String> params) throws IncorrectCommandFormat,IncorrectMessageException;

    public String getCommand() { return command; }

    public String getInfo() { return info; }
}
