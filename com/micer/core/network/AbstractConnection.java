package com.micer.core.network;

public abstract class AbstractConnection implements Connection{
    protected boolean isActive = false;


    public AbstractConnection() {}

    public boolean isActive()
    {
        return isActive;
    }
}
