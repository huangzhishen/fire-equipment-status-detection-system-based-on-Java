package com.micer.core.device.cache;

import com.micer.core.device.DeviceStatus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceStatusCacheInMemoryImpl implements DeviceStatusCache{

    private Map statusMap;
    private Map lastsendMap;
    public DeviceStatusCacheInMemoryImpl()
    {
        statusMap = new ConcurrentHashMap();
    }

    public void updateStatus(String deviceId, DeviceStatus deviceStatus)
    {
        statusMap.put(deviceId, deviceStatus);
    }

    public DeviceStatus queryStatus(String deviceId)
    {
        return (DeviceStatus)statusMap.get(deviceId);
    }

    public void updateTime(String deviceId, long lastsend)
    {
        lastsendMap.put(deviceId, lastsend);
    }

    public long queryTime(String deviceId)
    {
        return (long)lastsendMap.get(deviceId);
    }

    public void init(Map statusMap,Map lastsendMap)
    {
        this.statusMap.putAll(statusMap);
        this.lastsendMap.putAll(lastsendMap);
    }


}
