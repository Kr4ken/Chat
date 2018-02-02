package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.tests.ServerMock;
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



}
