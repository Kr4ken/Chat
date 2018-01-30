package com.ezhov.domain;

import com.ezhov.exceptions.IncorrectMessageException;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessage {
    private final String messageFormat = "[%s|%s] %s";
    private final String dateFormat = "HH:mm";
    private String message;
    private String client;
    private LocalTime time;
    private DateTimeFormatter dateTimeFormatter;
    private Pattern messagePattern;

    private void init(){
        messagePattern = Pattern.compile("\\[\\d{2}:\\d{2}\\|\\w+\\] .+");
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    private ChatMessage(){
        init();
    }


    public static ChatMessage fromFormatString(String formatMessage) throws IncorrectMessageException{
        ChatMessage result = new ChatMessage();
        result.parseMessage(formatMessage);
        return result;
    }


    public ChatMessage(String message, String client, LocalTime time) throws IncorrectMessageException {
        if (client == null || client.equals("") || time == null || message == null || message.equals(""))
            throw new IncorrectMessageException("Incorrect message params");
        this.message = message;
        this.client = client;
        this.time = time;
        init();
    }

    public ChatMessage(String message, String client) throws IncorrectMessageException {
        this(message, client, LocalTime.now());
    }

    public void parseMessage(String formatMessage) throws IncorrectMessageException {
        if(formatMessage == null || formatMessage.equals(""))
            throw new IncorrectMessageException("Error. Null message for format: " + formatMessage);
        Matcher matcher = messagePattern.matcher(formatMessage);

        if(matcher.matches()){
            Integer dateStart = formatMessage.indexOf("[") + 1;
            Integer dateEnd = formatMessage.indexOf("|");
            Integer nameStart = formatMessage.indexOf("|") + 1;
            Integer nameEnd = formatMessage.indexOf("]");
            this.message = formatMessage.substring(nameEnd+2);
            this.client = formatMessage.substring(nameStart, nameEnd);
            String timeString = formatMessage.substring(dateStart, dateEnd);
            try {
                this.time = LocalTime.parse(timeString, dateTimeFormatter);
            } catch (DateTimeParseException dpe) {
                throw new IncorrectMessageException(formatMessage + " : " + dpe.getLocalizedMessage());
            }

        }
        else {
            throw new IncorrectMessageException("Error. Incorrect message format: " + formatMessage);
        }


    }

    public String getFormatMessage() throws IncorrectMessageException {
        return String.format(messageFormat, time.format(dateTimeFormatter), client, message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}

