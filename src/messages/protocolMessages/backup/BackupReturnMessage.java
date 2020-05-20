package src.messages.protocolMessages.backup;

import src.chord.ChordInfo;
import src.messages.Message;
import src.CLI.Client;

public class BackupReturnMessage extends Message {
    public int repDegree;

    public BackupReturnMessage(String ipAddress, int port, ChordInfo sender, int repDegree) {
        super(ipAddress, port, sender);
        this.repDegree = repDegree;
    }

    @Override
    public void handle() {
        if (repDegree == 0) {
            System.out.println("Backup terminated successfully. Exiting...");
        } else {
            System.out.println("Backup terminated with a replication degree of " + (Client.origRepDegree - repDegree)  + ". Exiting...");
        }
        System.exit(1);
    }
    
}