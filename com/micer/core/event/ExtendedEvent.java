package com.micer.core.event;

public class ExtendedEvent extends Event{
    private int send;
    private long lastsent;

    public ExtendedEvent()
    {
        send = 0;
        lastsent = 0L;
    }

    public int getSend()
    {
        return send;
    }

    public void plusOneSend()
    {
        send++;
    }

    public long getLastsent()
    {
        return lastsent;
    }

    public void setLastsent(long lastsent)
    {
        this.lastsent = lastsent;
    }

}
