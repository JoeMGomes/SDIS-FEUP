package src.CLI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import src.Utils;
import src.chord.ChordInfo;
import src.messages.protocolMessages.*;
import src.messages.protocolMessages.backup.*;
import src.messages.protocolMessages.state.*;
import src.messages.*;

public class Client {

    public static MessageReceiver receiver;
    public ChordInfo target;
    public ChordInfo info;
    ScheduledExecutorService executor;

    Client() {
        receiver = new MessageReceiver(0);
        int portAssigned = receiver.getPort();
        info = new ChordInfo("127.0.0.1", portAssigned);
        executor = Executors.newScheduledThreadPool(10);
        executor.schedule(receiver, 0, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {

        Client client = new Client();

        System.out.println(client.info.toString());

        int targetPort = 0;
        String targetIP = "";

        if (args.length >= 2) {
            targetIP = args[0];
            targetPort = Integer.parseInt(args[1]);
            client.target = new ChordInfo(targetIP, targetPort);
        } else {
            System.out.println("Bad Args, exiting");
            System.exit(0);
        }
        try {
            String op = args[2].toUpperCase();
            switch (op) {
                case "BACKUP":
                    client.backup(args[3], Integer.parseInt(args[4]));
                    break;
                case "DELETE":
                    break;
                case "RESTORE":
                    break;
                case "RECLAIM":
                    break;
                case "STATE":
                    client.state();
                    break;
                default:
                    System.out.println("Unknown protocol, exiting..");
                    return;
            }
        } catch (NullPointerException e) {
            System.out.println("Bad arguments, exiting...");
            return;
        }

        // }

    }

    /**
     * client ip port BACKUP file rd client ip port DELETE file client ip port
     * RESTORE file client ip port RECLAIM space client ip port STATE
     */

    public void backup(String filePath, int repDegree) {

        if (repDegree < 1 || repDegree > 9) {
            System.out.println("Replication degree must be in the interval [1,9]");
            return;
        }

        byte[] fileContents = null;
        String toHash = new String();
        int fileKey = -1;
        try {
            File file = new File(filePath);
            toHash = file.toPath().getFileName().toString();
            if (file.exists() && file.canRead()) {
                //reads file to byte[]
                fileContents = Files.readAllBytes(file.toPath());
                //gets file metadata to create hash
                BasicFileAttributes metaData = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                toHash += metaData.creationTime().toString() + metaData.lastModifiedTime().toString() + metaData.size();

                //Get key from file info
                fileKey = Utils.hashString(toHash);
                System.out.println("Key == " + fileKey);
            }else{
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Could not open file to read. Exiting...");
            return;
        }

        //Request backup
        Message message = new ClientBackupMessage(this.target.getIp(), this.target.getPort(), this.info, fileContents, repDegree, fileKey);
        MessageSender sender = new MessageSender(message);
        sender.send();
    }

    public void state(){
        GetStateMessage message = new GetStateMessage(this.target.getIp(), this.target.getPort(), this.info);
        MessageSender sender = new MessageSender(message);
        sender.send();
    }

}