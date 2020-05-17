package src.messages.chordMessages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.*;
import src.*;
import java.util.*;


public class PredecessorMessage extends Message {

    private ChordInfo predecessorInfo;
    private List<ChordInfo> successorList;

    public PredecessorMessage(String ipAddressDest, int portDest, ChordInfo sender, ChordInfo predecessorInfo, List<ChordInfo> successorList) {
        super(ipAddressDest, portDest, sender);
        this.predecessorInfo = predecessorInfo;
        this.successorList = successorList;
    }

    @Override
    public void handle() {
        try {
            if (Utils.isBetween(Peer.chordNode.getNodeHash(), Peer.chordNode.getFinger(0).getHashKey(),
                    predecessorInfo.getHashKey(), false)) {
                Peer.chordNode.setFinger(predecessorInfo, 0);
                List<ChordInfo> newSuc = Peer.chordNode.getSuccessorList();
                newSuc.add(0, getSender());
                successorList.remove(ChordNode.successorListSize);
                Peer.chordNode.setSuccessorList(newSuc);
            }
            else {
                this.successorList.add(0, getSender());
                successorList.remove(ChordNode.successorListSize);
                Peer.chordNode.setSuccessorList(this.successorList);
            }
            Peer.log("Sending Notify message");
            ChordInfo successor = Peer.chordNode.getFinger(0);
            NotifyMessage message = new NotifyMessage(successor.getIp(), successor.getPort(),
                    Peer.chordNode.getNodeInfo());
            MessageSender sender = new MessageSender(message);
            sender.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}