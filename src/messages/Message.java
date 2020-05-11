package src.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private String ipAdress;
    private int port;

    public Message(String ipAdress, int port) {
        this.ipAdress = ipAdress;
        this.port = port;
    }

    public String getIpAdress() {
        return this.ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public abstract void handle();
    
}