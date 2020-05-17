package src.chord;

import src.CLI.Peer;
import src.messages.chordMessages.FindPredecessor;
import src.messages.MessageSender;

public class Stabilize implements Runnable{

    @Override
    public void run(){
        Peer.log("Stabilizing...");
        ChordInfo self = Peer.chordNode.getNodeInfo();
        int i = 0;
        while (i < ChordNode.successorListSize) {
            ChordInfo successor = Peer.chordNode.getSuccessor(i);
            FindPredecessor message = new FindPredecessor(successor.getIp(), successor.getPort(), self, successor);
            MessageSender sender = new MessageSender(message);
            if (sender.send()) break;
            i++;
        }
        
        //if (successor.getHashKey() == Peer.chordNode.getNodeHash()) return;
        
    }
}