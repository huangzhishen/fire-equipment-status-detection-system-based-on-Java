package com.micer.engine.processor;

import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.cache.ResponseEventCache;
import com.micer.core.worker.Worker;

public class ResponseEventProcessor extends AbstractEventProcessor{

    private ResponseEventCache responseEventCache;//可阻塞的队列
    protected DeviceStatusCache deviceStatusCache;//Map存设备信息

    public ResponseEventProcessor(String processorId)
    {
        super(processorId);
    }

    public void setResponseEventCache(ResponseEventCache responseEventCache)
    {
        this.responseEventCache = responseEventCache;
    }

    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }

//    public void addWorker(Worker worker) {
//    }
}
