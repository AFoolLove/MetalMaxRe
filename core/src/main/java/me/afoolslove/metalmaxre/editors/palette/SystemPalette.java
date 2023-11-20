package me.afoolslove.metalmaxre.editors.palette;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 显示的颜色的调色板，在导出图片等可视化中使用
 * <p>
 * *不是游戏中的颜色，如果模拟器支持自定义调色板，可以导出为文件后加载
 *
 * @author AFoolLove
 */
public class SystemPalette {
    public static final int PALETTE_LENGTH = 8 * 8;
    public static final int PALETTE_BYTE_LENGTH = PALETTE_LENGTH * 3;

    public static final SystemPalette DEFAULT_SYSTEM_PALETTE = SystemPalette.fromIntColor(new int[]{
            0x585858, 0x00238C, 0x00139B, 0x2D0585, 0x5D0052, 0x7A0017, 0x7A0800, 0x5F1800, 0x352A00, 0x093900, 0x003F00, 0x003C22, 0x00325D, 0x000000, 0x000000, 0x000000,
            0xA1A1A1, 0x0053EE, 0x153CFE, 0x6028E4, 0xA91D98, 0xD41E41, 0xD22C00, 0xAA4400, 0x6C5E00, 0x2D7300, 0x007D06, 0x007852, 0x0069A9, 0x000000, 0x000000, 0x000000,
            0xFFFFFF, 0x1FA5FE, 0x5E89FE, 0xB572FE, 0xFE65F6, 0xFE6790, 0xFE773C, 0xFE9308, 0xC4B200, 0x79CA10, 0x3AD54A, 0x11D1A4, 0x06BFFE, 0x424242, 0x000000, 0x000000,
            0xFFFFFF, 0xA0D9FE, 0xBDCCFE, 0xE1C2FE, 0xFEBCFB, 0xFEBDD0, 0xFEC5A9, 0xFED18E, 0xE9DE86, 0xC7E992, 0xA8EEB0, 0x95ECD9, 0x91E4FE, 0xACACAC, 0x000000, 0x000000,
    });
    private final Color[] colors = new Color[PALETTE_LENGTH];

    public Color[] getColors() {
        return colors;
    }

    public Color getWhite() {
        return getColor(0x30);
    }

    public Color getBlack() {
        return getColor(0x0F);
    }

    /**
     * @return 指定颜色
     */
    public Color getColor(@Range(from = 0x00, to = 0x3F) int color) {
        return getColors()[color & 0B0011_1111];
    }

    /**
     * @return 指定颜色
     */
    public Color getColor(@Range(from = 0x00, to = 0x3F) byte color) {
        return getColors()[color & 0B0011_1111];
    }

    /**
     * 将调色板转换为字节数组，数组大小固定为 {@link SystemPalette#PALETTE_LENGTH}
     *
     * @return 调色板字节数组
     */
    public byte[] toBytes() {
        byte[] bytes = new byte[PALETTE_BYTE_LENGTH];
        for (int i = 0; i < PALETTE_LENGTH; i++) {
            Color color = colors[i];
            bytes[(i * 3)] = color.getRed();
            bytes[(i * 3) + 1] = color.getGreen();
            bytes[(i * 3) + 2] = color.getBlue();
        }
        return bytes;
    }

    /**
     * 将调色板保存到文件
     *
     * @param path 文件路径
     */
    public void toFile(@NotNull Path path) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            outputStream.write(toBytes());
            outputStream.flush();
        }
    }

    /**
     * 导入int数组类型的调色板数据
     *
     * @param colors 颜色
     * @return 调色板
     */
    public static SystemPalette fromIntColor(int[] colors) {
        SystemPalette palette = new SystemPalette();
        for (int i = 0; i < colors.length; i++) {
            palette.getColors()[i] = new Color(colors[i]);
        }
        return palette;
    }

    /**
     * 从文件中读取调色板数据
     *
     * @param path 文件路径
     * @return 调色板
     */
    public static SystemPalette fromFile(@NotNull Path path) throws IOException {
        return fromBytes(Files.readAllBytes(path));
    }

    /**
     * 从字节数组中获取调色板数据
     *
     * @param paletteBytes 字节数组
     * @param offset       数组偏移量
     * @return 调色板
     */
    public static SystemPalette fromBytes(byte[] paletteBytes, int offset) {
        if ((paletteBytes.length - offset) != ((8 * 8) * 3)) {
            byte[] bytes = new byte[8 * 8];
            System.arraycopy(paletteBytes, offset, bytes, 0, Math.min(paletteBytes.length, 8 * 8));
            paletteBytes = bytes;
        }

        SystemPalette palette = new SystemPalette();
        for (int i = 0; i < paletteBytes.length; i += 3) {
            palette.getColors()[i / 3] = new Color(0x00, paletteBytes[i], paletteBytes[i + 1], paletteBytes[i + 2]);
        }
        return palette;
    }

    /**
     * 从字节数组中获取调色板数据
     *
     * @param paletteBytes 字节数组
     * @return 调色板
     */
    public static SystemPalette fromBytes(byte[] paletteBytes) {
        return fromBytes(paletteBytes, 0);
    }
}
