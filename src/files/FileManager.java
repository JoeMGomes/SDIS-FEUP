package src.files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private final String defaultRoot = "peer";
    private final String defaultFiles = "files";
    private final String defaultRestore = "restore";
    private String rootFolder;

    public FileManager(String id) {
        rootFolder = defaultRoot + id;

        Path root = Paths.get(rootFolder);
        Path filesPath = Paths.get(rootFolder, defaultFiles);
        //this.restorePath = Paths.get(restoreFolder);
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

            // Path peer = Paths.get(peerFolder, peerFolder);
            // if(Files.exists(peer)) {
            //     readFromFile(peer);
            //     Files.deleteIfExists(peer);
            // }
            // Files.createFile(peer);
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
                System.out.println("Completed: " + new String(((ByteBuffer) arg1).array()));                
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
    
}