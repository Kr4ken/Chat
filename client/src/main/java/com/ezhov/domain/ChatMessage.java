package com.ezhov.domain;

import java.util.Date;

public class ChatMessage {
    private final String message;
    private final String client;
    private final Date date;

    public ChatMessage(String message, String client, Date date) {
        this.message = message;
        this.client = client;
        this.date = date;
    }

    public ChatMessage(String message, String client) {
        this(message, client, new Date());
    }

    public ChatMessage(String formatMesage) {
        Integer nameStart = formatMesage.indexOf("[");
        Integer nameEnd = formatMesage.indexOf("]");
        this.message = formatMesage.substring(nameEnd);
        this.client = formatMesage.substring(nameStart,nameEnd);
        this.date = new Date();
    }
    public String getFormatMessage(){
        return "[" + client + "]:" + message;
    }

    public String getMessage() {
        return message;
    }

    public String getClient() {
        return client;
    }

    public Date getDate() {
        return date;
    }
}
