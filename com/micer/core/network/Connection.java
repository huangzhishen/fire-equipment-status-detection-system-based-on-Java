package com.micer.core.network;

public interface Connection {
    public abstract void newConnect()
            throws NetworkException;

    public abstract void disConnect();

    public abstract boolean isActive();
}
