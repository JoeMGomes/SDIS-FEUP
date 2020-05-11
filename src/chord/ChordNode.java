package src.chord;

import java.util.ArrayList;
import java.util.Collections;

public class ChordNode {
    // private final static int mBits = 6;
    // private ArrayList<ChordInfo> fingerTable = new ArrayList<ChordInfo>();
    // private ChordInfo predecessor;
    // private int nodeHash;

    // public ChordNode() {

    // }

    // public void join(ChordNode n1) {
    //     this.predecessor = null;
    //     //TCP MESSAGE
    //     this.setFinger(n1.findSuccessor(this.getNodeHash()), 0 );
    // }

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

    // public void fixFingers(){
    //     //TODO Dunno what is "next"
    //     //Move to Runnable class
    // }

    // // TODO: Really an in id? Ver melhor
    // public ChordInfo findSucessor(int id) {

    //     if (id > this.key || id <= this.getFinger(0).getNodeHash()) {
    //         return this.getFinger(0);
    //     } else {
    //         ChordNode n1 = this.closestPrecedingNode(id);
    //         //TCP MESSAGE
    //         return n1.findSucessor(id);
    //     }
    // }

    // private ChordNode closestPrecedingNode(int id) {

    //     for (int i = mBits; i > 0; i--) {
    //         // TODO: verificar se é >= ou só >
    //         if (this.getFinger(i).getNodeHash() > this.getNodeHash() || this.getFinger(i).getNodeHash() < id)
    //             return this.getFinger(i);
    //     }

    //     return this;
    // }

    // // Getters and setters bellow this line

    // public int getNodeHash() {
    //     return this.nodeHash;
    // }

    // public void setNodeHash(int nodeHash) {
    //     this.nodeHash = nodeHash;
    // }

    // public ArrayList<ChordInfo> getFingerTable() {
    //     return this.fingerTable;
    // }

    // public void setFingerTable(ArrayList<ChordInfo> fingerTable) {
    //     this.fingerTable = fingerTable;
    // }

    // public ChordInfo getPredecessor() {
    //     return this.predecessor;
    // }

    // public void setPredecessor(ChordInfo predecessor) {
    //     this.predecessor = predecessor;
    // }
	
    // public ChordInfo getFinger(int i){
    //     return this.getFingerTable().get(i);
    // }
    // public void setFinger(ChordInfo info, int i){
    //     this.getFingerTable().set(i, info);
    // }
}