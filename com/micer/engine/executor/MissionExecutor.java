package com.micer.engine.executor;


import com.micer.core.codec.Protocol;
import com.micer.core.network.Connection;

public interface MissionExecutor {
    public abstract String getExecutorId();

    public abstract int getWorkerSize();

    public abstract void setWorkerConnection(String s, Connection connection);

    public abstract void setWorkerProtocol(String s, Protocol protocol);

    public abstract void start()
            throws ExecutorException;

    public abstract void suspend()
            throws ExecutorException;

    public abstract void resume()
            throws ExecutorException;

    public abstract void stop()
            throws ExecutorException;
}
