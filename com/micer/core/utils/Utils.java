package com.micer.core.utils;

import java.io.File;

public class Utils {
    public Utils() {}

    public static String genDeviceId(String deviceConfigId, String deviceProtocolId) {
        return deviceConfigId + "-" + deviceProtocolId;
    }

    public static synchronized String uuid()
    {
        return java.util.UUID.randomUUID().toString().toLowerCase();
    }

    public static File getFile(String filePath) {
        java.net.URL url = Utils.class.getClassLoader().getResource(filePath);
        File file = new File(url.getFile());
        return file;
    }
}
