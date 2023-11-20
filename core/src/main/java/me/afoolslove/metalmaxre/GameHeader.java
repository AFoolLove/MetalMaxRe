package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 游戏头属性
 *
 * @author AFoolLove
 */
public class GameHeader {
    public static final int HEADER_LENGTH = 0x10;
    public static final int PRG_LENGTH = 0x4000;
    public static final int CHR_LENGTH = 0x2000;


    /**
     * NES头属性
     */
    private final byte[] header;

    private final Set<MapperChangeListener> mapperChangeListeners = new HashSet<>();
    private final Set<PrgRomChangeListener> prgRomChangeListeners = new HashSet<>();
    private final Set<ChrRomChangeListener> chrRomChangeListeners = new HashSet<>();
    private final Set<TrainerChangeListener> trainerChangeListeners = new HashSet<>();


    public GameHeader() {
        this.header = new byte[HEADER_LENGTH];

    }

    public GameHeader(byte[] header) {
        if (header == null || header.length < HEADER_LENGTH) {
            // 如果传入的头数据长度小于所需要的头数据长度，使用0x00填充
            byte[] temp = new byte[HEADER_LENGTH];
            if (header != null) {
                // 将数据复制到新的数组里
                System.arraycopy(header, 0, temp, 0, header.length);
            }
            this.header = temp;
        } else if (header.length == HEADER_LENGTH) {
            this.header = header;
        } else {
            // 传入的数据大于或等于所需的头数据长度
            this.header = Arrays.copyOf(header, HEADER_LENGTH);
        }
    }

    /**
     * mapper就不用改了吧？
     */
    public void setMapper(@Range(from = 0x00, to = 0xFF) int mapper) {
        byte oldValue = header[0x06];

        // 设置低位
        header[0x06] &= 0B1111_0000;
        header[0x06] |= (mapper & 0B0000_1111) << 4;
        // 设置高位
        header[0x07] &= 0B1111_0000;
        header[0x07] |= (mapper & 0B1111_0000);

        for (MapperChangeListener listener : mapperChangeListeners) {
            listener.onMapperChange(this, oldValue, header[0x06]);
        }
    }

    /**
     * 获取所有监听Mapper变更的监听器
     *
     * @return 所有监听Mapper变更的监听器
     */
    public Set<MapperChangeListener> getMapperChangeListeners() {
        return mapperChangeListeners;
    }

    /**
     * 设置 PRG ROM大小(KB)
     */
    public void setPrgRom(@Range(from = 0x00, to = 0xFF) int prgRom) {
        header[0x04] = (byte) (prgRom & 0xFF);
    }

    /**
     * 设置 CHR ROM大小(KB)
     */
    public void setChrRom(@Range(from = 0x00, to = 0xFF) int chrRom) {
        header[0x05] = (byte) (chrRom & 0xFF);
    }

    /**
     * 是否开启Trainer
     */
    public void setTrainer(boolean trainer) {
        boolean oldValue = isTrained();
        if (isTrained() != trainer) {
            if (trainer) {
                header[0x06] |= 0B0000_0100;
            } else {
                header[0x06] &= 0B1111_1011;
            }
        }

        for (TrainerChangeListener trainerChangeListener : trainerChangeListeners) {
            trainerChangeListener.onTrainerChange(this, oldValue, isTrained());
        }
    }

    public byte[] getHeader() {
        return header;
    }

    /**
     * @return 当前游戏的Mapper
     */
    public int getMapper() {
        return ((header[0x06] & 0B1111_0000) >> 4) + (header[0x07] & 0B1111_0000);
    }

    /**
     * 获取PRG ROM的大小(KB)
     * 得到实际位置需要 *16KB(0x4000(16384)) + 16(header)
     *
     * @return PRG ROM大小(KB)
     */
    public int getPrgRom() {
        return header[0x04] & 0xFF;
    }

    public int getLastPrgRom() {
        return getPrgRom() - 1;
    }

    public int getLastPrgRomLength() {
        return getLastPrgRom() * PRG_LENGTH;
    }

    /**
     * 获取PRG ROM所占用的字节数量
     *
     * @return PRG ROM的字节数量
     */
    public int getPrgRomLength() {
        return getPrgRom() * PRG_LENGTH;
    }

    /**
     * 获取CHR ROM的大小(KB)
     * 得到实际位置需要 *8KB(0x2000(8192)) + getPrgRom() * 16KB + 16(header)
     *
     * @return CHR ROM大小(KB)
     */
    public int getChrRom() {
        return header[0x05] & 0xFF;
    }


    /**
     * 获取CHR ROM所占用的字节数量
     *
     * @return CHR ROM的字节数量
     */
    public int getChrRomLength() {
        return getChrRom() * CHR_LENGTH;
    }

    /**
     * @return 是否已开启Trainer
     */
    public boolean isTrained() {
        return (header[0x06] & 0B0000_0100) != 0x00;
    }

    /**
     * @return PRG ROM 数据的起始位置（含 header、trainer
     */
    public int getPrgRomStart() {
        return HEADER_LENGTH + (isTrained() ? Trainer.TRAINER_LENGTH : 0x00000);
    }

