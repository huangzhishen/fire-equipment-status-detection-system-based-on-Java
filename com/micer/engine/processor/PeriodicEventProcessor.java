package com.micer.engine.processor;

import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.cache.ExtendedEventCache;

public class PeriodicEventProcessor extends AbstractEventProcessor
{
    private ExtendedEventCache periodicEventCache;
    protected DeviceStatusCache deviceStatusCache;

    public PeriodicEventProcessor(String processorId)
    {
        super(processorId);
    }

    public void setPeriodicEventCache(ExtendedEventCache periodicEventCache)
    {
        this.periodicEventCache = periodicEventCache;
    }

    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }
}
