package me.afoolslove.metalmaxre.editor.text;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

public class TextImpl extends BaseTextImpl implements CharSequence {
    private final char[] value; // 储存纯文本
    private final byte[] source; // 储存文本的细节，对话变量等

    public TextImpl(@NotNull char[] value, @NotNull byte[] source) {
        this.value = value;
        this.source = source;
    }

    public TextImpl(@NotNull String value, @NotNull byte[] source) {
        this.value = value.toCharArray();
        this.source = source;
    }

    public TextImpl(@NotNull String text) {
        this.value = text.toCharArray();
        this.source = WordBank.toBytes(text);
    }

    public TextImpl(@NotNull char[] text) {
        this.value = text;
        this.source = WordBank.toBytes(text);
    }

    public TextImpl(@NotNull byte[] source) {
        this.value = WordBank.toString(source).toCharArray(); // TODO: 应该解析为纯文本
        this.source = source;
    }

    @NotNull
    public byte[] getSource() {
        return source;
    }

    @Override
    public int length() {
        return value.length;
    }

    @Override
    public char charAt(int index) {
        return value[index];
    }

    @Override
    public boolean isEmpty() {
        return value.length == 0;
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return new TextImpl(Arrays.copyOfRange(value, start, end));
    }

    @NotNull
    @Override
    public IntStream chars() {
        return CharSequence.super.chars();
    }

    @NotNull
    @Override
    public IntStream codePoints() {
        return CharSequence.super.codePoints();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public byte[] toByteArray() {
        return getSource();
    }
}
