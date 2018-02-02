package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class TestSocketChatConnector {
    private static final Integer portNumber = 8989;
    private static final String hostName = "127.0.0.1";
    private static final ConnectorSettings settings = new ConnectorSettings(portNumber, hostName);
    private static ServerMock server;

    @Before
    public void beforeEachTest() {
        server = new ServerMock(settings.getPortNumber());
    }

    @Test
    public void testCreationAndConnection() throws IOException {
        SocketChatConnector connector = new SocketChatConnector(settings);
        connector.connect();
        // Must connect without errors
        assertTrue(true);
        try {
            SocketChatConnector connector2 = new SocketChatConnector(settings);
            connector2.connect();
            fail("Must fall");
        } catch (IOException ex) {
            assertTrue(true);
        }
        connector.disconnect();
    }

    @Test
    public void testSendMessage() throws IOException, IncorrectMessageException {
        SocketChatConnector connector = new SocketChatConnector(settings);
        connector.connect();
        ChatMessage message = new ChatMessage("My important message", "Kirill");
        connector.sendMessage(message);
        ChatMessage recievedMessage = server.readMessage();
        assertEquals(message, recievedMessage);
    }

    @Test
    public void testRecieveMessage() throws IOException, IncorrectMessageException {
        SocketChatConnector connector = new SocketChatConnector(settings);
        connector.connect();
        ChatMessage message = new ChatMessage("My important message", "Kirill");
        server.sendMessage(message);
        ChatMessage recievedMessage = connector.readMessage();
        assertEquals(message, recievedMessage);
    }

    @Test
    public void testStatusCheck() throws IOException, IncorrectMessageException {
        SocketChatConnector connector = new SocketChatConnector(settings);
        assertFalse(connector.checkStatus());
        connector.connect();
        assertTrue(connector.checkStatus());
        connector.disconnect();
        assertFalse(connector.checkStatus());
    }

    private static class ServerMock {
        private Socket server;
        private BufferedReader in;
        private BufferedWriter out;

        public ServerMock(Integer portNumber) {
            try {
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Thread serverThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            server = serverSocket.accept();
                            System.out.println("Client accepted");
                            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                            out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
                        } catch (IOException ex) {
                            System.out.println("Server mock Error");
                        } finally {
                            try {
                                serverSocket.close();
                            } catch (Exception e) {
                            }

                        }
                    }
                };
                serverThread.start();
            } catch (IOException ex) {

            }
        }

        public void sendMessage(ChatMessage message) {
            try {
                out.write(message.getFormatMessage() + "\n");
                out.flush();
            } catch (Exception e) {

            }
        }

        public ChatMessage readMessage() {
            try {
                String message = in.readLine();
                return ChatMessage.fromFormatString(message);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void finalize() throws Throwable {
            server.close();
            in.close();
            out.close();
        }

    }


}
