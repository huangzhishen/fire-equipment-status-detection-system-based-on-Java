package com.micer.engine.processor.worker;

import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.engine.worker.AbstractEnginerWorker;

public abstract class AbstractProcessorWorker extends AbstractEnginerWorker {
    protected ExtendedEventCache extendedEventCache;
    public AbstractProcessorWorker() {}
    public void setExtendedEventCache(ExtendedEventCache extendedEventCache)
    {
        this.extendedEventCache = extendedEventCache;
    }
}
