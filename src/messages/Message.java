package src.messages;

import java.io.Serializable;

import src.chord.ChordInfo;

public abstract class Message implements Serializable {

    private String ipAddress;
    private int port;
    private ChordInfo sender;

    public Message(String ipAddress, int port, ChordInfo sender) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.sender = sender;
    }
    
    public abstract void handle();
    
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChordInfo getSender() {
        return this.sender;
    }

    public void setSender(ChordInfo sender) {
        this.sender = sender;
    }

    
}