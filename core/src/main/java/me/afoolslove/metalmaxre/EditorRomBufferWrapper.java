package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * 添加了编辑器支持的 {@link RomBuffer}
 * <p>
 * *因为position基本为同步方法，所以效率不佳
 */
public class EditorRomBufferWrapper extends RomBufferWrapper {
    private final IRomEditor editor;

    public EditorRomBufferWrapper(@NotNull RomBuffer romBuffer, @NotNull IRomEditor editor) {
        super(romBuffer);
        this.editor = editor;
    }

    public synchronized void get(byte[] bytes, int offset, int length) {
        get(editor.position(), bytes, offset, length);
        editor.offsetPosition(length);
    }

    public synchronized void get(byte[] bytes) {
        get(editor.position(), bytes);
        editor.offsetPosition(bytes.length);
    }

    public synchronized byte get() {
        final byte b = get(editor.position());
        editor.offsetPosition(1);
        return b;
    }

    public synchronized int getToInt() {
        final int b = getToInt(editor.position());
        editor.offsetPosition(1);
        return b;
    }

    public synchronized char getToChar() {
        final char b = getToChar(editor.position());
        editor.offsetPosition(1);
        return b;
    }

    public synchronized char getChar() {
        final char b = getChar(editor.position());
        editor.offsetPosition(2); // char占2字节
        return b;
    }

    public synchronized void put(byte[] bytes, int offset, int length) {
        put(editor.position(), bytes, offset, length);
        editor.offsetPosition(length);
    }

    public synchronized void put(byte[]... bytes) {
        put(editor.position(), bytes);

        int length = 0;
        for (byte[] aByte : bytes) {
            length += aByte.length;
        }
        editor.offsetPosition(length);
    }

    public synchronized void put(byte[] bytes) {
        put(editor.position(), bytes);
        editor.offsetPosition(bytes.length);
    }

    public synchronized void put(byte b) {
        put(editor.position(), b);
        editor.offsetPosition(1);
    }

    public synchronized void putInt(int n) {
        putInt(editor.position(), n);
        editor.offsetPosition(4); // int占4字节
    }

    public synchronized void putChar(char c) {
        putChar(editor.position(), c);
        editor.offsetPosition(2); // char占2字节
    }

    public synchronized void getWholeBytes(int offset, int length, byte[]... aaBytes) {
        getWholeBytes(editor.position(), offset, length, aaBytes);
        editor.offsetPosition(length * aaBytes.length);
    }

    public synchronized void putWholeBytes(int offset, int length, byte[]... aaBytes) {
        putWholeBytes(editor.position(), offset, length, aaBytes);
        editor.offsetPosition(length * aaBytes.length);
    }

    public void getAABytes(int offset, int length, byte[]... aaBytes) {
        getAABytes(editor.position(), offset, length, aaBytes);
        editor.offsetPosition(length * aaBytes.length);
    }

    public void putAABytes(int offset, int length, byte[]... aaBytes) {
        putAABytes(editor.position(), offset, length, aaBytes);
        editor.offsetPosition(length * aaBytes.length);
    }
}
