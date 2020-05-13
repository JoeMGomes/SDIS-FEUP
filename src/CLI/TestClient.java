package src.CLI;

import src.chord.ChordInfo;
import src.chord.ChordNode;
import src.messages.*;

public class TestClient {

    public static void main(String[] Args) {
        

        MessageReceiver messageReceiver = new MessageReceiver(0);
        Thread t = new Thread(messageReceiver);
        t.start();

        ChordNode c = new ChordNode(null, "127.0.0.1", messageReceiver.getPort());

        TestMessage test = new TestMessage("127.0.0.1", 37047, c.getNodeInfo());
        MessageSender sender = new MessageSender(test);
        sender.send();

    }
}