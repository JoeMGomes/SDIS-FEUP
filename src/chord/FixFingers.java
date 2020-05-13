package src.chord;

import src.CLI.Peer;
import src.messages.FindSuccessor;
import src.messages.MessageSender;

public class FixFingers implements Runnable{

    public int next = 0;

    public int getNextFinger() {
        return next;
    }

    @Override
    public void run (){

        Peer.log("Fixing fingers...");
        ChordInfo thisChordInfo = Peer.chordNode.getNodeInfo();
        if(this.next > ChordNode.mBits){
            next = 0;
        }
        int keyToFind = (thisChordInfo.getHashKey() + (int) Math.pow(2, next)) % (int) Math.pow(2, ChordNode.mBits); 
        ChordInfo predecessor = Peer.chordNode.closestPrecedingNode(keyToFind);
        Peer.log("Sending FixFindSuccessor in FixFingers to " + predecessor.getIp() + ':' + predecessor.getPort());
        FindSuccessor message = new FindSuccessor(predecessor.getIp(), predecessor.getPort(), Peer.chordNode.getNodeInfo(), new ChordInfo(keyToFind), true);
        MessageSender sender = new MessageSender(message);
        sender.send();
        next++;
    }
    
}