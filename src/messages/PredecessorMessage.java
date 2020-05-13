package src.messages;

import src.CLI.Peer;
import src.chord.ChordInfo;

public class PredecessorMessage extends Message {

    private ChordInfo predecessorInfo;

    public PredecessorMessage(String ipAddressDest, int portDest, ChordInfo sender, ChordInfo predecessorInfo) {
        super(ipAddressDest, portDest, sender);
        this.predecessorInfo = predecessorInfo;
    }

    @Override
    public void handle() {
        try {
            System.out.println(Peer.chordNode.getNodeHash());
            if (predecessorInfo.getHashKey() > Peer.chordNode.getNodeHash()
                    || predecessorInfo.getHashKey() < Peer.chordNode.getFinger(0).getHashKey()) {
                Peer.chordNode.setFinger(predecessorInfo, 0);
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