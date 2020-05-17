package src.messages.protocolMessages;

import src.messages.*;
import src.CLI.Peer;
import src.chord.*;

public class Backup extends Message {
    public int key;
    public byte[] content;
    public int repDegree;

    public Backup(String ipAdress, int port, ChordInfo sender, int key, byte[] content, int repDegree ) {
        super(ipAdress, port, sender);
        this.key = key;
        this.content = content;
        this.repDegree = repDegree;
    }

    @Override
    public void handle() {
        if (Peer.maxSpace.get() < (Peer.usedSpace.get() + content.length)) {
            // TODO store forwarding table to know it was forwarded
            // TODO send backup message with same repDegree to successor
            return;
        }

        System.out.println(new String(this.content));
        Peer.fileManager.write(Integer.toString(this.key), this.content);
        if(this.repDegree - 1 > 0){
            ChordInfo successor = Peer.chordNode.getFinger(0);
            Message message = new Backup(successor.getIp(), successor.getPort(), Peer.chordNode.getNodeInfo(), this.key, this.content, this.repDegree -1);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }
        
    }

}