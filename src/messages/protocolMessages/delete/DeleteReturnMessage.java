package src.messages.protocolMessages.delete;

import src.chord.ChordInfo;
import src.messages.Message;
import src.CLI.Client;

public class DeleteReturnMessage extends Message{

    public DeleteReturnMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
    }

    @Override
    public void handle() {
        System.out.println("Deleted successfully. Exiting...");
        System.exit(1);
    }
    
}