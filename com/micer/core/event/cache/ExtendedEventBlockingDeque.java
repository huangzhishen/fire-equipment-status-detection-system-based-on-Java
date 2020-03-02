package com.micer.core.event.cache;

import com.micer.core.event.ExtendedEvent;
import java.util.concurrent.LinkedBlockingDeque;

public class ExtendedEventBlockingDeque implements ExtendedEventCache {

    protected LinkedBlockingDeque eventCache;
    public ExtendedEventBlockingDeque()
    {
        eventCache = new LinkedBlockingDeque();
    }

    public ExtendedEvent take()
            throws InterruptedException
    {
        return (ExtendedEvent)eventCache.take();
    }

    public void put(ExtendedEvent event)
    {
        eventCache.add(event);
    }


}
