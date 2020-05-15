package src.chord;

import src.CLI.Peer;
import src.messages.FindPredecessor;
import src.messages.MessageSender;

public class Stabilize implements Runnable{

    @Override
    public void run(){
        Peer.log("Stabilizing...");
        ChordInfo self = Peer.chordNode.getNodeInfo();
        ChordInfo successor = Peer.chordNode.getFinger(0);
        //if (successor.getHashKey() == Peer.chordNode.getNodeHash()) return;
        FindPredecessor message = new FindPredecessor(successor.getIp(), successor.getPort(), self, successor);
        MessageSender sender = new MessageSender(message);
        sender.send();
    }
}