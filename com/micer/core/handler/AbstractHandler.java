package com.micer.core.handler;

public abstract class AbstractHandler implements Handler{

    protected String handlerId;
    public String getHandlerId()
    {
        return handlerId;
    }

    public AbstractHandler(String handlerId)
    {
        this.handlerId = handlerId;
    }

}
