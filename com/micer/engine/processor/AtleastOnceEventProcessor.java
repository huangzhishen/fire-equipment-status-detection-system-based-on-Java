package com.micer.engine.processor;

import com.micer.core.event.cache.ExtendedEventCache;

public class AtleastOnceEventProcessor extends AbstractEventProcessor{

    private ExtendedEventCache atleastOnceEventCache;
    public AtleastOnceEventProcessor(String processorId)
    {
        super(processorId);
    }

    public void setAtleastOnceEventCache(ExtendedEventCache atleastOnceEventCache)
    {
        this.atleastOnceEventCache = atleastOnceEventCache;
    }
}
