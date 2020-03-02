package com.micer;

import com.micer.core.codec.MissionRuntime;
import com.micer.core.mission.Mission;
import com.micer.core.network.Connection;
import com.micer.core.network.TCPConnection;
import com.micer.engine.codec.haiwan.Haiwan001Protocol;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestHaiwanProtocol2 {

    public static void main(final String[] args) {
        final Logger netLogger = Logger.getLogger("networkFileLogger");
        final String host = "192.168.0.1";
        final int port = 8080;
        final int con_timeout = 3000;
        final int op_timeout = 3000;
        final TCPConnection connection = new TCPConnection(host, port, con_timeout, op_timeout);
        try {
            connection.newConnect();
            final MissionRuntime<String, Object> runtime = (MissionRuntime<String, Object>)new MissionRuntime();
            runtime.putConnection((Connection)connection);
            final Haiwan001Protocol protocol = new Haiwan001Protocol();
            long i = 0L;
            while (i < Long.MAX_VALUE) {
                try {
                    final Map<String, Object> params = new HashMap<String, Object>();
                    params.put("registerStart", "0000");
                    params.put("numbers", 32);
                    final List<Mission> list = (List<Mission>)protocol.encodeMission((Map)params);
                    for (final Mission mission : list) {
                        long before = System.currentTimeMillis();
                        protocol.executeMission(mission, (MissionRuntime)runtime);
                        long after = System.currentTimeMillis();
                        netLogger.debug((Object)(i + ": status: " + runtime.getExecutedStatus()));
                        netLogger.debug((Object)(i + ": result: " + runtime.getExecutedResult()));
                        before = System.currentTimeMillis();
                        protocol.decodeMission(mission, (MissionRuntime)runtime);
                        after = System.currentTimeMillis();
                        final String status = runtime.getExecutedStatus();
                        if (status.equalsIgnoreCase(MissionRuntime.EXECUTED_STATUS_200)) {
                            final Map<String, String> deviceEventMap = (Map<String, String>)runtime.getDeviceEvent();
                            for (final String deviceProtocolId : deviceEventMap.keySet()) {
                                final String deviceStatus = deviceEventMap.get(deviceProtocolId);
                                if (!deviceStatus.equalsIgnoreCase("0")) {
                                    netLogger.debug((Object)("DeviceEvent: " + deviceProtocolId + " : " + deviceStatus));
                                }
                            }
                        }
                    }
                    ++i;
                }
                catch (Exception e2) {
                    while (true) {
                        try {
                            do {
                                connection.newConnect();
                            } while (!connection.isActive());
                        }
                        catch (Exception ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            netLogger.debug((Object)e.getMessage());
        }
    }
}
