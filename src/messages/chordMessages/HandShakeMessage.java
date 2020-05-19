package src.messages.chordMessages;

import src.chord.ChordInfo;
import src.messages.*;

public class HandShakeMessage extends Message {

    public HandShakeMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
    }

    @Override
    public void handle() {

    }
    
}