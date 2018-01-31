package com.ezhov.commands;

import com.ezhov.server.ChatServer;

import java.util.List;

public class RegisterChatCommand  extends ChatCommand{

    protected ChatServer server;

    RegisterChatCommand(ChatServer server) {
        this.server = server;
        command = "register";
    }

    @Override
    public String action(List<String> params) {

        return null;
    }
}
