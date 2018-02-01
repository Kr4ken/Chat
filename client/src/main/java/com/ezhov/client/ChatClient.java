package com.ezhov.client;

import com.ezhov.commands.ChatCommand;
import com.ezhov.connector.ChatConnector;
import com.ezhov.connector.ConnectorSettings;
import com.ezhov.domain.ChatMessage;
import com.ezhov.exceptions.IncorrectCommandFormat;
import com.ezhov.exceptions.IncorrectMessageException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public abstract class ChatClient {
    protected ChatConnector connector;
    protected List<ChatMessage> messages;
    protected List<ChatCommand> commands;
    protected String currentMessage;
    protected Boolean isStarted;
    protected String name;
    protected Scanner scanner;

    protected Thread readerThread;
    protected Thread writerThread;

    protected ConnectorSettings connectorSettings;

    protected final String commandPatternString = "^\\/\\w+\\ *(\\w+\\ *)*";
    protected Pattern commandPattern;

    protected InputStream inputStream;
    protected PrintStream printStream;
    protected OutputStream outputStream;

    protected abstract void initCommandsList();

    public ChatClient() {
        initCommandsList();
        commandPattern = Pattern.compile(commandPatternString);
        messages = new LinkedList<>();
        isStarted = false;
        readerThread = new Thread() { public void run() { readMessages(); } };
        writerThread = new Thread() { public void run() { writeMessages(); } };
    }

    public void connect() {
        try {
            if(!connector.checkStatus()) {
                connector.connect();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
        }
    }

    public void disconnect() {
        try {
            connector.disconnect();
            inputStream.close();
            printStream.close();
            scanner.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during established server connection" + ex);
        }
    }

    private Boolean isCommand(String message) {
        return commandPattern.matcher(message).matches();
    }

    private String getCommandFromMessage(String message) {
        if (isCommand(message)) {
            return message.split(" ")[0];
        }
        return null;
    }

    private List<String> getParamsFromMessage(String message) {
        if (isCommand(message)) {
            String[] params = message.split(" ");
            params = Arrays.copyOfRange(params, 1, params.length);
            return Arrays.asList(params);
        }
        return null;
    }

    private void executeCommand(String commandString) {
        String command = getCommandFromMessage(commandString);
        List<String> params = getParamsFromMessage(commandString);
        Optional<ChatCommand> chatCommand = commands.stream().filter(e -> e.getCommand().equals(command)).findAny();
        if (chatCommand.isPresent()) {
            try {
                chatCommand.get().action(params);
            } catch (IncorrectMessageException | IncorrectCommandFormat ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
            }
        } else {
            printStream.println(String.format("Command %s not found", command));
        }
    }

    protected void reconnect(){
        printStream.println("Server connection error. Trying to reconnect? (y/n) ");
        String answer =  new Scanner(inputStream).nextLine();
        if(answer.equals("y")){
            stop();
            start();
        }
    }

    protected void readMessages() {
        while (isStarted) {
            try {
                ChatMessage mess = connector.readMessage();
                if (isCommand(mess.getMessage())) {
                    executeCommand(mess.getMessage());
                }
                messages.add(mess);
                printStream.println(mess.getFormatMessage());
            } catch (IncorrectMessageException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during read server message " + ex);
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Server connection error" + ex);
                isStarted = false;
                reconnect();
            }
        }
    }

    protected void writeMessages() {
        while (isStarted) {
            try {
                currentMessage = scanner.nextLine();
                // Add endline symbol in the end
                ChatMessage mess = new ChatMessage(currentMessage, name);
                connector.sendMessage(mess);
                currentMessage = null;
            } catch (IncorrectMessageException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Occured error during read server message " + ex);
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, "Server connection error" + ex);
                isStarted = false;
                reconnect();
            }
        }

    }

    public abstract void start();

    public abstract void stop();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        scanner = new Scanner(inputStream);
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }
}
