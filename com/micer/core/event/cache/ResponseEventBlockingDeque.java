package com.micer.core.event.cache;

import com.micer.core.event.ResponseEvent;

import java.util.concurrent.LinkedBlockingDeque;

public class ResponseEventBlockingDeque implements ResponseEventCache{

    protected LinkedBlockingDeque responseEventCache;

    public ResponseEventBlockingDeque()
    {
        responseEventCache = new LinkedBlockingDeque();
    }

    public ResponseEvent take()
            throws InterruptedException
    {
        return (ResponseEvent)responseEventCache.take();
    }

    public void put(ResponseEvent event)
    {
        responseEventCache.add(event);
    }



}
