package com.micer.engine.event.handler;

import com.micer.core.device.DeviceStatus;
import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.Event;
import com.micer.core.event.ExtendedEvent;
import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.core.kafka.KafkaProducerGenerator;
import com.micer.core.utils.Constants;
import com.micer.core.utils.DatetimeUtils;
import com.micer.core.utils.GsonUtils;
import com.micer.core.utils.Utils;
import com.micer.engine.handler.AbstractEngineHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.InitializingBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class PeriodicEventHandler extends AbstractEngineHandler implements InitializingBean {

    private KafkaProducerGenerator producerGenerator;
    private ExtendedEventCache periodicEventCache;
    protected DeviceStatusCache deviceStatusCache;



    public PeriodicEventHandler(String handlerId)
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

    public void setPeriodicEventCache(ExtendedEventCache periodicEventCache)
    {
        this.periodicEventCache = periodicEventCache;
    }

    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }

    public void doHandle(Map context, Object object) throws ExecutionException, InterruptedException {
        Event event = (Event)object;
        String deviceConfigId = (String)event.getDeviceConfigId();
        String deviceProtocolId = (String)event.getDeviceProtocolId();
        String deviceId = Utils.genDeviceId(deviceConfigId, deviceProtocolId);
        DeviceStatus deviceStatus = deviceStatusCache.queryStatus(deviceId);
        String oldStatusCode = deviceStatus.getStatusCode();
        String eventStatusCode = (String)event.getValues().get(Constants.DEVICE_STATUS_CODE);//从000中拿value
        if(eventStatusCode.equalsIgnoreCase(oldStatusCode))
        {
            long lastSent = ((ExtendedEvent)event).getLastsent();
            long current = System.currentTimeMillis();
            long diff = current - lastSent;
            long seconds = diff / 1000L;
            long periodiInternal = engineContext.getPeriodicInternal();
            int send = ((ExtendedEvent) event).getSend();

            if(seconds >= periodiInternal   &&  send!=0)
            {
                ((ExtendedEvent)event).plusOneSend();
                ((ExtendedEvent)event).setLastsent(System.currentTimeMillis());


                String deviceEventTopic = engineContext.getDeviceEventTopic();

                ProducerRecord record = new ProducerRecord(deviceEventTopic, event.getEventId().toString(), event);
                KafkaProducer producer = producerGenerator.getEventProducer();
                RecordMetadata metadata = (RecordMetadata)producer.send(record).get();
                System.out.println("第二次发送"+event);
                System.out.println(currentTime());
                producer.close();

                if(fileLogger != null)
                    fileLogger.debug(GsonUtils.getDefaultGson().toJson((ExtendedEvent)event));
                if(consoleLogger != null)
                    consoleLogger.debug((new StringBuilder()).append(DatetimeUtils.toDateTimeString(new Date())).append(" : ").append(event.getEventId()).append(" : ").append(event.getValues()).toString());
            }

            try
            {
                Thread.currentThread();
                Thread.sleep(10000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            //periodicEventCache.put((ExtendedEvent)event);
        }
    }
    public static String currentTime()
    {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }
}
