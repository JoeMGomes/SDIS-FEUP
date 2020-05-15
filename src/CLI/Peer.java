package src.CLI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
            System.out.println("Bad Args, continuing");
        }

        fixFingers = new FixFingers();
        stabilize = new Stabilize();
        receiver = new MessageReceiver(portAssigned);
        portAssigned = receiver.getPort();
        chordNode = new ChordNode(knownPeer, "127.0.0.1", portAssigned);

        System.out.println(portAssigned);
        System.out.println(chordNode.getNodeInfo().getIp());

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> handle1 = executor.schedule(receiver, 0, TimeUnit.SECONDS);
        ScheduledFuture<?> handle2 = executor.scheduleAtFixedRate(Peer.fixFingers, 0, 500, TimeUnit.MILLISECONDS);
        ScheduledFuture<?> handle3 = executor.scheduleAtFixedRate(Peer.stabilize, 0, 500, TimeUnit.MILLISECONDS);

        // new Thread(receiver).start();

        // while (true) {
        //     try {
		// 		Thread.sleep(500);
		// 	} catch (InterruptedException e) {
		// 		// TODO Auto-generated catch block
		// 		e.printStackTrace();
		// 	}
        //     new Thread(Peer.fixFingers).start();
        //     new Thread(Peer.stabilize).start();
        // }
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