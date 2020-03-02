package com.micer.core.utils;

import com.google.common.io.BaseEncoding;


public class CRCFuanUtils {
    static final char[] TABLE1021;

    public static String CRC_XModem(final byte[] bytes, final int offset, final int len) {
        int crc = 0;
        final int polynomial = 4129;
        for (int index = offset; index < len; ++index) {
            final byte b = bytes[index];
            for (int i = 0; i < 8; ++i) {
                final boolean bit = (b >> 7 - i & 0x1) == 0x1;
                final boolean c15 = (crc >> 15 & 0x1) == 0x1;
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xFFFF;
        return Integer.toHexString(crc).toUpperCase();
    }

    public static String getCRC1021(final byte[] b, int len) {
        char crc = '\0';
        byte hb = 0;
        int j = 0;
        while (len-- > 0) {
            hb = (byte)(crc / '\u0100');
            final int index = (hb ^ b[j]) & 0xFF;
            crc <<= 8;
            crc ^= CRCFuanUtils.TABLE1021[index];
            ++j;
        }
        return Integer.toHexString(crc).toUpperCase();
    }

    public static void main(final String[] args) {
        final String requst2 = "F00014A5C20409018112091410032B00000100000000003AF4";
        final byte[] data2 = BaseEncoding.base16().decode((CharSequence)requst2);
        final String crc2 = CRC_XModem(data2, 1, data2.length - 2);
        System.out.println(crc2);
        final byte[] data3 = BaseEncoding.base16().decode((CharSequence)requst2.substring(2));
        final String crc3 = getCRC1021(data3, data3.length - 2);
        System.out.println(crc3);
    }

    static {
        TABLE1021 = new char[] { '\0', '\u1021', '\u2042', '\u3063', '\u4084', '\u50a5', '\u60c6', '\u70e7', '\u8108', '\u9129', '\ua14a', '\ub16b', '\uc18c', '\ud1ad', '\ue1ce', '\uf1ef', '\u1231', '\u0210', '\u3273', '\u2252', '\u52b5', '\u4294', '\u72f7', '\u62d6', '\u9339', '\u8318', '\ub37b', '\ua35a', '\ud3bd', '\uc39c', '\uf3ff', '\ue3de', '\u2462', '\u3443', '\u0420', '\u1401', '\u64e6', '\u74c7', '\u44a4', '\u5485', '\ua56a', '\ub54b', '\u8528', '\u9509', '\ue5ee', '\uf5cf', '\uc5ac', '\ud58d', '\u3653', '\u2672', '\u1611', '\u0630', '\u76d7', '\u66f6', '\u5695', '\u46b4', '\ub75b', '\ua77a', '\u9719', '\u8738', '\uf7df', '\ue7fe', '\ud79d', '\uc7bc', '\u48c4', '\u58e5', '\u6886', '\u78a7', '\u0840', '\u1861', '\u2802', '\u3823', '\uc9cc', '\ud9ed', '\ue98e', '\uf9af', '\u8948', '\u9969', '\ua90a', '\ub92b', '\u5af5', '\u4ad4', '\u7ab7', '\u6a96', '\u1a71', '\u0a50', '\u3a33', '\u2a12', '\udbfd', '\ucbdc', '\ufbbf', '\ueb9e', '\u9b79', '\u8b58', '\ubb3b', '\uab1a', '\u6ca6', '\u7c87', '\u4ce4', '\u5cc5', '\u2c22', '\u3c03', '\u0c60', '\u1c41', '\uedae', '\ufd8f', '\ucdec', '\uddcd', '\uad2a', '\ubd0b', '\u8d68', '\u9d49', '\u7e97', '\u6eb6', '\u5ed5', '\u4ef4', '\u3e13', '\u2e32', '\u1e51', '\u0e70', '\uff9f', '\uefbe', '\udfdd', '\ucffc', '\ubf1b', '\uaf3a', '\u9f59', '\u8f78', '\u9188', '\u81a9', '\ub1ca', '\ua1eb', '\ud10c', '\uc12d', '\uf14e', '\ue16f', '\u1080', '\u00a1', '\u30c2', '\u20e3', '\u5004', '\u4025', '\u7046', '\u6067', '\u83b9', '\u9398', '\ua3fb', '\ub3da', '\uc33d', '\ud31c', '\ue37f', '\uf35e', '\u02b1', '\u1290', '\u22f3', '\u32d2', '\u4235', '\u5214', '\u6277', '\u7256', '\ub5ea', '\ua5cb', '\u95a8', '\u8589', '\uf56e', '\ue54f', '\ud52c', '\uc50d', '\u34e2', '\u24c3', '\u14a0', '\u0481', '\u7466', '\u6447', '\u5424', '\u4405', '\ua7db', '\ub7fa', '\u8799', '\u97b8', '\ue75f', '\uf77e', '\uc71d', '\ud73c', '\u26d3', '\u36f2', '\u0691', '\u16b0', '\u6657', '\u7676', '\u4615', '\u5634', '\ud94c', '\uc96d', '\uf90e', '\ue92f', '\u99c8', '\u89e9', '\ub98a', '\ua9ab', '\u5844', '\u4865', '\u7806', '\u6827', '\u18c0', '\u08e1', '\u3882', '\u28a3', '\ucb7d', '\udb5c', '\ueb3f', '\ufb1e', '\u8bf9', '\u9bd8', '\uabbb', '\ubb9a', '\u4a75', '\u5a54', '\u6a37', '\u7a16', '\u0af1', '\u1ad0', '\u2ab3', '\u3a92', '\ufd2e', '\ued0f', '\udd6c', '\ucd4d', '\ubdaa', '\uad8b', '\u9de8', '\u8dc9', '\u7c26', '\u6c07', '\u5c64', '\u4c45', '\u3ca2', '\u2c83', '\u1ce0', '\u0cc1', '\uef1f', '\uff3e', '\ucf5d', '\udf7c', '\uaf9b', '\ubfba', '\u8fd9', '\u9ff8', '\u6e17', '\u7e36', '\u4e55', '\u5e74', '\u2e93', '\u3eb2', '\u0ed1', '\u1ef0' };
    }
}
