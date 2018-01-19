package com.ezhov;

import com.ezhov.server.ChatClient;
import com.ezhov.server.ChatClientImpl;

public class ClientApp {
    public static void main(String args[]){
      System.out.println("Hello world");
        ChatClient client = new ChatClientImpl();
        client.start();
    }
}