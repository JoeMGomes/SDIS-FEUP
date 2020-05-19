package src.messages.protocolMessages.backup;

import src.messages.*;
import src.chord.*;
import src.Utils;
import src.CLI.*;

public class FindBackup extends Message{
    public int key;
    public byte[] content;
    public int repDegree;

    public FindBackup(String ipAdress, int port, ChordInfo sender, int key, byte[] content, int repDegree ) {
        super(ipAdress, port, sender);
        this.key = key;
        this.content = content;
        this.repDegree = repDegree;
    }

    @Override
    public void handle() {

        try {

            if (Utils.isBetween(Peer.chordNode.getNodeHash(), Peer.chordNode.getFinger(0).getHashKey(), this.key, true)) {  
                ChordInfo successor = Peer.chordNode.getFinger(0);
                Peer.log("Sending FindBackup Message");
                //Sends Sucessor Message to original asker
                Message message = new Backup(successor.getIp(), successor.getPort(), getSender(), this.key, this.content, this.repDegree);
                MessageSender sender = new MessageSender(message);
                sender.send();
            } else {
                Peer.log("Sending FindBackup ");
                //Forward FindSucessor Message to best Peer
                ChordInfo n1 = Peer.chordNode.closestPrecedingNode(this.key);
                FindBackup message = new FindBackup(n1.getIp(), n1.getPort(), getSender(), this.key, this.content, this.repDegree);
                MessageSender sender = new MessageSender(message);
                sender.send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}