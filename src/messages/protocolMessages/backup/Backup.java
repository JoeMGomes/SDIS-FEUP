package src.messages.protocolMessages.backup;

import src.messages.*;
import src.CLI.Peer;
import src.chord.*;

public class Backup extends Message {
    public int key;
    public byte[] content;
    public int repDegree;
    public ChordInfo client;

    public Backup(String ipAdress, int port, ChordInfo sender, ChordInfo client, int key, byte[] content, int repDegree) {
        super(ipAdress, port, sender);
        this.key = key;
        this.content = content;
        this.repDegree = repDegree;
        this.client = client;
    }

    @Override
    public void handle() {
    
        if (Peer.maxSpace.get() < (Peer.usedSpace.get() + content.length)) {
            //If there is not enough space
            Peer.forwarded.add(key);
        }else{
            //Write to disk
            Peer.fileManager.write(Integer.toString(this.key), this.content);
            Peer.usedSpace.getAndAdd(content.length);
            this.repDegree -= 1;
        }
        
        //If replication degree not achieved
        if(this.repDegree > 0){
            //If has been to every Peer
            if (getSender().getHashKey() == Peer.chordNode.getNodeHash()) {
                //Return to client with replication degree not reached
                Message message = new BackupReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo(), this.repDegree);
                MessageSender sender = new MessageSender(message);
                sender.send();
                return;
            }

            //Replicate the file forward in the ring
            ChordInfo successor = Peer.chordNode.getSuccessor(0);
            Message message = new Backup(successor.getIp(), successor.getPort(), getSender(), this.client, this.key, this.content, this.repDegree);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }else{
            //Return to client
            Message message = new BackupReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo(), this.repDegree);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }
    }
}