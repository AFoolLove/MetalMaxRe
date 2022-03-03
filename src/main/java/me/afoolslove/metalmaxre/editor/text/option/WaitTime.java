package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.text.IBaseText;
import org.jetbrains.annotations.Range;

/**
 * 在对话（或其它）时，添加等待时间
 */
public class WaitTime implements IBaseText {
    private final byte[] time = {(byte) 0xF1, 0x3C};

    public WaitTime() {
    }

    public WaitTime(byte time) {
        this.time[0x01] = time;
    }

    public WaitTime(@Range(from = 0x00, to = 0xFF) int time) {
        this.time[0x01] = (byte) (time & 0xFF);
    }

    @Override
    public byte[] toByteArray() {
        return time;
    }
}
