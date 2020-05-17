package src.messages.protocolMessages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.Message;
import src.messages.MessageSender;

public class GetStateMessage extends Message {

    public GetStateMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
    }

    @Override
    public void handle() {

        try{
            Peer.log("Sending STATE message to " + getSender().getPort());
            ReturnStateMessage message = new ReturnStateMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(), Peer.chordNode);
            MessageSender sender = new MessageSender(message);
            sender.send();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
}