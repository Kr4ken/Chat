package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientImpl;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class ClientApp {
    public static void main(String args[]) throws IOException{
      System.out.println("Hello world");
        ChatClient client = new ChatClientImpl("Kirill");
        client.start();
    }
}