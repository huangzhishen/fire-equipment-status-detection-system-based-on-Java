package com.micer.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtils {
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

    public DatetimeUtils() {}

    public static String toDateTimeString(Date date) { return dateTimeFormat.format(date); }

    public static Date toDateTime(String dateString) throws ParseException
    {
        return dateTimeFormat.parse(dateString);
    }

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");

    public static String toDateString(Date date)
    {
        return dateFormat.format(date);
    }

    public static Date toDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}
