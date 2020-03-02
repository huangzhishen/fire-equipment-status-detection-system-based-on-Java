package com.micer.core.device;

public class DeviceStatus {
    private String statusCode;
    private long lastUpdate;
    public DeviceStatus() {}
    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
