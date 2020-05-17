package src.chord;

import java.io.Serializable;
import java.util.*;
import java.util.Collections;

import src.messages.chordMessages.FindSuccessor;
import src.messages.MessageSender;
import src.CLI.Peer;
import src.*;

public class ChordNode implements Serializable {
    public final static int mBits = 6;
    private List<ChordInfo> fingerTable = Collections.synchronizedList(new ArrayList<ChordInfo>());
    private ChordInfo predecessor;
    public ChordInfo nodeInfo;

    public ChordNode(ChordInfo node, String ip, int port) {
        this.nodeInfo = new ChordInfo(ip, port);
        for (int i = 0; i < mBits; i++) {
            fingerTable.add(nodeInfo);
        }
        if (node != null) {
            this.join(node);
        }
    }

    public void join(ChordInfo n1) {
        this.predecessor = null;
        Peer.log("Sending FindSuccessor in ChordNode to " + n1.getIp() + ':' + n1.getPort());
        FindSuccessor f = new FindSuccessor(n1.getIp(), n1.getPort(), getNodeInfo(), getNodeInfo(), false);
        MessageSender sender = new MessageSender(f);
        sender.send();
    }

    public ChordInfo closestPrecedingNode(int hashKey) {

        for (int i = ChordNode.mBits - 1; i >= 0; i--) {
            if(Utils.isBetween( getNodeHash(), hashKey, getFinger(i).getHashKey(), false))
                return getFinger(i);
        }

        return getNodeInfo();
    }

    // Getters and setters bellow this line
    public ChordInfo getNodeInfo() {
        return this.nodeInfo;
    }

    public int getNodeHash() {
        return this.nodeInfo.getHashKey();
    }

    public List<ChordInfo> getFingerTable() {
        return this.fingerTable;
    }

    public void setFingerTable(List<ChordInfo> fingerTable) {
        this.fingerTable = fingerTable;
    }

    public ChordInfo getPredecessor() {
        return this.predecessor;
    }

    public void setPredecessor(ChordInfo predecessor) {
        this.predecessor = predecessor;
    }

    public ChordInfo getFinger(int i) {
        if (i >= getFingerTable().size()) {
            return null;
        }
        return this.getFingerTable().get(i);
    }

    public void setFinger(ChordInfo info, int i) {
        this.getFingerTable().set(i, info);
    }


    public static void main(String[] args) {
        System.out.println(Utils.isBetween(10, 40, 30, false));//truye
        System.out.println(Utils.isBetween(10, 40, 1, false));//F
        System.out.println(Utils.isBetween(10, 40, 40, false));//False
        System.out.println(Utils.isBetween(10, 40, 40, true));//Trye
        System.out.println(Utils.isBetween(40, 10, 30, false));//false
        System.out.println(Utils.isBetween(40, 10, 1, false)); // t
        System.out.println(Utils.isBetween(40, 10, 10, false)); //f
        System.out.println(Utils.isBetween(40, 10, 10, true)); //true
    }

}