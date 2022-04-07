package me.afoolslove.metalmaxre.palette;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 调色板
 *
 * @author AFoolLove
 */
public class Palette {
    public static final int PALETTE_LENGTH = 8 * 8;
    public static final int PALETTE_BYTE_LENGTH = PALETTE_LENGTH * 3;
    private final Color[] colors = new Color[PALETTE_LENGTH];

    public Color[] getColors() {
        return colors;
    }

    /**
     * 将调色板转换为字节数组，数组大小固定为 {@link Palette#PALETTE_LENGTH}
     *
     * @return 调色板字节数组
     */
    public byte[] toBytes() {
        var bytes = new byte[PALETTE_BYTE_LENGTH];
        for (int i = 0; i < PALETTE_LENGTH; i++) {
            var color = colors[i];
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
        try (var outputStream = Files.newOutputStream(path)) {
            outputStream.write(toBytes());
            outputStream.flush();
        }
    }

    /**
     * 从文件中读取调色板数据
     *
     * @param path 文件路径
     * @return 调色板
     */
    public static Palette fromFile(@NotNull Path path) throws IOException {
        return fromBytes(Files.readAllBytes(path));
    }

    /**
     * 从字节数组中获取调色板数据
     *
     * @param paletteBytes 字节数组
     * @param offset       数组偏移量
     * @return 调色板
     */
    public static Palette fromBytes(byte[] paletteBytes, int offset) {
        if ((paletteBytes.length - offset) != ((8 * 8) * 3)) {
            var bytes = new byte[8 * 8];
            System.arraycopy(paletteBytes, offset, bytes, 0, Math.min(paletteBytes.length, 8 * 8));
            paletteBytes = bytes;
        }

        var palette = new Palette();
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
    public static Palette fromBytes(byte[] paletteBytes) {
        return fromBytes(paletteBytes, 0);
    }
}
