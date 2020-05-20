package src.messages;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MessageSender {
    
    Message message;

    public MessageSender(Message message) {
        this.message = message;
        this.setSocketProperties();
    }

    private void setSocketProperties() {
        System.setProperty("javax.net.ssl.keyStore", "src/server.keys");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.trustStore", "src/truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
    }

    public boolean send(){

        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket clientSocket;
        try {
            clientSocket = (SSLSocket) socketFactory.createSocket(InetAddress.getByName(message.getIpAddress()), message.getPort());
            clientSocket.setSoTimeout(1000);
            clientSocket.startHandshake();

            ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
            outToServer.writeObject(message);
        } catch (Exception e) {
            System.out.println("Error in SSL Conection");
            return false;
        }
        return true;
    }   

}