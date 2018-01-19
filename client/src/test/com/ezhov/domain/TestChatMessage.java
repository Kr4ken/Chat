package com.ezhov.domain;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;

public class TestChatMessage extends TestCase {

    @Test
    public void testCreation() {
        ChatMessage message = new ChatMessage("Hi", "Kirill", new Date("19.01.1994"));
        assertEquals("Hi", message.getMessage());
        assertEquals("Kirill", message.getClient());
        assertEquals(new Date("19.01.1994"), message.getDate());
    }

    @Test
    public void testFormatMessage() {
        ChatMessage message = new ChatMessage("Hi", "Kirill", new Date("19.01.1994"));
    }
}
