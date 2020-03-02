package com.micer.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.micer.core.device.DeviceStatus;
import com.micer.core.event.ExtendedEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GsonUtils {
    public GsonUtils() {}

    public static Gson getDefaultGson()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = gsonBuilder.create();
        return gson;
    }

    public static Gson getPrettyGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson;
    }


    public static void main(String[] args)
    {
        try
        {
            ExtendedEvent extendedEvent = new ExtendedEvent();

            extendedEvent.setEventId(Utils.uuid());

            extendedEvent.setDeviceConfigId("gz0001");

            extendedEvent.setDeviceProtocolId("00000");

            extendedEvent.setTimestamp(Long.valueOf(System.currentTimeMillis()));

            extendedEvent.setLastsent(0L);

            Map<CharSequence, CharSequence> values = new HashMap();
            values.put(Constants.DEVICE_STATUS_CODE, "119");
            extendedEvent.setValues(values);

            System.out.println(getPrettyGson().toJson(extendedEvent));



            DeviceStatus deviceStatus = new DeviceStatus();

            deviceStatus.setStatusCode("0");
            deviceStatus.setLastUpdate(System.currentTimeMillis());
            System.out.println(getPrettyGson().toJson(deviceStatus));
        }
        catch (Exception localException) {}
    }
}
