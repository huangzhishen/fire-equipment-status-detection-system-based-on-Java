package com.micer.engine.event.handler;

import com.micer.core.codec.MissionRuntime;
import com.micer.core.codec.Protocol;
import com.micer.core.codec.ProtocolException;
import com.micer.core.device.DeviceStatus;
import com.micer.core.device.cache.DeviceStatusCache;
import com.micer.core.event.ExtendedEvent;
import com.micer.core.event.ResponseEvent;
import com.micer.core.event.cache.ExtendedEventCache;
import com.micer.core.event.cache.ResponseEventCache;
import com.micer.core.utils.Constants;
import com.micer.core.utils.GsonUtils;
import com.micer.core.utils.Utils;
import com.micer.engine.handler.AbstractEngineHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ResponseEventHandler extends AbstractEngineHandler implements InitializingBean {

    Logger receiveDataLogger = Logger.getLogger("receiveDataLogger");
    Logger fileLogger = Logger.getLogger("eventFileLogger");
    private Protocol protocol;

    public ResponseEventHandler(String handlerId) { super(handlerId); }

    public void setProtocol(Protocol protocol)
    {
        this.protocol = protocol;
    }
    public void setAtleastOnceEventCache(ExtendedEventCache atleastOnceEventCache)
    {
        this.atleastOnceEventCache = atleastOnceEventCache;
    }
    public void setResponseEventCache(ResponseEventCache responseEventCache)
    {
        this.responseEventCache = responseEventCache;
    }

    protected ExtendedEventCache atleastOnceEventCache;

    protected ResponseEventCache responseEventCache;

    protected DeviceStatusCache deviceStatusCache;

    public void setDeviceStatusCache(DeviceStatusCache deviceStatusCache)
    {
        this.deviceStatusCache = deviceStatusCache;
    }

    public void doHandle(Map<String, Object> context, Object object)
    {
        ResponseEvent event = (ResponseEvent)object;
        MissionRuntime<String, Object> runtime = new MissionRuntime();//new一个定制map存放事件信息
        runtime.putExecutedResult(event.getResponse());
        runtime.putExecutedStatus(event.getExecutedStatus());

        if (engineContext.getDeviceProtocol().contains("haiwan"))
        {
            runtime.put("singleLoopSize", Integer.valueOf(engineContext.getSingleLoopSize()));
        }
        try
        {
            protocol.decodeMission(event.getMission(), runtime);//解析数据，得到的数据存在runtime中，一次获取了一批设备的状态信息
            handleEvent(runtime);//处理该事件，判断设备的新旧状态以觉得是否放入atleastOnceEventCache中发送
        }
        catch (ProtocolException e)
        {
            e.printStackTrace();
        }
    }

    public void afterPropertiesSet() throws Exception
    {}

    protected synchronized void handleEvent(MissionRuntime<String, Object> runtime)
    {
        String executedStatus = runtime.getExecutedStatus();
        if ((executedStatus != null) && (!executedStatus.equalsIgnoreCase(MissionRuntime.EXECUTED_STATUS_500)))
        {
            DeviceStatus hubStatus = new DeviceStatus();
            hubStatus.setStatusCode(executedStatus);
            hubStatus.setLastUpdate(System.currentTimeMillis());
            String deviceConfigId = engineContext.getDeviceConfigId();
            String hubDeviceId = Utils.genDeviceId(deviceConfigId, Constants.HUB_DEVICE);
            deviceStatusCache.updateStatus(hubDeviceId, hubStatus);
            if (!executedStatus.equalsIgnoreCase(MissionRuntime.EXECUTED_STATUS_400))
            {
                Map<String, String> deviceEvent = (Map)runtime.getDeviceEvent();
                if ((deviceEvent != null) && (!deviceEvent.isEmpty()))
                {
                    long eventTime = runtime.getDeviceEventTime();
                    Iterator iter = deviceEvent.keySet().iterator();

//                    int ij=1;
                    while (iter.hasNext())
                    {
                        String deviceProtocolId = (String)iter.next();
                        String deviceProtocolStatus = (String)deviceEvent.get(deviceProtocolId);
                        String deviceConfigStatus = engineContext.getDeviceConfigStatus(deviceProtocolStatus);

//                        if(ij==1){
//                            ij=0;
//                            //System.out.println(deviceProtocolId);
//                            deviceConfigStatus="119";
//                        }


                        String deviceId = Utils.genDeviceId(deviceConfigId, deviceProtocolId);
                        DeviceStatus oldStatus = deviceStatusCache.queryStatus(deviceId);
                        String oldStatusCode = "";

                        boolean trigger = false;
                        boolean periodisend = false;
                        //System.out.println(deviceConfigStatus);
                        //System.out.println("ResponseEvenntHandle");
                        if (oldStatus != null)
                        {
                            oldStatusCode = oldStatus.getStatusCode();
                            long oldtime =  oldStatus.getLastUpdate();
                            if (!deviceConfigStatus.equalsIgnoreCase(oldStatusCode))
                            {
                                trigger = true;

                                //当状态发生改变的时候，需要：
                                //new一个设备的状态信息对象，然后更新put进deviceStatusCache中
                                DeviceStatus deviceStatus = new DeviceStatus();
                                deviceStatus.setStatusCode(deviceConfigStatus);
                                deviceStatus.setLastUpdate(System.currentTimeMillis());
                                deviceStatusCache.updateStatus(deviceId, deviceStatus);

                            }
                            else
                            {
                                long diff=((System.currentTimeMillis())-oldtime) / 1000L;
                                long periodiInternal = engineContext.getPeriodicInternal();
                                //System.out.println(periodiInternal);
                                if(diff >= periodiInternal)
                                {
                                    trigger = true;
                                    periodisend = true;
                                }

                                  //当状态没有改变的时候，不需要：
                                  //new一个设备的状态信息对象，然后更新put进deviceStatusCache中
//                                DeviceStatus deviceStatus = new DeviceStatus();
//                                deviceStatus.setStatusCode(deviceConfigStatus);
//                                deviceStatus.setLastUpdate(System.currentTimeMillis());
//                                deviceStatusCache.updateStatus(deviceId, deviceStatus);

                            }
                        }
                        else {
                            trigger = true;

                            //当oldStatus为空，初始状态下，需要：
                            //new一个设备的状态信息对象，然后更新put进deviceStatusCache中
                            DeviceStatus deviceStatus = new DeviceStatus();
                            deviceStatus.setStatusCode(deviceConfigStatus);
                            deviceStatus.setLastUpdate(System.currentTimeMillis());
                            deviceStatusCache.updateStatus(deviceId, deviceStatus);

                        }



                        ExtendedEvent extendedEvent = new ExtendedEvent();
                        extendedEvent.setEventId(Utils.uuid());
                        extendedEvent.setDeviceConfigId(deviceConfigId);
                        extendedEvent.setDeviceProtocolId(deviceProtocolId);
                        extendedEvent.setTimestamp(Long.valueOf(eventTime));
                        Map<CharSequence, CharSequence> values = new HashMap();
                        //000是在这里放进去的
                        values.put(Constants.DEVICE_STATUS_CODE, deviceConfigStatus);
                        extendedEvent.setValues(values);

                        if (!deviceConfigStatus.equalsIgnoreCase(Constants.DEVICE_STATUS_200))
                        {
                            if (fileLogger != null)
                            {
                                fileLogger.info(GsonUtils.getDefaultGson().toJson(extendedEvent));
                            }

                            if (trigger)
                            {
                                if (consoleLogger != null) {
                                    consoleLogger.debug(com.micer.core.utils.DatetimeUtils.toDateTimeString(new Date()) + " : " + extendedEvent
                                            .getEventId() + " : " + extendedEvent.getValues());
                                }

                                extendedEvent.setLastsent(System.currentTimeMillis());
                                if(periodisend)
                                {
                                    extendedEvent.plusOneSend();
                                }
                                atleastOnceEventCache.put(extendedEvent);
                                //System.out.println("hahahaha"+" "+extendedEvent);
                            }
                        }
                    }
                }
            }
        }
    }

}
