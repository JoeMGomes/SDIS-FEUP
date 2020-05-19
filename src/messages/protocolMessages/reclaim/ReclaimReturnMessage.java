package src.messages.protocolMessages.reclaim;

import src.chord.ChordInfo;
import src.messages.Message;

public class ReclaimReturnMessage extends Message {

    public ReclaimReturnMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
    }

    @Override
    public void handle() {
        System.out.println("Reclaim terminated successfully. Exiting...");
        System.exit(1);
    }
    
}