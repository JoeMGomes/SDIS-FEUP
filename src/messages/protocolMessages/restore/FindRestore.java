package src.messages.protocolMessages.restore;

import src.Utils;
import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.Message;
import src.messages.MessageSender;

public class FindRestore extends Message {
    public int key;

    public FindRestore(String ipAddress, int port, ChordInfo sender, int key) {
        super(ipAddress, port, sender);
        this.key = key;
    }

    @Override
    public void handle() {

            //Se for o peer responsavel pela key
            if (Utils.isBetween(Peer.chordNode.getNodeHash(), Peer.chordNode.getFinger(0).getHashKey(), this.key, true)) {  
                ChordInfo successor = Peer.chordNode.getFinger(0);
                RequestRestore message = new RequestRestore(successor.getIp(), successor.getPort(), Peer.chordNode.getNodeInfo(), getSender(), this.key);
                MessageSender sender = new MessageSender(message);
                sender.send();   
            } else {
                Peer.log("Sending FindRestore ");
                //Forward FindSucessor Message to best Peer
                ChordInfo n1 = Peer.chordNode.closestPrecedingNode(this.key);
                FindRestore message = new FindRestore(n1.getIp(), n1.getPort(), getSender(), this.key);
                MessageSender sender = new MessageSender(message);
                sender.send();
            }
    }
}
