package com.micer.core.kafka;

import com.micer.core.event.Event;
import com.micer.core.utils.Utils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.InitializingBean;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EventProducerGenerator implements InitializingBean {

    private Properties props = new Properties();
    private String kafkaConfig;
    public EventProducerGenerator() {}

    public void setKafkaConfig(String kafkaConfig)
    {
        this.kafkaConfig = kafkaConfig;
    }

    public void afterPropertiesSet()
            throws ProducerException
    {
        try
        {
            FileInputStream input = new FileInputStream(Utils.getFile(kafkaConfig));
            props.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            throw new ProducerException("Producer init error", e);
        }
    }
    public KafkaProducer<String, Event> getEventProducer()
    {
        return new KafkaProducer(props);
    }
}
