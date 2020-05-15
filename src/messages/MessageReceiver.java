package src.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import src.CLI.Peer;

public class MessageReceiver implements Runnable {

    private SSLServerSocket serverSocket;
    private int port;
    private ScheduledExecutorService executor;

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
                    } catch (IOException e) {
                        System.out.println("Error reading message (IOException)");
                        e.printStackTrace();
                    }
                }
            }

            Message e = (Message) receivedMessage;
            Peer.log("Received from " + e.getSender().getIp() + ':' + e.getSender().getPort());
            
            // try {
            // // TODO Por isto num thread
            executor.schedule(new Thread(() -> e.handle()), 0, TimeUnit.SECONDS);

            //     handle1.get();
            // } catch (InterruptedException e1) {

            //     Exception a = (Exception) e1.getCause();
            //     a.printStackTrace();
            // } catch (ExecutionException e1) {
            //     Exception a = (Exception) e1.getCause();
            //     a.printStackTrace();
            // }
        }

    }
}