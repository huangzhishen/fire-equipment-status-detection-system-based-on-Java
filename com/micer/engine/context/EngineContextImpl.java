package com.micer.engine.context;

import com.micer.core.utils.Constants;
import com.micer.core.utils.Utils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class EngineContextImpl implements EngineContext, InitializingBean, BeanPostProcessor {

    protected String engineConfig;
    public EngineContextImpl() {}

    public String getEngineConfig()
    {
        return engineConfig;
    }

    public void setEngineConfig(String engineConfig) {
        this.engineConfig = engineConfig;
    }

    protected Map<String, String> engineConfigMap = new HashMap();

    protected String statusCodeConfig;

    public String getStatusCodeConfig()
    {
        return statusCodeConfig;
    }

    public void setStatusCodeConfig(String statusCodeConfig) {
        this.statusCodeConfig = statusCodeConfig;
    }

    protected Map<String, String> statusCodeMapping = new HashMap();

    public String getDeviceConfigStatus(String deviceProtocolStatus)
    {
        return (String)statusCodeMapping.get(deviceProtocolStatus);
    }

    public String getDeviceConfigId()
    {
        return (String)engineConfigMap.get(Constants.DEVICE_CONFIG_ID);
    }

    public String getDeviceEventTopic()
    {
        return (String)engineConfigMap.get(Constants.DEVICE_EVENT_TOPIC);
    }

    public long getPeriodicInternal()
    {
        return Long.parseLong((String)engineConfigMap.get(Constants.PERIODIC_INTERNAL));
    }

    public int getAtleastonceProcessorWorkerSize()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.ATLEASTONCE_PROCESSOR_WORKER_SIZE));
    }

    public int getPeriodicProcessorWorkerSize()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.PERIODIC_PROCESSOR_WORKER_SIZE));
    }

    public String getEngineId()
    {
        return (String)engineConfigMap.get(Constants.ENGINE_ID);
    }

    public String getEngineName()
    {
        return (String)engineConfigMap.get(Constants.ENGINE_NAME);
    }

    public long getHeartbeatInternal()
    {
        return Long.parseLong((String)engineConfigMap.get(Constants.HEARTBEAT_INTERNAL));
    }

    public String getHeartBeatTopic()
    {
        return (String)engineConfigMap.get(Constants.HEARTBEAT_TOPIC);
    }

    public String getConIp()
    {
        return (String)engineConfigMap.get(Constants.CON_IP);
    }

    public int getConPort()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.CON_PORT));
    }

    public int getConConTimeout()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.CON_CON_TIMEOUT));
    }

    public int getConOpTimeout()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.CON_OP_TIMEOUT));
    }

    public String getDeviceProtocol()
    {
        return (String)engineConfigMap.get(Constants.DEVICE_PROTOCOL);
    }

    public long getFreshThreshold()
    {
        return Long.parseLong((String)engineConfigMap.get(Constants.FRESH_THRESHOLD));
    }


    public void afterPropertiesSet() throws ContextInitException
    {
        try
        {
            initStatusCodeMap();
            initEngienConfigMap();
        }
        catch (Exception e) {
            throw new ContextInitException("EngineContext init error!", e);
        }
    }

    private void initStatusCodeMap() throws IOException
    {
        FileInputStream input = new FileInputStream(Utils.getFile(statusCodeConfig));
        Properties props = new Properties();
        props.load(new InputStreamReader(input, Charset.forName("UTF-8")));

        Iterator iter = props.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            String value = props.getProperty(key);
            statusCodeMapping.put(key, value);
        }
    }

    private void initEngienConfigMap() throws IOException
    {
        FileInputStream input = new FileInputStream(Utils.getFile(engineConfig));
        Properties props = new Properties();
        props.load(new InputStreamReader(input, Charset.forName("UTF-8")));

        Iterator iter = props.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            String value = props.getProperty(key);
            engineConfigMap.put(key, value);
        }
    }

    public Object postProcessAfterInitialization(Object bean, String name)
            throws BeansException
    {
        return bean;
    }

    //bean实例化前
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException
    {
        if ((bean instanceof EngineContextAware)) {
            ((EngineContextAware)bean).setEngineContext(this);
        }
        return bean;
    }

    public String getRegisterStart()
    {
        return (String)engineConfigMap.get(Constants.REGISTER_START);
    }

    public int getRegisterNumbers()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.REGISTER_NNMBERS));
    }

    public int getSingleLoopSize()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.SINGLE_LOOP_SIZE));
    }

    public int getUsedLoopNumber()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.USED_LOOP_NUMBER));
    }

    public int getResponseEventProcessorWorkerSize()
    {
        return Integer.parseInt((String)engineConfigMap.get(Constants.HANDLERESPONSE_PROCESSOR_WORKER_SIZE));
    }
}
