package src.messages.protocolMessages.backup;

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
        //If there is not enough space
        if (Peer.maxSpace.get() < (Peer.usedSpace.get() + content.length)) {
            Peer.forwarded.add(key);
            //repDegree incremented to be decremented in next if
            this.repDegree++;
        }else{
            Peer.fileManager.write(Integer.toString(this.key), this.content);
            Peer.usedSpace.getAndAdd(content.length);
        }
        
        //If replication degree not achieved
        if(this.repDegree - 1 > 0){
            ChordInfo successor = Peer.chordNode.getFinger(0);
            Message message = new Backup(successor.getIp(), successor.getPort(), getSender(), this.key, this.content, this.repDegree -1);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }else{
            Message message = new BackupReturnMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo());
            MessageSender sender = new MessageSender(message);
            sender.send();
        }
        
    }
}