package src.messages.protocolMessages.reclaim;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.Message;
import src.messages.MessageSender;

public class ClientReclaimMessage extends Message {

    private int maxSpace;

    public ClientReclaimMessage(String ipAddress, int port, ChordInfo sender, int maxSpace) {
        super(ipAddress, port, sender);
        this.maxSpace = maxSpace;
    }

    @Override
    public void handle() {
        Peer.setMaxSpace(this.maxSpace);
        Message message = new ReclaimReturnMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo());
        MessageSender sender = new MessageSender(message);
        sender.send();
    }
    
}