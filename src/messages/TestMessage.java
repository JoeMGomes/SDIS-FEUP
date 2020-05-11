package src.messages;

import src.chord.ChordInfo;

public class TestMessage extends Message {
    
    public int i = 99;

    public TestMessage(String ipAdress, int port, ChordInfo sender) {
        super(ipAdress, port, sender);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handle() {
        ChordInfo c = new ChordInfo(getIpAddress(), getPort());

        System.out.println("Handled message!" + i);
        TestMessage teste = new TestMessage(getSender().getIp(), getSender().getPort(), c);
        MessageSender s = new MessageSender(teste);
        s.send();
    }
}