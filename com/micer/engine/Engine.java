package com.micer.engine;

public interface Engine {

    public abstract String getEngineId();

    public abstract String getEngineName();

    public abstract void start()
            throws EngineException;

    public abstract void suspend()
            throws EngineException;

    public abstract void resume()
            throws EngineException;

    public abstract void stop()
            throws EngineException;
}
