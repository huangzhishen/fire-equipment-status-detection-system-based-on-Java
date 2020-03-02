package com.micer.engine.executor.worker;

import com.micer.core.codec.Protocol;
import com.micer.core.codec.ProtocolException;
import com.micer.core.device.DeviceStatus;
import com.micer.core.event.ResponseEvent;
import com.micer.core.mission.Mission;
import com.micer.core.network.Connection;
import com.micer.core.network.TimedSocket;
import com.micer.core.utils.Constants;
import com.micer.engine.context.EngineContext;
import com.micer.core.codec.MissionRuntime;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HubOrientedExecutorWorker extends AbstractExecutorWorker{
    protected Connection connection;
    private Protocol protocol;

    public HubOrientedExecutorWorker() {}

    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }


    public void setProtocol(Protocol protocol)
    {
        this.protocol = protocol;
    }


    /**
     * 功能
     * 1、完成一次主机的所有设备检查。一次encodeMission，多次executeMission
     */
    protected void doJob()
    {
        while(true)
        {
            List<Mission> missionList = null;
            Map<String, Object> params = new HashMap();
            params.put("registerStart", engineContext.getRegisterStart());//取engine.properties属性文件中的寄存器起始地址
            params.put("numbers", Integer.valueOf(engineContext.getRegisterNumbers()));//取engine.properties属性文件中的寄存器数量
            try
            {
                missionList = protocol.encodeMission(params);//encodeMission方法的参数是一个Map,返回值是一个List
            }
            catch (ProtocolException e1) {
                e1.printStackTrace();
            }
            String host = engineContext.getConIp();
            int port = engineContext.getConPort();
            int con_timeout = engineContext.getConConTimeout();
            int op_timeout = engineContext.getConOpTimeout();
            Socket socket = null;
            for (Mission mission : missionList)
            {
                try {
                    socket = TimedSocket.getTCPSocket(host, port, con_timeout);
                    socket.setSoTimeout(op_timeout);
                }
                catch (InterruptedIOException e2) {
                    e2.printStackTrace();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }

                MissionRuntime<String, Object> runtime = new MissionRuntime();//MissionRuntime是Map的定制化子类
                runtime.put(MissionRuntime.SOCKET, socket);

                try
                {
                    ResponseEvent event = protocol.executeMission(mission, runtime);//executeMission方法的参数是一个Map,一个Mission对象，返回值是一个Event对象
                    responseEventCache.put(event);//responseEventCache在父类AbstractExecutorWorker中定义
                    //System.out.println(event);
                }
                catch (ProtocolException e1)
                {
                    e1.printStackTrace();
                }
            }
            try
            {
                Thread.currentThread();
                Thread.sleep(3000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

      }





    private List<Mission> handleEncodeMission()
            throws ProtocolException
    {
        Map<String, Object> params = new HashMap();
        params.put("registerStart", engineContext.getRegisterStart());
        params.put("numbers", Integer.valueOf(engineContext.getRegisterNumbers()));
        List<Mission> missionList = protocol.encodeMission(params);
        return missionList;
    }
    private void handleTransaction(Mission mission, MissionRuntime<String, Object> runtime)
            throws ProtocolException
    {
        String host = engineContext.getConIp();
        int port = engineContext.getConPort();
        int con_timeout = engineContext.getConConTimeout();
        int op_timeout = engineContext.getConOpTimeout();
        protocol.executeMission(mission, runtime);
    }

    private void handleAnalysis(Mission mission, MissionRuntime<String, Object> runtime)
            throws ProtocolException
    {
        protocol.decodeMission(mission, runtime);
    }

    protected void handleReConnectionFail(long timestamp)
    {
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setStatusCode(Constants.NET_CONN_STATUS_FAIL);
        deviceStatus.setLastUpdate(timestamp);
        deviceStatusCache.updateStatus(Constants.NET_CONN, deviceStatus);
    }

    public static String currentTime() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }
}
