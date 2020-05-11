package src.CLI;

import src.messages.*;

public class TestClient {

    public static void main(String[] Args) {
        
        TestMessage test = new TestMessage("127.0.0.1", 3000);
        MessageSender sender = new MessageSender(test);
        sender.send();

    }
}