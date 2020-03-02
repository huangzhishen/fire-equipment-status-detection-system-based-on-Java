package com.micer.engine.codec.fuan;

import com.google.common.io.BaseEncoding;
import com.micer.core.codec.AbstractProtocol;
import com.micer.core.codec.MissionRuntime;
import com.micer.core.codec.ProtocolException;
import com.micer.core.event.ResponseEvent;
import com.micer.core.mission.Mission;
import com.micer.core.network.StreamConnection;
import com.micer.core.utils.CRCFuanUtils;
import com.micer.core.utils.HexUtils;
import com.micer.core.utils.Utils;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class Fuan001Protocol extends AbstractProtocol{
    private String xunjian = "F00001A2A699";


    private String reset = "F00001A196FA";


    private String answer = "F00003A200004820";


    private String repeat = "F00003A2A100CC3E";

    public Fuan001Protocol() {}

    public List<Mission> encodeMission(Map<String, Object> params) throws ProtocolException
    {
        List<Mission> missionList = new LinkedList();
        FuanMission mission = new FuanMission(Utils.uuid());
        missionList.add(mission);
        return missionList;
    }

    public ResponseEvent executeMission(Mission mission, MissionRuntime<String, Object> runtime)
            throws ProtocolException
    {
        ResponseEvent event = new ResponseEvent();

        try
        {
            StreamConnection connection = (StreamConnection)runtime.getConnection();


            Integer f0 = connection.read();

            Integer addr = Integer.valueOf(connection.read());

            Integer size = Integer.valueOf(connection.read());

            Integer cmd = Integer.valueOf(connection.read());

            Integer[] receiveDatas = new Integer[size.intValue() + 5];

            receiveDatas[0] = f0;
            receiveDatas[1] = addr;
            receiveDatas[2] = size;
            receiveDatas[3] = cmd;

            if (cmd.equals(Integer.valueOf(162))) //如果命令字为巡检，则需要回应
            {
                Integer[] sendDatas = HexUtils.HexString2Integers(answer);

                for (Integer data : sendDatas) {
                    connection.write(data.intValue());
                }
                connection.flush();
            } else if (!cmd.equals(Integer.valueOf(161)))//只要不是广播复位的命令，都正常接收
            {
                if (cmd.equals(Integer.valueOf(165))) //如果命令是发送事件,则继续接受数据
                {
                    for (int i = 0; i < size.intValue() - 1 + 2; i++) {
                        receiveDatas[(i + 4)] = Integer.valueOf(connection.read());
                    }
                }
            }

            String response = HexUtils.Integers2HexString(receiveDatas);
            runtime.putExecutedResult(response);
            event.setResponse(response);
            event.setMission(mission);

            if (checkCRC(response))
            {
                event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_200);
            }
            else
            {
                event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_400);
            }
        }
        catch (Exception e)
        {
            event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_500);
            e.printStackTrace();
            throw new ProtocolException("Execute mission error!", e);
        }
        return event;
    }

    public void decodeMission(Mission mission, MissionRuntime<String, Object> runtime) throws ProtocolException
    {
        try
        {
            String status = runtime.getExecutedStatus();
            if (status.equalsIgnoreCase(MissionRuntime.EXECUTED_STATUS_200))
            {
                String response = (String)runtime.getExecutedResult();
                Integer[] receiveDatas = HexUtils.HexString2Integers(response);
                Integer f0 = receiveDatas[0];
                Integer addr = receiveDatas[1];
                Integer size = receiveDatas[2];
                Integer cmd = receiveDatas[3];

                Map<String, String> deviceEventMap = new HashMap();

                if (cmd.equals(Integer.valueOf(165)))
                {
                    Integer line = receiveDatas[15];//回路号
                    String serial0 = response.substring(40, 41);
                    String serial1 = response.substring(42, 43);
                    String serial = serial1 + serial0;
                    String deviceProtocolId = genDeviceProtocolId(line, serial);

                    Integer deviceStatus = receiveDatas[4];//功能属性
                    deviceEventMap.put(deviceProtocolId, deviceStatus.toString());
                }

                runtime.putDeviceEventTime(System.currentTimeMillis());
                runtime.putDeviceEvent(deviceEventMap);
            }
        } catch (Exception e) {
            throw new ProtocolException("Decode mission error!", e);
        }
    }

    private String genDeviceProtocolId(Integer line, String serial)
    {
        String lineStr = line.toString();
        lineStr = StringUtils.leftPad(lineStr, 3, '0');

        String serialStr = new Integer(Integer.parseInt(serial, 16)).toString();
        serialStr = StringUtils.leftPad(serialStr, 3, '0');

        return lineStr + serialStr;
    }







    public static boolean checkCRC(String response)
    {
        byte[] datas = BaseEncoding.base16().decode(response.toUpperCase());
        int length = datas.length;
        int offset = length - 2;
        String newCRC = CRCFuanUtils.CRC_XModem(datas, 1, offset);

        String oldCRC = response.toUpperCase().substring(response.length() - 4);

        if (oldCRC.equals(newCRC)) {
            return true;
        }

        return false;
    }




    public static void main(String[] args)
    {
        Fuan001Protocol protocol = new Fuan001Protocol();

        String response = "F00014A5C20409018112091410032B00000100000000003AF4";

        System.out.println(checkCRC(response));







        FuanMission mission = new FuanMission(Utils.uuid());

        MissionRuntime<String, Object> runtime = new MissionRuntime();

        runtime.putExecutedStatus(MissionRuntime.EXECUTED_STATUS_200);
        runtime.putExecutedResult(response);
        try
        {
            protocol.decodeMission(mission, runtime);

            Map<String, String> deviceEventMap = (Map)runtime.getDeviceEvent();
            Iterator<String> events = deviceEventMap.keySet().iterator();
            while (events.hasNext()) {
                String deviceProtocolId = (String)events.next();
                String deviceStatus = (String)deviceEventMap.get(deviceProtocolId);

                System.out.println("DeviceEvent: " + deviceProtocolId + " : " + deviceStatus);
            }
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
    }
}
