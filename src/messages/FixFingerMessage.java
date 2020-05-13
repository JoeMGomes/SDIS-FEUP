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
        try {
            Peer.log("SETTING FINGER " + successorInfo.toString());
            Peer.chordNode.setFinger(successorInfo, Peer.fixFingers.getNextFinger());
            Peer.log("Finger was set to: " + successorInfo.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}