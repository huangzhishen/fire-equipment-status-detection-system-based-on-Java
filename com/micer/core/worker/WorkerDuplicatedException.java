package com.micer.core.worker;

public class WorkerDuplicatedException extends Exception{
    public WorkerDuplicatedException(String msg) {
        super(msg);
    }

    public WorkerDuplicatedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
