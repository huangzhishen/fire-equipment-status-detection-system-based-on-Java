package com.micer.engine.processor;

public class ProcessorException extends Exception{

    public ProcessorException(String msg)
    {
        super(msg);
    }

    public ProcessorException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
