package com.micer.engine;

public class EngineException extends Exception{
    public EngineException(String msg)
    {
        super(msg);
    }

    public EngineException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
