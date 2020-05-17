package src.messages.chordMessages;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.*;

public class SuccessorMessage extends Message {

    private ChordInfo successorInfo;

    public SuccessorMessage(String ipAddressDest, int portDest, ChordInfo sender, ChordInfo successorInfo) {
        super(ipAddressDest, portDest, sender);
        this.successorInfo = successorInfo;
    }

    @Override
    public void handle() {
        try {

            Peer.log("Sucessor was set to: " + successorInfo.toString());
            Peer.chordNode.setFinger(successorInfo, 0);
            NotifyMessage message = new NotifyMessage(successorInfo.getIp(), successorInfo.getPort(),
                        Peer.chordNode.getNodeInfo());
            MessageSender sender = new MessageSender(message);
            sender.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}