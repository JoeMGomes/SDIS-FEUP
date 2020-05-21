package src.messages.chordMessages;

import src.messages.*;
import src.CLI.Peer;
import src.chord.ChordInfo;

import java.util.ArrayList;
import java.util.List;

import src.*;

public class NotifyMessage extends Message {

    public ChordInfo predecessor;

    public NotifyMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
        this.predecessor = sender;
    }

    @Override
    public void handle() {
        try {
            if (Peer.chordNode.getPredecessor() == null || Utils.isBetween(Peer.chordNode.getPredecessor().getHashKey(),
                    Peer.chordNode.getNodeHash(), this.predecessor.getHashKey(), false)) {
                Peer.chordNode.setPredecessor(predecessor);
                            
                List<Integer> toForward = Peer.fileManager.getFileKeys(Peer.chordNode.getPredecessor().getHashKey());

                for (Integer c : Peer.forwarded) {
                    if(c <= Peer.chordNode.getPredecessor().getHashKey() || c > Peer.chordNode.getNodeHash()){
                        toForward.add(c);
                    }
                }

                NewPredecessorMessage message = new NewPredecessorMessage(predecessor.getIp(), predecessor.getPort(), 
                    Peer.chordNode.getNodeInfo(), toForward);

                MessageSender sender = new MessageSender(message);
                sender.send();                
            } else {
                ChordInfo peerPred = Peer.chordNode.getPredecessor();
                Message message = new HandShakeMessage(peerPred.getIp(), peerPred.getPort(), Peer.chordNode.getNodeInfo());
                MessageSender sender = new MessageSender(message);
                if (!sender.send()) {
                    Peer.chordNode.setPredecessor(predecessor);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}