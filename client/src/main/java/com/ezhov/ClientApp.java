package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientImpl;

public class ClientApp {
    public static void main(String args[]){
      System.out.println("Hello world");
        ChatClient client = new ChatClientImpl("Kirill");
        client.start();
    }
}