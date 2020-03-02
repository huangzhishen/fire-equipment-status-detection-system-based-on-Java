package com.micer.engine.executor;

import com.micer.core.mission.cache.Mission4ExecuteCache;
import com.micer.engine.context.EngineContext;
import com.micer.engine.context.EngineContextAware;

public abstract class AbstractMissionExecutor implements MissionExecutor, EngineContextAware {
    protected EngineContext engineContext;
    protected String executorId;
    protected Mission4ExecuteCache mission4ExecuteCache;

    public void setEngineContext(EngineContext engineContext)
    {
        this.engineContext = engineContext;
    }

    public String getExecutorId()
    {
        return executorId;
    }

    public AbstractMissionExecutor(String executorId)
    {
        this.executorId = executorId;
    }

    public void setMission4ExecuteCache(Mission4ExecuteCache mission4ExecuteCache)
    {
        this.mission4ExecuteCache = mission4ExecuteCache;
    }


}
