package src.CLI;

import java.net.InetAddress;
import java.net.UnknownHostException;

import src.chord.ChordNode;

public class Peer {

    public static ChordNode chordNode;

    public Peer() throws UnknownHostException {
        chordNode = new ChordNode(null, InetAddress.getLocalHost().getHostAddress(), 3000);
    }
    
    public static void main(String[] Args){

    }
}