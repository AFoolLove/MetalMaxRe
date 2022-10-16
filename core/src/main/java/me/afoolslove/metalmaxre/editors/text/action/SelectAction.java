package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * 选择选项
 * <p>
 * 一个对话中只能使用一个，并且在文本的末尾，否则后面的文本无效
 *
 * @author AFoolLove
 */
public class SelectAction implements IBaseText {
    private final byte[] value = {0x00, 0x00, 0x00};

    /**
     * 进行选择的操作有着结束当前文本结束功能，所以优先级最高
     */
    @Override
    public byte priority() {
        return Byte.MAX_VALUE;
    }

    public SelectAction(@NotNull Byte yes, Byte no) {
        setYes(yes);
        if (no != null) {
            setNo(no);
        }
    }

    /**
     * 设置选择 是 时显示的文本索引
     *
     * @param yes 文本索引
     */
    public void setYes(byte yes) {
        value[0x01] = yes;
    }

    /**
     * 设置选择 否 时显示的文本索引
     *
     * @param no 文本索引
     */
    public void setNo(@Nullable Byte no) {
        if (no == null) {
            value[0x00] = (byte) 0xE3;
            return;
        }
        value[0x00] = (byte) 0xEB;
        value[0x02] = no;
    }

    /**
     * 选择 是 时显示的文本索引
     *
     * @return 文本索引
     */
    public byte getYes() {
        return value[0x01];
    }

    /**
     * 选择 否 时显示的文本索引
     *
     * @return 文本索引
     */
    public byte getNo() {
        return value[0x02];
    }

    @Override
    public byte[] toByteArray() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public String toText() {
        return String.format("[%02X%02X%02X]", value[0], value[1], value[2]);
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public String toString() {
        return String.format("yes=%02X,no=%02X", getYes(), getNo());
    }
}
