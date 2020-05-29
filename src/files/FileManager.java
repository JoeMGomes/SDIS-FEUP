package src.files;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import src.CLI.Peer;
import src.chord.*;
import src.messages.*;
import src.messages.protocolMessages.reclaim.*;
import src.messages.protocolMessages.restore.*;

public class FileManager {

    private final String defaultRoot = "peer";
    private String rootFolder;

    public FileManager(String id) {
        rootFolder = defaultRoot + id;

        Path root = Paths.get(rootFolder);
        try {
            if(!Files.exists(root) || !Files.isDirectory(root)) {
                Files.deleteIfExists(root);
                Files.createDirectory(root);
            }

            // if (!Files.exists(this.restorePath) || !Files.isDirectory(this.restorePath)) {
            //     Files.deleteIfExists(this.restorePath);
            //     Files.createDirectory(this.restorePath);
            // }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
    }

    public void write(String name, byte[] data) {
        Path path = Paths.get(rootFolder, name);

        // idk what to do with completion handler
        AsyncWrite.write(path, data, new CompletionHandler<Integer,Object>(){
        
            @Override
            public void failed(Throwable arg0, Object arg1) {
                System.out.println("Failed");
            }
        
            @Override
            public void completed(Integer arg0, Object arg1) {
                System.out.println("Completed");                
            }
        });
    }

    public void read(String name) {
        Path path = Paths.get(rootFolder, name);

        // idk what to do with completion handler
        AsyncRead.read(path, null);
    }

    public void readAndBackup(String name ) {
        Path path = Paths.get(rootFolder, name);

        AsyncRead.read(path, new CompletionHandler<Integer,ByteBuffer>(){
        
            @Override
            public void failed(Throwable arg0, ByteBuffer arg1) {
                try {
                    Files.delete(path);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                System.out.println("Failed backup, deleted " + name);
            }
        
            @Override
            public void completed(Integer arg0, ByteBuffer arg1) {
                ChordInfo successor = Peer.chordNode.getSuccessor(0);

                Message message = new ReclaimBackupMessage(successor.getIp(), successor.getPort(), Peer.chordNode.getNodeInfo(), Integer.parseInt(name), arg1.array());
                MessageSender sender = new MessageSender(message);
                sender.send();

                try {
                    Files.delete(path);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                System.out.println("Successful backup, deleted " + name);
            }
        });
    }

    public void readAndRestore(String name, ChordInfo client) {
        Path path = Paths.get(rootFolder, name);

        AsyncRead.read(path, new CompletionHandler<Integer,ByteBuffer>(){
        
            @Override
            public void failed(Throwable arg0, ByteBuffer arg1) {
                Message message = new RestoreReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo(), -1, Integer.parseInt(name), null);
                MessageSender sender = new MessageSender(message);
                sender.send();
                System.out.println("Failed restore of " + name);
            }
        
            @Override
            public void completed(Integer arg0, ByteBuffer arg1) {

                Message message = new RestoreReturnMessage(client.getIp(), client.getPort(), Peer.chordNode.getNodeInfo(), 1, Integer.parseInt(name), arg1.array());
                MessageSender sender = new MessageSender(message);
                sender.send();

                System.out.println("Successful restore of " + name);
            }
        });
    }

    public void delete(String name) {
        Path path = Paths.get(rootFolder, name);
        try {
            Peer.usedSpace.getAndAdd((int) -Files.size(path));

            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void deleteUntilMaxSpace(int space) {
        File folder = new File(rootFolder);
        File[] files = folder.listFiles();

        for (File file : files) {
            System.out.println("Peer: " + Peer.chordNode.getNodeInfo().getPort() + 
                                "\nnew max Space: " + space +
                                "\nused space: " + Peer.usedSpace.get());
            if (space != 0 && space >= Peer.usedSpace.get()) {
                break;
            }

            Peer.usedSpace.getAndAdd((int) -file.length());

            String deleted = file.getName();
            this.readAndBackup(deleted);

            if (!Peer.forwarded.contains(Integer.parseInt(deleted)))
                Peer.forwarded.add(Integer.parseInt(deleted));
        }
    }

    public boolean fileExists(String name) {
        Path file = Paths.get(rootFolder, name);
        return Files.exists(file);
    }
    
    public static void writeClient(String name, byte[] data) {
        Path path = Paths.get("client", name);
        try {
            Path client = Paths.get("client");
            if (!Files.exists(client))
                Files.createDirectory(client);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // idk what to do with completion handler
        AsyncWrite.write(path, data, new CompletionHandler<Integer,Object>(){
        
            @Override
            public void failed(Throwable arg0, Object arg1) {
                System.out.println("Failed");
                System.exit(1);
            }
        
            @Override
            public void completed(Integer arg0, Object arg1) {
                System.exit(0);           
            }
        });
    }

    public List<Integer> getFileKeys(int key) {
        File folder = new File(rootFolder);
        File[] files = folder.listFiles();
        List<Integer> stored = new ArrayList<>();

        for (File file : files) {
            if (key >= Integer.parseInt(file.getName()) || Integer.parseInt(file.getName()) > Peer.chordNode.getNodeHash())  {
                stored.add(Integer.parseInt(file.getName()));
            }
        }
        return stored;
    }

    public List<Integer> getAllKeys() {
        File folder = new File(rootFolder);
        File[] files = folder.listFiles();
        List<Integer> stored = new ArrayList<>();

        for (File file : files) {
            stored.add(Integer.parseInt(file.getName()));
        }
        return stored;
    }
}