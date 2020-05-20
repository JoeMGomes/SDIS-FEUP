package src.messages.protocolMessages.reclaim;

import src.messages.*;
import src.chord.*;
import src.CLI.*;

public class ReclaimBackupMessage extends Message {
    public int key;
    public byte[] content;
    
    public ReclaimBackupMessage(String ipAdress, int port, ChordInfo sender, int key, byte[] content) {
        super(ipAdress, port, sender);
        this.key = key;
        this.content = content;
    }

    @Override
    public void handle() {
        if (getSender().getHashKey() == Peer.chordNode.getNodeHash()) {
            return;
        }
        
        //If there is not enough space or if it already has the file
        if (Peer.maxSpace.get() < (Peer.usedSpace.get() + content.length)) {
            Peer.forwarded.add(key);            
        }else if (!Peer.fileManager.fileExists(Integer.toString(this.key))){
            Peer.fileManager.write(Integer.toString(this.key), this.content);
            Peer.usedSpace.getAndAdd(content.length);
            return;
        }
        
        ChordInfo successor = Peer.chordNode.getSuccessor(0);
        Message message = new ReclaimBackupMessage(successor.getIp(), successor.getPort(), getSender(), this.key, this.content);
        MessageSender sender = new MessageSender(message);
        sender.send();
    }
}