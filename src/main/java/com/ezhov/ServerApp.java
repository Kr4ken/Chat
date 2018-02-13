package com.ezhov;

import com.ezhov.connector.ListenerSettings;
import com.ezhov.server.ChatServer;
import com.ezhov.server.ChatServerSettings;

public class ServerApp {
    // Args <portNumber>
    public static void main(String args[]) {
        ChatServerSettings settings = ChatServerSettings.getDefault();
        ListenerSettings listenerSettings = settings.getListenerSettings();
        if(args.length > 0){
            listenerSettings.setPortNumber(Integer.parseInt(args[0]));
        }
        ChatServer server = new ChatServer(settings);
        server.run();
    }
}