package src.messages;

public class TestMessage extends Message {
    
    public int i = 99;

    public TestMessage(String ipAdress, int port) {
        super(ipAdress, port);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handle() {
        System.out.println("Handled message!" + i);
    }

}