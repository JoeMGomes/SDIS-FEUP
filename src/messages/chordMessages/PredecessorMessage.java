package src.messages.chordMessages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.*;
import src.Utils;
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
            List<ChordInfo> tempList = Collections.synchronizedList(new ArrayList<ChordInfo>());
            tempList.add(getSender());
            tempList.addAll(successorList.subList(0, successorList.size()-1));
            Peer.chordNode.setSuccessorList(tempList);
            if (Utils.isBetween(Peer.chordNode.getNodeHash(), getSender().getHashKey(),
                    predecessorInfo.getHashKey(), false)) {
                Message message = new HandShakeMessage(predecessorInfo.getIp(), predecessorInfo.getPort(), Peer.chordNode.getNodeInfo());
                MessageSender sender = new MessageSender(message);
                if (sender.send())  {
                    Peer.chordNode.setFinger(predecessorInfo, 0);
                    List<ChordInfo> newSuccList = Collections.synchronizedList(new ArrayList<ChordInfo>());
                    newSuccList.add(predecessorInfo);
                    List<ChordInfo> succList = Peer.chordNode.getSuccessorList();
                    newSuccList.addAll(succList.subList(0, succList.size()-1));
                    Peer.chordNode.setSuccessorList(newSuccList);
                } else {
                    Peer.chordNode.setFinger(Peer.chordNode.getSuccessor(0), 0);
                }
            }
            Peer.log("Sending Notify message");
            ChordInfo successor = Peer.chordNode.getSuccessor(0);
            if (Peer.chordNode.getNodeInfo().getPort() == 9000) 
                System.out.println("Successor: " + successor.getPort());
            NotifyMessage message = new NotifyMessage(successor.getIp(), successor.getPort(),
                    Peer.chordNode.getNodeInfo());
            MessageSender sender = new MessageSender(message);
            sender.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}