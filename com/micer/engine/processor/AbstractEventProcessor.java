package com.micer.engine.processor;

import com.micer.core.handler.HandlerException;
import com.micer.core.handler.HandlerNotFoundException;
import com.micer.core.worker.Worker;
import com.micer.core.worker.WorkerDuplicatedException;
import com.micer.core.worker.WorkerNotFoundException;
import com.micer.engine.context.EngineContext;
import com.micer.engine.context.EngineContextAware;
import com.micer.engine.processor.worker.AbstractProcessorWorker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractEventProcessor implements EventProcessor, EngineContextAware {

    protected EngineContext engineContext;
    protected String processorId;

    public AbstractEventProcessor(String processorId)
    {
        this.processorId = processorId;
    }
    public void setEngineContext(EngineContext engineContext)
    {
        this.engineContext = engineContext;
    }
    public String getProcessorId()
    {
        return processorId;
    }
    protected final Lock lock = new ReentrantLock();
    protected HashMap processorWorkerMap = new HashMap();
    public int getWorkerSize()
    {
        try
        {
            lock.lock();
            return processorWorkerMap.size();
        } finally {
            lock.unlock();
        }
    }
    public void addWorker(Worker worker) throws WorkerDuplicatedException, ProcessorException
    {
        try
        {
            lock.lock();

            String workerId = worker.getWorkerId();

            if (processorWorkerMap.containsKey(workerId)) {
                throw new WorkerDuplicatedException("Worker: " + workerId + " duplicate!");
            }
            processorWorkerMap.put(workerId, (AbstractProcessorWorker)worker);
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] addWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }

    public Worker getWorker(String workerId)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();

            Worker worker = (Worker)processorWorkerMap.get(workerId);
            if (worker == null) {
                throw new WorkerNotFoundException("Worker: " + workerId + " not found!");
            }
            return worker;
        } catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] getWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }

    public void start()
            throws ProcessorException
    {
        try
        {
            lock.lock();

            operateOnWorkers(null, "start");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] fail to start", e);
        } finally {
            lock.unlock();
        }
    }

    public void suspend()
            throws ProcessorException
    {
        try
        {
            lock.lock();
            operateOnWorkers(null, "suspend");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] fail to suspend", e);
        } finally {
            lock.unlock();
        }
    }

    public void resume()
            throws ProcessorException
    {
        try
        {
            lock.lock();
            operateOnWorkers(null, "resume");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] fail to resume", e);
        } finally {
            lock.unlock();
        }
    }

    public void stop()
            throws ProcessorException
    {
        try
        {
            lock.lock();

            operateOnWorkers(null, "stop");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] fail to stop", e);
        } finally {
            lock.unlock();
        }
    }

    public void removeWorker(String workerId)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();

            Worker worker = (Worker)processorWorkerMap.get(workerId);
            if (worker != null) {
                worker.stop();
            } else
                throw new WorkerNotFoundException("Worker: " + workerId + " not found!", new NullPointerException());
            processorWorkerMap.remove(workerId);
        } catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] removeWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }

    public void startWorker(String workerId)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();

            operateOnWorkers(workerId, "start");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] startWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }

    public void suspendWorker(String workerId)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();
            operateOnWorkers(workerId, "suspend");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] suspendWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }

    public void resumeWorker(String workerId)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();
            operateOnWorkers(workerId, "resume");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] resumeWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }

    public void stopWorker(String workerId)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();

            operateOnWorkers(workerId, "stop");
        }
        catch (Exception e) {
            throw new ProcessorException("Processor [" + processorId + "] stopWorker fail!", e);
        } finally {
            lock.unlock();
        }
    }


    /**
     * @param workerId :线程ID
     * @param operation：操作，start,stop
     * @throws WorkerNotFoundException
     * @throws ProcessorException
     * 功能：
     * 1、在线程map中循环遍历，开启线程
     * 2、为什么这里的Worker能指定为ResponseEventProcessorWorker？因为在初始化Init中指定了相应的bean去实例化
     * 3、指定bean实例化后就放进了map，所以从map中取出来的，就是我们想要的那个ResponseEventProcessorWorker了
     */
    private void operateOnWorkers(String workerId, String operation)
            throws WorkerNotFoundException, ProcessorException
    {
        try
        {
            if (workerId != null) {
                Worker worker = (Worker)processorWorkerMap.get(workerId);
                if (worker == null)
                    throw new WorkerNotFoundException("ProcessorWorker: " + workerId + " not found!");
                worker.start();
            } else {
                Collection<Worker> workers = processorWorkerMap.values();
                Iterator<Worker> iterator = workers.iterator();
                while (iterator.hasNext()) {
                    Worker worker = (Worker)iterator.next();
                    switch (operation) {
                        case "start":
                            worker.start();
                            break;
                        case "stop":
                            worker.stop();
                            break;
                        case "suspend":
                            worker.suspend();
                            break;
                        case "resume":
                            worker.resume();
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new ProcessorException("OperateOnWorkers: Unexcepted error!", e);
        }
    }

    public void suspendHandler(String workerId, String handlerId)
            throws WorkerNotFoundException, HandlerNotFoundException, ProcessorException
    {
        try
        {
            lock.lock();
            operateOnHandlers(workerId, handlerId, "suspend");
            lock.unlock();
        }
        finally
        {
            lock.unlock();
        }
    }

    public void resumeHandler(String workerId, String handlerId)
            throws WorkerNotFoundException, HandlerNotFoundException, ProcessorException
    {
        lock.lock();
        try {
            operateOnHandlers(workerId, handlerId, "resume");
            lock.unlock();
        }
        finally
        {
            lock.unlock();
        }
    }

    private void operateOnHandlers(String workerId, String handlerId, String operation)
            throws WorkerNotFoundException, HandlerNotFoundException, ProcessorException
    {
        try
        {
            if (workerId != null) {
                Worker worker = (Worker)processorWorkerMap.get(workerId);
                if (worker == null)
                    throw new WorkerNotFoundException("Worker: " + workerId + " not found!");
                switch (operation) {
                    case "suspend":
                        ((AbstractProcessorWorker)worker).suspendHandler(handlerId);
                        break;
                    case "resume":
                        ((AbstractProcessorWorker)worker).resumeHandler(handlerId);
                }
            }
            else
            {
                Collection<Worker> workers = processorWorkerMap.values();
                Object iterator = workers.iterator();
                while (((Iterator)iterator).hasNext()) {
                    Worker worker = (Worker)((Iterator)iterator).next();
                    switch (operation)
                    {
                        case "suspend":
                            ((AbstractProcessorWorker)worker).suspendHandler(handlerId);
                            break;
                        case "resume":
                            ((AbstractProcessorWorker)worker).resumeHandler(handlerId);
                    }

                }
            }
        }
        catch (HandlerException e)
        {
            throw new ProcessorException("operateOnHandlers: Unexcepted error!", e);
        }
    }
}
