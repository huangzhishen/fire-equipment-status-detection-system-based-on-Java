package com.micer.engine.processor.worker;

import com.micer.core.event.ExtendedEvent;
import com.micer.core.handler.HandlerChain;
import java.util.HashMap;
import java.util.Map;

public class DefaultEventProcessorWorker extends AbstractProcessorWorker{

    public DefaultEventProcessorWorker() {}

    protected void doJob()
    {
        try
        {
            while(true)
            {
                //System.out.println("jjjjjjj");
                ExtendedEvent event = extendedEventCache.take();
//                if(event == null)
//                {
//                    System.out.println("jjjjjjj");
//                }
                //System.out.println(event);
                //System.out.println("jjjjjjj");
                Map<String, Object> context = new HashMap();

                handlerChain.doHandle(context, event);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
