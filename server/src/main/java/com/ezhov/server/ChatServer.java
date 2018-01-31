package com.ezhov.server;

import com.ezhov.domain.ChatMessage;

public interface ChatServer {
    void run();
    void stop();
    void addMessage(ChatMessage chatMessage);
    String getSystemUserName();
}
