package com.micer.core.handler;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface Handler {
    public abstract String getHandlerId();

    public abstract void doHandle(Map<String, Object> map, Object obj) throws ExecutionException, InterruptedException;
}
