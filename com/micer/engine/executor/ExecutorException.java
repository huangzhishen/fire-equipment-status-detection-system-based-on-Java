package com.micer.engine.executor;

import com.micer.core.worker.WorkerException;

public class ExecutorException extends Exception {
    public ExecutorException(String msg)
    {
        super(msg);
    }

    public ExecutorException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public ExecutorException(String msg, WorkerException e) {
    }
}
