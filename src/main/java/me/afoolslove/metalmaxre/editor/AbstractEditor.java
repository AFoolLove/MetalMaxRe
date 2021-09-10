package me.afoolslove.metalmaxre.editor;

import me.afoolslove.metalmaxre.GameHeader;
import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 基本的编辑器功能
 * 所有操作不计算NES头0x10数据（并没有
 *
 * @author AFoolLove
 */
public abstract class AbstractEditor<T extends AbstractEditor<T>> {
    protected ByteBuffer buffer;
    protected final List<Listener<T>> listeners = new ArrayList<>();
    protected int bufferPosition;
    protected boolean enabled = true;


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

    /**
     * 启用/禁用 当前编辑器
     *
     * @param enabled 启用/禁用
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return 是否已启用当前当前编辑器
     */
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * @return 是否未启用当前当前编辑器
     */
    public boolean isNotEnabled() {
        return enabled;
    }

    /**
     * 设置为 PRG COM 的偏移量
     */
    public void setPrgRomPosition(int offset) {
        bufferPosition = getHeader().getPrgRomStart(offset);
    }

    /**
     * 设置为 CHR COM 的偏移量
     */
    public void setChrRomPosition(int offset) {
        bufferPosition = getHeader().getChrRomStart(offset);
    }

    public void setPosition(int position) {
        bufferPosition = position;
    }

    public List<Listener<T>> getListeners() {
        return listeners;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public byte get(@NotNull ByteBuffer buffer) {
        return buffer.get(bufferPosition++);
    }

    @SuppressWarnings("unchecked")
    public T get(@NotNull ByteBuffer buffer, byte[] dst) {
        buffer.get(bufferPosition, dst);
        bufferPosition += dst.length;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T get(@NotNull ByteBuffer buffer, byte[] dst, int offset, int length) {
        buffer.get(bufferPosition, dst, offset, length);
        bufferPosition += length;
        return (T) this;
    }

    public byte getChrRom(@NotNull ByteBuffer buffer, int offset) {
        return buffer.get(getHeader().getChrRomStart(offset));
    }

    public byte getPrgRom(@NotNull ByteBuffer buffer, int offset) {
        return buffer.get(getHeader().getPrgRomStart(offset));
    }

    @SuppressWarnings("unchecked")
    public T put(@NotNull ByteBuffer buffer, byte b) {
        buffer.put(bufferPosition, b);
        bufferPosition++;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T put(@NotNull ByteBuffer buffer, int b) {
        buffer.put(bufferPosition, (byte) (b & 0xFF));
        bufferPosition++;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T put(@NotNull ByteBuffer buffer, byte[] dst) {
        buffer.put(bufferPosition, dst);
        bufferPosition += dst.length;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T put(@NotNull ByteBuffer buffer, byte[] dst, int offset, int length) {
        buffer.put(bufferPosition, dst, offset, length);
        bufferPosition += length;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T put(@NotNull ByteBuffer buffer, int index, byte b) {
        buffer.put(index, b);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T put(@NotNull ByteBuffer buffer, int index, byte[] src) {
        buffer.put(index, src);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T putChrRom(@NotNull ByteBuffer buffer, int offset, byte b) {
        buffer.put(getHeader().getChrRomStart(offset), b);
        return (T) this;
    }

    public T putChrRom(@NotNull ByteBuffer buffer, int offset, int b) {
        return putChrRom(buffer, offset, (byte) (b & 0xFF));
    }

    @SuppressWarnings("unchecked")
    public T putPrgRom(@NotNull ByteBuffer buffer, int offset, byte b) {
        buffer.put(getHeader().getPrgRomStart(offset), b);
        return (T) this;
    }

    public T putPrgRom(@NotNull ByteBuffer buffer, int offset, int b) {
        return putPrgRom(buffer, offset, (byte) (b & 0xFF));
    }

    @SuppressWarnings("unchecked")
    public T putChar(@NotNull ByteBuffer buffer, char value) {
        buffer.putChar(bufferPosition, value);
        bufferPosition += 2;
        return (T) this;
    }

    /**
     * 获取一个字节并转换为 int
     *
     * @param buffer buffer
     * @return 转换为 int
     */
    public int getToInt(@NotNull ByteBuffer buffer) {
        return getAndInt(buffer, 0xFF);
    }

    /**
     * 获取一个字节并进行 & 后返回
     *
     * @param buffer buffer
     * @param and    & 的数据
     * @return 字节与 and & 后的数据
     */
    public int getAndInt(@NotNull ByteBuffer buffer, int and) {
        return buffer.get(bufferPosition++) & and;
    }

    /**
     * 跳过1个字节
     */
    public void skip() {
        bufferPosition++;
    }

    /**
     * 跳过指定长度的字节
     *
     * @param length 跳过的字节数量
     */
    public void skip(int length) {
        bufferPosition += length;
    }

    /**
     * @return 获取程序主体实例
     */
    @NotNull
    public static MetalMaxRe getMetalMaxRe() {
        return MetalMaxRe.getInstance();
    }

    /**
     * @return 获取头属性
     */
    @NotNull
    public static GameHeader getHeader() {
        return MetalMaxRe.getInstance().getHeader();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBuffer(), getListeners());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEditor<?> editor)) {
            return false;
        }
        return Objects.equals(getBuffer(), editor.getBuffer())
                && Objects.equals(getListeners(), editor.getListeners());
    }

    /**
     * 编辑器读写监听器
     *
     * @param <T> 编辑器
     */
    public interface Listener<T extends AbstractEditor<T>> {
        /**
         * 编辑器读取之前执行
         *
         * @param editor 编辑器
         */
        default void onReadBefore(@NotNull T editor) {
        }

        /**
         * 编辑器读取完毕之后执行
         *
         * @param editor 编辑器
         */
        default void onReadAfter(@NotNull T editor) {
        }

        /**
         * 编辑器写入之前执行
         *
         * @param editor 编辑器
         */
        default void onWriteBefore(@NotNull T editor) {
        }

        /**
         * 编辑器写入完毕后执行
         *
         * @param editor 编辑器
         */
        default void onWriteAfter(@NotNull T editor) {
        }
    }
}
