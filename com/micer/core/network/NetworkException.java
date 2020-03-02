package com.micer.core.network;

public class NetworkException extends Exception{
    public NetworkException(String msg)
    {
        super(msg);
    }

    public NetworkException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
