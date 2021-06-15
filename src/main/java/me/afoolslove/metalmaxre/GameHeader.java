package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 游戏头属性
 *
 * @author AFoolLove
 */
public class GameHeader {
    public static final int HEADER_LENGTH = 0x10;
    /**
     * 全部属性
     */
    public byte[] header;

    public GameHeader(ByteBuffer buffer) {
        header = new byte[HEADER_LENGTH];
        buffer.position(0x00);
        buffer.get(header);
    }

    public GameHeader(byte[] header) {
        this.header = Arrays.copyOf(header, header.length);
    }

    /**
     * mapper就不用改了吧？
     */
    public void setMapper(@Range(from = 0x00, to = 0xFF) int mapper) {
        // 设置低位
        header[0x06] &= 0B1111_0000;
        header[0x06] |= (mapper & 0B0000_1111) << 4;
        // 设置高位
        header[0x07] &= 0B1111_0000;
        header[0x07] |= (mapper & 0B1111_0000);
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
     * 是否开启作弊
     */
    public void setTrainer(boolean trainer) {
        if (trainer) {
            // 开启
            header[0x06] |= 0B0000_0100;
        } else {
            // 关闭
            header[0x06] &= 0B1111_1011;
        }
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
     * @return 是否已开启作弊
     */
    public boolean isTrained() {
        return (header[0x06] & 0B0000_0100) != 0x00;
    }

    /**
     * @return PRG ROM 数据的起始位置（含 header、trainer
     */
    public int getPrgRomStart() {
        return header.length + (isTrained() ? 0x200 : 0x000);
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
        return (getPrgRom() * 0x4000) + header.length;
    }

    /**
     * @return CHR ROM 数据的结束位置（含 header、trainer
     */
    public int getChrRomEnd() {
        return getChrRomStart() + (getChrRom() * 0x2000) - 1;
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


}
