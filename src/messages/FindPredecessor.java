package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;

public class FindPredecessor extends Message {

    public ChordInfo nodeToFindPredecessor;

    public FindPredecessor(String ipAdress, int port, ChordInfo sender, ChordInfo nodeToFindPredecessor) {
        super(ipAdress, port, sender);
        this.nodeToFindPredecessor = nodeToFindPredecessor;
    }

    @Override
    public void handle() {
        ChordInfo predecessor = Peer.chordNode.getPredecessor();
        Peer.log("Peer " + Peer.chordNode.getNodeInfo().toString() + "is the predecessor."  );
        PredecessorMessage message = new PredecessorMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(), predecessor);
        MessageSender sender = new MessageSender(message);
        sender.send();
    }
    
}