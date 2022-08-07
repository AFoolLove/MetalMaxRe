package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;

import java.util.Arrays;

/**
 * 空文本
 * <p>
 * 在不影响后面的文本（覆盖文字）的情况下，写入空文本（跳过些文本）
 * <p>
 * 用于排版或填充文本的字节（不能使用0x42的地方）
 *
 * @author AFoolLove
 */
public class SpaceAction implements IBaseText {
    private final byte[] value = {(byte) 0xED, 0x00};

    public SpaceAction(byte space) {
        setSpace(space);
    }

    /**
     * 获取空文本的长度
     *
     * @return 空文本的长度
     */
    public byte getSpace() {
        return value[0x01];
    }

    /**
     * 设置空文本的长度
     *
     * @param space 空文本的长度
     */
    public void setSpace(byte space) {
        value[0x01] = space;
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
    public String toString() {
        return String.format("Space %02X", getSpace());
    }
}
