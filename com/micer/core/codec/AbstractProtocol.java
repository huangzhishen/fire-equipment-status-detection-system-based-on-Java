package com.micer.core.codec;

public abstract class AbstractProtocol implements Protocol{
    protected String protocolId;

    public AbstractProtocol() {}

    public String getProtocolId() { return protocolId; }

    public void setProtocolId(String protocolId)
    {
        this.protocolId = protocolId;
    }
}
