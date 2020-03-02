package com.micer.engine;

import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.core.event.cache.ResponseEventCache;
import com.micer.core.mission.cache.Mission4ExecuteCache;
import com.micer.core.worker.Worker;
import com.micer.engine.context.EngineContext;
import com.micer.engine.context.EngineContextAware;
import com.micer.engine.event.handler.ResponseEventHandler;
import com.micer.engine.executor.MissionExecutor;
import com.micer.engine.processor.EventProcessor;
import com.micer.engine.processor.ResponseEventProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * AbstractEngine类两个最重要的方法
 */
public abstract class AbstractEngine  implements Engine, EngineContextAware, ApplicationContextAware {

    protected ApplicationContext applicationContext;
    protected String engineId;
    protected String engineName;
    protected EngineContext engineContext;
    protected DeviceStatusCache deviceStatusCache;
    protected ExtendedEventCache atleastOnceEventCache;
    protected EventProcessor atleastOnceEventProcessor;
    protected ExtendedEventCache periodicEventCache;
    protected EventProcessor periodicEventProcessor;
    protected ResponseEventCache responseEventCache;
    protected ResponseEventProcessor responseEventProcessor;
    protected ResponseEventHandler responseEventHandler;
    protected Worker onlineHeartBeatWorker;
    protected Mission4ExecuteCache mission4ExecuteCache;
    protected MissionExecutor missionExecutor;

    public AbstractEngine()
    {
    }
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public void setEngineContext(EngineContext engineContext)
    {
        this.engineContext = engineContext;
    }

    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }

    public void setAtleastOnceEventCache(ExtendedEventCache atleastOnceEventCache)
    {
        this.atleastOnceEventCache = atleastOnceEventCache;
    }

    public void setAtleastOnceEventProcessor(EventProcessor atleastOnceEventProcessor)
    {
        this.atleastOnceEventProcessor = atleastOnceEventProcessor;
    }

    public void setPeriodicEventCache(ExtendedEventCache periodicEventCache)
    {
        this.periodicEventCache = periodicEventCache;
    }

    public void setPeriodicEventProcessor(EventProcessor periodicEventProcessor)
    {
        this.periodicEventProcessor = periodicEventProcessor;
    }

    public void setResponseEventCache(ResponseEventCache responseEventCache)
    {
        this.responseEventCache = responseEventCache;
    }

    public void setResponseEventProcessor(ResponseEventProcessor responseEventProcessor)
    {
        this.responseEventProcessor = responseEventProcessor;
    }

    public void setResponseEventHandler(ResponseEventHandler responseEventHandler)
    {
        this.responseEventHandler = responseEventHandler;
    }

    public void setOnlineHeartBeatWorker(Worker onlineHeartBeatWorker)
    {
        this.onlineHeartBeatWorker = onlineHeartBeatWorker;
    }

    public void setMission4ExecuteCache(Mission4ExecuteCache mission4ExecuteCache)
    {
        this.mission4ExecuteCache = mission4ExecuteCache;
    }

    public void setMissionExecutor(MissionExecutor missionExecutor)
    {
        this.missionExecutor = missionExecutor;
    }

    /**
     * 功能：
     * 1、从engineContext中取相关属性参数(EngineContext这个类在实例化时已经从engine.properties中加载了全部属性)
     * 2、atleastOnceEventProcessor与responseEventProcessor中的Worker数为3、5
     * @throws EngineException
     */
    protected void init() throws EngineException
    {
        try
        {
            String engineId = engineContext.getEngineId();//采集程序编码
            setEngineId(engineId);
            String engineName = engineContext.getEngineName();//采集程序名称
            setEngineName(engineName);
            int atleastOnceProcessorWorkerSize = engineContext.getAtleastonceProcessorWorkerSize();
            for(int i = 0; i < atleastOnceProcessorWorkerSize; i++)
            {
                Worker worker = (Worker)applicationContext.getBean("atleastOnceEventProcessorWorker");
                atleastOnceEventProcessor.addWorker(worker);
            }

            int responseEventProcessorWorkerSize = engineContext.getResponseEventProcessorWorkerSize();
            for(int i = 0; i < responseEventProcessorWorkerSize; i++)
            {
                Worker worker = (Worker)applicationContext.getBean("responseEventProcessorWorker");
                responseEventProcessor.addWorker(worker);
            }

            int PeriodicProcessorWorkerSize = engineContext.getPeriodicProcessorWorkerSize();
            for(int i = 0; i < PeriodicProcessorWorkerSize; i++)
            {
                Worker worker = (Worker)applicationContext.getBean("periodicEventProcessorWorker");
                periodicEventProcessor.addWorker(worker);
            }

        }
        catch(Exception e)
        {
            throw new EngineException("Engine init fail!", e);
        }
    }


    /**
     * 功能：
     * 1、开启四类线程
     * atleastOnceEventProcessor:负责发送事件至Kafka。其start()函数与其他不同，因为它有多个
     * onlineHeartBeatWorker:负责Kafka服务的心跳检测。
     * missionExecutor:负责通过路由器向消防主机请求数据
     * responseEventProcessor:负责解析从消防主机回传数据，取出设备状态。其start()函数与其他不同，因为它有多个
     *
     * @throws EngineException
     */
    public void start() throws EngineException
    {
        init();
        try
        {

                atleastOnceEventProcessor.start();
                onlineHeartBeatWorker.start();
                missionExecutor.start();
               // periodicEventProcessor.start();
                responseEventProcessor.start();



        }
        catch(Exception e)
        {
            throw new EngineException((new StringBuilder()).append("Engine [").append(engineId).append(":").append(engineName).append("] fail to start!").toString(), e);
        }
    }

    public void suspend() throws EngineException
    {
        try
        {
            responseEventProcessor.suspend();
            missionExecutor.suspend();
            onlineHeartBeatWorker.suspend();
            atleastOnceEventProcessor.suspend();
        }
        catch(Exception e)
        {
            throw new EngineException((new StringBuilder()).append("Engine [").append(engineId).append(":").append(engineName).append("] fail to suspend!").toString(), e);
        }
    }

    public void resume() throws EngineException
    {
        try
        {
            responseEventProcessor.resume();
            atleastOnceEventProcessor.resume();
            onlineHeartBeatWorker.resume();
            missionExecutor.resume();
        }
        catch(Exception e)
        {
            throw new EngineException((new StringBuilder()).append("Engine [").append(engineId).append(":").append(engineName).append("] fail to resume!").toString(), e);
        }
    }

    public void stop() throws EngineException
    {
        try
        {
            responseEventProcessor.stop();
            onlineHeartBeatWorker.stop();
            missionExecutor.stop();
            atleastOnceEventProcessor.stop();
        }
        catch(Exception e)
        {
            throw new EngineException((new StringBuilder()).append("Engine [").append(engineId).append(":").append(engineName).append("] fail to stop!").toString(), e);
        }
    }


}