    /**
     * @return PRG ROM 数据的结束位置（含 header、trainer
     */
    public int getPrgRomEnd() {
        return getChrRomStart() - 1;
    }

    /**
     * @return CHR ROM 数据的起始位置（含 header、trainer
     */
    public int getChrRomStart() {
        return getPrgRomLength() + getPrgRomStart();
    }

    /**
     * @return CHR ROM 数据的结束位置（含 header、trainer
     */
    public int getChrRomEnd() {
        return getChrRomStart() + getChrRomLength() - 1;
    }

    /**
     * @return PRG ROM 起始的偏移值
     */
    public int getPrgRomStart(int offset) {
        return getPrgRomStart() + offset;
    }

    /**
     * @return CHR ROM 起始的偏移值
     */
    public int getChrRomStart(int offset) {
        return getChrRomStart() + offset;
    }


    /**
     * 获取所有监听PrgRom大小变更的监听器
     *
     * @return 所有监听PrgRom大小变更的监听器
     */
    public Set<PrgRomChangeListener> getPrgRomChangeListeners() {
        return prgRomChangeListeners;
    }


    /**
     * 添加一个Mapper变更的监听器
     *
     * @param listener 被添加的监听器
     */
    public void addMapperChangeListener(@Nullable MapperChangeListener listener) {
        if (listener == null) {
            return;
        }
        getMapperChangeListeners().add(listener);
    }

    /**
     * 移除一个现有的Mapper变更的监听器
     *
     * @param listener 被移除的监听器
     */
    public void removeMapperChangeListener(@Nullable MapperChangeListener listener) {
        if (listener == null) {
            return;
        }
        getMapperChangeListeners().remove(listener);
    }

    /**
     * 添加一个PrgRom大小变更监听器
     *
     * @param listener 被添加的监听器
     */
    public void addPrgRomChangeListener(@Nullable PrgRomChangeListener listener) {
        if (listener == null) {
            return;
        }
        getPrgRomChangeListeners().add(listener);
    }

    /**
     * 移除一个监听PrgRom大小变更的监听器
     *
     * @param listener 被移除的监听器
     */
    public void removePrgRomChangeListener(@Nullable PrgRomChangeListener listener) {
        if (listener == null) {
            return;
        }
        getPrgRomChangeListeners().remove(listener);
    }

    /**
     * 获取所有监听ChrRom大小变更的监听器
     *
     * @return 所有监听ChrRom大小变更的监听器
     */
    public Set<ChrRomChangeListener> getChrRomChangeListeners() {
        return chrRomChangeListeners;
    }

    /**
     * 添加一个ChrRom大小变更的监听器
     *
     * @param listener 被添加的监听器
     */
    public void addChrRomChangeListener(@Nullable ChrRomChangeListener listener) {
        if (listener == null) {
            return;
        }
        getChrRomChangeListeners().add(listener);
    }

    /**
     * 移除一个监听ChrRom大小变更的监听器
     *
     * @param listener 被移除的监听器
     */
    public void removeChrRomChangeListener(@Nullable ChrRomChangeListener listener) {
        if (listener == null) {
            return;
        }
        getChrRomChangeListeners().remove(listener);
    }

    /**
     * 获取所有监听Trainer变更的监听器
     *
     * @return 所有监听Trainer变更的监听器
     */
    public Set<TrainerChangeListener> getTrainerChangeListeners() {
        return trainerChangeListeners;
    }

    /**
     * 添加一个Trainer变更的监听器
     *
     * @param listener 被添加的监听器
     */
    public void addTrainerChangeListener(@Nullable TrainerChangeListener listener) {
        if (listener == null) {
            return;
        }
        getTrainerChangeListeners().add(listener);
    }

    /**
     * 移除一个监听Trainer变更的监听器
     *
     * @param listener 被移除的监听器
     */
    public void removeTrainerChangeListener(@Nullable TrainerChangeListener listener) {
        if (listener == null) {
            return;
        }
        getTrainerChangeListeners().remove(listener);
    }

    @Override
    public String toString() {
        return String.format("header={mapper=%d,trainer=%b,prgRom=%02X,chrRom=%02X}", getMapper(), isTrained(), getPrgRom(), getChrRom());
    }

    /**
     * Mapper变更的监听器
     */
    @FunctionalInterface
    public interface MapperChangeListener {
        void onMapperChange(@NotNull GameHeader header, byte oldValue, byte newValue);
    }


    /**
     * PrgRom变更的监听器
     */
    @FunctionalInterface
    interface PrgRomChangeListener {
        void onPrgRomChange(@NotNull GameHeader header, byte oldValue, byte newValue);
    }

    /**
     * ChrRom变更的监听器
     */
    @FunctionalInterface
    interface ChrRomChangeListener {
        void onChrRomChange(@NotNull GameHeader header, byte oldValue, byte newValue);
    }

    /**
     * Trainer变更的监听器
     */
    @FunctionalInterface
    public interface TrainerChangeListener {
        void onTrainerChange(@NotNull GameHeader header, boolean oldValue, boolean newValue);
    }
}
