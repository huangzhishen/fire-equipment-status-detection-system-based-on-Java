package com.micer.engine.processor;

import com.micer.core.handler.HandlerNotFoundException;
import com.micer.core.worker.Worker;
import com.micer.core.worker.WorkerDuplicatedException;
import com.micer.core.worker.WorkerNotFoundException;

public interface EventProcessor {
    public abstract String getProcessorId();

    public abstract int getWorkerSize();

    public abstract void start()
            throws ProcessorException;

    public abstract void suspend()
            throws ProcessorException;

    public abstract void resume()
            throws ProcessorException;

    public abstract void stop()
            throws ProcessorException;

    public abstract Worker getWorker(String s)
            throws WorkerNotFoundException, ProcessorException;

    public abstract void addWorker(Worker worker)
            throws WorkerDuplicatedException, ProcessorException;

    public abstract void removeWorker(String s)
            throws WorkerNotFoundException, ProcessorException;

    public abstract void startWorker(String s)
            throws WorkerNotFoundException, ProcessorException;

    public abstract void suspendWorker(String s)
            throws WorkerNotFoundException, ProcessorException;

    public abstract void resumeWorker(String s)
            throws WorkerNotFoundException, ProcessorException;

    public abstract void stopWorker(String s)
            throws WorkerNotFoundException, ProcessorException;

    public abstract void suspendHandler(String s, String s1)
            throws WorkerNotFoundException, HandlerNotFoundException, ProcessorException;

    public abstract void resumeHandler(String s, String s1)
            throws WorkerNotFoundException, HandlerNotFoundException, ProcessorException;
}
