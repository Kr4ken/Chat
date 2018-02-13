package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.*;
import java.net.Socket;

public class SocketChatConnector implements ChatConnector {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private ConnectorSettings settings;

    public SocketChatConnector(Socket socket) {
        this.socket = socket;
    }

    public SocketChatConnector(ConnectorSettings settings) {
        this.settings = settings;
    }

    public void connect() throws IOException {
        if(socket==null)
            socket = new Socket(settings.getHostName(), settings.getPortNumber());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void disconnect() throws IOException {
        socket.close();
        in.close();
        out.close();
    }

    public void sendMessage(ChatMessage message) throws IOException, IncorrectMessageException {
        out.write(message.getFormatMessage() + "\n");
        out.flush();
    }

    public ChatMessage readMessage() throws IOException, IncorrectMessageException {
        String formatMessage = in.readLine();
        return ChatMessage.fromFormatString(formatMessage);
    }

    public Boolean checkStatus() {
        return socket != null && socket.isConnected() && !socket.isClosed() && in!=null && out!= null;
    }
}

