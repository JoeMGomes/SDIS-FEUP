package src.messages.protocolMessages.restore;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.Message;
import src.messages.MessageSender;

public class RequestRestore extends Message {

    public int key;
    public ChordInfo client;

    public RequestRestore(String ipAddress, int port, ChordInfo sender, ChordInfo client, int key) {
        super(ipAddress, port, sender);
        this.key = key;
        this.client = client;
    }

    @Override
    public void handle() {
        if(Peer.fileManager.fileExists(Integer.toString(this.key))){
            // Send to client
            Peer.fileManager.readAndRestore(Integer.toString(this.key), client);

        }else if(Peer.forwarded.contains(this.key)){
            if (getSender().getHashKey() == Peer.chordNode.getNodeHash()) {
                Message message = new RestoreReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo(), -1, this.key, null);
                MessageSender sender = new MessageSender(message);
                sender.send();
                return;
            }
            
            System.out.println("Sending request request to successor2");
            // Query successor
            RequestRestore message = new RequestRestore(Peer.chordNode.getSuccessor(0).getIp(),
            Peer.chordNode.getSuccessor(0).getPort(), getSender(), this.client, this.key);
            MessageSender sender = new MessageSender(message);
            sender.send();
        } else {    
            // Error message
            RestoreReturnMessage message = new RestoreReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo(), -1, this.key, null);
            MessageSender sender = new MessageSender(message);
            sender.send();
            return;
        }
    }

}
