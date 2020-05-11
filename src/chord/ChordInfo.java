package src.chord;

import java.io.Serializable;

public class ChordInfo implements Serializable {
    
    private int hashKey;
    private String ip;
    private int port;

    public ChordInfo(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.hashKey = ChordNode.hashString(this.ip + this.port);
    }

    //Getters and Setters bellow this line
    public int getHashKey() {
        return this.hashKey;
    }

    public void setHashKey(int hashKey) {
        this.hashKey = hashKey;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String toString() {
        return "{" +
            " hashKey='" + hashKey + "'" +
            ", ip='" + ip + "'" +
            ", port='" + port + "'" +
            "}";
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}