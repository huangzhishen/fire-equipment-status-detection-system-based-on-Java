package com.micer.engine.event.handler;

import com.google.gson.Gson;
import com.micer.core.event.Event;
import com.micer.core.utils.*;
import com.micer.core.codec.*;
import com.micer.core.event.ExtendedEvent;
import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.core.kafka.KafkaProducerGenerator;
import com.micer.engine.handler.AbstractEngineHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.InitializingBean;

import java.text.SimpleDateFormat;
import java.util.*;


public class AtleastOnceEventHandler extends AbstractEngineHandler implements InitializingBean {

    private KafkaProducerGenerator producerGenerator;
    private ExtendedEventCache atleastOnceEventCache;
    private ExtendedEventCache periodicEventCache;

    public AtleastOnceEventHandler(String handlerId)
    {
        super(handlerId);
    }

    public void setProducerGenerator(KafkaProducerGenerator producerGenerator)
    {
        this.producerGenerator = producerGenerator;
    }

    public void afterPropertiesSet()
            throws Exception
    {
    }

    public void setAtleastOnceEventCache(ExtendedEventCache atleastOnceEventCache)
    {
        this.atleastOnceEventCache = atleastOnceEventCache;
    }

    public void setPeriodicEventCache(ExtendedEventCache periodicEventCache)
    {
        this.periodicEventCache = periodicEventCache;
    }


    /**
     * @param context：map
     * @param object：ExtendedEvent事件
     * 功能：
     * 1、发送数据
     */
    public void doHandle(Map context, Object object)
    {
        Event event;
        ProducerRecord record;
        event = (Event)object;
//        System.out.println(event);
        String deviceEventTopic = engineContext.getDeviceEventTopic();
        record = new ProducerRecord(deviceEventTopic, event.getEventId().toString(), event);
        try
        {

                long before = System.currentTimeMillis();
                KafkaProducer producer = producerGenerator.getEventProducer();
                RecordMetadata metadata = (RecordMetadata)producer.send(record).get();
                producer.close();


                System.out.println("第一次发送  "+event);
                System.out.println(currentTime());


                long after = System.currentTimeMillis();
                if(networkLogger != null)
                    networkLogger.debug((new StringBuilder()).append(currentTime()).append(": ").append(after - before).toString());

                //atleastOnceEventCache.put((ExtendedEvent)event);
                //System.out.println(currentTime());



            if(fileLogger != null)
                fileLogger.info(GsonUtils.getDefaultGson().toJson((ExtendedEvent)event));
            if(consoleLogger != null)
            {
                consoleLogger.debug((new StringBuilder()).append(DatetimeUtils.toDateTimeString(new Date())).append(" : ").append(event.getEventId()).append(" : ").append(event.getValues()).toString());
            }

        }
        catch(Exception e)
        {
            atleastOnceEventCache.put((ExtendedEvent)event);
            if(consoleLogger != null)
                consoleLogger.debug((new StringBuilder()).append("Send event fail : ").append(event.getEventId()).append(" : ").append(event.getValues()).toString());
        }
        return;
    }

    public static String currentTime()
    {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }


}
