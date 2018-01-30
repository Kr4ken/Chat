package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.io.*;
import java.net.Socket;

public class SocketChatConnector extends ChatConnector {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public SocketChatConnector(ConnectorSettings settings){
        super(settings);
    }

    @Override
    public void connect() throws IOException {
        socket = new Socket(settings.getHostName(),settings.getPortNumber());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void disconnect() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    @Override
    public void sendMessage(ChatMessage message) throws IOException,IncorrectMessageException {
        out.write(message.getFormatMessage());
        out.flush();
    }

    @Override
    public ChatMessage readMessage() throws IOException,IncorrectMessageException {
        String formatMessage = in.readLine();
        return ChatMessage.fromFormatString(formatMessage);
    }
}
