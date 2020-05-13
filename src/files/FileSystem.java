package src.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileSystem {

    private final String defaultRoot = "peer";
    private final String defaultFiles = "files";
    private final String defaultRestore = "restore";
    private String rootFolder;

    public FileSystem(String id) {
        rootFolder = defaultRoot + id;
    }

    public void write(String name, byte[] data) {
        Path path = Paths.get(rootFolder, defaultFiles, name);

        // idk what to do with completion handler
        AsyncWrite.write(path, data, null);
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