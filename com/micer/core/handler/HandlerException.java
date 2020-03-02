package com.micer.core.handler;

public class HandlerException extends Exception{
    public HandlerException(String msg)
    {
        super(msg);
    }

    public HandlerException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
