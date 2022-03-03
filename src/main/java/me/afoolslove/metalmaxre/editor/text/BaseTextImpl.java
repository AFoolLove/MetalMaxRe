package me.afoolslove.metalmaxre.editor.text;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class BaseTextImpl implements IBaseText {
    protected final byte[] value;
    protected final String type;
    protected byte priority = 0x00;

    protected BaseTextImpl(@NotNull String type, int length) {
        this.type = type;
        this.value = new byte[length];
    }

    protected BaseTextImpl(@NotNull String type, byte... bytes) {
        this.type = type;
        this.value = Arrays.copyOfRange(bytes, 0, bytes.length);
    }

    protected BaseTextImpl(@NotNull String type, int length, byte... bytes) {
        this.type = type;
        this.value = new byte[length];
        System.arraycopy(this.value, 0, bytes, 0, Math.min(length, bytes.length));
    }

    @Override
    public @NotNull String getType() {
        return type;
    }

    @Override
    public byte[] toByteArray() {
        return value;
    }

    @Override
    public BaseTextImpl priority(byte priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseTextImpl baseText)) return false;
        return Arrays.equals(value, baseText.value) && Objects.equals(getType(), baseText.getType());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toByteArray());
    }
}
