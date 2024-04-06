package me.afoolslove.metalmaxre.helper;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.MapBuilder;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.map.tileset.ITileSetEditor;
import me.afoolslove.metalmaxre.editors.map.tileset.TileAttributes;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.map.world.WorldMapEditorImpl;
import me.afoolslove.metalmaxre.editors.palette.Color;
import me.afoolslove.metalmaxre.editors.palette.IPaletteEditor;
import me.afoolslove.metalmaxre.editors.palette.PaletteRow;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class TileSetHelper {
    private TileSetHelper() {
    }

    /**
     * 将图块倒序
     * 0x00-0x07 倒序
     * 0x08-0x10 倒序
     * e.g:
     * 00 01 02 03 04 05 06 07 = 07 06 05 04 03 02 01 00
     * 08 09 0A 0B 0C 0D 0E 0F = 0F 0E 0D 0C 0B 0A 09 08
     *
     * @param tiles 长度必须为 0x10
     */
    public static void reverse(byte[] tiles) {
        byte temp;
        // 0x00-0x07 倒序
        for (int i = 0; i < 0x08 / 2; i++) {
            temp = tiles[i];
            tiles[i] = tiles[0x08 - i - 1];
            tiles[0x08 - i - 1] = temp;
        }

        // 0x08-0x10 倒序
        for (int i = 0x08; i < 0x08 + 0x08 / 2; i++) {
            temp = tiles[i];
            tiles[i] = tiles[0x10 - (i - 0x08) - 1];
            tiles[0x10 - (i - 0x08) - 1] = temp;
        }
    }

    /**
     * 将图块左右翻转
     * e.g:
     * 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F = 0F 0E 0D 0C 0B 0A 09 08 07 06 05 04 03 02 01 00
     */
    public static void flip(byte[] tiles) {
        byte temp;
        for (int j = 0; j < tiles.length; j++) {
            temp = 0;
            for (int k = 7, h = 0x80; k >= 0; h >>>= 1, k--) {
                temp |= ((tiles[j] & h) >>> k) << (7 - k);
            }
            tiles[j] = temp;
        }
    }


    /**
     * @return 通过地图属性生成一张 TileSet 图片
     * @see #generateTileSet(MetalMaxRe, int, int, int, int, int, int, Color[][])
     */
    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, @NotNull MapProperties mapProperties, @Nullable Color[][] palette) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();

        if (palette == null) {
            IPaletteEditor paletteEditor = metalMaxRe.getEditorManager().getEditor(IPaletteEditor.class);
            List<PaletteRow> palettes = paletteEditor.getPaletteByIndex(mapProperties.getPalette());

            palette = new Color[0x04][];
            palette[0x00] = palettes.get(0x00).toColors(systemPalette);
            palette[0x01] = palettes.get(0x01).toColors(systemPalette);
            palette[0x02] = palettes.get(0x02).toColors(systemPalette);
            palette[0x03] = palettes.get(0x03).toColors(systemPalette);
        }
        return generateTileSet(metalMaxRe,
                mapProperties.tilesIndexA & 0xFF,
                mapProperties.tilesIndexB & 0xFF,
                mapProperties.tilesIndexC & 0xFF,
                mapProperties.tilesIndexD & 0xFF,
                mapProperties.combinationA & 0xFF,
                mapProperties.combinationB & 0xFF,
                palette
        );
    }

    /**
     * 生成一张 TileSet 图片
     * 用于到地图中
     * 导出的图片不适用于世界地图
     * <p>
     * 图片的大小为 256*128
     */
    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0, int combinationA, int combinationB, @Nullable Color[][] palette) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        if (palette == null) {
            // 如果没有提供颜色，就适用灰白
            palette = new Color[][]{
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)}
            };
        }
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);

        return generate(metalMaxRe, 0x100, 0x80,
                x00, x40, x80, xC0,
                new byte[][][]{tileSetEditor.getCombinations()[combinationA], tileSetEditor.getCombinations()[combinationB]},
                new TileAttributes[]{tileSetEditor.getAttributes()[combinationA], tileSetEditor.getAttributes()[combinationB]},
                palette);
    }


    /**
     * 生成一张没有组合过的 TileSet 图片
     * 4个32*128直接拼接的图片
     * <p>
     * 图片的大小为 128*128
     */
    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0, Color[] colors) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);

        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = tileSetEditor.getTiles()[x00]; // $00-$3F
        tiles[1] = tileSetEditor.getTiles()[x40]; // $40-$7F
        tiles[2] = tileSetEditor.getTiles()[x80]; // $80-$BF
        tiles[3] = tileSetEditor.getTiles()[xC0]; // $C0-$FF

        Color[][] image = new Color[0x80][0x80];

        // 4个32*128直接拼接的图片
        for (int part = 0; part < 0x04; part++) {
            for (int tileX = 0; tileX < 0x40; tileX++) {
                byte[] bytes = tiles[part][tileX];
                for (int b = 0; b < 0x08; b++) { // byte
                    for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                        int l = (bytes[b] & d) >>> (7 - k);
                        l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;
                        // 设置颜色
//                        graphics.setColor(colors[l]);
                        int y = (part * 0x20) + ((tileX / 0x10) * 0x08) + b;
                        int x = ((tileX % 0x10) * 0x08) + k;
                        // 绘制像素点
//                        graphics.drawLine(x, y, x, y);
                        image[y][x] = colors[l];
                    }
                }
            }
        }
        return image;
    }

    /**
     * 生成一张没有组合过的 TileSet 图片
     * 4个32*128直接拼接的图片
     * <p>
     * 图片的大小为 128*128
     */
    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        return generateTileSet(metalMaxRe, x00, x40, x80, xC0, new Color[]{
                systemPalette.getBlack(), systemPalette.getWhite(), new Color(0xA1A1A1), new Color(0x585858)
        });
    }

    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int xXX, int combinationA, int combinationB, @Nullable Color[][] palette) {
        return generateTileSet(metalMaxRe,
                (xXX & 0xFF000000) >>> 24,
                (xXX & 0x00FF0000) >>> 16,
                (xXX & 0x0000FF00) >>> 8,
                xXX & 0x000000FF,
                combinationA, combinationB, palette);
    }

    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int xXX, int combinationA, int combinationB) {
        return generateTileSet(metalMaxRe, xXX, combinationA, combinationB, null);
    }

    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int xXX) {
        return generateTileSet(metalMaxRe,
                (xXX & 0xFF000000) >>> 24,
                (xXX & 0x00FF0000) >>> 16,
                (xXX & 0x0000FF00) >>> 8,
                xXX & 0x000000FF);
    }

    public static Color[][] generateTileSet(@NotNull MetalMaxRe metalMaxRe, int xXX, Color[] colors) {
        return generateTileSet(metalMaxRe,
                (xXX & 0xFF000000) >>> 24,
                (xXX & 0x00FF0000) >>> 16,
                (xXX & 0x0000FF00) >>> 8,
                xXX & 0x000000FF, colors);
    }

    /**
     * 生成一个世界地图的 TileSet 图片
     * 使用了3个0x40组合和颜色
     * 图片的大小为 256*192
     */
    public static Color[][] generateWorldTileSet(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                                 byte[][][] combinations, TileAttributes[] attributes,
                                                 Color[][] colors) {
        return generate(metalMaxRe, 0x100, 0x100, x00, x40, x80, xC0, combinations, attributes, colors);
    }

    /**
     * 生成一个世界地图的 TileSet 图片
     * 使用了3个0x40组合和颜色
     * 图片的大小为 256*192
     *
     * @param xXX 分割为4个，高位到低位分别为 x00、x40、x80、xC0
     */
    public static Color[][] generateWorldTileSet(@NotNull MetalMaxRe metalMaxRe, int xXX,
                                                 byte[][][] combinations, TileAttributes[] attributes,
                                                 Color[][] colors) {
        int x00 = NumberR.at(xXX, 3) & 0xFF;
        int x40 = NumberR.at(xXX, 2) & 0xFF;
        int x80 = NumberR.at(xXX, 1) & 0xFF;
        int xC0 = NumberR.at(xXX, 0) & 0xFF;
        return generate(metalMaxRe, 0x100, 0x100, x00, x40, x80, xC0, combinations, attributes, colors);
    }

    /**
     * 生成一个世界地图的 TileSet 图片
     * 使用了3个0x40组合和颜色
     * 图片的大小为 256*192
     *
     * @param xXX 分割为4个，高位到低位分别为 x00、x40、x80、xC0
     */
    public static Color[][] generateWorldTileSet(@NotNull MetalMaxRe metalMaxRe, int xXX) {
        int x00 = NumberR.at(xXX, 3) & 0xFF;
        int x40 = NumberR.at(xXX, 2) & 0xFF;
        int x80 = NumberR.at(xXX, 1) & 0xFF;
        int xC0 = NumberR.at(xXX, 0) & 0xFF;
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
        IPaletteEditor paletteEditor = metalMaxRe.getEditorManager().getEditor(IPaletteEditor.class);
        List<PaletteRow> palette = paletteEditor.getPaletteByIndex(0x9AD0);

        Color[][] colors = new Color[0x04][];
        colors[0x00] = palette.get(0x00).toColors(systemPalette);
        colors[0x01] = palette.get(0x01).toColors(systemPalette);
        colors[0x02] = palette.get(0x02).toColors(systemPalette);
        colors[0x03] = palette.get(0x03).toColors(systemPalette);
        return generate(metalMaxRe, 0x100, 0x100, x00, x40, x80, xC0, tileSetEditor.getWorldCombinations(), tileSetEditor.getWorldAttributes(), colors);
    }

    public static Color[][] generateSpriteTileSet(@NotNull MetalMaxRe metalMaxRe, byte sprite) {
        return generateSpriteTileSet(metalMaxRe, sprite, false);
    }

    public static Color[][] generateSpriteTileSet(@NotNull MetalMaxRe metalMaxRe, byte sprite, boolean running) {
        return generateSpriteTileSet(metalMaxRe, sprite & 0xFF, running);
    }

    public static Color[][] generateSpriteTileSet(@NotNull MetalMaxRe metalMaxRe, int sprite) {
        return generateSpriteTileSet(metalMaxRe, sprite, false);
    }

    /**
     * 生成一张精灵的 TileSet 图片
     * 该算法不完整，所以有部分错误的图像
     * 图片的大小为
     */
    public static Color[][] generateSpriteTileSet(@NotNull MetalMaxRe metalMaxRe, int sprite, boolean running) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
        IPaletteEditor paletteEditor = metalMaxRe.getEditorManager().getEditor(IPaletteEditor.class);

        // 获取精灵使用的图块表
        byte[][][] spriteTiles = new byte[4][0x40][0x10];
        spriteTiles[0] = tileSetEditor.getTiles()[0x04]; // $00-$3F
        spriteTiles[1] = tileSetEditor.getTiles()[0x05]; // $40-$7F
        // 上面两个为固定的精灵表
        spriteTiles[2] = tileSetEditor.getTiles()[sprite]; // $80-$BF
        spriteTiles[3] = tileSetEditor.getTiles()[sprite + 1]; // $C0-$FF

        // 获取精灵调色板
        List<PaletteRow> palette = paletteEditor.getSpritePalette();

        final byte[] xA597 = tileSetEditor.getXA597();
        final byte[] xA59E = tileSetEditor.getXA59E();
        final byte[] xA59B = tileSetEditor.getXA59B();
        final byte[] xA5DD = tileSetEditor.getXA5DD();
        final byte[] x847B = tileSetEditor.getX847B();
        final byte[] x8552 = tileSetEditor.getX8552();
        final byte[] x8629 = tileSetEditor.getX8629();
        final byte[] x83F2 = tileSetEditor.getX83F2();
        final byte[] x83FA = tileSetEditor.getX83FA();

        Color[][] image = new Color[0x100][0x100];

        // 精灵的的图像一共有 0x100 / 0x04 = 0x40 种
        // 0x04 为精灵的四个朝向
        // 精灵的id为0时，不显示精灵，所以直接跳过
        for (int spriteId = 1; spriteId < 0x40; spriteId++) {
            // 顺序为：上、下、左、右
            for (int direction = 0; direction < 0x04; direction++) {
                // 模拟内存地址$0150-$015F的数据
                byte bA597 = xA597[direction];
                byte bA59E = xA59E[spriteId];
                int x0150 = (bA59E & 0xFF) + (bA597 & 0xFF) + (running ? 1 : 0);
                // 模拟内存地址$0160-$016F的数据
                byte bA59B = xA59B[direction];
                byte bA5DD = xA5DD[spriteId];
                int x0160 = (bA5DD & 0xFF) | (bA59B & 0xFF);

                // 得到精灵图像差值索引
                byte b847B = (byte) (x847B[x0150] & 0B0000_0111);

                // 精灵图像上半部分的图像索引
                byte b8552 = x8552[x0150];
                // 精灵图像下半部分的图像索引
                byte b8629 = x8629[x0150];
                // 精灵上半部分两个图像块的差值
                byte b83F2 = x83F2[b847B];
                // 精灵下半部分两个图像块的差值
                byte b83FA = x83FA[b847B];

                // 获取上半部分图像索引
                int up1 = b8552 & 0xFF;
                int up2 = (b8552 + b83F2) & 0xFF;
                // 获取下半部分图像索引
                int down1 = b8629 & 0xFF;
                int down2 = ((byte) (b8629 + b83FA)) & 0xFF;

                // 0x10为精灵大小，0x04为精灵四个朝向
                // 0x100为画布大小
                // id + direction = 这个精灵的朝向

                // 精灵的起始X坐标
                int x = ((spriteId * 0x10 * 0x04) + (direction * 0x10)) % 0x100;
                // 精灵的起始Y坐标
                int y = (((spriteId * 0x10 * 0x04) + (direction * 0x10)) / 0x100) * 0x10;

                // 读取精灵材质
                byte[][] texture = new byte[0x04][0x10];
                texture[0x00] = Arrays.copyOf(spriteTiles[up1 / 0x40][up1 % 0x40], 0x10);
                texture[0x01] = Arrays.copyOf(spriteTiles[up2 / 0x40][up2 % 0x40], 0x10);
                texture[0x02] = Arrays.copyOf(spriteTiles[down1 / 0x40][down1 % 0x40], 0x10);
                texture[0x03] = Arrays.copyOf(spriteTiles[down2 / 0x40][down2 % 0x40], 0x10);

                // 精灵姿态

                // 精灵图块单独的左右翻转
                if ((x847B[x0150] & 0B0111_1000) != 0x00) {
                    for (int i = 0, t = (x847B[x0150] & 0B0111_1000) >>> 3; i < 0x04; i++, t >>>= 1) {
                        // 翻转循序为：左上、右上、左下、右下
                        if ((t & 0B0000_0001) != 0x00) {
                            TileSetHelper.flip(texture[i]);
                        }
                    }
                }

                // 上下翻转
                if ((x0160 & 0B1000_0000) == 0B1000_0000) {
                    // 上下图块互换
                    // 0x00 -> 0x02
                    // 0x01 -> 0x03
                    byte[] temp = texture[0x00];
                    texture[0x00] = texture[0x02];
                    texture[0x02] = temp;

                    temp = texture[0x01];
                    texture[0x01] = texture[0x03];
                    texture[0x03] = temp;

                    // 图块上下翻转
                    for (byte[] bytes : texture) {
                        TileSetHelper.reverse(bytes);
                    }
                }

                // 左右翻转
                if ((x0160 & 0B0100_0000) == 0B0100_0000) {
                    // 左右图块互换
                    // 0x00 -> 0x01
                    // 0x02 -> 0x03
                    byte[] temp = texture[0x00];
                    texture[0x00] = texture[0x01];
                    texture[0x01] = temp;

                    temp = texture[0x02];
                    texture[0x02] = texture[0x03];
                    texture[0x03] = temp;

                    for (byte[] bytes : texture) {
                        TileSetHelper.flip(bytes);
                    }
                }

                for (int j = 0; j < texture.length; j++) {
                    int x2 = j % 0x02; // 精灵图像的小块(2*2)X值绘制位置
                    int y2 = j / 0x02; // 精灵图像的小块(2*2)Y值绘制位置

                    byte[] bytes = texture[j];
                    if (bytes == null) {
                        continue;
                    }
                    for (int b = 0; b < 0x08; b++) { // byte
                        for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                            int l = (bytes[b] & d) >>> (7 - k);
                            l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;


                            if (l == 0) {
                                // 精灵的第0颜色为透明
                                continue;
                            }

                            Color color;

                            // 翻转保持调色板
                            if ((x0160 & 0B1000_0000) == 0B1000_0000) {
                                // 上下翻转替换调色板
                                if (y2 != 0) {
                                    // 上半部分使用下半部分的调色板
//                                    graphics.setColor(palette.get((x0160 & 0B0000_1100) >>> 2).getToColor(systemPalette, l).toAwtColor());
                                    color = palette.get((x0160 & 0B0000_1100) >>> 2).getToColor(systemPalette, l);
                                } else {
                                    // 下半部分使用上半部分的调色板
//                                    graphics.setColor(palette.get(x0160 & 0B0000_0011).getToColor(systemPalette, l).toAwtColor());
                                    color = palette.get(x0160 & 0B0000_0011).getToColor(systemPalette, l);
                                }
                            } else {
                                if (y2 != 0) {
                                    // 上半部分调色板
//                                    graphics.setColor(palette.get(x0160 & 0B0000_0011).getToColor(systemPalette, l).toAwtColor());
                                    color = palette.get(x0160 & 0B0000_0011).getToColor(systemPalette, l);
                                } else {
                                    // 下半部分调色板
//                                    graphics.setColor(palette.get((x0160 & 0B0000_1100) >>> 2).getToColor(systemPalette, l).toAwtColor());
                                    color = palette.get((x0160 & 0B0000_1100) >>> 2).getToColor(systemPalette, l);
                                }
                            }

                            int x3 = x + (x2 * 0x08) + k; // 精灵图像X值绘制位置
                            int y3 = y + (y2 * 0x08) + b; // 精灵图像Y值绘制位置
//                            graphics.drawLine(x3, y3, x3, y3);
                            image[y3][x3] = color;
                        }
                    }
                }
            }
        }
        return image;
    }

    /**
     * 生成指定大小的图片，图块集、图块组合、颜色组合
     * <p>
     * 0B0011_0000  调色板 Y<p>
     * 0B0000_0011  调色板 X
     */
    private static byte[][] generate(@NotNull MetalMaxRe metalMaxRe, int width, int height,
                                     int x00, int x40, int x80, int xC0,
                                     byte[][][] combinations, TileAttributes[] attributes) {
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);

        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = tileSetEditor.getTiles()[x00]; // $00-$3F
        tiles[1] = tileSetEditor.getTiles()[x40]; // $40-$7F
        tiles[2] = tileSetEditor.getTiles()[x80]; // $80-$BF
        tiles[3] = tileSetEditor.getTiles()[xC0]; // $C0-$FF

        byte[][] image = new byte[height][width];

        // 每0x40个tile作为一个部分
        // 怎么写出来的？别问，问就是不知道
        for (int part = 0; part < combinations.length; part++) {
            // 获取该部分的组合集
            byte[][] combination = combinations[part];
            // 获取该部分的颜色
            byte[] color = attributes[part].getAttributes();

            // pixel y
            // 该部分的所有y值
            // 0x40被分成0x04*0x10
            for (int y = 0; y < 0x04; y++) {
                // pixel (x * 16) tile width
                for (int tileX = 0; tileX < 0x10; tileX++) {
                    // tile由4个小tile组成
                    for (int smallTile = 0; smallTile < 0x04; smallTile++) {
                        // 得到tile图像
                        int b1 = combination[(y % 0x04 * 0x10) + tileX][smallTile] & 0xFF;
                        byte[] bytes = tiles[b1 / 0x40][b1 % 0x40];
                        // 得到调色板
                        int b2 = color[(y % 0x04 * 0x10) + tileX] & 0xFF;

                        int colorColumn = b2 & 0B0000_0011; // 获取调色盘 Y
                        colorColumn <<= 4; // 0B0011_0000

                        // 绘制小tile
                        // 小tile的大小为8*8
                        for (int b = 0; b < 0x08; b++) {
                            // 将0x10byte合成为一行8*1的图像
                            for (int m = 0, d = 0x80; m < 0x08; m++, d >>>= 1) {
                                // 得到该像素点的调色板
                                int l = (bytes[b] & d) >>> (7 - m);
                                l += ((bytes[b + 0x08] & d) >>> (7 - m)) << 1;
//                                graphics.setColor(colors1[l].toAwtColor());

                                // 获取像素点坐标y
                                int y2 = (part * 0x40) + (y * 0x08 * 0x02) + ((smallTile / 0x02) * 0x08) + b;
                                // 获取像素点坐标x
                                int x2 = (tileX * 0x08 * 0x02) + ((smallTile % 0x02) * 0x08) + m;
                                // 绘制像素点
//                                graphics.drawLine(x2, y2, x2, y2);
                                image[y2][x2] = (byte) (colorColumn | l); // 0B0011_0011 (Y + X)
                            }
                        }
                    }
                }
            }
        }
        return image;
    }

    private static Color[][] generate(@NotNull MetalMaxRe metalMaxRe, int width, int height,
                                      int x00, int x40, int x80, int xC0,
                                      byte[][][] combinations, TileAttributes[] attributes,
                                      Color[][] colors) {
        return palette(generate(metalMaxRe, width, height, x00, x40, x80, xC0, combinations, attributes), colors);
    }

    /**
     * 通过调色板进行调色
     */
    public static Color[][] palette(byte[][] imageData, @NotNull Color[][] colors) {
        Color[][] image = new Color[imageData.length][];

        for (int y = 0; y < imageData.length; y++) {
            byte[] imageColumn = imageData[y];
            image[y] = new Color[imageColumn.length];
            for (int x = 0; x < imageColumn.length; x++) {
                byte point = imageColumn[x];
                // 获取Y和X坐标
                image[y][x] = colors[(point & 0B0011_0000) >>> 4][point & 0B0000_0011];
            }
        }
        return image;
    }

    /**
     * 通过调色板进行调色
     */
    public static Color[][] palette(byte[][] imageData, @NotNull Color[] color) {
        return palette(imageData, new Color[][]{color, color, color, color});
    }

    public static Color[][] generate(@NotNull MetalMaxRe metalMaxRe,
                                     int x00, int x40, int x80, int xC0,
                                     Color[][] colors) {
        return generate(metalMaxRe, x00, x40, x80, xC0, colors[0x03]);
    }

    public static Color[][] generate(@NotNull MetalMaxRe metalMaxRe,
                                     int x00, int x40, int x80, int xC0,
                                     Color[] colors) {
        final Color[][] image = new Color[0x80][0x80];
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);

        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = tileSetEditor.getTiles()[x00]; // $00-$3F
        tiles[1] = tileSetEditor.getTiles()[x40]; // $40-$7F
        tiles[2] = tileSetEditor.getTiles()[x80]; // $80-$BF
        tiles[3] = tileSetEditor.getTiles()[xC0]; // $C0-$FF

        for (int i = 0; i < tiles.length; i++) { //
            for (int j = 0; j < tiles[i].length; j++) { // 0x40 size
                final byte[] bytes = tiles[i][j]; // 0x10 size
                for (int b = 0; b < 0x08; b++) { // byte
                    for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                        int l = (bytes[b] & d) >>> (7 - k);
                        l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;

                        int y = (i * 0x20) + ((j / 0x10) * 0x08) + b;
                        int x = ((j % 0x10) * 0x08) + k;
                        image[y][x] = colors[l];
                    }
                }
            }
        }
        return image;
    }

    public static byte[][] generate(@NotNull MetalMaxRe metalMaxRe,
                                    int x00, int x40, int x80, int xC0) {
        final byte[][] image = new byte[0x80][0x80];
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);

        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = tileSetEditor.getTiles()[x00]; // $00-$3F
        tiles[1] = tileSetEditor.getTiles()[x40]; // $40-$7F
        tiles[2] = tileSetEditor.getTiles()[x80]; // $80-$BF
        tiles[3] = tileSetEditor.getTiles()[xC0]; // $C0-$FF

        for (int i = 0; i < tiles.length; i++) { //
            for (int j = 0; j < tiles[i].length; j++) { // 0x40 size
                final byte[] bytes = tiles[i][j]; // 0x10 size
                for (int b = 0; b < 0x08; b++) { // byte
                    for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                        int l = (bytes[b] & d) >>> (7 - k);
                        l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;

                        int y = (i * 0x20) + ((j / 0x10) * 0x08) + b;
                        int x = ((j % 0x10) * 0x08) + k;
                        image[y][x] = (byte) l;
                    }
                }
            }
        }
        return image;
    }

    public static byte[][] generateMonsterModel(@NotNull MetalMaxRe metalMaxRe,
                                                int x00, int x40, int x80, int xC0) {
        final byte[][] image = new byte[0x80][0x80];
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);

        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = tileSetEditor.getTiles()[x00]; // $00-$3F
        tiles[1] = tileSetEditor.getTiles()[x40]; // $40-$7F
        tiles[2] = tileSetEditor.getTiles()[x80]; // $80-$BF
        tiles[3] = tileSetEditor.getTiles()[xC0]; // $C0-$FF

        for (int i = 0; i < tiles.length; i++) { //
            for (int j = 0; j < tiles[i].length; j++) { // 0x40 size
                final byte[] bytes = tiles[i][j]; // 0x10 size
                for (int b = 0; b < 0x08; b++) { // byte
                    for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                        int l = (bytes[b] & d) >>> (7 - k);
                        l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;

                        int y = (i * 0x20) + ((j / 0x10) * 0x08) + b;
                        int x = ((j % 0x10) * 0x08) + k;
                        image[y][x] = (byte) l;
                    }
                }
            }
        }
        return image;
    }


    public static BufferedImage generateMapImage(@NotNull MetalMaxRe metalMaxRe, int mapId) {
        IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);
        IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);


        MapBuilder mapBuilder = mapEditor.getMap(mapId);
        MapProperties mapProperties = mapPropertiesEditor.getMapProperties(mapId);

        int width = mapProperties.intWidth();
        int height = mapProperties.intHeight();

        byte[][] map = mapBuilder.toMapArray(mapProperties);

        BufferedImage tileSet = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));

        BufferedImage bufferedImage = new BufferedImage(width * 0x10, height * 0x10, BufferedImage.TYPE_INT_RGB);

        // 储存1tile(16*16)
        final Map<Integer, BufferedImage> tmpTiles = new HashMap<>();

        Graphics2D graphics = bufferedImage.createGraphics();

        // mapX和mapY的单位是tile，1tile=16*16像素
        for (int mapY = 0; mapY < height; mapY++) {
            for (int mapX = 0; mapX < width; mapX++) {
                int tile = map[mapY][mapX] & 0xFF;
                int y2 = (tile / 0x10) * 0x10;
                int x2 = (tile % 0x10) * 0x10;

                BufferedImage tileImage = tmpTiles.get(tile);
                if (tileImage == null) {
                    // 复制一个tile出来保存到临时map中
                    tileImage = tileSet.getSubimage(x2, y2, 0x10, 0x10);
                    tmpTiles.put(tile, tileImage);
                }
                // 绘制tile
                graphics.drawImage(tileImage, mapX * 0x10, mapY * 0x10, 0x10, 0x10, null);
            }
        }
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage generateWorldMapImage(@NotNull MetalMaxRe metalMaxRe) {
        IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);

        byte[][] map = worldMapEditor.getMap();

        Map<Integer, List<BufferedImage>> tileSetMap = WorldMapEditorImpl.DEFAULT_PIECES.values().parallelStream().distinct().map(integer -> {
                    BufferedImage bufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(metalMaxRe, integer));
                    return Map.entry(integer, TileSetHelper.diced(bufferedImage));
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        BufferedImage bufferedImage = new BufferedImage(0x100 * 0x10, 0x100 * 0x10, BufferedImage.TYPE_INT_RGB);


        Graphics graphics = bufferedImage.getGraphics();

        WorldMapEditorImpl.DEFAULT_PIECES.entrySet().parallelStream().forEach(entry -> {
            Rectangle rectangle = entry.getKey();
            List<BufferedImage> tileSets = tileSetMap.get(entry.getValue());
            int x = (int) rectangle.getX();
            int y = (int) rectangle.getY();
            int width = (int) rectangle.getWidth();
            int height = (int) rectangle.getHeight();

            for (int offsetY = 0; offsetY < height; offsetY++) {
                for (int offsetX = 0; offsetX < width; offsetX++) {
                    int mapX = x + offsetX;
                    int mapY = y + offsetY;

                    // 绘制16*16的tile
                    int tile = map[mapY][mapX] & 0xFF;
                    graphics.drawImage(tileSets.get(tile), mapX * 0x10, mapY * 0x10, null);
                }
            }
        });
        graphics.dispose();
        return bufferedImage;
    }

    /**
     * 将图片分割为数个0x10*0x10的图块
     *
     * @see #diced(int, int, BufferedImage)
     */
    public static List<BufferedImage> diced(BufferedImage bufferedImage) {
        return diced(0x10, 0x10, bufferedImage);
    }

    /**
     * 将图片分割为数个指定大小的图块
     *
     * @param w             图块宽度
     * @param h             图库高度
     * @param bufferedImage 图片
     * @return 被分割完成的图块
     */
    public static List<BufferedImage> diced(int w, int h, BufferedImage bufferedImage) {
        List<BufferedImage> value = new ArrayList<>();
        int tW = bufferedImage.getWidth() / w;
        int tH = bufferedImage.getHeight() / h;
        for (int y = 0; y < tH; y++) {
            for (int x = 0; x < tW; x++) {
                value.add(bufferedImage.getSubimage(x * w, y * h, w, h));
            }
        }
        return value;
    }

    public static List<byte[][]> diced(int w, int h, byte[][] data) {
        List<byte[][]> value = new ArrayList<>();
        int tW = data[0x00].length / w;
        int tH = data.length / h;
        for (int y = 0; y < tH; y++) {
            for (int x = 0; x < tW; x++) {
                byte[][] bytes = new byte[h][w];

                for (int offsetY = 0; offsetY < h; offsetY++) {
                    for (int offsetX = 0; offsetX < w; offsetX++) {
                        bytes[offsetY][offsetX] = data[(y * h) + offsetY][(x * w) + offsetX];
                    }
                }
                value.add(bytes);
            }
        }
        return value;
    }

}
