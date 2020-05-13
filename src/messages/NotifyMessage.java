package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;

public class NotifyMessage extends Message {

    public ChordInfo predecessor;

    public NotifyMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
        this.predecessor = sender;
    }

    @Override
    public void handle() {
        Peer.log("Notified");
        if (Peer.chordNode.getPredecessor() == null || 
                predecessor.getHashKey() > Peer.chordNode.getPredecessor().getHashKey() ||
                predecessor.getHashKey() < Peer.chordNode.getNodeHash()) {
            Peer.chordNode.setPredecessor(predecessor);
            Peer.log("New predecessor");
        }
    }
    
}