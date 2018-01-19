package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;

import java.io.*;
import java.net.Socket;

public class SocketChatConnector implements ChatConnector {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    @Override
    public void connect() throws IOException {
        String hostName = "127.0.0.1";
        Integer portNumber = 8989;
        socket = new Socket(hostName,portNumber);
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
    public void sendMessage(ChatMessage message) throws IOException {
        out.write(message.getFormatMessage());
        out.flush();
    }

    @Override
    public ChatMessage readMessage() throws IOException {
        String formatMessage = in.readLine();
        return new ChatMessage(formatMessage);
    }
}
