package com.ezhov;

import com.ezhov.client.ChatClient;
import com.ezhov.client.ChatClientTerminal;
import com.ezhov.client.ClientSettings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClientLoadApp {
    private static Integer clientCount = 1000 ;
    private static Integer delay = 20; // In seconds
    private static Integer spamCount = 100;
    private static String outputFilesPath = "C:\\Chat\\loadTest\\output_files\\";

    private static InputStream getNewSpamerInputStream(String clientName){
        try {
            PipedOutputStream pos = new PipedOutputStream();
            InputStream is = new PipedInputStream(pos);

            new Thread(() -> {
                int counter = 0;
                byte[] bytes = String.format("%d Message from %s\n",counter, clientName).getBytes();
                while (counter < spamCount){
                    try {
                        pos.write(bytes);
                        pos.flush();
                        counter++;
                        bytes = String.format("%d Message from %s\n",counter, clientName).getBytes();
                        Thread.sleep(delay * 1000);
                    }catch (IOException | InterruptedException ex){
//                    }catch (IOException  ex){

                    }
                }
                try {
                    pos.write("/close".getBytes());
                    pos.flush();
                }catch (IOException ex){
                }

//                try (ObjectOutputStream oos = new ObjectOutputStream(pos)) {
//                    Iterator<String> myItr = Stream.concat(Stream.generate(() -> String.format("Message from %s\n", clientName)).limit(spamCount),Stream.of("/close")).iterator();
//                    while (myItr.hasNext()) {
//                        oos.writeObject(myItr.next());
//                        Thread.sleep(delay * 1000);
//                    }
//                } catch (IOException | InterruptedException e) {
////                } catch (IOException  e) {
//                }
//                finally {
//                    try {
//                        is.close();
//                        pos.close();
//                    }catch (IOException e){
//
//                    }
//                }
            }).start();
            return is;
        }catch (IOException ex){

        }
        return null;
    }

    private static InputStream getSpamerInputStream(String clientName){
        try {
            PipedOutputStream pos = new PipedOutputStream();
            InputStream is = new PipedInputStream(pos);

            new Thread(() -> {
                try (ObjectOutputStream oos = new ObjectOutputStream(pos)) {
                    Iterator<String> myItr = Stream.generate(() -> new String(String.format("Message from %s\n", clientName).getBytes(UTF_8),UTF_8)).limit(spamCount).iterator();
                    while (myItr.hasNext()) {
                        oos.writeObject(myItr.next().toString()
                                .getBytes(UTF_8));
                        Thread.sleep(delay * 1000);
                    }
                } catch (IOException | InterruptedException e) {
                }
                finally {
                    try {
                        is.close();
                        pos.close();
                    }catch (IOException e){

                    }
                }
            }).start();
            return is;
        }catch (IOException ex){

        }
        return null;
    }

    private static PrintStream getFilwWriterStream(String clientName){
        String path = outputFilesPath + clientName + ".txt";
        File clientFile = new File(path);
        PrintStream fileWriter = null;
        try {
          clientFile.createNewFile();
          fileWriter = new PrintStream(new FileOutputStream(clientFile, false));
        } catch ( IOException ex){

        }
        return fileWriter;

    }

    public static void main(String args[]) throws IOException {
//        List<ChatClient> clients = new LinkedList<>();
        for (int i =0;i< clientCount;i++){
        try{
                String newClientName = String.format("Client_%d",i);
                System.out.println(newClientName);
                ChatClient newClient = new ChatClientTerminal(ClientSettings.getDefault());
                newClient.setName(newClientName);
                newClient.setInputStream(getNewSpamerInputStream(newClientName));
                newClient.setPrintStream(getFilwWriterStream(newClientName));
                newClient.start();
//                clients.add(newClient);
                Thread.sleep(10);
        }
        catch (Exception ex){
        }
        }
//        finally {
//            for(ChatClient client:clients){
//                client.stop();
//            }
//        }





//
//        ChatClient client = new ChatClientTerminal(ClientSettings.getDefault());
//        if(args.length > 0){
//            client.setName(args[0]);
//        }
//        client.start();
    }
}
