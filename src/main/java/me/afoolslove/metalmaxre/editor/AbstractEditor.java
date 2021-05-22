package me.afoolslove.metalmaxre.editor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.nio.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 基本的编辑器功能
 * 所有操作不计算NES头0x10数据
 *
 * @author AFoolLove
 */
public abstract class AbstractEditor {
    /**
     * NES 头数据的长度
     */
    public static final int NES_HEADER_LENGTH = 0x10;


    protected ByteBuffer buffer;


    public AbstractEditor() {
        this.buffer = getMetalMaxRe().getBuffer();
    }

    /**
     * 读取数据时调用，会重复调用
     * 使用 Buffer 前请先定位数据
     */
    public abstract boolean onRead(@NotNull ByteBuffer buffer);


    /**
     * 写入数据时调用，会重复调用
     * 使用 Buffer 前请先定位数据
     */
    public abstract boolean onWrite(@NotNull ByteBuffer buffer);


    public ByteBuffer slice() {
        return buffer.slice();
    }

    public ByteBuffer duplicate() {
        return buffer.duplicate();
    }

    public ByteBuffer asReadOnlyBuffer() {
        return buffer.asReadOnlyBuffer();
    }

    public byte get() {
        return buffer.get();
    }

    public ByteBuffer put(byte b) {
        return buffer.put(b);
    }

    public byte get(int index) {
        return buffer.get(index);
    }

    public ByteBuffer put(int index, byte b) {
        return buffer.put(index, b);
    }

    public ByteBuffer compact() {
        return buffer.compact();
    }

    public boolean isDirect() {
        return buffer.isDirect();
    }

    public char getChar() {
        return buffer.getChar();
    }

    public ByteBuffer putChar(char value) {
        return buffer.putChar(value);
    }

    public char getChar(int index) {
        return buffer.getChar(index);
    }

    public ByteBuffer putChar(int index, char value) {
        return buffer.putChar(index, value);
    }

    public CharBuffer asCharBuffer() {
        return buffer.asCharBuffer();
    }

    public short getShort() {
        return buffer.getShort();
    }

    public ByteBuffer putShort(short value) {
        return buffer.putShort(value);
    }

    public short getShort(int index) {
        return buffer.getShort(index);
    }

    public ByteBuffer putShort(int index, short value) {
        return buffer.putShort(index, value);
    }

    public ShortBuffer asShortBuffer() {
        return buffer.asShortBuffer();
    }

    public int getInt() {
        return buffer.getInt();
    }

    public ByteBuffer putInt(int value) {
        return buffer.putInt(value);
    }

    public int getInt(int index) {
        return buffer.getInt(index);
    }

    public ByteBuffer putInt(int index, int value) {
        return buffer.putInt(index, value);
    }

    public IntBuffer asIntBuffer() {
        return buffer.asIntBuffer();
    }

    public long getLong() {
        return buffer.getLong();
    }

    public ByteBuffer putLong(long value) {
        return buffer.putLong(value);
    }

    public long getLong(int index) {
        return buffer.getLong(index);
    }

    public ByteBuffer putLong(int index, long value) {
        return buffer.putLong(index, value);
    }

    public LongBuffer asLongBuffer() {
        return buffer.asLongBuffer();
    }

    public float getFloat() {
        return buffer.getFloat();
    }

    public ByteBuffer putFloat(float value) {
        return buffer.putFloat(value);
    }

    public float getFloat(int index) {
        return buffer.getFloat(index);
    }

    public ByteBuffer putFloat(int index, float value) {
        return buffer.putFloat(index, value);
    }

    public FloatBuffer asFloatBuffer() {
        return buffer.asFloatBuffer();
    }

    public double getDouble() {
        return buffer.getDouble();
    }

    public ByteBuffer putDouble(double value) {
        return buffer.putDouble(value);
    }

    public double getDouble(int index) {
        return buffer.getDouble(index);
    }

    public ByteBuffer putDouble(int index, double value) {
        return buffer.putDouble(index, value);
    }

    public DoubleBuffer asDoubleBuffer() {
        return buffer.asDoubleBuffer();
    }

    public boolean isReadOnly() {
        return buffer.isReadOnly();
    }

    public ByteBuffer get(byte[] dst, int offset, int length) {
        return buffer.get(dst, offset, length);
    }

    public ByteBuffer get(byte[] dst) {
        return buffer.get(dst);
    }

    public ByteBuffer put(ByteBuffer src) {
        return buffer.put(src);
    }

    public ByteBuffer put(byte[] src, int offset, int length) {
        return buffer.put(src, offset, length);
    }

    public ByteBuffer position(int newPosition) {
        return buffer.position(newPosition);
    }

    public ByteBuffer limit(int newLimit) {
        return buffer.limit(newLimit);
    }

    public ByteBuffer mark() {
        return buffer.mark();
    }

    public ByteBuffer reset() {
        return buffer.reset();
    }

    public ByteBuffer clear() {
        return buffer.clear();
    }

    public ByteBuffer flip() {
        return buffer.flip();
    }

    public ByteBuffer rewind() {
        return buffer.rewind();
    }

    public int mismatch(ByteBuffer that) {
        return buffer.mismatch(that);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * @return 获取程序主体实例
     */
    @NotNull
    public static MetalMaxRe getMetalMaxRe() {
        return MetalMaxRe.getInstance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEditor)) {
            return false;
        }
        AbstractEditor that = (AbstractEditor) o;
        return Objects.equals(buffer, that.buffer);
    }


    public static <T> void limit(@NotNull Iterator<T> iterator, @NotNull Predicate<?> condition, @Nullable Predicate<T> removed) {
        while (condition.test(null) && iterator.hasNext()){
            T remove = iterator.next();
            iterator.remove();
            if (removed != null) {
                removed.test(remove);
            }
        }
    }

}
