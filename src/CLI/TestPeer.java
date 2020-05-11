package src.CLI;

import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.*;

public class TestPeer {

    public static void main(String[] Args) {
        
        MessageReceiver messageReceiver = new MessageReceiver(3000);
        Thread t = new Thread(messageReceiver);
        t.start();
        
        ChordNode c = new ChordNode(null, "127.0.0.1", 3000);
    }


    
}