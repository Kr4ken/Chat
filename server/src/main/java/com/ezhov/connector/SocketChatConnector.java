package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketChatConnector extends ChatConnector {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private ServerSocket serverSocket;

    public SocketChatConnector(ConnectorSettings settings){
        super(settings);
        try{
           serverSocket = new ServerSocket(settings.getPortNumber());
        }
        catch (IOException ex){

        }
    }

    public SocketChatConnector(Socket socket){
        //TODO: Исправить, пока оставил костыль
        super(null);
        this.socket = socket;
    }

    @Override
    public void connect() throws IOException {
        if(socket== null)
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
        out.write(message.getFormatMessage() + "\n");
        out.flush();
    }

    @Override
    public ChatMessage readMessage() throws IOException,IncorrectMessageException {
        String formatMessage = in.readLine();
        return ChatMessage.fromFormatString(formatMessage);
    }

    @Override
    public Boolean checkStatus() {
        return socket != null && socket.isConnected() && !socket.isClosed() ;
    }
}

