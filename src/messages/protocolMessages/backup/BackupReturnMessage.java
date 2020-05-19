package src.messages.protocolMessages.backup;

import src.chord.ChordInfo;
import src.messages.Message;

public class BackupReturnMessage extends Message {

    public BackupReturnMessage(String ipAddress, int port, ChordInfo sender) {
        super(ipAddress, port, sender);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void handle() {
        System.out.println("Backup terminated successfully. Exiting...");
        System.exit(1);
    }


    
}