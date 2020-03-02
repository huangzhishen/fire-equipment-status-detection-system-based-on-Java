package com.micer.core.network;

public interface StreamConnection {
    public abstract void write(int paramInt)
            throws NetworkException;

    public abstract void flush()
            throws NetworkException;

    public abstract int read()
            throws NetworkException;

    public abstract void writeUTF(String paramString)
            throws NetworkException;
}
