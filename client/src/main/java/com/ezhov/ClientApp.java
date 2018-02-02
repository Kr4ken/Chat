package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientTerminal;
import com.ezhov.client.ClientSettings;

import java.io.IOException;

public class ClientApp {
    public static void main(String args[]) throws IOException {
        ChatClient client = new ChatClientTerminal(ClientSettings.getDefault());
        client.start();
    }
}