package com.micer.core.event.cache;

import com.micer.core.event.ExtendedEvent;

public interface ExtendedEventCache {
    public abstract ExtendedEvent take()
            throws InterruptedException;

    public abstract void put(ExtendedEvent extendedevent);
}
