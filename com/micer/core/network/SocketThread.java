package com.micer.core.network;

import java.io.IOException;
import java.net.Socket;

public class SocketThread extends Thread{
    private String host;
    private int port;

    public SocketThread(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    private Socket socket = null;
    private IOException exception = null;

    public boolean isConnected() {
        if (socket != null)
            return true;
        return false;
    }

    public boolean isError() {
        if (exception != null)
            return true;
        return false;
    }

    public Socket getSocket() {
        return socket;
    }

    public IOException getError() {
        return exception;
    }


    public void run()
    {
        Socket m_socket = null;

        try
        {
            m_socket = new Socket(host, port);
        }
        catch (IOException ioe) {
            exception = ioe;
            return;
        }


        socket = m_socket;
    }
}
