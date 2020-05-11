package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.chord.ChordNode;

public class FindSuccessor extends Message {

    ChordInfo nodeToFindSuccessor;

    public FindSuccessor(String ipAdress, int port, ChordInfo sender, ChordInfo nodeToFindSuccessor) {
        super(ipAdress, port, sender);
        this.nodeToFindSuccessor = nodeToFindSuccessor;
    }

    @Override
    public void handle() {

        if (nodeToFindSuccessor.getHashKey() > Peer.chordNode.getNodeHash() ||
        nodeToFindSuccessor.getHashKey() <= Peer.chordNode.getFinger(0).getHashKey()) {
            //TODO: create successor message
            TestMessage test = new TestMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo());
            MessageSender sender = new MessageSender(test);
            sender.send();
        } else {
            ChordInfo n1 = this.closestPrecedingNode(nodeToFindSuccessor);
            FindSuccessor test = new FindSuccessor(n1.getIp(), n1.getPort(), getSender(), nodeToFindSuccessor);
            MessageSender sender = new MessageSender(test);
            sender.send();
        }

    }

    private ChordInfo closestPrecedingNode(ChordInfo node) {

        for (int i = ChordNode.mBits; i > 0; i--) {
        
            if (Peer.chordNode.getFinger(i).getHashKey() > Peer.chordNode.getNodeHash() || 
            Peer.chordNode.getFinger(i).getHashKey() < node.getHashKey())
                return Peer.chordNode.getFinger(i);
        }

        return Peer.chordNode.getNodeInfo();
    }

}