package src.messages.protocolMessages.restore;

import src.messages.*;
import src.CLI.Peer;
import src.chord.ChordInfo;
import src.*;
import src.messages.protocolMessages.restore.*;


public class ClientRestoreMessage extends Message {

    int key;

    public ClientRestoreMessage(String ipAddress, int port, ChordInfo sender,  int key){
        super(ipAddress, port, sender);
        this.key = key;
    }


    @Override
    public void handle() {

        //If is the Peer responsible for the key
        if(Peer.chordNode.getPredecessor() != null && Utils.isBetween(Peer.chordNode.getPredecessor().getHashKey(), Peer.chordNode.getNodeHash(),key,false)){

            if(Peer.fileManager.fileExists(Integer.toString(this.key))){
                // Send to client
                Peer.fileManager.readAndRestore(Integer.toString(this.key), getSender());

            }else if(Peer.forwarded.contains(this.key)){
                System.out.println("Sending request request to successor1");
                //else query successor
                ChordInfo successor = Peer.chordNode.getSuccessor(0);
                RequestRestore message = new RequestRestore(successor.getIp(),successor.getPort(), Peer.chordNode.getNodeInfo(), getSender(), this.key);
                MessageSender sender = new MessageSender(message);
                sender.send();
            } else {
                //Error message
                RestoreReturnMessage message = new RestoreReturnMessage(getSender().getIp(), getSender().getPort(), Peer.chordNode.getNodeInfo(), -1, this.key, null);
                MessageSender sender = new MessageSender(message);
                sender.send();
                return;
            }
        } else {
            //Find Responsible
            ChordInfo n1 = Peer.chordNode.closestPrecedingNode(this.key);
            FindRestore message = new FindRestore(n1.getIp(), n1.getPort(), getSender(), this.key);
            MessageSender sender = new MessageSender(message);
            sender.send();
        }

    }
    
}