package com.micer.engine.codec.haiwan;

import com.google.common.io.BaseEncoding;
import com.micer.core.codec.AbstractModbusRTUProtocol;
import com.micer.core.codec.MissionRuntime;
import com.micer.core.codec.ProtocolException;
import com.micer.core.event.ResponseEvent;
import com.micer.core.mission.Mission;
import com.micer.core.utils.HexUtils;
import com.micer.core.utils.ModbusRTUCRC16Utils;
import com.micer.core.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Haiwan001Protocol extends AbstractModbusRTUProtocol {
    private int threshold = 32;

    Logger receiveDataLogger = Logger.getLogger("receiveDataLogger");
    Logger tcpConnLogger = Logger.getLogger("tcpConnFileLogger");
    Logger flowWriteLogger = Logger.getLogger("flowWriteFileLogger");
    Logger flowReadLogger = Logger.getLogger("flowReadFileLogger");

    public Haiwan001Protocol() {}

    public List<Mission> encodeMission(Map<String, Object> params)
            throws ProtocolException
    {
        try
        {
            List<Mission> missionList = new LinkedList();

            String registerStart = (String)params.get("registerStart");

            int numbers = ((Integer)params.get("numbers")).intValue();

            int set = numbers / threshold;

            int remain = numbers % threshold;

            HaiwanMission mission = null;

            String start = null;
            for (int i = 0; i < set; i++)
            {
                start = genHexAddress(registerStart, i * threshold);

                mission = new HaiwanMission(Utils.uuid(), start, threshold);
                missionList.add(mission);
            }

            if (remain > 0) {
                start = genHexAddress(registerStart, set * threshold);

                mission = new HaiwanMission(Utils.uuid(), start, remain);
                missionList.add(mission);
            }

            return missionList;
        }
        catch (Exception e) {
            throw new ProtocolException("Execute mission error!", e);
        }
    }

    public ResponseEvent executeMission(Mission mission, MissionRuntime<String, Object> runtime)
            throws ProtocolException
    {
        Socket socket = runtime.getSocket();
        ResponseEvent event = new ResponseEvent();
        String request = ((HaiwanMission)mission).getRequest();
        Integer[] sendDatas = HexUtils.HexString2Integers(request);
        int index = 0;
        try
        {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            for (Integer data : sendDatas) {
                dataOutputStream.write(data.intValue());
            }
            dataOutputStream.flush();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int registerSize = Integer.parseInt(request.substring(10, 12), 16);
            int responseSize = registerSize * 2 + 5;
            Integer[] receiveDatas = new Integer[responseSize];
            //System.out.println(123);
            //System.out.println(System.currentTimeMillis());
            while (index < responseSize)
            {
                receiveDatas[index] = dataInputStream.read();
                //System.out.println(Integer.valueOf(receiveDatas[index]));
                index++;
            }
            String response = HexUtils.Integers2HexString(receiveDatas);
            //System.out.println(response);
            runtime.putExecutedResult(response);
            event.setMission((HaiwanMission)mission);
            event.setResponse(response);
            if (checkCRC(response))
            {
                event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_200);
            }
            else
            {
                event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_400);
                receiveDataLogger.info("CRC error ");
            }
        }
        catch (InterruptedIOException e)
        {
            event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_500);
            throw new ProtocolException("Execute mission error! " + index, e);
        }
        catch (IOException e)
        {
            event.setExecutedStatus(MissionRuntime.EXECUTED_STATUS_500);
            throw new ProtocolException("Execute mission error! " + index, e);
        }
        try
        {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

    public void decodeMission(Mission mission, MissionRuntime<String, Object> runtime)
            throws ProtocolException
    {
        try
        {
            String status = runtime.getExecutedStatus();
            if (status.equalsIgnoreCase(MissionRuntime.EXECUTED_STATUS_200)) {
                String registerStart = ((HaiwanMission)mission).getRegisterStart();
                int registerLength = ((HaiwanMission)mission).getRegisterLength();
                String response = (String)runtime.getExecutedResult();
                Integer[] receiveDatas = HexUtils.HexString2Integers(response);
                Integer station = receiveDatas[0];
                Integer funCode = receiveDatas[1];
                String funCodeBinaryString = String.format("%8s", new Object[] { Integer.toBinaryString(funCode.intValue() & 0xFF) }).replace(' ', '0');

                Integer length = receiveDatas[2];

                Map<String, String> deviceEventMap = new HashMap();

                if (funCodeBinaryString.charAt(0) != '\001') {
                    int i = 0;
                    String deviceProtocolId = null;
                    while ((i < registerLength) && (i < 32))
                    {
                        int index = 2 * (i + 1) + 3 - 1;
                        String statusCode = receiveDatas[index].toString();
                        deviceProtocolId = genDeviceProtocolId(registerStart, i, Integer.valueOf(runtime.get("singleLoopSize").toString()).intValue());
                        deviceEventMap.put(deviceProtocolId, statusCode);
                        i++;
                    }
                }
                runtime.putDeviceEventTime(System.currentTimeMillis());
                runtime.putDeviceEvent(deviceEventMap);
            }
        }
        catch (Exception e)
        {
            throw new ProtocolException("Decode mission error!", e);
        }
    }

    private String genHexAddress(String registerStart, int offset)
    {
        Integer start = Integer.valueOf(Integer.parseInt(registerStart, 16));
        Integer current = Integer.valueOf(start.intValue() + offset);
        String currentHex = Integer.toHexString(current.intValue());
        currentHex = StringUtils.leftPad(currentHex, 4, '0');
        return currentHex;
    }









    private String genDeviceProtocolId(String registerStart, int offset, int singleLoopSize)
    {
        Integer start = Integer.valueOf(Integer.parseInt(registerStart, 16));
        Integer current = Integer.valueOf(start.intValue() + offset);




        int loop = current.intValue() / singleLoopSize;



        int order = current.intValue() % singleLoopSize + 1;

        return loop + "-" + order;
    }







    private boolean checkCRC(String response)
    {
        byte[] datas = BaseEncoding.base16().decode(response.toUpperCase());
        int length = datas.length;
        int offset = length - 2;
        int[] crc = ModbusRTUCRC16Utils.calculateCRC(datas, 0, offset);

        if (((byte)crc[0] == datas[(length - 2)]) && ((byte)crc[1] == datas[(length - 1)])) {
            return true;
        }

        return false;
    }

    public String currentTime() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }

    public static void main(String[] args)
    {
        Haiwan001Protocol protocol = new Haiwan001Protocol();

        System.out.println(protocol.genDeviceProtocolId("0000", 2, 240));
        System.out.println(protocol.genDeviceProtocolId("00E0", 25, 240));
    }
}
