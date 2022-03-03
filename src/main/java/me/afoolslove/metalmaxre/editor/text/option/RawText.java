package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.text.IBaseText;

/**
 * 源字符，原本为中文的字节可以以两个源符号的形式显示
 */
public class RawText implements IBaseText {
    private final byte[] value;

    public RawText(byte... raw) {
        this.value = new byte[1 + raw.length];
        this.value[0x00] = (byte) 0xF6;

        System.arraycopy(raw, 0, this.value, 1, raw.length);
    }

    public RawText(int... raw) {
        this.value = new byte[1 + raw.length];
        this.value[0x00] = (byte) 0xF6;

        for (int i = 0; i < raw.length; i++) {
            this.value[1 + i] = (byte) (raw[i] & 0xFF);
        }
    }

    @Override
    public byte[] toByteArray() {
        return value;
    }
}
