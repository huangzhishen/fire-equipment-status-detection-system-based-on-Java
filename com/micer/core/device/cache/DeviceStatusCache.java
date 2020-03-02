package com.micer.core.device.cache;

import com.micer.core.device.DeviceStatus;
import java.util.Map;

public interface DeviceStatusCache {
    public abstract void updateStatus(String s, DeviceStatus devicestatus);

    public abstract DeviceStatus queryStatus(String s);

    public abstract void updateTime(String s, long lastsend);

    public abstract long queryTime(String s);

    public abstract void init(Map map,Map map1);
}
