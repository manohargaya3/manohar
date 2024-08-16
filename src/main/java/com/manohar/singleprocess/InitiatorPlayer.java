package com.manohar.singleprocess;

import java.util.concurrent.BlockingQueue;

/**
 * InitiatorPlayer class.
 * InitiatorPlayer class is extended from PlayerRunnable.
 * @author manohar
 *
 */
public class InitiatorPlayer extends PlayerRunnable
{
    private static final String INIT_MESSAGE = "Hello ";

    public InitiatorPlayer(BlockingQueue<String> sent, BlockingQueue<String> received)
    {
        super(sent, received);
    }

    @Override
    public void run()
    {
        sendInitMessage();
        while (true)
        {
            String receivedMessage = receive();
            reply(receivedMessage);
        }
    }

    private void sendInitMessage()
    {
        try
        {
            sent.put(INIT_MESSAGE);
            System.out.println(this.getClass().getSimpleName()+ sent);
        }
        catch (InterruptedException interrupted)
        {
            String error = String.format(
                    "Player [%s] failed to sent message [%s].",
                    this, INIT_MESSAGE);
            throw new IllegalStateException(error, interrupted);
        }
    }
}
