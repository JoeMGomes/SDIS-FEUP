package src.messages.protocolMessages;

import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.Message;

public class ReturnStateMessage extends Message {

    private ChordNode info;

    public ReturnStateMessage(String ipAddress, int port, ChordInfo sender, ChordNode info) {
        super(ipAddress, port, sender);
        this.info = info;
    }

    @Override
    public void handle() {

        System.out.println(this.toString());
    }

    public String toString() {
        String returnString = new String();
        try {
            returnString += "Node Hash: " + info.getNodeHash()+ "\n";
            returnString += info.getNodeInfo().toString();
            returnString += "\n\n";

            int i = 0;
            for (ChordInfo c : info.getFingerTable()) {

                int fingerKey = (info.getNodeHash() + (int) Math.pow(2, i)) % (int) Math.pow(2, ChordNode.mBits);
                returnString += "Finger " + i  + "["+ fingerKey + "]"+ ": " + c.toString() + "\n";
                i++;
            }

            if (info.getPredecessor() != null)
                returnString += "\nPredecessor:" + info.getPredecessor().toString() + "\n";
            else
                returnString += "\nPredeccessor: NULL\n";

            i = 0;
            for (ChordInfo c : info.getSuccessorList()) {
                returnString += "Successor " + i + ": " + c.toString() + "\n";
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

}