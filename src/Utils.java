package src;

import src.chord.ChordNode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.*;
import java.nio.ByteBuffer;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Utils {
    public static boolean isBetween(int min, int max, int value, boolean semiclosed) {
        if (min >= max) {
            return semiclosed ? min < value || value <= max 
                                : min < value || value < max;
        }

        return semiclosed ? min < value && value <= max 
                            : min < value && value < max;
    }

    public static int hashString(String s) {
        return Math.floorMod(encryptSha1(s), (int) Math.pow(2, ChordNode.mBits));
    }

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