package com.micer.engine.executor;

import com.micer.core.codec.Protocol;
import com.micer.core.network.Connection;
import com.micer.core.worker.Worker;
import com.micer.core.worker.WorkerException;
import com.micer.engine.executor.worker.AbstractExecutorWorker;

public class HubOrientedMissionExecutor  extends AbstractMissionExecutor {
    private Worker executorWorker;
    public HubOrientedMissionExecutor(String executorId)
    {
        super(executorId);
    }
    public void setExecutorWorker(Worker executorWorker)
    {
        this.executorWorker = executorWorker;
    }
    public int getWorkerSize()
    {
        return 1;
    }
    public void setWorkerConnection(String workerId, Connection connection)
    {
        ((AbstractExecutorWorker)executorWorker).setConnection(connection);
    }
    public void setWorkerProtocol(String workerId, Protocol protocol)
    {
        ((AbstractExecutorWorker)executorWorker).setProtocol(protocol);
    }
    public void start() throws ExecutorException
    {
        try
        {
            executorWorker.start();//通过bean的属性，可以指向HubOrientedExecutorWorker
        }
        catch(WorkerException e)
        {
            throw new ExecutorException((new StringBuilder()).append("Executor [").append(executorId).append("] fail to start!").toString(), e);
        }
    }
    public void suspend() throws ExecutorException
    {
        try
        {
            executorWorker.suspend();
        }
        catch(WorkerException e)
        {
            throw new ExecutorException((new StringBuilder()).append("Executor [").append(executorId).append("] fail to suspend!").toString(), e);
        }
    }
    public void resume() throws ExecutorException
    {
        try
        {
            executorWorker.resume();
        }
        catch(WorkerException e)
        {
            throw new ExecutorException((new StringBuilder()).append("Executor [").append(executorId).append("] fail to resume!").toString(), e);
        }
    }
    public void stop()
            throws ExecutorException
    {
        try
        {
            executorWorker.stop();
        }
        catch(WorkerException e)
        {
            throw new ExecutorException((new StringBuilder()).append("Executor [").append(executorId).append("] fail to stop!").toString(), e);
        }
    }
}
