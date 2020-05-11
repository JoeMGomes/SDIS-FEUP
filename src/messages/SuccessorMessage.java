package src.messages;

import src.chord.ChordInfo;

public class SuccessorMessage extends Message {

    private ChordInfo successorInfo;

    public SuccessorMessage(String ipAddress, int port, ChordInfo sender, ChordInfo successorInfo) {
        super(ipAddress, port, sender);
        this.successorInfo = successorInfo;
    }

    @Override
    public void handle() {
        // TODO Auto-generated method stub

    }
    
}