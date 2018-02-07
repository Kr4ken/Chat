package com.ezhov.connector;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.Assert.*;

public class TestSocketChatListener {

    private static ConnectorSettings settings;
    private static Socket client;
    private SocketChatListener listener;

    @BeforeClass
    public static void execOnce() {
        settings = ConnectorSettings.getDefault();
    }

    @Before
    public void beforeEachTest() {
        listener = new SocketChatListener(settings);
    }

    @Test
    public void testStartStopChatListener() throws IOException {
        assertFalse(listener.checkStatus());
        listener.start();
        assertTrue(listener.checkStatus());
        listener.stop();
        assertFalse(listener.checkStatus());
    }

    private void startClient() {
        try {
            client = new Socket("127.0.0.1", settings.getPortNumber());
        } catch (IOException ex) {
            fail("Start Should not raise exception");
        }
    }

    private void stopClient() {
        try {
            client.close();
        } catch (IOException ex) {
            fail("Stop Should not raise exception");
        }
    }

    @Test
    public void testConnectChatListener() throws IOException {
        listener.start();
        startClient();
        ChatConnector connector = listener.getClient();
        assertNotNull(connector);
        listener.stop();
        stopClient();
        assertFalse(listener.checkStatus());
    }

    @Test
    public void testChatConnector() throws IOException {
        listener.start();
        startClient();
        ChatConnector connector = listener.getClient();
        assertFalse(connector.checkStatus());
        connector.connect();
        assertTrue(connector.checkStatus());
        connector.disconnect();
        assertFalse(connector.checkStatus());
        listener.stop();
        stopClient();
    }
    @Test
    public void testChatMessagingConnectors() throws IOException,IncorrectMessageException {
        listener.start();
        startClient();
        ChatConnector connector = listener.getClient();
        connector.connect();
        ChatMessage message = new ChatMessage("Message","Client");
        client.getOutputStream().write((message.getFormatMessage() + "\n").getBytes());
        client.getOutputStream().flush();
        ChatMessage recieved = connector.readMessage();
        assertEquals(message,recieved);
        connector.sendMessage(message);
        recieved =ChatMessage.fromFormatString(new BufferedReader(new InputStreamReader(client.getInputStream())).readLine());
        assertEquals(message,recieved);
        listener.stop();
        stopClient();
    }
}
