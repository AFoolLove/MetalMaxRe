package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;

public class ByteAction implements IBaseText {
    private final byte[] value = {0x00};

    public ByteAction(byte b) {
        setByte(b);
    }

    public byte getByte() {
        return value[0];
    }

    public void setByte(byte b) {
        value[0] = b;
    }

    @Override
    public byte[] toByteArray() {
        return value;
    }

    @Override
    public String toText() {
        return String.format("[%02X]", value[0]);
    }

    @Override
    public int length() {
        return 1;
    }
}
