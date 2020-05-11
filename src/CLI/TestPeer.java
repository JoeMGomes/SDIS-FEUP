package src.CLI;

import src.messages.*;

public class TestPeer {

    public static void main(String[] Args) {
        

        MessageReceiver messageReceiver = new MessageReceiver(3000);

        Thread t = new Thread(messageReceiver);
        t.start();

    }


    
}