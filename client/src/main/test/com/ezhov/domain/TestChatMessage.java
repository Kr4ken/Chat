package com.ezhov.domain;

import com.ezhov.exceptions.IncorrectMessageException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class TestChatMessage {

    static String string;
    static DateTimeFormatter format;
    static LocalTime time;

    @BeforeClass
    public static void onlyPreparing() {
        try {
            string = "18:35:12";
            format = DateTimeFormatter.ofPattern("HH:mm:ss");
            time = LocalTime.parse(string, format);
        } catch (Exception e) {

        }
    }

    @Test
    public void testSimpleCreation() throws Exception {
        ChatMessage message = new ChatMessage("Hi", "Kirill", time);
        assertEquals("Hi", message.getMessage());
        assertEquals("Kirill", message.getClient());
        assertEquals(time, message.getTime());
    }

    @Test
    public void testFormatMessage() throws Exception {
        ChatMessage message = new ChatMessage("Hi", "Kirill", time);
        assertEquals("[18:35:12|Kirill] Hi", message.getFormatMessage());
    }

    private boolean isMessageCorrect(String message, String client, LocalTime time) {
        try {
            new ChatMessage(message, client, time);
            return true;
        } catch (IncorrectMessageException ex) {
            return false;
        }
    }

    private boolean isMessageCorrect(String message, String client) {
        try {
            new ChatMessage(message, client);
            return true;
        } catch (IncorrectMessageException ex) {
            return false;
        }
    }

    @Test
    public void testNullEmpyFields() throws Exception {
        // Message
        assertFalse(isMessageCorrect(null,"Kirill",time));
        assertFalse(isMessageCorrect("","Kirill",time));
        // Client
        assertFalse(isMessageCorrect("Hi", null, time));
        assertFalse(isMessageCorrect("Hi", "", time));
        // Date
        assertFalse(isMessageCorrect("Hi", "Kirill", null));
        assertTrue(isMessageCorrect("Hi", "Kirill"));
    }

    private boolean isParseMessageCorrect(String message) {
        try {
            ChatMessage.fromFormatString(message);
            return true;
        } catch (IncorrectMessageException ex) {
            return false;
        }
    }

    @Test
    public void testMessageParse() {
        String message = "[12:35:12|Kraken] Hi there";
        assertTrue(isParseMessageCorrect(message));

        message = "[|Kraken] Hi there";
        assertFalse(isParseMessageCorrect(message));

        message = "[12:35:11|] Hi there";
        assertFalse(isParseMessageCorrect(message));

        message = "[] Hi there";
        assertFalse(isParseMessageCorrect(message));

        message = "";
        assertFalse(isParseMessageCorrect(message));

        message = null;
        assertFalse(isParseMessageCorrect(message));

        message = "[12:89:11|Kraken] Hi there";
        assertFalse(isParseMessageCorrect(message));

        message = "[12:55:11|Kraken] g";
        assertTrue(isParseMessageCorrect(message));

        message = "aldkfjlaksdj 3[12:89|Kraken]";
        assertFalse(isParseMessageCorrect(message));

        message = "[12:89:33Kraken] asd[fap[|[]";
        assertFalse(isParseMessageCorrect(message));
    }

    @Test
    public void correctParse() throws Exception{
        String message = "[12:35:32|Kraken] Hi there";
        ChatMessage mess = ChatMessage.fromFormatString(message);
        assertEquals(LocalTime.parse("12:35:32",format),mess.getTime());
        assertEquals("Hi there",mess.getMessage());
        assertEquals("Kraken",mess.getClient());

        message = "[14:22:32|SYSTEM] Enter command /help";
        mess = ChatMessage.fromFormatString(message);
        assertEquals(LocalTime.parse("14:22:32",format),mess.getTime());
        assertEquals("Enter command /help",mess.getMessage());
        assertEquals("SYSTEM",mess.getClient());


        message = "[14:22:12|fskhfkj] fff";
        mess = ChatMessage.fromFormatString(message);
        assertEquals(LocalTime.parse("14:22:12",format),mess.getTime());
        assertEquals("fff",mess.getMessage());
        assertEquals("fskhfkj",mess.getClient());

        message = "[14:22:12|SYSTEM] [][][][|p[p{}";
        mess = ChatMessage.fromFormatString(message);
        assertEquals(LocalTime.parse("14:22:12",format),mess.getTime());
        assertEquals("[][][][|p[p{}",mess.getMessage());
        assertEquals("SYSTEM",mess.getClient());

    }


}
