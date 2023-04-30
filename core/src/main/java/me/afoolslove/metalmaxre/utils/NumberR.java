package me.afoolslove.metalmaxre.utils;

import org.jetbrains.annotations.Range;

/**
 * 将数字解析为 NES能够识别的 byte
 * （就是倒序byte）
 *
 * @author AFoolLove
 */
public class NumberR {

    public static String toHex(byte value) {
        return String.format("%02X", value);
    }

    public static String toHex(int len, byte value) {
        return String.format(String.format("%%0%dX", len), value);
    }

    public static String toHex(char value) {
        return String.format("%04X", (int) value);
    }

    public static String toHex(int len, char value) {
        return String.format(String.format("%%0%dX", len), value);
    }

    public static String toHex(int value) {
        return toHex(2, value);
    }

    public static String toHex(int len, int value) {
        return String.format(String.format("%%0%dX", len), value);
    }

    /**
     * 获取int中指定位置的byte
     * <p>
     * e.g: at(0x10F001, 1) = (byte) 0xF0
     *
     * @param value 被获取的值
     * @param index 索引
     * @return 获取的值
     */
    public static byte at(int value, @Range(from = 0, to = 3) int index) {
        if (index == 0) {
            return toByte(value);
        }
        index *= 8; // 8 bit = 1 byte
        value &= (0xFF << index); // 保留需要获取的8bit，其它bit置零
        return (byte) (value >>> index); // 将保留的8bit右位移，转换为byte
    }

    /**
     * 获取int中指定bit位和长度的数据
     *
     * @param value  被获取的值
     * @param index  bit位索引
     * @param length 获取的bit长度
     * @param reset  是否将值右位移到D0开始
     * @return 获取的值
     */
    public static int at(int value, int index, int length, boolean reset) {
        if (value == 0 || length <= 0 || index < (length - 1)) {
            return 0;
        }

        int fill = 0xFF_FF_FF_FF;
        fill >>>= 32 - length;        // 0B1111_1111 > 0B0000_1111

        fill <<= index - length + 1;  // 0B0000_1111 > 0B1111_0000
        value &= fill;                // 0B1111_0000 = at value
        if (!reset) {
            return value;
        }
        return value >>> index - length + 1; // value: 0B1111_0000 > 0B0000_1111
    }

    public static void main(String[] args) {
        int n = at(0B0101_0001, 6, 3, false);
        System.out.println(n);
    }

    /**
     * 将byte数组转换为int
     * <p>
     * e.g: toInt(false, 1,2,3) = 0x030201
     * <p>
     * e.g: toInt(true, 1,2,3) = 0x010203
     *
     * @param reverse 倒序
     * @param bytes   被转换的byte
     * @return 数组转换的int
     */
    public static int toInt(boolean reverse, byte... bytes) {
        if (bytes.length == 0) {
            return 0;
        }
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            int index = reverse ? (bytes.length - 1 - i) : i;
            index *= 8; // 8bit = 1 byte
            value |= (bytes[i] & 0xFF) << index;
        }
        return value;
    }

    /**
     * 将byte数组转换为int
     * <p>
     * e.g: toInt(1,2,3) = 0x030201
     *
     * @param bytes 被转换的byte
     * @return 数组转换的int
     */
    public static int toInt(byte... bytes) {
        return toInt(false, bytes);
    }

    /**
     * 将byte数组转换为int
     * <p>
     * e.g: toInt(false, 1,2,3) = 0x030201
     * e.g: toInt(true, 1,2,3) = 0x010203
     *
     * @param reverse 倒序
     * @param bytes   被转换的byte
     * @return 数组转换的int
     */
    public static int toInt(boolean reverse, int... bytes) {
        if (bytes.length == 0) {
            return 0;
        }
        int value = 0;
        for (int i = 0, length = Math.min(bytes.length, 4); i < length; i++) {
            int index = reverse ? (length - 1 - i) : i;
            index *= 8; // 8bit = 1 byte
            value |= (bytes[i] & 0xFF) << index;
        }
        return value;
    }

    /**
     * 将byte数组转换为int
     * <p>
     * e.g: toInt(1,2,3) = 0x030201
     *
     * @param bytes 被转换的byte
     * @return 数组转换的int
     */
    public static int toInt(int... bytes) {
        return toInt(false, bytes);
    }

    /**
     * 只需要互换两个byte
     * <p>
     * e.g: toChar(0xF1F2) = 0xF2F1
     *
     * @return 互换后的char
     */
    public static char toChar(int number) {
        number &= 0x0000FFFF; // 转换为 0xFFFF
        // (number & 0x000000FF) << 16 = 0xFF << 16 = 0x00FF0000
        // number |= 0x00FF0000 = 0x00FFFF00
        number |= (number & 0x000000FF) << 16;
        // number >>> 8 = 0x00FFFF00 >>> 8 = 0x0000FFFF
        return (char) (number >>> 8);
    }

    /**
     * 转换为byte，丢弃其余数据
     */
    public static byte toByte(int number) {
        return (byte) (number & 0xFF);
    }

    /**
     * 加法，两个char相加
     */
    public static char charAdd(int numberA, int numberB) {
        return charAdd(numberA, numberB, true);
    }

    /**
     * 加法，char是否与纯数字相加
     */
    public static char charAdd(int numberA, int numberB, boolean number) {
        return toChar(toChar(numberA) + (number ? numberB : toChar(numberB)));
    }

    /**
     * 减法，numberA是否减去的是纯数字
     */
    public static char charSub(int numberA, int numberB, boolean number) {
        return toChar(toChar(numberA) - (number ? numberB : toChar(numberB)));
    }

    /**
     * 减法，numberA减去numberB
     */
    public static char charSub(int numberA, int numberB) {
        return charSub(numberA, numberB, true);
    }

    /**
     * 将int分解为1-4个byte，低位在前，高位在后
     *
     * @param number  被分解的数字
     * @param length  分解的长度
     * @param reverse 倒序，变为：高位在前，低位在后
     * @return 分解出的byte
     */
    public static byte[] toByteArray(int number, @Range(from = 1, to = 4) int length, boolean reverse) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = reverse ? (length - 1 - i) : i;
            bytes[i] = at(number, index);
        }
        return bytes;
    }

    /**
     * 将字节数组转换为十六进制文本
     */
    public static String toHexString(byte[] a) {
        if (a == null)
            return "[]";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(String.format("%02X", a[i]));
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    /**
     * 将数组中的Number以字节数组的方式转换为十六进制文本
     */
    public static String toHexString(Object[] a) {
        if (a == null)
            return "[]";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            if (a[i] instanceof Number n) {
                b.append(String.format("%02X", n.intValue() & 0xFF));
            } else {
                b.append("null");
            }
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    /**
     * 将字节数组转换为纯十六进制文本
     */
    public static String toPlainHexString(byte[] a) {
        if (a == null)
            return "";
        int iMax = a.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.format("%02X", a[i]));
            if (i == iMax)
                return b.toString();
        }
    }
}
