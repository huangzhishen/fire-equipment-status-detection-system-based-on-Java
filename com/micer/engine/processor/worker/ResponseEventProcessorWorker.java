package com.micer.engine.processor.worker;

import com.micer.core.event.ResponseEvent;
import com.micer.core.event.cache.ResponseEventCache;
import com.micer.core.handler.HandlerChain;
import java.util.HashMap;
import java.util.Map;

public class ResponseEventProcessorWorker extends AbstractProcessorWorker{
    protected ResponseEventCache responseEventCache;

    public ResponseEventProcessorWorker() {}

    public void setResponseEventCache(ResponseEventCache responseEventCache)
    {
        this.responseEventCache = responseEventCache;
    }

    protected void doJob()
    {
        try
        {
            while(true)
            {
                ResponseEvent event = responseEventCache.take();
                //System.out.println(event);
                Map<String, Object> context = new HashMap();
                handlerChain.doHandle(context, event);//handlerChain在其父类的父类AbstractEnginerWorker中定义，但在该bean中指定指向responseEventHandlerChain
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
