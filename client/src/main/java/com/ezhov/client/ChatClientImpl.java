package com.ezhov.client;

import com.ezhov.commands.ChatCommand;
import com.ezhov.commands.RegisterChatCommand;
import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.connector.SocketChatConnector;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ChatClientImpl implements ChatClient {
    private ChatConnector connector;
    private List<ChatMessage> messages;
    private List<ChatCommand> commands;
    private String currentMessage;
    private Boolean isStarted;
    private String name;
    private Scanner scanner;

    private Thread readerThread;
    private Thread writerThread;

    private ConnectorSettings connectorSettings;

    private final String commandPatternString  = "^\\/\\w+\\ *(\\w+\\ *)*";
    private Pattern commandPattern;

    public ChatClientImpl(String name) {
        this();
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private void initCommandsList(){
       commands = new LinkedList<>();
       commands.add(new RegisterChatCommand(this));
    }

    private Boolean isCommand(String message){
        return commandPattern.matcher(message).matches();
    }

    private String getCommandFromMessage(String message){
        if(isCommand(message)){
            //
            return message.split(" ")[0];
        }
        return null;
    }

    private List<String> getParamsFromMessage(String message){
        if(isCommand(message)){
            String[] params =  message.split(" ");
            params = Arrays.copyOfRange(params,1,params.length);
            return Arrays.asList(params);
        }
        return null;
    }

    private void executeCommand(String commandString){
       String command = getCommandFromMessage(commandString);
       List<String> params = getParamsFromMessage(commandString);
        Optional<ChatCommand> chatCommand =  commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if(chatCommand.isPresent()){
            System.out.println("Command found");
            try {
                chatCommand.get().action(params);
            }
            catch (IncorrectMessageException | IncorrectCommandFormat ex){

            }
        }
        else {
            String.format("Command %s not found",command);
        }
    }

    public ChatClientImpl() {
        commandPattern = Pattern.compile(commandPatternString);
        connectorSettings = new ConnectorSettings(8989, "127.0.0.1");
        connector = new SocketChatConnector(connectorSettings);
        messages = new LinkedList<>();
        currentMessage = "";
        isStarted = false;
        scanner = new Scanner(System.in);
        initCommandsList();
        readerThread = new Thread() {
            public void run() {
                readMessages();
            }
        };
        writerThread = new Thread() {
            public void run() {
                writeMessages();
            }
        };
    }

    private void readMessages() {

        while (isStarted) {
            try {
                ChatMessage mess = connector.readMessage();
                if(isCommand(mess.getMessage())){
                    System.out.println("Get command from server");
                   executeCommand(mess.getMessage());
                }
                messages.add(mess);
                System.out.println(mess.getFormatMessage());
            } catch (IOException | IncorrectMessageException ex) {
                Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
            }
        }
    }

    private void writeMessages() {
        while (isStarted) {
            try {
                currentMessage = scanner.nextLine();
                currentMessage += "\n";
                ChatMessage mess = new ChatMessage(currentMessage, name);
                connector.sendMessage(mess);
                currentMessage = "";
            } catch (IOException | IncorrectMessageException ex) {
                Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, "Occured error during read server message" + ex);
            }
        }

    }

    public void start() {
        try {
            connector.connect();
            isStarted = true;
            readerThread.start();
            writerThread.start();
        } catch (IOException ex) {
            Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
        }

    }

    public void end() {
        isStarted = false;
        try {
            connector.disconnect();
        }catch (IOException ex){
            Logger.getLogger(ChatClientImpl.class.getName()).log(Level.SEVERE, "Occured error on disconnecting server " + ex);
        }

    }


}
