package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.chord.ChordNode;

public class NotifyMessage extends Message {

    public ChordInfo predecessor;

    public NotifyMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
        this.predecessor = sender;
    }

    @Override
    public void handle() {
        try {

            Peer.log("Notified");
            if (Peer.chordNode.getPredecessor() == null || ChordNode.isBetween(Peer.chordNode.getPredecessor().getHashKey(),
                    Peer.chordNode.getNodeHash(), this.predecessor.getHashKey(), false)) {
                Peer.chordNode.setPredecessor(predecessor);
                Peer.log("New predecessor");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}