package src.CLI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import src.Utils;
import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.chord.FixFingers;
import src.chord.Stabilize;
import src.messages.MessageReceiver;
import src.files.FileManager;

public class Peer {

    public static final boolean debug = false;

    public static ChordNode chordNode;
    public static FixFingers fixFingers;
    public static Stabilize stabilize;
    public static MessageReceiver receiver;
    public static FileManager fileManager;
    public static AtomicInteger maxSpace = new AtomicInteger(Integer.MAX_VALUE);
    public static AtomicInteger usedSpace = new AtomicInteger(0);
    public static List<Integer> forwarded = Collections.synchronizedList(new ArrayList<Integer>());

    public static void main(String[] args) {

        int knownPeerPort;
        String knownPeerIP;
        ChordInfo knownPeer = null;
        int portAssigned = 0;

        try {
            portAssigned = Integer.parseInt(args[0]);
            knownPeerIP = args[1];
            knownPeerPort = Integer.parseInt(args[2]);
            knownPeer = new ChordInfo(knownPeerIP, knownPeerPort);
        } catch (Exception e) {
            System.out.println("Initiating First Peer");
        }

        fixFingers = new FixFingers();
        stabilize = new Stabilize();
        receiver = new MessageReceiver(portAssigned);

        chordNode = new ChordNode(knownPeer, Utils.getOwnIP(), receiver.getPort());
        fileManager = new FileManager(Integer.toString(chordNode.getNodeHash()));

        System.out.println("Initiating Peer: ");
        System.out.println(chordNode.getNodeInfo().toString());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        executor.schedule(receiver, 0, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(Peer.fixFingers, 0, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(Peer.stabilize, 0, 500, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(Peer::shutdown));
    }

    public static void setMaxSpace(int newSpace) {

        if (newSpace < usedSpace.get()) {
            fileManager.deleteUntilMaxSpace(newSpace);
        }
        // Sets new max space
        maxSpace.set(newSpace);
    }

    public static void shutdown() {
        setMaxSpace(0);

        Peer.receiver.closeSocket();
        System.out.println("\nGracefully exiting peer " + Peer.chordNode.getNodeHash() + "...");
    }

    public static void log(String string){
        if(debug){
            try {

                System.out.println("Log " + receiver.getPort()+ ": " + string);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}