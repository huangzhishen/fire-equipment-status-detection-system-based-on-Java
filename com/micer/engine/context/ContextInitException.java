package com.micer.engine.context;

public class ContextInitException extends Exception{
    public ContextInitException(String msg)
    {
        super(msg);
    }

    public ContextInitException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
