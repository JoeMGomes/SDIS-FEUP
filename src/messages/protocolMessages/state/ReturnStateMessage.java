package src.messages.protocolMessages.state;

import java.util.List;

import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.Message;

public class ReturnStateMessage extends Message {

    private ChordNode info;
    int maxSpace, usedSpace;
    List<Integer> forwardingTable;

    public ReturnStateMessage(String ipAddress, int port, ChordInfo sender, ChordNode info, int usedSpace, int maxSpace, List<Integer> forwardingTable) {
        super(ipAddress, port, sender);
        this.info = info;
        this.usedSpace = usedSpace;
        this.maxSpace = maxSpace;
        this.forwardingTable = forwardingTable;
    }

    @Override
    public void handle() {
        System.out.println(this.toString());
        System.exit(0);
    }

    public String toString() {
        String returnString = new String();
        try {
            returnString += "-------- Peer " + info.getNodeHash() + " State --------\n";
            returnString += info.getNodeInfo().toString();
            returnString += "\n\n";

            
            returnString += "--- Chord Information ---\n";
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

            returnString += "--- Aplication Information ---\n";
            returnString += "Max Space: " + maxSpace + "\n";
            returnString += "Used Space: " + usedSpace + "\n";
            returnString += "\n";
            returnString += "Forwarded Files:\n";
            for (Integer k : this.forwardingTable) {
                returnString += "\tFile: " + k + "\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

}