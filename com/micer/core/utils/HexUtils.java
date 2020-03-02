package com.micer.core.utils;

import java.util.regex.Pattern;

public class HexUtils {

    public HexUtils() {}

    public static byte[] hexStringToByteArray(String s)
    {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = ((byte)v);
        }
        return b;
    }
    public static Integer[] HexString2Integers(String hex)
    {
        java.util.List<Integer> list = new java.util.ArrayList();
        Integer[] data = new Integer[2];
        Pattern p = Pattern.compile("(?i)[\\da-f]{2}");
        java.util.regex.Matcher m = p.matcher(hex);
        while (m.find()) {
            String str = m.group();
            list.add(Integer.valueOf(Integer.parseInt(str, 16)));
        }
        data = (Integer[])list.toArray(data);
        return data;
    }

    public static String Integers2HexString(Integer[] datas)
    {
        StringBuilder builder = new StringBuilder();
        for (Integer data : datas) {
            String hex = Integer.toHexString(data.intValue());
            if (hex.length() != 2)
                builder.append("0");
            builder.append(hex);
        }
        return builder.toString().toUpperCase();
    }
}
