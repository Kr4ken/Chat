package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientTerminal;
import com.ezhov.client.ClientSettings;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientLoadApp {
    private static Integer clientCount = 1000;
    private static Integer delay = 20; // In seconds
    private static Integer spamCount = 100;
    private static String outputFilesPath = "/home/kraken/projects/Chat/loadTest/";

    private static InputStream getNewSpamerInputStream(String clientName) {
        try {
            PipedOutputStream pos = new PipedOutputStream();
            InputStream is = new PipedInputStream(pos);

            new Thread(() -> {
                int counter = 0;
                byte[] bytes = String.format("%d Message from %s\n", counter, clientName).getBytes();
                while (counter < spamCount) {
                    try {
                        pos.write(bytes);
                        pos.flush();
                        counter++;
                        bytes = String.format("%d Message from %s\n", counter, clientName).getBytes();
                        Thread.sleep(delay * 1000);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(ClientLoadApp.class.getName()).log(Level.WARNING, String.format("Spam thread error " + clientName + "\n" + ex));
                    }
                }
                try {
                    pos.write("/close".getBytes());
                    pos.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ClientLoadApp.class.getName()).log(Level.WARNING, String.format("Close error " + clientName + "\n" + ex));
                }
            }).start();
            return is;
        } catch (IOException ex) {

        }
        return null;
    }

    private static PrintStream getFilwWriterStream(String clientName) {
        String path = outputFilesPath + clientName + ".txt";
        File clientFile = new File(path);
        PrintStream fileWriter = null;
        try {
            clientFile.createNewFile();
            fileWriter = new PrintStream(new FileOutputStream(clientFile, false));
        } catch (IOException ex) {
            Logger.getLogger(ClientLoadApp.class.getName()).log(Level.WARNING, String.format("File writer error " + clientName + "\n" + ex));
        }
        return fileWriter;

    }

    public static void main(String args[]) throws IOException {
        // Args <outputPath> <clientCount> <delay> <spamCount>
        if (args.length > 0) {
            outputFilesPath = args[0];
        }
        if (args.length > 1) {
            clientCount = Integer.getInteger(args[1]);
        }
        if (args.length > 2) {
            delay = Integer.getInteger(args[2]);
        }
        if (args.length > 3) {
            spamCount = Integer.getInteger(args[3]);
        }
        for (int i = 0; i < clientCount; i++) {
            try {
                String newClientName = String.format("Client_%d", i);
                System.out.println(newClientName);
                ChatClient newClient = new ChatClientTerminal(ClientSettings.getDefault());
                newClient.setName(newClientName);
                newClient.setInputStream(getNewSpamerInputStream(newClientName));
                newClient.setPrintStream(getFilwWriterStream(newClientName));
                newClient.start();
                Thread.sleep(11);
            } catch (Exception ex) {
                Logger.getLogger(ClientLoadApp.class.getName()).log(Level.WARNING, String.format("new client creating error\n" + ex));
            }
        }
    }
}
