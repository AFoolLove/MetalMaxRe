package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;

import java.io.ByteArrayOutputStream;

/**
 * 字节数组
 *
 * @author AFoolLove
 */
public class ByteArrayAction implements IBaseText {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(16);

    public ByteArrayAction() {
    }

    public ByteArrayAction(byte[] bytes) {
        outputStream.writeBytes(bytes);
    }

    /**
     * 获取使用的OutputStream
     *
     * @return OutputStream
     */
    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public byte[] toByteArray() {
        return outputStream.toByteArray();
    }

    @Override
    public String toText() {
        if (outputStream.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (byte b : toByteArray()) {
            builder.append(String.format("%02X", b));
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public int length() {
        return outputStream.size();
    }
}
