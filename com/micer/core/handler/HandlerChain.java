package com.micer.core.handler;

import org.springframework.beans.factory.InitializingBean;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class HandlerChain implements InitializingBean {

    private List handlerList;
    private Map activeIndicatorMap;

    public HandlerChain()
    {
        activeIndicatorMap = new ConcurrentHashMap();
    }

    public List getHandlerList()
    {
        return handlerList;
    }

    public void setHandlerList(List handlerList)
    {
        this.handlerList = handlerList;
    }

    public void afterPropertiesSet() throws Exception
    {
        for(int i = 0; i < handlerList.size(); i++)
        {
            Handler handler = (Handler)handlerList.get(i);
            String handlerId = handler.getHandlerId();
            activeIndicatorMap.put(handlerId, Boolean.TRUE);
        }
    }

    public void suspendHandler(String handlerId)
            throws HandlerException, HandlerNotFoundException
    {
        try
        {
            if(!activeIndicatorMap.containsKey(handlerId))
                throw new HandlerNotFoundException((new StringBuilder()).append("Handler: ").append(handlerId).append("not found!").toString());
            activeIndicatorMap.put(handlerId, Boolean.FALSE);
        }
        catch(Exception e)
        {
            throw new HandlerNotFoundException("suspendHandler: Unexcepted error!", e);
        }
    }

    public void resumeHandler(String handlerId)
            throws HandlerException, HandlerNotFoundException
    {
        try
        {
            if(!activeIndicatorMap.containsKey(handlerId))
                throw new HandlerNotFoundException((new StringBuilder()).append("Handler: ").append(handlerId).append("not found!").toString());
            activeIndicatorMap.put(handlerId, Boolean.TRUE);
        }
        catch(Exception e)
        {
            throw new HandlerNotFoundException("resumeHandler: Unexcepted error!", e);
        }
    }

    public void doHandle(Map context, Object object) throws ExecutionException, InterruptedException {
        for(int i = 0; i < handlerList.size(); i++)
        {
            Handler handler = (Handler)handlerList.get(i);//根据bean的属性，指向不同的Handler，有ResponseEventHandler和atleastOnceEventHandler
            Boolean isActive = (Boolean)activeIndicatorMap.get(handler.getHandlerId());
            if(isActive)
                handler.doHandle(context, object);
        }
    }
}
