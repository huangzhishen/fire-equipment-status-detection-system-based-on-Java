package com.micer.engine.worker;

import com.micer.core.handler.HandlerChain;
import com.micer.core.handler.HandlerException;
import com.micer.core.handler.HandlerNotFoundException;
import com.micer.core.worker.AbstractWorker;
import com.micer.engine.context.EngineContext;
import com.micer.engine.context.EngineContextAware;
import org.apache.log4j.Logger;

public abstract class AbstractEnginerWorker extends AbstractWorker implements EngineContextAware {

    protected Logger networkLogger;
    protected Logger fileLogger;
    protected Logger consoleLogger;
    protected EngineContext engineContext;
    protected HandlerChain handlerChain;
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

    public AbstractEnginerWorker()
    {
    }

    public void setEngineContext(EngineContext engineContext)
    {
        this.engineContext = engineContext;
    }

    public void setHandlerChain(HandlerChain handlerChain)
    {
        this.handlerChain = handlerChain;
    }

    public void suspendHandler(String handlerId)
            throws HandlerException, HandlerNotFoundException
    {
        handlerChain.suspendHandler(handlerId);
    }

    public void resumeHandler(String handlerId)
            throws HandlerException, HandlerNotFoundException
    {
        handlerChain.resumeHandler(handlerId);
    }
}
