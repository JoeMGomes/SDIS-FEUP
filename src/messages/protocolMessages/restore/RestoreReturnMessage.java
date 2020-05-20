package src.messages.protocolMessages.restore;

import src.chord.ChordInfo;
import src.files.FileManager;
import src.messages.Message;

public class RestoreReturnMessage extends Message {

    int state;
    int key;
    byte[] contents;

    public RestoreReturnMessage(String ipAddress, int port, ChordInfo sender, int state, int key, byte [] contents) {
        super(ipAddress, port, sender);
        this.state = state;
        this.key = key;
        this.contents = contents;
    }

    @Override
    public void handle() {

        switch(this.state){
            case 1:
                FileManager.writeClient(Integer.toString(this.key), contents);
                System.out.println("Restore terminated successfully. Exiting... " + getSender().getPort());
                break;
            case -1:
                System.out.println("Failed to find specified file. Exiting...");
        }
        
    }
    
}