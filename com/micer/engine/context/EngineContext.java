package com.micer.engine.context;

public interface EngineContext {
    public abstract String getDeviceConfigId();

    public abstract String getDeviceEventTopic();

    public abstract long getPeriodicInternal();

    public abstract int getAtleastonceProcessorWorkerSize();

    public abstract int getPeriodicProcessorWorkerSize();

    public abstract int getResponseEventProcessorWorkerSize();

    public abstract String getEngineId();

    public abstract String getEngineName();

    public abstract long getHeartbeatInternal();

    public abstract String getHeartBeatTopic();

    public abstract String getConIp();

    public abstract int getConPort();

    public abstract int getConConTimeout();

    public abstract int getConOpTimeout();

    public abstract String getDeviceProtocol();

    public abstract String getEngineConfig();

    public abstract String getStatusCodeConfig();

    public abstract String getDeviceConfigStatus(String s);

    public abstract long getFreshThreshold();

    public abstract String getRegisterStart();

    public abstract int getRegisterNumbers();

    public abstract int getSingleLoopSize();

    public abstract int getUsedLoopNumber();
}
