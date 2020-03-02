package com.micer.core.utils;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {

    public DateSerializer() {}

    public com.google.gson.JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(DatetimeUtils.dateTimeFormat.format(date));
    }
}
