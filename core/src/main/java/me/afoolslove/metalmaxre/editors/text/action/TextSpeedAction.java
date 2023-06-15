package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * 文本显示的速度
 * <p>
 * *文本显示速度是全局性的，修改后要恢复为默认速度
 *
 * @author AFoolLove
 */
public class TextSpeedAction implements IBaseText {
    private final byte[] value = {(byte) 0xEE, 0x00};

    public TextSpeedAction(byte speed) {
        setSpeed(speed);
    }

    /**
     * 获取文本显示的速度
     *
     * @return 文本显示的速度
     */
    public byte getSpeed() {
        return value[0x01];
    }

    /**
     * 设置文本显示的速度
     *
     * @param speed 文本显示的速度
     */
    public void setSpeed(byte speed) {
        value[0x01] = speed;
    }

    @Override
    public byte[] toByteArray(@Nullable ICharMap charMap) {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public String toText(@Nullable ICharMap charMap) {
        return String.format("[%02X%02X]", value[0], value[1]);
    }

    @Override
    public int length(@Nullable ICharMap charMap) {
        return 2;
    }

    @Override
    public String toString() {
        return String.format("TextSpeed %02X", getSpeed());
    }
}
