package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;

public class SuccessorMessage extends Message {

    private ChordInfo successorInfo;

    public SuccessorMessage(String ipAddressDest, int portDest, ChordInfo sender, ChordInfo successorInfo) {
        super(ipAddressDest, portDest, sender);
        this.successorInfo = successorInfo;
    }

    @Override
    public void handle() {
        System.out.println("Sucessor was set to: " + successorInfo.toString());
        Peer.chordNode.setFinger(successorInfo, 0);
    }
    
}