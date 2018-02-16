package com.ezhov.commands.server;

import com.ezhov.controller.ChatClientController;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.server.ChatServer;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloseCommandServer extends ServerChatCommand {

    private static Logger LOGGER = Logger.getLogger(CloseCommandServer.class.getName());

    public CloseCommandServer() {
        command = "/close";
        info = "Close connection";
    }

    @Override
    public void action(List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
    }

    @Override
    public void action(ChatClientController client, ChatServer server, List<String> params) throws IncorrectCommandFormat, IncorrectMessageException {
        LOGGER.log(Level.INFO,"Execute close command");
        ChatMessage closeMessage = new ChatMessage(command, server.getSystemUserName());
        client.sendMessage(closeMessage);
        client.stop();
        server.removeClient(client);
    }
}
