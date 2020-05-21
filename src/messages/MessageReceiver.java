package src.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class MessageReceiver implements Runnable {

    private SSLServerSocket serverSocket;
    private int port;
    private ScheduledExecutorService executor;
    private boolean closed = false;

    public MessageReceiver(int port) {
        this.port = port;
        this.setSocketProperties();
        this.createSocket();
        executor = Executors.newScheduledThreadPool(50);
    }

    public int getPort() {
        return this.port;
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
            serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(this.port);
            this.port = serverSocket.getLocalPort();
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

        while (!closed) {

            receivedMessage = null;
            clientSocket = null;

            try {
                clientSocket = (SSLSocket) serverSocket.accept();
            } catch (IOException e) {
                if (closed) {
                    System.out.println("Socket is closed.");
                    break;
                } else {
                    System.out.println("Error accepting client socket.");
                    e.printStackTrace();
                }
            }

            ObjectInputStream inputStream = null;

            try {
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                System.out.println("Error getting client input stream.");
            }
            if (clientSocket != null) {
                if (inputStream != null) {

                    try {
                        receivedMessage = inputStream.readObject();
                    } catch (ClassNotFoundException e) {
                        System.out.println("Error reading message (Class Not Found)");
                    } catch (IOException e) {
                        System.out.println("Error reading message (IOException)");
                    }
                }
            }

            Message e = (Message) receivedMessage;

            executor.schedule(new Thread(() -> e.handle()), 0, TimeUnit.SECONDS);
        }

    }

    public void closeSocket() {
        closed = true;
        System.out.println("Closing receiver socket...");
        try {
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}