package src.CLI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import src.Utils;
import src.chord.ChordInfo;
import src.messages.protocolMessages.backup.*;
import src.messages.protocolMessages.state.*;
import src.messages.protocolMessages.reclaim.*;
import src.messages.protocolMessages.restore.*;
import src.messages.protocolMessages.delete.*;
import src.messages.*;

/**
 * class that works as an interface to comunicate to Peer when executing
 */
public class Client {

    public static MessageReceiver receiver;
    public ChordInfo target;
    public ChordInfo info;
    ScheduledExecutorService executor;
    public static int origRepDegree;

    /**
     * constructor, initializes the receiver socket
     */
    Client() {
        receiver = new MessageReceiver(0);
        int portAssigned = receiver.getPort();
        info = new ChordInfo(Utils.getOwnIP(), portAssigned);
        executor = Executors.newScheduledThreadPool(10);
        executor.schedule(receiver, 0, TimeUnit.SECONDS);
    }

    /**
     * first function that executes when Client starts running
     * @param args
     */
    public static void main(String[] args) {

        Client client = new Client();
        System.out.println("\n-------- Client Info --------");
        System.out.println(client.info.toString() + "\n");

        int targetPort = 0;
        String targetIP = "";

        // parses arguments passed to the program
        if (args.length >= 2) {
            targetIP = args[0];
            targetPort = Integer.parseInt(args[1]);
            client.target = new ChordInfo(targetIP, targetPort);
        } else {
            System.out.println("Bad Args, exiting");
            System.exit(0);
        }

        // checks what is the protocol that we want to execute on the peer
        // and calls the respective function
        try {
            String op = args[2].toUpperCase();
            switch (op) {
                case "BACKUP":
                    client.backup(args[3], Integer.parseInt(args[4]));
                    break;
                case "DELETE":
                    client.delete(args[3]);
                    break;
                case "RESTORE":
                    client.restore(args[3]);
                    break;
                case "RECLAIM":
                    client.reclaim(Integer.parseInt(args[3]));
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

    }

    /**
     * client ip port BACKUP file rd client ip port DELETE file client ip port
     * RESTORE file client ip port RECLAIM space client ip port STATE
     */

    /**
     * executes the backup protocol in the given peer
     * @param filePath - file to backup
     * @param repDegree - replication degree of the file
     */
    public void backup(String filePath, int repDegree) {

        if (repDegree < 1 || repDegree > 9) {
            System.out.println("Replication degree must be in the interval [1,9]");
            return;
        }

        origRepDegree = repDegree;

        byte[] fileContents = null;
        String toHash = new String();
        int fileKey = -1;
        try {
            File file = new File(filePath);
            toHash = file.toPath().getFileName().toString();
            if (file.exists() && file.canRead()) {
                // reads file to byte[]
                fileContents = Files.readAllBytes(file.toPath());
                // gets file metadata to create hash
                BasicFileAttributes metaData = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                toHash += metaData.creationTime().toString() + metaData.lastModifiedTime().toString() + metaData.size();

                // Get key from file info
                fileKey = Utils.hashString(toHash);
                System.out.println("FileKey: " + fileKey);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Could not open file to read. Exiting...");
            return;
        }

        // Request backup
        Message message = new ClientBackupMessage(this.target.getIp(), this.target.getPort(), this.info, fileContents,
                repDegree, fileKey);
        MessageSender sender = new MessageSender(message);
        if (!sender.send()) {
            System.out.println("Could not contact requested peer. Exiting...");
            System.exit(-1);
        }

    }

    /**
     * executes the reclaim protocol in thje given peer
     * @param space - ne max space for the peer\
     */
    public void reclaim(int space) {

        // Request reclaim
        Message message = new ClientReclaimMessage(this.target.getIp(), this.target.getPort(), this.info, space);
        MessageSender sender = new MessageSender(message);
        if (!sender.send()) {
            System.out.println("Could not contact requested peer. Exiting...");
            System.exit(-1);
        }
    }

    /**
     * executes the state protocol of the given peer
     * prints in the console the state of the peer with all of its relevant informations
     */
    public void state() {
        GetStateMessage message = new GetStateMessage(this.target.getIp(), this.target.getPort(), this.info);
        MessageSender sender = new MessageSender(message);
        if (!sender.send()) {
            System.out.println("Could not contact requested peer. Exiting...");
            System.exit(-1);
        }
    }

    /**
     * executes the restore protocol of a given file, asked to a given peer
     * @param filePath - file to be restored
     */
    public void restore(String filePath) {

        int fileKey = getFileHash(filePath);

        ClientRestoreMessage message = new ClientRestoreMessage(this.target.getIp(), this.target.getPort(), this.info, fileKey);
        MessageSender sender = new MessageSender(message);
        if(!sender.send()){
            System.out.println("Could not contact requested peer. Exiting...");
            System.exit(-1);
        }
    }

    /**
     * executes the delete protocol of a file
     * @param filePath - file to be deleted
     */
    public void delete(String filePath) {

        int fileKey = getFileHash(filePath);
    
        ClientDeleteMessage message = new ClientDeleteMessage(this.target.getIp(), this.target.getPort(), this.info, fileKey);
        MessageSender sender = new MessageSender(message);
        if(!sender.send()){
            System.out.println("Could not contact requested peer. Exiting...");
            System.exit(-1);
        }
    }

    /**
     * calculates the hash of a given file using metadata and its name
     * @param filePath - file to be hashed
     * @return the hash of the file
     */
    public int getFileHash(String filePath){
        String toHash = new String();
        int fileKey = -1;
        try {
            File file = new File(filePath);
            toHash = file.toPath().getFileName().toString();
            if (file.exists() && file.canRead()) {
                // gets file metadata to create hash
                BasicFileAttributes metaData = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                toHash += metaData.creationTime().toString() + metaData.lastModifiedTime().toString() + metaData.size();

                // Get key from file info
                fileKey = Utils.hashString(toHash);
                System.out.println("Key == " + fileKey);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Could not open file to read. Exiting...");
            System.exit(1);
        }
        return fileKey;
    }
}