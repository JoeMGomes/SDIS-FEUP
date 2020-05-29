package src;

import src.chord.ChordNode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.*;
import java.nio.ByteBuffer;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * class that has a bunch of functions used all over the program 
 */
public class Utils {

    /**
     * function that calculates if the value is in the (min, max) interval
     * it checks if min is greater than max, when the interval goes around the ring
     * 
     * @param min - lower bound
     * @param max - upper bound
     * @param value - vlaue to analize
     * @param semiclosed - check if the upper bound is equal to value
     * @return true if is in the interval, false otherwise
     */
    public static boolean isBetween(int min, int max, int value, boolean semiclosed) {
        if (min >= max) {
            return semiclosed ? min < value || value <= max 
                                : min < value || value < max;
        }

        return semiclosed ? min < value && value <= max 
                            : min < value && value < max;
    }

    /**
     * Encrypts one string using sha1, and mods it to pow(2, mBits)
     * 
     * @param s - string to be hashed
     * @return hash % pow(2, mBits)
     */
    public static int hashString(String s) {
        return Math.floorMod(encryptSha1(s), (int) Math.pow(2, ChordNode.mBits));
    }

    /**
     * Encrypt one string using sha1
     * 
     * @param s - string to be hashed
     * @return full hash returned as int
     */
    public static int encryptSha1(String s) {
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        ByteBuffer wrapped = ByteBuffer.wrap(hash);

        return wrapped.getInt();
    }

    /**
     * function that sends a packet to 8.8.8.8 and checks the PC IP
     * 
     * @return the correct IP of the machine
     */
    public static String getOwnIP() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostName();
            return InetAddress.getByName(ip).getHostAddress();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}