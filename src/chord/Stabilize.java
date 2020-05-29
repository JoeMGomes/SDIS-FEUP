package src.chord;

import src.CLI.Peer;
import src.messages.chordMessages.FindPredecessor;
import src.messages.MessageSender;

public class Stabilize implements Runnable{

    @Override
    public void run(){
        Peer.log("Stabilizing...");
        ChordInfo self = Peer.chordNode.getNodeInfo();
        while (!Peer.chordNode.getSuccessorList().isEmpty()) {
            ChordInfo successor = Peer.chordNode.getSuccessor(0);
            if (successor == null) {
                break;
            }
            FindPredecessor message = new FindPredecessor(successor.getIp(), successor.getPort(), self, successor);
            MessageSender sender = new MessageSender(message);
            if (sender.send()) break;
            Peer.chordNode.removeSuccessor(0);
        }
        
    }
}