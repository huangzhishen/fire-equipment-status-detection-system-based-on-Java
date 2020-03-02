package com.micer.engine.codec.haiwan;

import com.google.common.io.BaseEncoding;
import com.micer.core.mission.Mission;
import com.micer.core.utils.HexUtils;

public class HaiwanMission extends Mission {

    private String request;
    private int expectedSize;

    public String getRequest()
    {
        return request;
    }

    public int getExpectedSize()
    {
        return expectedSize;
    }

    protected int exceptionSize = 5;
    protected String registerStart;

    public int getExceptionSize()
    {
        return exceptionSize;
    }

    public String getRegisterStart()
    {
        return registerStart;
    }

    public int getRegisterLength()
    {
        return registerLength;
    }

    public HaiwanMission(String missionId, String registerStart, int registerLength)
    {
        super(missionId);
        request = genRequest(registerStart, registerLength);
        expectedSize = (2 * registerLength + 5);
        this.registerStart = registerStart;
        this.registerLength = registerLength;
    }

    private String genRequest(String registerStartHex, int registerLength) {

        String request = null;
        byte[] datas = new byte[8];
        datas[0] = 1;
        datas[1] = 3;
        byte[] startRegisters = BaseEncoding.base16().decode(registerStartHex.toUpperCase());
        datas[2] = startRegisters[0];
        datas[3] = startRegisters[1];
        datas[4] = 0;
        datas[5] = ((byte)registerLength);
        int[] crc = com.micer.core.utils.ModbusRTUCRC16Utils.calculateCRC(datas, 0, 6);
        datas[6] = ((byte)crc[0]);
        datas[7] = ((byte)crc[1]);
        request = BaseEncoding.base16().encode(datas);
        return request;
    }

    public String addressHex(String registerStartHex, int registerLength)
    {
        Integer start = Integer.valueOf(Integer.parseInt(registerStart, 16));
        Integer end = Integer.valueOf(start.intValue() + registerLength);
        String endHex = Integer.toHexString(end.intValue());
        int length = endHex.length();
        int defaultLength = registerStartHex.length();
        int diff = defaultLength - length;
        return registerStartHex.substring(0, diff) + endHex;
    }

    protected int registerLength;
    public static void main(String[] args)
    {
        HaiwanMission mission = new HaiwanMission("test", "0000", 32);
        System.out.println(mission.getRequest());
        byte[] bytes = BaseEncoding.base16().decode(mission.getRequest());

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + ":");
            System.out.print(String.format("%8s", new Object[] { Integer.toBinaryString(bytes[i] & 0xFF) }).replace(' ', '0'));
            System.out.print(" ");
        }
        System.out.println(" ");

        Integer[] integers = HexUtils.HexString2Integers(mission.getRequest());
        for (int i = 0; i < integers.length; i++) {
            System.out.print(integers[i] + ":");
            System.out.print(String.format("%8s", new Object[] { Integer.toBinaryString(integers[i].intValue() & 0xFF) }).replace(' ', '0'));
            System.out.print(" ");
        }
        System.out.println(" ");
        Integer start = Integer.valueOf(Integer.parseInt("A5", 16));
        System.out.println(start);
    }
}
