package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * 数据地址
 *
 * @author AFoolLove
 */
public class DataAddress extends SingleMapEntry<Integer, Integer> implements Serializable {
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

    @Override
    public void set(Integer key, Integer value) {
        if (value != null) {
            // 需要先将值设置为null，否则在设置key时可能会因为验证问题设置失败
            setValue(null);
        }
        setKey(key);
        setValue(value);
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
        return getAbsStartAddress(buffer, 0);
    }

    /**
     * 获取实际起始地址，包含头长度等
     *
     * @return 实际起始地址
     */
    public int getAbsStartAddress(@NotNull RomBuffer buffer, int offset) {
        return switch (getType()) {
            case PRG -> buffer.getHeader().getPrgRomStart(getStartAddress(offset));
            case CHR -> buffer.getHeader().getChrRomStart(getStartAddress(offset));
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
        return getAbsEndAddress(buffer, 0);
    }

    /**
     * 获取实际结束地址，包含头长度等
     *
     * @return 实际结束地址
     */
    public int getAbsEndAddress(@NotNull RomBuffer buffer, int offset) {
        return switch (getType()) {
            case PRG -> buffer.getHeader().getPrgRomStart(getEndAddress(offset));
            case CHR -> buffer.getHeader().getChrRomStart(getEndAddress(offset));
        };
    }


    /**
     * 获取地址在bank中的偏移量
     *
     * @return 地址在bank中的偏移量
     */
    public int getBankOffset() {
        return getStartAddress() % switch (getType()) {
            case PRG -> 0x2000;
            case CHR -> 0x400;
        };
    }

    public int getBank() {
        return getStartAddress() / switch (getType()) {
            case PRG -> 0x2000;
            case CHR -> 0x400;
        };
    }

    public int getBankStartAddress() {
        return getStartAddress(-getBankOffset());
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

    /**
     * @return 判断value是否在范围内
     */
    public boolean range(int value) {
        return getStartAddress() <= value && getValue() != null && getEndAddress() >= value;
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

    public static DataAddress from(@NotNull Type type, @NotNull Integer start, @Nullable Integer end) {
        if (end == null) {
            return new DataAddress(type, start, null);
        } else if (end < start) {
            return new DataAddress(type, end, start);
        } else {
            return new DataAddress(type, start, end);
        }
    }

    public static DataAddress from(@NotNull Type type, int start) {
        return new DataAddress(type, start, null);
    }

    public static DataAddress fromPRG(int start) {
        return new DataAddress(Type.PRG, start, null);
    }

    public static DataAddress fromPRGLength(int start, int length) {
        return new DataAddress(Type.PRG, start, start + length);
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


    public static DataAddress fromCHRLength(int start, int length) {
        return new DataAddress(Type.CHR, start, start + length);
    }

    public static DataAddress from(int start, int end) {
        return from(Type.PRG, start, end);
    }

    public static DataAddress from(int start) {
        return from(Type.PRG, start);
    }
}
