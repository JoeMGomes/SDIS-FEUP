package files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * class responsible for encampsulating the async mechanisms of Java NIO 
 * TODO kinda not doing it because of CompletionHandler received
 */
public class AsyncRead {

    /**
     * 
     * @param filePath - Path of the file to be read from
     * @param handler - CompletionHandler that is composed of a completed and a failed functions to be run when the read is done
     */
    public static void read(Path filePath, CompletionHandler<Integer, ByteBuffer> handler) {
        try {
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.READ);
            int fileSize = (int) Files.size(filePath);
            ByteBuffer buffer = ByteBuffer.allocate(fileSize);
            long position = 0;

            fileChannel.read(buffer, position, buffer, handler);

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}