package src.messages.chordMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import src.CLI.Peer;
import src.chord.ChordInfo;
import src.messages.Message;

public class NewPredecessorMessage extends Message {

    private List<Integer> forwardList;

    public NewPredecessorMessage(String ipAddress, int port, ChordInfo sender, List<Integer> forwardList) {
        super(ipAddress, port, sender);
        this.forwardList = forwardList;
    }

    @Override
    public void handle() {
        System.out.println("Received New Predecessor Message");
        Set<Integer> set = new LinkedHashSet<Integer>(Peer.forwarded);
        for(Integer a : set){
            System.out.println(a);
        }
        set.addAll(this.forwardList);
        Peer.forwarded = Collections.synchronizedList(new ArrayList<Integer>());
        Peer.forwarded.addAll(set);
    }
    
}