package com.ezhov.settings;

public class ChatServerSettings {
    private ListenerSettings listenerSettings;
    private String systemName;
    private Integer lastMessageCount;
    private Integer maxMessages;

    private ChatServerSettings() {
    }

    ;

    public ChatServerSettings(ListenerSettings listenerSettings, String systemName, Integer lastMessageCount) {
        this.listenerSettings = listenerSettings;
        this.systemName = systemName;
        this.lastMessageCount = lastMessageCount;
    }

    public static ChatServerSettings getDefault() {
        ChatServerSettings result = new ChatServerSettings();
        result.listenerSettings = ListenerSettings.getDefault();
        result.systemName = "SYSTEM";
        result.lastMessageCount =100;
        result.maxMessages = 200;
        return result;
    }

    public ListenerSettings getListenerSettings() {
        return listenerSettings;
    }

    public void setListenerSettings(ListenerSettings listenerSettings) {
        this.listenerSettings = listenerSettings;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getLastMessageCount() {
        return lastMessageCount;
    }

    public void setLastMessageCount(Integer lastMessageCount) {
        this.lastMessageCount = lastMessageCount;
    }

    public Integer getMaxMessages() {
        return maxMessages;
    }

    public void setMaxMessages(Integer maxMessages) {
        this.maxMessages = maxMessages;
    }
}
