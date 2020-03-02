package com.micer;

import com.google.common.io.BaseEncoding;
import com.micer.core.network.TimedSocket;
import com.micer.core.utils.HexUtils;
import com.micer.core.utils.ModbusRTUCRC16Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TestHaiwanTCPConnection {
    public TestHaiwanTCPConnection()
    {
    }

    public static void main(String args[])
    {
        Logger logger = Logger.getLogger("root");
        Logger receiveDataLogger = Logger.getLogger("receiveDataLogger");
        String host = "192.168.0.1";
        int port = 8080;
        int con_timeout = 3000;
        int op_timeout = 1000;
        Socket socket = null;
        int i = 0;
        do
        {
            List requestDataList = getRequestDataList(32);
            for(int j = 0; j < requestDataList.size(); j++)
            {
                String request = (String)requestDataList.get(j);
                Integer sendDatas[] = HexUtils.HexString2Integers(request);
                int index = 0;
                try
                {
                    socket = TimedSocket.getTCPSocket(host, port, con_timeout);
                    socket.setSoTimeout(op_timeout);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    Integer ainteger[] = sendDatas;
                    int responseSize = ainteger.length;
                    for(int k = 0; k < responseSize; k++)
                    {
                        Integer data = ainteger[k];
                        dataOutputStream.write(data.intValue());
                    }

                    dataOutputStream.flush();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    responseSize = 69;
                    if(j == requestDataList.size() - 1)
                        responseSize = 9;
                    Integer receiveDatas[] = new Integer[responseSize];
                    for(; index < responseSize; index++)
                        receiveDatas[index] = Integer.valueOf(dataInputStream.read());

                    String response = HexUtils.Integers2HexString(receiveDatas);
                    if(checkCRC(response))
                    {
                        receiveDataLogger.info(Integer.valueOf(i));
                        receiveDataLogger.info((new StringBuilder()).append(currentTime()).append(" request=").append(request).toString());
                        receiveDataLogger.info((new StringBuilder()).append(currentTime()).append(" response=").append(response).toString());
                    } else
                    {
                        receiveDataLogger.info("CRC error ");
                    }
                }
                catch(InterruptedIOException e2)
                {
                    System.out.println((new StringBuilder()).append("InterruptedIOException: ").append(i).append(" ").append(request).toString());
                    System.out.println((new StringBuilder()).append("index: ").append(index).toString());
                    e2.printStackTrace();
                }
                catch(IOException e2)
                {
                    System.out.println((new StringBuilder()).append("IOException: ").append(i).append(" ").append(request).toString());
                    e2.printStackTrace();
                }
                try
                {
                    socket.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                Thread.currentThread();
                Thread.sleep(20L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            i++;
        } while(true);
    }

    public static List getRequestDataList(int numbers)
    {
        List list = new LinkedList();
        String registerStart = "0000";
        int threshold = 32;
        int set = numbers / threshold;
        int remain = numbers % threshold;
        String start = null;
        for(int i = 0; i < set; i++)
        {
            start = genHexAddress(registerStart, i * threshold);
            String requestData = genRequest(start, threshold);
            list.add(requestData);
        }

        if(remain > 0)
        {
            start = genHexAddress(registerStart, set * threshold);
            String requestData = genRequest(start, remain);
            list.add(requestData);
        }
        return list;
    }

    private static String genRequest(String registerStartHex, int registerLength)
    {
        String request = null;
        byte datas[] = new byte[8];
        datas[0] = 1;
        datas[1] = 3;
        byte startRegisters[] = BaseEncoding.base16().decode(registerStartHex.toUpperCase());
        datas[2] = startRegisters[0];
        datas[3] = startRegisters[1];
        datas[4] = 0;
        datas[5] = (byte)registerLength;
        int crc[] = ModbusRTUCRC16Utils.calculateCRC(datas, 0, 6);
        datas[6] = (byte)crc[0];
        datas[7] = (byte)crc[1];
        request = BaseEncoding.base16().encode(datas);
        return request;
    }

    private static String genHexAddress(String registerStart, int offset)
    {
        Integer start = Integer.valueOf(Integer.parseInt(registerStart, 16));
        Integer current = Integer.valueOf(start.intValue() + offset);
        String currentHex = Integer.toHexString(current.intValue());
        currentHex = StringUtils.leftPad(currentHex, 4, '0');
        return currentHex;
    }

    private static boolean checkCRC(String response)
    {
        byte datas[] = BaseEncoding.base16().decode(response.toUpperCase());
        int length = datas.length;
        int offset = length - 2;
        int crc[] = ModbusRTUCRC16Utils.calculateCRC(datas, 0, offset);
        return (byte)crc[0] == datas[length - 2] && (byte)crc[1] == datas[length - 1];
    }

    public static String currentTime()
    {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }
}
