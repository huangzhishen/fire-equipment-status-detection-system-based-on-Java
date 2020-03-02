package com.micer.core.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

public class TimedSocket {
    private static int POLL_DELAY = 10;

    public TimedSocket() {}

    public static Socket getTCPSocket(String host, int port, int timeout) throws InterruptedIOException, IOException {
        SocketThread st = new SocketThread(host, port);
        st.start();
        Socket socket = null;
        int timer = 0;
        do
        {
            if (st.isConnected())
            {
                socket = st.getSocket();
                break;
            }

            if (st.isError())
            {
                throw st.getError();
            }
            try
            {
                Thread.sleep(POLL_DELAY);
            }
            catch (InterruptedException localInterruptedException) {}
            timer += POLL_DELAY;
        } while (timer <= timeout);
        if(socket != null)
        {
            return socket;
        }
        else
        {
            throw new InterruptedIOException("Could not connect for " + timeout + " milliseconds");
        }
    }
}
