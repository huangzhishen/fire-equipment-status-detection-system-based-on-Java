package com.micer.engine.handler;

import com.micer.core.handler.AbstractHandler;
import com.micer.engine.context.EngineContext;
import com.micer.engine.context.EngineContextAware;
import org.apache.log4j.Logger;

public abstract class AbstractEngineHandler  extends AbstractHandler implements EngineContextAware {

    protected Logger networkLogger;
    protected Logger fileLogger;
    protected Logger consoleLogger;
    protected EngineContext engineContext;

    public void setNetworkLogger(Logger networkLogger)
    {
        this.networkLogger = networkLogger;
    }

    public void setFileLogger(Logger fileLogger)
    {
        this.fileLogger = fileLogger;
    }

    public void setConsoleLogger(Logger consoleLogger)
    {
        this.consoleLogger = consoleLogger;
    }

    public AbstractEngineHandler(String handlerId) {
        super(handlerId);
    }

    public void setEngineContext(EngineContext engineContext)
    {
        this.engineContext = engineContext;
    }
}
