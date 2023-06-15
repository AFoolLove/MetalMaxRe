package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public byte[] toByteArray(@Nullable ICharMap charMap) {
        return outputStream.toByteArray();
    }

    @Override
    public String toText(@NotNull ICharMap charMap) {
        if (outputStream.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (byte b : toByteArray(charMap)) {
            builder.append(String.format("%02X", b));
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public int length(@Nullable ICharMap charMap) {
        return outputStream.size();
    }
}
