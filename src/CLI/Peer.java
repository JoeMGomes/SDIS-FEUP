package src.CLI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.chord.FixFingers;
import src.chord.Stabilize;
import src.messages.MessageReceiver;

public class Peer {

    public static final boolean debug = true;


    public static ChordNode chordNode;
    public static FixFingers fixFingers;
    public static Stabilize stabilize;
    public static MessageReceiver receiver;
    
    public static void main(String[] args){

        int knownPeerPort;
        String knownPeerIP;
        ChordInfo knownPeer = null;

        if(args.length == 2) {
            knownPeerIP = args[0];
            knownPeerPort = Integer.parseInt(args[1]);
            knownPeer = new ChordInfo(knownPeerIP, knownPeerPort);
        }
        
        fixFingers = new FixFingers();
        stabilize = new Stabilize();
        receiver = new MessageReceiver(0);
        int portAssigned = receiver.getPort();
            chordNode = new ChordNode(knownPeer, "127.0.0.1", portAssigned);

        System.out.println(portAssigned);
        System.out.println(chordNode.getNodeInfo().getIp());


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.schedule(receiver, 0, TimeUnit.SECONDS);
        //executor.scheduleAtFixedRate(Peer.fixFingers, 500, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(Peer.stabilize, 500, 500, TimeUnit.MILLISECONDS);
    }

    public static void log(String string){
        if(debug){
            System.out.println("Log: " + string);
        }
    }
}