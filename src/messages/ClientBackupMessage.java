package src.messages;

import src.chord.ChordInfo;

public class ClientBackupMessage extends Message {

    public ClientBackupMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
        
    }

    @Override
    public void handle() {
        // TODO Auto-generated method stub

    }
    
}