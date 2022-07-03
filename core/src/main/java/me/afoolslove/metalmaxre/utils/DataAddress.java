package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 数据地址
 *
 * @author AFoolLove
 */
public class DataAddress extends SingleMapEntry<Integer, Integer> {
    public enum Type {
        PRG,
        CHR
    }

    private Type type;

    private DataAddress(@NotNull Integer key, @Nullable Integer value) {
        super(key, value);
    }

    private DataAddress(@NotNull Type type, @NotNull Integer key, @Nullable Integer value) {
        super(key, value);
        this.type = type;
    }

    /**
     * key不能大于value
     *
     * @param key 需要设置的值
     * @return 返回null设置失败，否则返回被设置前的数据
     */
    @Override
    public Integer setKey(@NotNull Integer key) {
        if (getValue() != null) {
            if (key > getValue()) {
                return null;
            }
        }
        return super.setKey(key);
    }

    /**
     * value不能小于key
     *
     * @param value 需要设置的值
     * @return 返回null设置失败，否则返回被设置前的数据
     */
    @Override
    public Integer setValue(@Nullable Integer value) {
        if (value != null) {
            if (getKey() > value) {
                return null;
            }
        }
        return super.setValue(value);
    }

    /**
     * 设置地址的类型
     *
     * @param type 地址类型
     */
    public void setType(@NotNull Type type) {
        this.type = type;
    }

    /**
     * 获取地址的类型
     *
     * @return 地址类型
     */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * 设置起始地址
     *
     * @param startAddress 起始地址
     * @return 返回null设置失败，否则返回被设置前的数据
     */
    public Integer setStartAddress(int startAddress) {
        return setKey(startAddress);
    }

    /**
     * 获取起始地址
     *
     * @return 起始地址
     */
    public int getStartAddress() {
        return getKey();
    }

    /**
     * 获取起始地址偏移后的地址
     *
     * @return 起始地址偏移后的地址
     */
    public int getStartAddress(int offset) {
        return getStartAddress() + offset;
    }

    /**
     * 获取实际起始地址，包含头长度等
     *
     * @return 实际起始地址
     */
    public int getAbsStartAddress(@NotNull RomBuffer buffer) {
        return switch (getType()) {
            case PRG -> buffer.getHeader().getPrgRomStart(getStartAddress());
            case CHR -> buffer.getHeader().getChrRomStart(getStartAddress());
        };
    }

    /**
     * 设置结束地址
     *
     * @param endAddress 结束地址
     * @return 返回null设置失败，否则返回被设置前的数据
     */
    public Integer setEndAddress(int endAddress) {
        return setValue(endAddress);
    }

    /**
     * 获取结束地址
     *
     * @return 结束地址
     */
    public int getEndAddress() {
        return getValue();
    }

    /**
     * 获取结束地址偏移后的地址
     *
     * @return 结束地址偏移后的地址
     */
    public int getEndAddress(int offset) {
        return getValue() + offset;
    }

    /**
     * 获取实际结束地址，包含头长度等
     *
     * @return 实际结束地址
     */
    public int getAbsEndAddress(@NotNull RomBuffer buffer) {
        return switch (getType()) {
            case PRG -> buffer.getHeader().getPrgRomStart(getEndAddress());
            case CHR -> buffer.getHeader().getChrRomStart(getEndAddress());
        };
    }

    /**
     * 是否为有限的数据范围，即结束地址不为null
     *
     * @return 是否为有限的数据范围
     */
    public boolean isLimited() {
        return getValue() != null;
    }

    /**
     * 计算起始地址到结束的长度
     * <p>
     * *包含起始和结束地址
     *
     * @return 返回null为没有长度，否则返回起始地址到结束地址的长度
     */
    public Integer length() {
        if (!isLimited()) {
            return null;
        }
        return getValue() - getKey() + 1;
    }

    @Override
    public String toString() {
        if (isLimited()) {
            return String.format("%05X-%05X", getStartAddress(), getEndAddress());
        }
        return String.format("%05X-.....", getStartAddress());
    }

    public static DataAddress fromEnd(@NotNull DataAddress start, int end) {
        if (end < start.getEndAddress() + 1) {
            return new DataAddress(start.getType(), end, start.getEndAddress() + 1);
        }
        return new DataAddress(start.getType(), start.getEndAddress() + 1, end);
    }

    public static DataAddress from(@NotNull Type type, int start, int end) {
        if (end < start) {
            return new DataAddress(type, end, start);
        }
        return new DataAddress(type, start, end);
    }

    public static DataAddress from(@NotNull Type type, int start) {
        return new DataAddress(type, start, null);
    }

    public static DataAddress fromPRG(int start) {
        return new DataAddress(Type.PRG, start, null);
    }

    public static DataAddress fromPRG(int start, int end) {
        if (end < start) {
            return new DataAddress(Type.PRG, end, start);
        }
        return new DataAddress(Type.PRG, start, end);
    }

    public static DataAddress fromCHR(int start, int end) {
        if (end < start) {
            return new DataAddress(Type.CHR, end, start);
        }
        return new DataAddress(Type.CHR, start, end);
    }

    public static DataAddress fromCHR(int start) {
        return new DataAddress(Type.CHR, start, null);
    }

    public static DataAddress from(int start, int end) {
        return from(Type.PRG, start, end);
    }

    public static DataAddress from(int start) {
        return from(Type.PRG, start);
    }
}
