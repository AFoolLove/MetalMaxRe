package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;

import java.util.Arrays;

/**
 * 会话时等待一段时间
 *
 * @author AFoolLove
 */
public class WaitTimeAction implements IBaseText {
    private final byte[] value = {(byte) 0xF1, 0x3C};

    public WaitTimeAction(byte time) {
        setTime(time);
    }

    public WaitTimeAction(int time) {
        setTime((byte) (time & 0xFF));
    }

    /**
     * 获取等待时间
     *
     * @return 等待的时间
     */
    public byte getTime() {
        return value[0x01];
    }

    /**
     * 设置等待时间
     *
     * @param time 等待时间
     */
    public void setTime(byte time) {
        value[0x01] = time;
    }

    /**
     * 恢复为默认等待时间
     */
    public void defaultTime() {
        setTime((byte) 0x3C);
    }

    @Override
    public byte[] toByteArray() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public String toText() {
        return String.format("[%02X%02X]", value[0], value[1]);
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public String toString() {
        return String.format("WaitTIme %02X", getTime());
    }
}
