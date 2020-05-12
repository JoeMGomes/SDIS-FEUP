package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;

public class FixFingerMessage extends Message {

    private ChordInfo successorInfo;

    public FixFingerMessage(String ipAddressDest, int portDest, ChordInfo sender, ChordInfo successorInfo) {
        super(ipAddressDest, portDest, sender);
        this.successorInfo = successorInfo;
    }

    @Override
    public void handle() {

        
        Peer.chordNode.setFinger(successorInfo, Peer.chordNode.getNextFinger());
        System.out.println("Finger was set to: " + successorInfo.toString());
    }
    
}