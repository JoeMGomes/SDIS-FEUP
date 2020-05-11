package src.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class MessageReceiver implements Runnable {

    private SSLServerSocket serverSocket;
    private int port;

    public MessageReceiver(int port) {
        this.port = port;
        this.setSocketProperties();
        this.createSocket();
    }

    private void setSocketProperties() {
        System.setProperty("javax.net.ssl.keyStore", "src/server.keys");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.trustStore", "src/truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
    }

    private void createSocket() {

        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try {
            serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
            serverSocket.setNeedClientAuth(true);
            serverSocket.setEnabledProtocols(serverSocket.getSupportedProtocols());
            System.out.println("SSL Server Socket created");
        } catch (IOException e) {
            System.err.println("Error creating SSL Server Socket");
            e.printStackTrace();
        }

    }

    public void run() {

        Object receivedMessage = null;
        SSLSocket clientSocket = null;

        while (true) {

        receivedMessage = null;
        clientSocket = null;

            try {
                clientSocket = (SSLSocket) serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Error accepting client socket");
                e.printStackTrace();
            }

            ObjectInputStream inputStream = null;

            try {
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                System.out.println("Error getting clinet input stream.");
                e.printStackTrace();
            }
            if (clientSocket != null) {
                if (inputStream != null) {

                    try {
                        receivedMessage = inputStream.readObject();
                    } catch (ClassNotFoundException e) {
                        System.out.println("Error reading message (Class Not Found)");
                        e.printStackTrace();
                    } catch (IOException e){
                        System.out.println("Error reading message (IOException)");
                        e.printStackTrace();
                    }
                }
            }

            // Por isto num thread
            ((Message) receivedMessage).handle();
        
        }

    }
}