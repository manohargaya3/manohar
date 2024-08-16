package com.manohar.singleprocess;


import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * PlayerRunnable class.
 * PlayerRunnable class is implemented from Runnable interface.
 * @author manohar
 *
 */
public class PlayerRunnable implements Runnable
{
    protected final BlockingQueue<String> sent;
    protected final BlockingQueue<String> received;

    // Please aware that integer field may overflow during prolonged run
    // of the program. So after 2147483647 we'll get -2147483648. We can
    // either use BigInteger or compare the field with Integer.MAX_VALUE
    // before each increment.
    //
    // Let's choose BigInteger for simplicity.
    private BigInteger numberOfMessagesSent = new BigInteger("0");

    public PlayerRunnable(BlockingQueue<String> sent, BlockingQueue<String> received)
    {
        this.sent = sent;
        this.received = received;
    }

    @Override
    public void run()
    {
        while (true)
        {
            String receivedMessage = receive();
            reply(receivedMessage);
        }
    }

    protected String receive()
    {
        String receivedMessage;
        try
        {
            // Take message from the queue if available or wait otherwise.
            receivedMessage = received.take();
        }
        catch (InterruptedException interrupted)
        {
            String error = String.format(
                    "Main player [%s] failed to receive message on iteration [%d].",
                    this, numberOfMessagesSent);
            throw new IllegalStateException(error, interrupted);
        }
        return receivedMessage;
    }

    protected void reply(String receivedMessage)
    {
        String reply = receivedMessage + " " + numberOfMessagesSent;
        try
        {
            // Send message if the queue is not full or wait until one message
            // can fit.
            sent.put(reply);

            System.out.println(this.getClass().getSimpleName()+ sent);
            numberOfMessagesSent = numberOfMessagesSent.add(BigInteger.ONE);

            // All players will work fine without this delay. It placed here just
            // for slowing the console output down.
            Thread.sleep(1000);
            if (Integer.parseInt(numberOfMessagesSent.toString()) >= 10) {
                Thread.currentThread().interrupt();
                //System.exit(0);
            }
        }
        catch (InterruptedException interrupted)
        {
            String error = String.format(
                    "Player [%s] failed to send message [%s] on iteration [%d].",
                    this, reply, numberOfMessagesSent);
            throw new IllegalStateException(error);
        }
    }

    public void printThreadIDAndProcessID() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        long processId = Long.parseLong(processName.split("@")[0]);
        System.out.println("Process ID: " + processId);
    }
}
