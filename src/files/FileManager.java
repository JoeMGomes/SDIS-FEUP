package src.files;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import src.CLI.Peer;

public class FileManager {

    private final String defaultRoot = "peer";
    private final String defaultFiles = "files";
    private String rootFolder;
    private String filesFolder;

    public FileManager(String id) {
        rootFolder = defaultRoot + id;

        Path root = Paths.get(rootFolder);
        Path filesPath = Paths.get(rootFolder, defaultFiles);
        filesFolder = filesPath.toString();
        try {
            if(!Files.exists(root) || !Files.isDirectory(root)) {
                Files.deleteIfExists(root);
                Files.createDirectory(root);
            }

            if (!Files.exists(filesPath) || !Files.isDirectory(filesPath)) {
                Files.deleteIfExists(filesPath);
                Files.createDirectory(filesPath);
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
        Path path = Paths.get(rootFolder, defaultFiles, name);

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
        Path path = Paths.get(rootFolder, defaultFiles, name);

        // idk what to do with completion handler
        AsyncRead.read(path, null);
    }

    public void delete(String name) {
        Path path = Paths.get(rootFolder, defaultFiles, name);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void deleteUntilMaxSpace(int space) {
        File folder = new File(filesFolder);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (space != 0 && space >= Peer.usedSpace.get()) {
                break;
            }

            Peer.usedSpace.getAndAdd((int) -file.length());
 
            String deleted = file.getName();
            Path path = Paths.get(filesFolder, deleted);

            // TODO read and send to successor

            try {
                Files.delete(path);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    
}