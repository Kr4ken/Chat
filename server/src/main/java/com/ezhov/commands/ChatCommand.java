package com.ezhov.commands;

import java.util.List;

public abstract class ChatCommand {
    protected String command;

    abstract public String action(List<String> params);
}
