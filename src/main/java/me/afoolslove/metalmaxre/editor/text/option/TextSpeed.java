package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.text.IBaseText;
import org.jetbrains.annotations.Range;

/**
 * 控制显示文本的速度
 * <p>
 * *全局有效，使用后务必恢复
 */
public class TextSpeed implements IBaseText {
    public static final TextSpeed RESET_TEXT_SPEED = new TextSpeed(0x00); // TODO: 初始速度未验证

    private final byte[] value = {(byte) 0xEE, 0x00};

    public TextSpeed(byte speed) {
        this.value[0x01] = speed;
    }

    public TextSpeed(@Range(from = 0x00, to = 0xFF) int speed) {
        this.value[0x01] = (byte) (speed & 0xFF);
    }

    @Override
    public byte[] toByteArray() {
        return value;
    }
}
