package src.messages.protocolMessages.delete;

import src.Utils;
import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.Message;
import src.messages.MessageSender;

public class ClientDeleteMessage extends Message {

    int key;

    public ClientDeleteMessage(String ipAddress, int port, ChordInfo sender, int key) {
        super(ipAddress, port, sender);
        this.key = key;
    
    }

    @Override
    public void handle() {

        if(Peer.chordNode.getPredecessor() != null && Utils.isBetween(Peer.chordNode.getPredecessor().getHashKey(), Peer.chordNode.getNodeHash(),this.key,false)){
            //Handle it 
            ChordInfo self = Peer.chordNode.getNodeInfo();
            DeleteMessage delete = new DeleteMessage(self.getIp(), self.getPort(), getSender() ,getSender() , this.key);
            delete.handle();

        } else{
            ChordInfo n1 = Peer.chordNode.closestPrecedingNode(this.key);
            FindDelete message = new FindDelete(n1.getIp(), n1.getPort(), getSender(), this.key);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }
        

    }

}