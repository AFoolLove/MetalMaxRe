package me.afoolslove.metalmaxre.editors.palette;

import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 * 调色板
 * <p>
 * 只能设置三种颜色，第一个颜色固定为黑色
 *
 * @author AFoolLove
 */
public class PaletteRow {
    /**
     * 只能设置后三种颜色，第一种颜色固定为黑色，4byte
     */
    private byte[] paletteRow;

    /**
     * 有效为 4byte
     * 小于4byte使用黑色（0x0F）填充，0x00强制设置为黑色（0x0F）
     */
    public PaletteRow(byte[] paletteRow) {
        setPaletteRow(paletteRow);
    }

    public PaletteRow(@NotNull RomBuffer buffer, int bufferPosition) {
        this.paletteRow = new byte[4];
        // 固定为黑色
        this.paletteRow[0x00] = 0x0F;
        // 读取三种颜色
        buffer.get(bufferPosition, this.paletteRow, 1, 3);
    }

    public PaletteRow(byte colorA, byte colorB, byte colorC) {
        setPaletteRow(colorA, colorB, colorC);
    }

    public PaletteRow(int colorA, int colorB, int colorC) {
        setPaletteRow(colorA, colorB, colorC);
    }

    public void setPaletteRow(byte[] paletteRow) {
        this.paletteRow = new byte[4];
        // 固定为黑色
        this.paletteRow[0x00] = 0x0F;
        // 读取三种颜色
        for (int i = 0x01; i < 0x04; i++) {
            if (i > paletteRow.length) {
                // 使用黑色填充
                this.paletteRow[i] = 0x0F;
            } else {
                this.paletteRow[i] = paletteRow[i];
            }
        }
    }

    public void setPaletteRow(int colorA, int colorB, int colorC) {
        this.paletteRow = new byte[4];
        // 固定为黑色
        this.paletteRow[0x00] = 0x0F;
        // 设置三种颜色
        this.paletteRow[0x01] = (byte) (colorA & 0xFF);
        this.paletteRow[0x02] = (byte) (colorB & 0xFF);
        this.paletteRow[0x03] = (byte) (colorC & 0xFF);
    }

    /**
     * @return 所有颜色，0x00 固定为 黑色（0x0F）
     */
    public byte[] getPaletteRow() {
        return paletteRow;
    }


    @Override
    public String toString() {
        return String.format("Palette{colors=%s}", Arrays.toString(paletteRow));
    }
}