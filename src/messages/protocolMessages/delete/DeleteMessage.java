package src.messages.protocolMessages.delete;

import src.chord.ChordInfo;
import src.CLI.Peer;
import src.messages.Message;
import src.messages.MessageSender;


public class DeleteMessage extends Message{
    
    public int key;
    public ChordInfo client;

    public DeleteMessage(String ipAdress, int port, ChordInfo sender, ChordInfo client, int key ) {
        super(ipAdress, port, sender);
        this.key = key;
        this.client = client;
    }

    @Override
    public void handle() {

        if(Peer.fileManager.fileExists(Integer.toString(this.key)){
            Peer.fileManager.delete(Integer.toString(this.key));
        }
        
        if(Peer.forwarded.contains(this.key)){

            Peer.forwarded.remove(Integer.valueOf(this.key));

            DeleteMessage message = new DeleteMessage(Peer.chordNode.getSuccessor(0).getIp(),
                Peer.chordNode.getSuccessor(0).getPort(), getSender(), this.client, this.key);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }else{
            //Return to client
            DeleteReturnMessage message = new DeleteReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo());
            MessageSender sender = new MessageSender(message);
            sender.send();
        }
        
    }
}