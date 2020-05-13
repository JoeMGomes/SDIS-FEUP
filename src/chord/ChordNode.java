package src.chord;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Collections;

import src.messages.FindSuccessor;
import src.messages.MessageSender;
import src.CLI.Peer;

public class ChordNode {
    public final static int mBits = 6;
    private List<ChordInfo> fingerTable = Collections.synchronizedList(new ArrayList<ChordInfo>()); ;
    private ChordInfo predecessor;
    public ChordInfo nodeInfo;

    public ChordNode(ChordInfo node,String ip, int port) {
        this.nodeInfo = new ChordInfo(ip, port);
        for (int i =0; i < mBits; i++) {
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

        for (int i = ChordNode.mBits; i > 0; i--) {
        
            if (getFinger(i).getHashKey() > getNodeHash() || 
            getFinger(i).getHashKey() < hashKey)
                return getFinger(i);
        }

        return getNodeInfo();
    }

    

    // public void stabilize() {
    //     //TCP MESSAGE
    //     ChordInfo x = this.getFinger(0).getPredecessor();

    //     if (x.getNodeHash() > this.getNodeHash() || x.getNodeHash() < this.getFinger(0).getNodeHash()) {
    //         this.setFinger(x,0);
    //     }
    //     //TCP MESSAGE
    //     this.getFinger(0).notifyNode(this);
    // }

    // public void notifyNode(ChordNode n1) {
    //     //TCP MESSAGE
    //     int n1Key = n1.getNodeHash();

    //     if(this.predecessor == null || n1Key > predecessor.getNodeHash() || n1Key < this.getNodeHash()){
    //         this.predecessor = n1;
    //     }
    // }

    // Getters and setters bellow this line
    public ChordInfo getNodeInfo(){
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
	
    public ChordInfo getFinger(int i){
        if (i >= getFingerTable().size()) {
            return null;
        }
        return this.getFingerTable().get(i);
    }
    public void setFinger(ChordInfo info, int i){
        this.getFingerTable().set(i, info);
    }

    public static int hashString(String s){
        return Math.floorMod(encryptSha1(s), (int) Math.pow(2,mBits));
    }

    public static int encryptSha1(String s){
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        ByteBuffer wrapped = ByteBuffer.wrap(hash);

		return wrapped.getInt();
    }
}