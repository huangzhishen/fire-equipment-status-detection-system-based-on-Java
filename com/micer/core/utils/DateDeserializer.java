package com.micer.core.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

public class DateDeserializer implements com.google.gson.JsonDeserializer<Date>{
    public DateDeserializer() {}

    public Date deserialize(JsonElement dateStr, Type typeOfSrc, JsonDeserializationContext context)
    {
        try
        {
            return DatetimeUtils.dateTimeFormat.parse(dateStr.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
