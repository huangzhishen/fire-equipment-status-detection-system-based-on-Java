package com.micer.core.worker;

import java.util.Map;

public interface Worker {
    public abstract String getWorkerId();

    public abstract void setWorkerId(String paramString);

    public abstract void setWaitInternal(int paramInt);

    public abstract void setContext(Map paramMap);

    public abstract int getStatus();

    public abstract void start()
            throws WorkerException;

    public abstract void suspend()
            throws WorkerException;

    public abstract void resume()
            throws WorkerException;

    public abstract void stop()
            throws WorkerException;
}
