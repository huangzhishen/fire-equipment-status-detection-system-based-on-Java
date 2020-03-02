package com.micer.core.worker;

import com.micer.core.utils.Utils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractWorker implements Runnable, Worker {
    protected String workerId;
    protected Map context;

    public String getWorkerId()
    {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public AbstractWorker() {
        workerId = Utils.uuid();
    }

    public void setContext(Map context)
    {
        this.context = context;
    }

    protected final Lock lock = new ReentrantLock();
    protected final Condition runCondition = lock.newCondition();
    protected volatile boolean stop = false;
    protected volatile boolean suspended = false;
    protected AtomicBoolean active = new AtomicBoolean(false);
    protected AtomicInteger waitInternal = new AtomicInteger(0);
    protected Thread t;

    public void setWaitInternal(int milis)
    {
        if (milis >= 0) {
            waitInternal.set(milis);
        }
    }

    public int getStatus()
    {
        if (active.get())
        {
            return suspended ? 0 : 1;
        }

        return -1;
    }

    public void start()
            throws WorkerException
    {
        try
        {
            if (active.compareAndSet(false, true))
            {

                while (stop) {}

                t = new Thread(this);
                t.start();
            }
        } catch (Exception e) {
            throw new WorkerException("Worker [" + workerId + "] fail to start!", e);
        }
    }

    public void suspend()
            throws WorkerException
    {
        try
        {
            if (active.get()) {
                suspended = true;
            }
        } catch (Exception e) {
            throw new WorkerException("Worker [" + workerId + "] fail to suspend!", e);
        }
    }

    public void resume()
            throws WorkerException
    {
        try
        {
            if (active.get()) {
                suspended = false;
                lock.lock();
            }
            try { runCondition.signal();

                lock.unlock(); } finally { lock.unlock();
            }

            return;
        }
        catch (Exception e)
        {
            throw new WorkerException("Worker [" + workerId + "] fail to resume!", e);
        }
    }

    public void stop()
            throws WorkerException
    {
        try
        {
            if (active.compareAndSet(true, false)) {
                stop = true;


                while (stop) {}
            }
        }
        catch (Exception e) {
            throw new WorkerException("Worker [" + workerId + "] fail to stop!", e);
        }
    }


    protected abstract void doJob();


    public void run()
    {
        try
        {
            if (!stop)
            {
                long wait = waitInternal.longValue();
                if (wait > 0L) {
                    Thread.sleep(wait);
                }
                doJob();
                lock.lock();
                try
                {
                    while (suspended)
                    {
                        runCondition.await();
                    }
                    lock.unlock();
                }
                finally
                {
                    lock.unlock();
                }
            }
        }
        catch (Exception localException)
        {}
        finally
        {
            stop = false;
        }
    }
}
