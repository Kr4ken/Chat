package com.ezhov.domain;

import com.ezhov.exceptions.IncorrectMessageException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessage {
    private final String messageFormat = "[%s|%s] %s";
    private final String dateFormat = "HH:mm:ss";
    private String message;
    private String client;
    private LocalTime time;
    private DateTimeFormatter dateTimeFormatter;
    private Pattern messagePattern;
    private Pattern commandPattern;
    private final String commandPatternString = "^\\/\\w+\\ *(\\w+\\ *)*";
    private final String messagePatternString = "\\[\\d{2}:\\d{2}:\\d{2}\\|\\w+\\] .+";

    private ChatMessage() {
        init();
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
        // Set precision for seconds
        this(message, client, LocalTime.now().withNano(0));
    }

    public static ChatMessage fromFormatString(String formatMessage) throws IncorrectMessageException {
        ChatMessage result = new ChatMessage();
        result.parseMessage(formatMessage);
        return result;
    }

    private void init() {
        messagePattern = Pattern.compile(messagePatternString);
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        commandPattern = Pattern.compile(commandPatternString);
    }

    public void parseMessage(String formatMessage) throws IncorrectMessageException {
        if (formatMessage == null || formatMessage.equals(""))
            throw new IncorrectMessageException("Error. Null message for format: " + formatMessage);
        Matcher matcher = messagePattern.matcher(formatMessage);

        if (matcher.matches()) {
            Integer dateStart = formatMessage.indexOf("[") + 1;
            Integer dateEnd = formatMessage.indexOf("|");
            Integer nameStart = formatMessage.indexOf("|") + 1;
            Integer nameEnd = formatMessage.indexOf("]");
            this.message = formatMessage.substring(nameEnd + 2);
            this.client = formatMessage.substring(nameStart, nameEnd);
            String timeString = formatMessage.substring(dateStart, dateEnd);
            try {
                this.time = LocalTime.parse(timeString, dateTimeFormatter);
            } catch (DateTimeParseException dpe) {
                throw new IncorrectMessageException(formatMessage + " : " + dpe.getLocalizedMessage());
            }

        } else {
            throw new IncorrectMessageException("Error. Incorrect message format: " + formatMessage);
        }


    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ChatMessage)) return false;
        ChatMessage otherChatMessage = (ChatMessage) other;
        return client.equals(otherChatMessage.client) && message.equals(otherChatMessage.message) && time.equals(otherChatMessage.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, message, time);
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

    public Boolean isCommand() {
        return commandPattern.matcher(message).matches();
    }

    public String getCommandFromMessage() {
        if (this.isCommand()) {
            return message.split(" ")[0];
        }
        return null;
    }

    public List<String> getParamsFromMessage() {
        if (this.isCommand()) {
            String[] params = message.split(" ");
            params = Arrays.copyOfRange(params, 1, params.length);
            return Arrays.asList(params);
        }
        return null;
    }


}

