package me.afoolslove.metalmaxre.utils;

public class ByteUtils {
    /**
     * 多个byte转换为byte数组
     */
    public static byte[] ofBytes(byte... bytes) {
        return bytes;
    }

    /**
     * 多个int转换为byte数组
     */
    public static byte[] ofBytes(int... bytes) {
        byte[] tmp = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            tmp[i] = (byte) (bytes[i] & 0xFF);
        }
        return tmp;
    }
}
