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
    private byte[] header;

    public GameHeader(ByteBuffer buffer) {
        header = new byte[HEADER_LENGTH];
        buffer.position(0x00);
        buffer.get(header);
    }

    public GameHeader(byte[] header) {
        setHeader(header);
    }

    public void setHeader(byte[] header) {
        this.header = Arrays.copyOf(header, Math.min(HEADER_LENGTH, header.length));
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
        if (isTrained() != trainer) {
            MetalMaxRe instance = MetalMaxRe.getInstance();
            byte[] oldGameBytes = instance.getBuffer().array();
            GameHeader oldHeader = new GameHeader(header);

            ByteBuffer buffer;
            if (trainer) {
                // 开启
                header[0x06] |= 0B0000_0100;
                // getChrRomEnd() + 1 可以获取头数据所对应的文件的总大小（不是真实大小）
                buffer = ByteBuffer.allocate(getChrRomEnd() + 1);
                // 写入头数据
                buffer.put(0x00000, header);
                // 跳过trainer
                buffer.position(HEADER_LENGTH + 0x200);
            } else {
                // 关闭
                header[0x06] &= 0B1111_1011;
                // getChrRomEnd() + 1 可以获取头数据所对应的文件的总大小（不是真实大小）
                buffer = ByteBuffer.allocate(getChrRomEnd() + 1);
                // 写入头数据
                buffer.put(0x00000, header);
            }

            // 写入PRG ROM
            buffer.put(oldGameBytes, oldHeader.getPrgRomStart(), oldHeader.getPrgRomLength());
            // 写入CHR ROM
            buffer.put(oldGameBytes, oldHeader.getChrRomStart(), oldHeader.getChrRomLength());

            // 应用新的Buffer
            instance.setBuffer(buffer);
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

    /**
     * 获取PRG ROM所占用的字节数量
     *
     * @return PRG ROM的字节数量
     */
    public int getPrgRomLength() {
        return getPrgRom() * 0x4000;
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
        return getChrRom() * 0x2000;
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
        return HEADER_LENGTH + (isTrained() ? 0x200 : 0x000);
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


}
