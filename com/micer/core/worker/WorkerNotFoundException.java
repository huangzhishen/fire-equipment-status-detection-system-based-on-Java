package com.micer.core.worker;

public class WorkerNotFoundException extends Exception{
    public WorkerNotFoundException(String msg)
    {
        super(msg);
    }

    public WorkerNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
