package com.micer.core.handler;

public class HandlerNotFoundException extends Exception{
    public HandlerNotFoundException(String msg)
    {
        super(msg);
    }

    public HandlerNotFoundException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
