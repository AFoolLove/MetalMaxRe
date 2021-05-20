package me.afoolslove.metalmaxre;

/**
 * 将数字解析为 NES能够正常识别的 byte
 *
 * @author AFoolLove
 */
public class NumberR {
    /**
     * 两个byte只需要互换
     */
    public static char parseChar(int number) {
        //
        return (char) (((number & 0xFF00) >>> 8) + ((number & 0xFF) << 8));
    }

    /**
     * 沙雕？
     */
    public static byte parseByte(int number) {
        // 沙雕？
        return (byte) number;
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
        return parseChar(parseChar(numberA) + (number ? numberB : parseChar(numberB)));
    }

    /**
     * 减法，numberA是否减去的是纯数字
     */
    public static char charSub(int numberA, int numberB, boolean number) {
        return parseChar(parseChar(numberA) - (number ? numberB : parseChar(numberB)));
    }

    /**
     * 减法，numberA减去numberB
     */
    public static char charSub(int numberA, int numberB) {
        return charSub(numberA, numberB, true);
    }

}
