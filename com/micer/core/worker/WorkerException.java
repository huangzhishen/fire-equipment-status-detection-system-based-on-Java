package com.micer.core.worker;

public class WorkerException extends Exception{
    public WorkerException(String msg) {
        super(msg);
    }

    public WorkerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
