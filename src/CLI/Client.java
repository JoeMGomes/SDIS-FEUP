package src.CLI;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import src.chord.ChordInfo;
import src.messages.GetStateMessage;
import src.messages.MessageReceiver;
import src.messages.MessageSender;

public class Client {

    public static MessageReceiver receiver;

    public static void main(String[] args) {

        receiver = new MessageReceiver(0);
        int portAssigned = receiver.getPort();
        ChordInfo self = new ChordInfo("127.0.0.1", portAssigned);
        System.out.println(self.toString());

        int targetPort = 0;
        String targetIP = "";
        ChordInfo targetInfo = null;

        if (args.length >= 2) {
            targetIP = args[0];
            targetPort = Integer.parseInt(args[1]);
            targetInfo = new ChordInfo(targetIP, targetPort);
        } else {
            System.out.println("Bad Args, exiting");
            System.exit(0);
        }

        //ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        //executor.schedule(receiver, 0, TimeUnit.SECONDS);

        new Thread(receiver).start();
        // if (args.length == 3){
        GetStateMessage message = new GetStateMessage(targetIP, targetPort, self);
        MessageSender sender = new MessageSender(message);
        sender.send();
        // }

    }

    
}