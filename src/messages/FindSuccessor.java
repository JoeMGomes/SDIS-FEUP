package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.chord.ChordNode;

public class FindSuccessor extends Message {

    public ChordInfo nodeToFindSuccessor;
    public boolean toFixFinger;

    public FindSuccessor(String ipAdress, int port, ChordInfo sender, ChordInfo nodeToFindSuccessor, boolean toFixFinger) {
        super(ipAdress, port, sender);
        this.nodeToFindSuccessor = nodeToFindSuccessor;
        this.toFixFinger = toFixFinger;
    }

    @Override
    public void handle() {

        if (nodeToFindSuccessor.getHashKey() > Peer.chordNode.getNodeHash() ||
        nodeToFindSuccessor.getHashKey() <= Peer.chordNode.getFinger(0).getHashKey()) {
            Message message;

            if(toFixFinger){
                //Sends Sucessor Message to original asker
                message = new FixFingerMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(),Peer.chordNode.getFinger(0));
            }
            else{
                //Sends Sucessor Message to original asker
                message = new SuccessorMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(),Peer.chordNode.getFinger(0) );
            }

            MessageSender sender = new MessageSender(message);
            sender.send();
        } else {
            //Forward FindSucessor Message to best Peer
            ChordInfo n1 = Peer.chordNode.closestPrecedingNode(nodeToFindSuccessor.getHashKey());
            FindSuccessor message = new FindSuccessor(n1.getIp(), n1.getPort(), getSender(), nodeToFindSuccessor, false);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }

    }

}