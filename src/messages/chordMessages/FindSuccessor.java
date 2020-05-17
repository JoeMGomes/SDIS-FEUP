package src.messages.chordMessages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.*;
import src.*;

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

        try {

            if (Utils.isBetween(Peer.chordNode.getNodeHash(), Peer.chordNode.getFinger(0).getHashKey(),
                    nodeToFindSuccessor.getHashKey(), true)) {
                Message message;
    
                if(toFixFinger){
                    Peer.log("Sending FixFingerMessage to " + getSender().getIp() + ':' + getSender().getPort());
                    //Sends Sucessor Message to original asker
                    message = new FixFingerMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(),
                            Peer.chordNode.getFinger(0));
                } else{
                    Peer.log("Sending Sucerror Message to " + getSender().getIp() + ':' + getSender().getPort());
                    //Sends Sucessor Message to original asker
                    message = new SuccessorMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(),Peer.chordNode.getFinger(0));
                }
                MessageSender sender = new MessageSender(message);
                sender.send();
            } else {
                Peer.log("Sending FindSucessor");
                //Forward FindSucessor Message to best Peer
                ChordInfo n1 = Peer.chordNode.closestPrecedingNode(nodeToFindSuccessor.getHashKey());
                FindSuccessor message = new FindSuccessor(n1.getIp(), n1.getPort(), getSender(), nodeToFindSuccessor, this.toFixFinger);
                MessageSender sender = new MessageSender(message);
                sender.send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}