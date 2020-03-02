package com.micer.engine.heartbeat;

import com.micer.core.device.DeviceStatus;
import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.Event;
import com.micer.core.kafka.KafkaProducerGenerator;
import com.micer.core.utils.Constants;
import com.micer.core.utils.DatetimeUtils;
import com.micer.core.utils.GsonUtils;
import com.micer.core.utils.Utils;
import com.micer.engine.worker.AbstractEnginerWorker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class OnlineHeartBeatWorker extends AbstractEnginerWorker implements InitializingBean {
    Logger logger = Logger.getLogger(OnlineHeartBeatWorker.class);
    private KafkaProducerGenerator producerGenerator;
    private KafkaProducer<String, Event> producer;
    protected DeviceStatusCache deviceStatusCache;

    public OnlineHeartBeatWorker() {}

    public void setProducerGenerator(KafkaProducerGenerator producerGenerator)
    {
        this.producerGenerator = producerGenerator;
    }

    public void afterPropertiesSet() throws Exception
    {
        long heartBeatInternal = engineContext.getHeartbeatInternal();
        setWaitInternal((int)(heartBeatInternal * 1000L));
        producer = producerGenerator.getEventProducer();
    }

    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }

    protected void doJob()
    {
        try
        {
            String deviceConfigId = engineContext.getDeviceConfigId();

            String hubDeviceId = Utils.genDeviceId(deviceConfigId, Constants.HUB_DEVICE);

            DeviceStatus deviceStatus = deviceStatusCache.queryStatus(hubDeviceId);

            String statusCode = deviceStatus.getStatusCode();
            if (statusCode.equalsIgnoreCase(Constants.DEVICE_STATUS_200))
            {
                long lastUpdate = deviceStatus.getLastUpdate();

                long current = System.currentTimeMillis();

                long diff = current - lastUpdate;

                long seconds = diff / 1000L;

                long freshHold = engineContext.getFreshThreshold();
                if (seconds < freshHold)
                {
                    Event event = genHeartBeatEvent();
                    try
                    {
                        KafkaProducer<String, Event> producer = producerGenerator.getEventProducer();
                        String heartBeatTopic = engineContext.getHeartBeatTopic();
                        ProducerRecord<String, Event> record = new ProducerRecord(heartBeatTopic, event.getEventId().toString(), event);
                        long before = System.currentTimeMillis();
                        RecordMetadata metadata = (RecordMetadata)producer.send(record).get();
                        producer.close();
                        long after = System.currentTimeMillis();
                        if (networkLogger != null) {
                            networkLogger.debug(Long.valueOf(after - before));
                        }
                        if (fileLogger != null) {
                            fileLogger.debug(GsonUtils.getDefaultGson().toJson(event));
                        }
                        if (consoleLogger != null) {
                            consoleLogger.debug(DatetimeUtils.toDateTimeString(new Date()) + " : Heart Beat!!!!!");
                        }
                    }
                    catch (InterruptedException| ExecutionException e)
                    {
                        if (consoleLogger != null) {
                            consoleLogger.debug("Send event fail : " + event.getEventId() + " : " + event.getValues());
                        }
                    }
                }
            }
        }
        catch (Exception localException1) {}
    }

    private Event genHeartBeatEvent()
    {
        Event event = new Event();
        event.setEventId(Utils.uuid());

        String deviceConfigId = engineContext.getDeviceConfigId();
        event.setDeviceConfigId(deviceConfigId);

        event.setDeviceProtocolId(Constants.HUB_DEVICE);
        event.setTimestamp(Long.valueOf(System.currentTimeMillis()));

        Map<CharSequence, CharSequence> values = new HashMap();

        values.put(Constants.DEVICE_STATUS_CODE, Constants.DEVICE_STATUS_200);
        event.setValues(values);
        return event;
    }
}
