package src.files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * class responsible for encampsulating the async mechanisms of Java NIO 
 */
public class AsyncWrite {

    /**
     * 
     * @param filePath - Path of the file to be read from
     * @param handler - CompletionHandler that is composed of a completed and a failed functions to be run when the read is done
     */
    public static void write(Path filePath, byte[] data, CompletionHandler<Integer, Object> handler) {
        try {
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            
            int dataSize = data.length;
            ByteBuffer buffer = ByteBuffer.allocate(dataSize);
            buffer.put(data);
            long position = 0;
            //Obligatory flip instruction because ByteBuffers are weird
            buffer.flip();
            fileChannel.write(buffer, position, buffer, handler);

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}