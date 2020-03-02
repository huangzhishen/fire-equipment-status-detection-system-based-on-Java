package com.micer.core.codec;

import com.micer.core.network.Connection;

import java.net.Socket;
import java.util.HashMap;

public class MissionRuntime<S, O> extends HashMap {
    public static String CONNECTION = "connection";
    public MissionRuntime() {}
    public static String SOCKET = "socket";
    public static String SOCKET_IP = "socket_ip";
    public static String SOCKET_PORT = "socket_port";
    public static String SOCKET_CON_TIMEOUT = "socket_con_timeout";
    public static String SOCKET_OP_TIMEOUT = "socket_op_timeout";
    public static String EXECUTED_STATUS = "executed_status";
    public static String EXECUTED_STATUS_200 = "200";
    public static String EXECUTED_STATUS_500 = "500";
    public static String EXECUTED_STATUS_400 = "400";
    public static String EXECUTED_RESULT = "executed_result";
    public static String DEVICE_EVENT = "device_event";
    public static String DEVICE_EVENT_TIME = "device_event_time";
    public String getExecutedStatus() {
        return (String)get(EXECUTED_STATUS); }

    public void putExecutedStatus(String statusCode)
    {
        put(EXECUTED_STATUS, statusCode);
    }

    public Object getExecutedResult() {
        return (String)get(EXECUTED_RESULT);
    }

    public void putExecutedResult(Object result) {
        put(EXECUTED_RESULT, result);
    }

    public long getDeviceEventTime() {
        return ((Long)get(DEVICE_EVENT_TIME)).longValue();
    }

    public void putDeviceEventTime(long eventTime) {
        put(DEVICE_EVENT_TIME, Long.valueOf(eventTime));
    }

    public Object getDeviceEvent() {
        return get(DEVICE_EVENT);
    }

    public void putDeviceEvent(Object deviceEvent) {
        put(DEVICE_EVENT, deviceEvent);
    }

    public Connection getConnection() {
        return (Connection)get(CONNECTION);
    }

    public void putConnection(Connection connection) {
        put(CONNECTION, connection);
    }

    public Socket getSocket() {
        return (Socket)get(SOCKET);
    }

    public void putSocket(Socket socket) {
        put(SOCKET, socket);
    }

    public String getSocketIp() {
        return (String)get(SOCKET_IP);
    }

    public void putSocketIp(String socketIp) {
        put(SOCKET_IP, socketIp);
    }

    public int getSocketPort() {
        return ((Integer)get(SOCKET_PORT)).intValue();
    }

    public void putSocketPort(String socketPort) {
        put(SOCKET_PORT, socketPort);
    }

    public int getSocketConTimeout() {
        return ((Integer)get(SOCKET_CON_TIMEOUT)).intValue();
    }

    public void putSocketConTimeout(String socketConTimeout) {
        put(SOCKET_CON_TIMEOUT, socketConTimeout);
    }

    public int getSocketOpTimeout() {
        return ((Integer)get(SOCKET_OP_TIMEOUT)).intValue();
    }

    public void putSocketOpTimeout(String socketOpTimeout) {
        put(SOCKET_OP_TIMEOUT, socketOpTimeout);
    }
}
