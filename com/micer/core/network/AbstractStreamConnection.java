package com.micer.core.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class AbstractStreamConnection extends AbstractConnection implements StreamConnection{
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;

    public AbstractStreamConnection() {}
}
