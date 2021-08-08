package me.afoolslove.metalmaxre.editor.palette;

import me.afoolslove.metalmaxre.ColorTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 调色板
 * <p>
 * 只能设置三种颜色，第一个颜色固定为黑色
 *
 * @author AFoolLove
 */
public class Palette {
    /**
     * 只能设置后三种颜色，第一种颜色固定为黑色，4byte
     */
    byte[] colors;

    /**
     * 有效为 4byte
     * 小于4byte使用黑色（0x0F）填充，0x00强制设置为黑色（0x0F）
     */
    public Palette(byte[] colors) {
        setColors(colors);
    }

    public Palette(@NotNull ByteBuffer buffer, int bufferPosition) {
        this.colors = new byte[4];
        // 固定为黑色
        this.colors[0x00] = 0x0F;
        // 读取三种颜色
        for (int i = 0x01; i < 0x04; i++) {
            if (i > (buffer.capacity() - bufferPosition - 1)) {
                // 使用黑色填充
                this.colors[i] = 0x0F;
            } else {
                this.colors[i] = buffer.get(bufferPosition++);
            }
        }
    }

    public Palette(@NotNull ByteBuffer buffer) {
        this(buffer, buffer.position());
    }

    public Palette(byte colorA, byte colorB, byte colorC) {
        setColors(colorA, colorB, colorC);
    }

    public Palette(int colorA, int colorB, int colorC) {
        setColors(colorA, colorB, colorC);
    }

    public void setColors(byte[] colors) {
        this.colors = new byte[4];
        // 固定为黑色
        this.colors[0x00] = 0x0F;
        // 读取三种颜色
        for (int i = 0x01; i < 0x04; i++) {
            if (i > colors.length) {
                // 使用黑色填充
                this.colors[i] = 0x0F;
            } else {
                this.colors[i] = colors[i];
            }
        }
    }

    public void setColors(int colorA, int colorB, int colorC) {
        this.colors = new byte[4];
        // 固定为黑色
        this.colors[0x00] = 0x0F;
        // 设置三种颜色
        this.colors[0x01] = (byte) (colorA & 0xFF);
        this.colors[0x02] = (byte) (colorB & 0xFF);
        this.colors[0x03] = (byte) (colorC & 0xFF);
    }

    /**
     * @return 所有颜色，0x00 固定为 黑色（0x0F）
     */
    public byte[] getColors() {
        return colors;
    }

    /**
     * @return 指定位置的颜色
     */
    public byte getColor(@Range(from = 0x00, to = 0x03) int colorIndex) {
        return colors[colorIndex];
    }

    /**
     * @return Color对象的指定位置的颜色
     */
    public Color getToColor(@Range(from = 0x00, to = 0x03) int colorIndex) {
        return ColorTool.getColor(colors[colorIndex]);
    }

    /**
     * @return 转换为颜色
     */
    public Color toColor(@Range(from = 0x00, to = 0x03) int colorIndex) {
        return ColorTool.getColor(colorIndex);
    }

    /**
     * @return 转换为颜色
     */
    public Color[] toColors() {
        Color[] colors = new Color[0x04];
        colors[0x00] = ColorTool.BLACK;
        colors[0x01] = ColorTool.getColor(this.colors[0x01]);
        colors[0x02] = ColorTool.getColor(this.colors[0x02]);
        colors[0x03] = ColorTool.getColor(this.colors[0x03]);
        return colors;
    }

    @Override
    public String toString() {
        return String.format("Palette{colors=%s}", Arrays.toString(colors));
    }
}
