package com.micer;

import com.micer.core.codec.MissionRuntime;
import com.micer.core.mission.Mission;
import com.micer.core.network.TCPConnection;
import com.micer.core.utils.HexUtils;
import com.micer.engine.codec.fuan.Fuan001Protocol;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestFuanProtocol {
    public TestFuanProtocol()
    {
    }

    public static void main(String args[])
    {
        Logger netLogger = Logger.getLogger("networkFileLogger");
        String host = "192.168.0.1";
        int port = 8080;
        int con_timeout = 3000;
        int op_timeout = 3000;
        String answer = "F00003A200004820";
        TCPConnection connection = new TCPConnection(host, port, con_timeout, op_timeout);
        try
        {
            connection.newConnect();
            MissionRuntime runtime = new MissionRuntime();
            runtime.putConnection(connection);
            Fuan001Protocol protocol = new Fuan001Protocol();

            Integer[] sendDatas1 = HexUtils.HexString2Integers(answer);

            for (Integer data : sendDatas1) {
                connection.write(data.intValue());
            }
            connection.flush();

            long i = 0L;
            label0:
            do
            {
                if(i >= 0x7fffffffffffffffL)
                    break;
                i++;
                try
                {
                    Map params = new HashMap();
                    List list = protocol.encodeMission(params);
                    Iterator missions = list.iterator();
                    do
                    {
                        String status;
                        do
                        {
                            if(!missions.hasNext())
                                continue label0;
                            Mission mission = (Mission)missions.next();
                            long before = System.currentTimeMillis();
                            protocol.executeMission(mission, runtime);
                            long after = System.currentTimeMillis();
                            netLogger.debug(i + ": net: " + (after - before));
                            netLogger.debug(i + ": status: " + runtime.getExecutedStatus());
                            netLogger.debug(i + ": result: " + runtime.getExecutedResult());
                            before = System.currentTimeMillis();
                            protocol.decodeMission(mission, runtime);
                            after = System.currentTimeMillis();
                            netLogger.debug(i + ": pro: " + (after - before));
                            status = runtime.getExecutedStatus();
                        } while(!status.equalsIgnoreCase(MissionRuntime.EXECUTED_STATUS_200));
                        Map deviceEventMap = (Map)runtime.getDeviceEvent();
                        Iterator events = deviceEventMap.keySet().iterator();
                        while(events.hasNext())
                        {
                            String deviceProtocolId = (String)events.next();
                            String deviceStatus = (String)deviceEventMap.get(deviceProtocolId);
                            netLogger.debug("DeviceEvent: " + deviceProtocolId + " : " + deviceStatus);
                        }
                    } while(true);
                }
                catch(Exception e)
                {
                    do
                        try
                        {
                            do
                                connection.newConnect();
                            while(!connection.isActive());
                            break;
                        }
                        catch(Exception exception) { }
                    while(true);
                    netLogger.debug(e.getMessage());
                }
            } while(true);
        }
        catch(Exception e)
        {
            netLogger.debug(e.getMessage());
        }
    }
}
