package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.*;
import java.net.Socket;

public class SocketChatConnector implements ChatConnector {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public SocketChatConnector(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void connect() throws IOException {
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
    public void sendMessage(ChatMessage message) throws IOException, IncorrectMessageException {
        out.write(message.getFormatMessage() + "\n");
        out.flush();
    }

    @Override
    public ChatMessage readMessage() throws IOException, IncorrectMessageException {
        String formatMessage = in.readLine();
        return ChatMessage.fromFormatString(formatMessage);
    }

    @Override
    public Boolean checkStatus() {
        return socket != null && socket.isConnected() && !socket.isClosed() && in!=null && out!= null;
    }
}

