package com.micer.core.event.cache;

import com.micer.core.event.ResponseEvent;

public interface ResponseEventCache {

    public abstract ResponseEvent take() throws InterruptedException;
    public abstract void put(ResponseEvent responseevent);

}
