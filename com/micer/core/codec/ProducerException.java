package com.micer.core.codec;

public class ProducerException extends Exception{
    public ProducerException(String msg) {
        super(msg);
    }

    public ProducerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
