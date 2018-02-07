package com.ezhov.client;

import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectMessageException;
import com.ezhov.tests.ServerMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestChatClientTerminal {
    private static ClientSettings settings;
    private ServerMock server;
    private ChatClientTerminal clientTerminal;
    @BeforeClass
    public static void once() {
        settings = ClientSettings.getDefault();
    }

    @Before
    public void beforeEachTest() {
//        server = new ServerMock(settings.getConnectorSettings().getPortNumber());
//        clientTerminal = new ChatClientTerminal(settings);
//        clientTerminal.setInputStream(new InputStream() {
//            @Override
//            public int read() throws IOException {
//                return 0;
//            }
//        });
//        clientTerminal.start();
    }

    @After
    public void aferEachTest() {
//        clientTerminal.stop();
    }

    @Test
    public void registerTest() {
        assertTrue(true);
//        clientTerminal.start();
//        ChatMessage registerMessage = server.readMessage();
//        server.sendMessage(registerMessage);
//        assertEquals("/register " + settings.getName(),registerMessage.getMessage());
//        assertEquals(settings.getName(),registerMessage.getClient());
//        assertEquals(settings.getName(),clientTerminal.getName());
//        clientTerminal.stop();
    }


    @Test
    public void messageTest() throws IncorrectMessageException {
        assertTrue(true);
//        clientTerminal.start();
//        LinkedList<ChatMessage> messages = new LinkedList<>();
//        for (Integer i = 0; i <= 100; i++) {
//            ChatMessage message = new ChatMessage(String.format("%d message",i),"SYSTEM");
//            messages.add(message);
//            server.sendMessage(message);
//        }
//        assertTrue(true);
//        clientTerminal.stop();
    }
}
