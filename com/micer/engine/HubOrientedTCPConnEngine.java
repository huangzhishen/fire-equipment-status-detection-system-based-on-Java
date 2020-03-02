package com.micer.engine;

import com.micer.core.codec.Protocol;
import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.core.network.Connection;

public class HubOrientedTCPConnEngine extends AbstractEngine{

    protected Connection connection;
    protected Protocol protocol;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

 /*   protected void init() throws EngineException {
        try {
            super.init();
            String host = this.engineContext.getConIp();
            int port = this.engineContext.getConPort();
            int con_timeout = this.engineContext.getConConTimeout();
            int op_timeout = this.engineContext.getConOpTimeout();
            this.connection = new TCPConnection(host, port, con_timeout, op_timeout);
            String deviceProtocol = this.engineContext.getDeviceProtocol();
            this.protocol = (Protocol)this.applicationContext.getBean(deviceProtocol);
            this.missionExecutor.setWorkerConnection(null, this.connection);
            this.missionExecutor.setWorkerProtocol(null, this.protocol);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("registerStart", this.engineContext.getRegisterStart());
            params.put("numbers", this.engineContext.getRegisterNumbers());
            List missionList = this.protocol.encodeMission(params);
            this.mission4ExecuteCache.put(missionList);
        }
        catch (Exception e) {
            throw new EngineException("Engine init fail!", (Throwable)e);
        }
    }*/


    /**
     * 功能：
     * 1、初始化、实例化、设置协议
     * 2、实例化deviceStatusCache和atleastOnceEventCache
     * @throws EngineException
     */
    protected void init() throws EngineException {
        try {
            super.init();
            String deviceProtocol = this.engineContext.getDeviceProtocol();//从engineContext取该消防主机所用协议
            this.protocol = (Protocol)this.applicationContext.getBean(deviceProtocol);
            this.missionExecutor.setWorkerProtocol(null, this.protocol);//在missionExecutor设置工作协议，这个在执行请求数据时用到
            this.responseEventHandler.setProtocol(this.protocol);//在responseEventHandler设置工作协议，这个在解析数据的时候会用到
            this.deviceStatusCache = (DeviceStatusCache)this.applicationContext.getBean("deviceStatusCache");
            this.atleastOnceEventCache = (ExtendedEventCache)this.applicationContext.getBean("atleastOnceEventCache");
        }
        catch (Exception e) {
            throw new EngineException("Engine init fail!", (Throwable)e);
        }
    }
}
