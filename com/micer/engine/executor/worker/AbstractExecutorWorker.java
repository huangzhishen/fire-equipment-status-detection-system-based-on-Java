package com.micer.engine.executor.worker;

import com.micer.core.codec.Protocol;
import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.core.event.cache.ResponseEventCache;
import com.micer.core.mission.cache.Mission4ExecuteCache;
import com.micer.core.network.Connection;
import com.micer.engine.worker.AbstractEnginerWorker;

public abstract class AbstractExecutorWorker extends AbstractEnginerWorker {
    protected Mission4ExecuteCache mission4ExecuteCache;
    protected ExtendedEventCache atleastOnceEventCache;
    protected ResponseEventCache responseEventCache;
    protected DeviceStatusCache deviceStatusCache;

    public AbstractExecutorWorker() {}

    public void setMission4ExecuteCache(Mission4ExecuteCache mission4ExecuteCache)
    {
        this.mission4ExecuteCache = mission4ExecuteCache;
    }
    public void setAtleastOnceEventCache(ExtendedEventCache atleastOnceEventCache)
    {
        this.atleastOnceEventCache = atleastOnceEventCache;
    }
    public void setResponseEventCache(ResponseEventCache responseEventCache)
    {
        this.responseEventCache = responseEventCache;
    }
    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }
    public abstract void setConnection(Connection paramConnection);
    public abstract void setProtocol(Protocol paramProtocol);
}
