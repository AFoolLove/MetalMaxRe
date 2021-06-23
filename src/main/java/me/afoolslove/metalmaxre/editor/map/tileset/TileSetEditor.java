package me.afoolslove.metalmaxre.editor.map.tileset;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.map.MapProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 图块集
 *
 * @author AFoolLove
 */
public class TileSetEditor extends AbstractEditor {
    public static final int TILE_SET_START_OFFSET = 0x00000; // CHR
    public static final int TILE_SET_COMPOSITIONS_START_OFFSET = 0x35000; // CHR
    public static final int TILE_SET_COLOR_INDEX_START_OFFSET = 0x38700; // CHR


    public byte[][][] tiles = new byte[0x100][0x40][0x10]; // 0x04 = CHR表的四分之一
    public byte[][][] compositions = new byte[0x37][0x40][0x04]; // 每4byte一组，0x37个组合，0x40个4byte组
    public byte[][] colorIndex = new byte[0x37][0x40]; // 每0x40byte一组，0x37个组合，每byte对应一个图块的特性和调色板索引

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        // 所有tile
        if (tiles == null) {
            tiles = new byte[0x100][0x40][0x10];
        } else {
            for (byte[][] tile : tiles) {
                for (byte[] bytes : tile) {
                    Arrays.fill(bytes, (byte) 0x00);
                }
            }
        }
        // tile组合
        if (compositions == null) {
            compositions = new byte[0x37][0x40][0x04];
        } else {
            for (byte[][] composition : compositions) {
                for (byte[] bytes : composition) {
                    Arrays.fill(bytes, (byte) 0x00);
                }
            }
        }
        // tile特性和调色板
        if (colorIndex == null) {
            colorIndex = new byte[0x37][0x40];
        } else {
            for (byte[] index : colorIndex) {
                Arrays.fill(index, (byte) 0x00);
            }
        }

        // 读取所有 tile：0x00-0xFF
        // 0x10byte = 1tile
        // 0x40tile = x40（0x00、0x80、0xC0）
        setChrRomPosition(buffer, TILE_SET_START_OFFSET);
        // 一共0x100个 x40
        for (int count = 0; count <= 0xFF; count++) {
            byte[][] x40 = tiles[count] != null ? tiles[count] : new byte[0x40][0x10];
            tiles[count] = x40;
            // 读取 x40
            for (int i = 0; i < 0x40; i++) {
                // 读取 tile
                byte[] tile = x40[i] != null ? x40[i] : new byte[0x10];
                x40[i] = tile;
                // 读取
                buffer.get(tile);
            }
        }

        // 读取所有Tile组合 0x00-0x37
        // 0x04byte = 1tile composition
        // 0x40tile = x40（0x00、0x80、0xC0）
        setChrRomPosition(buffer, TILE_SET_COMPOSITIONS_START_OFFSET);
        for (int count = 0; count < 0x37; count++) {
            // 0x40 tile composition
            byte[][] tileCompositions = compositions[count] != null ? compositions[count] : new byte[0x40][0x04];
            compositions[count] = tileCompositions;
            for (int i = 0; i < 0x40; i++) {
                // tile composition
                byte[] tileComposition = tileCompositions[i] != null ? tileCompositions[i] : new byte[0x04];
                tileCompositions[i] = tileComposition;
                buffer.get(tileComposition);
            }
        }

        // 读取tile的特性和调色板 0x00-0x37
        // 0x01byte = 1tile特性和调色板
        // 0x40tile = 0x40（0x00、0x80、0xC0）
        setChrRomPosition(buffer, TILE_SET_COLOR_INDEX_START_OFFSET);
        for (int count = 0; count < 0x37; count++) {
            byte[] color = colorIndex[count] != null ? colorIndex[count] : new byte[0x40];
            colorIndex[count] = color;
            // 读取 x40 tile特性和调色板
            buffer.get(color);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入所有tile
        setChrRomPosition(buffer, TILE_SET_START_OFFSET);
        for (byte[][] tile : tiles) {
            for (byte[] bytes : tile) {
                buffer.put(bytes);
            }
        }

        // 写入tile组合
        setChrRomPosition(buffer, TILE_SET_COMPOSITIONS_START_OFFSET);
        for (byte[][] composition : compositions) {
            for (byte[] bytes : composition) {
                buffer.put(bytes);
            }
        }

        // 写入tile的特性和调色板
        setChrRomPosition(buffer, TILE_SET_COLOR_INDEX_START_OFFSET);
        for (byte[] bytes : colorIndex) {
            buffer.put(bytes);
        }
        return true;
    }

    /**
     * @return 通过地图属性生成一张 TileSet 图片
     * @see #generateTileSet(int, int, int, int, int, int, Color[][])
     */
    public BufferedImage generateTileSet(@NotNull MapProperties mapProperties, @Nullable Color[][] colors) {
        return generateTileSet(
                mapProperties.tilesIndexA & 0xFF,
                mapProperties.tilesIndexB & 0xFF,
                mapProperties.tilesIndexC & 0xFF,
                mapProperties.tilesIndexD & 0xFF,
                mapProperties.combinationA & 0xFF,
                mapProperties.combinationB & 0xFF,
                colors
        );
    }

    /**
     * 生成一张 TileSet 图片
     * 用于到地图中
     * 导出的图片不适用于世界地图
     * <p>
     * 图片的大小为 256*128
     */
    public BufferedImage generateTileSet(int x00, int x40, int x80, int xC0, int compositionA, int compositionB, @Nullable Color[][] colors) {
        if (colors == null) {
            // 如果没有提供颜色，就适用灰白
            // TODO 暂时这么写。。记得改
            colors = new Color[][]{
                    {Color.BLACK, Color.WHITE, new Color(0xa1a1a1), new Color(0x585858)},
                    {Color.BLACK, Color.WHITE, new Color(0xa1a1a1), new Color(0x585858)},
                    {Color.BLACK, Color.WHITE, new Color(0xa1a1a1), new Color(0x585858)},
                    {Color.BLACK, Color.WHITE, new Color(0xa1a1a1), new Color(0x585858)}
            };
        }

        return generate(0x100, 0x80,
                x00, x40, x80, xC0,
                new byte[][][]{this.compositions[compositionA], this.compositions[compositionB]},
                new byte[][]{this.colorIndex[compositionA], this.colorIndex[compositionB]},
                colors);
    }


    /**
     * 生成一张没有组合过的 TileSet 图片
     * 4个32*128直接拼接的图片
     * <p>
     * 图片的大小为 128*128
     */
    public BufferedImage generateTileSet(int x00, int x40, int x80, int xC0) {
        // 只能是灰白色了
        Color[] colors = new Color[]{
                Color.BLACK, Color.WHITE, new Color(0xa1a1a1), new Color(0x585858)
        };
        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = this.tiles[x00]; // $00-$3F
        tiles[1] = this.tiles[x40]; // $40-$7F
        tiles[2] = this.tiles[x80]; // $80-$BF
        tiles[3] = this.tiles[xC0]; // $C0-$FF

        BufferedImage bufferedImage = new BufferedImage(0x80, 0x80, BufferedImage.TYPE_INT_RGB);

        // 绘制
        Graphics graphics = bufferedImage.getGraphics();

        // 4个32*128直接拼接的图片
        for (int part = 0; part < 0x04; part++) {
            for (int tileX = 0; tileX < 0x40; tileX++) {
                byte[] bytes = tiles[part][tileX];
                for (int b = 0; b < 0x08; b++) { // byte
                    for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                        int l = (bytes[b] & d) >>> (7 - k);
                        l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;
                        // 设置颜色
                        graphics.setColor(colors[l]);
                        int y = (part * 0x20) + ((tileX / 0x10) * 0x08) + b;
                        int x = ((tileX % 0x10) * 0x08) + k;
                        // 绘制像素点
                        graphics.drawLine(x, y, x, y);
                    }
                }
            }
        }
        return bufferedImage;
    }


    /**
     * 生成一个世界地图的 TileSet 图片
     * 使用了3个0x40组合和颜色
     * 图片的大小为 256*192
     */
    public BufferedImage generateWorldTileSet(int x00, int x40, int x80, int xC0,
                                              byte[][][] compositions, byte[][] colorIndexes,
                                              Color[][] colors) {
        return generate(0x100, 0xC0, x00, x40, x80, xC0, compositions, colorIndexes, colors);
    }

    private BufferedImage generate(int width, int height,
                                   int x00, int x40, int x80, int xC0,
                                   byte[][][] compositions, byte[][] colorIndexes,
                                   Color[][] colors) {
        byte[][][] tiles = new byte[4][0x40][0x10];
        tiles[0] = this.tiles[x00]; // $00-$3F
        tiles[1] = this.tiles[x40]; // $40-$7F
        tiles[2] = this.tiles[x80]; // $80-$BF
        tiles[3] = this.tiles[xC0]; // $C0-$FF

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 绘制
        Graphics graphics = bufferedImage.getGraphics();

        // 每0x40个tile作为一个部分
        // 怎么写出来的？别问，问就是不知道
        for (int part = 0; part < compositions.length; part++) {
            // 获取该部分的组合集
            byte[][] composition = compositions[part];
            // 获取该部分的颜色
            byte[] color = colorIndexes[part];

            // pixel y
            // 该部分的所有y值
            for (int y = 0; y < 0x40; y++) {
                // pixel (x * 16) tile width
                for (int tileX = 0; tileX < 0x10; tileX++) {
                    // tile由4个小tile组成
                    for (int smallTile = 0; smallTile < 0x04; smallTile++) {
                        // 得到tile图像
                        int b1 = composition[(y % 0x04 * 0x10) + tileX][smallTile] & 0xFF;
                        byte[] bytes = tiles[b1 / 0x40][b1 % 0x40];
                        // 得到调色板
                        int b2 = color[(y % 0x04 * 0x10) + tileX] & 0xFF;
                        Color[] colors1 = colors[b2 & 0B0000_0011]; // 获取调色盘

                        // 绘制小tile
                        // 小tile的大小为8*8
                        for (int b = 0; b < 0x08; b++) {
                            // 将0x10byte合成为一行8*1的图像
                            for (int m = 0, d = 0x80; m < 0x08; m++, d >>>= 1) {
                                // 得到该像素点的调色板
                                int l = (bytes[b] & d) >>> (7 - m);
                                l += ((bytes[b + 0x08] & d) >>> (7 - m)) << 1;
                                graphics.setColor(colors1[l]);

                                // 获取像素点坐标y
                                int y2 = (part * 0x40) + (y * 0x08 * 0x02) + ((smallTile / 0x02) * 0x08) + b;
                                // 获取像素点坐标x
                                int x2 = (tileX * 0x08 * 0x02) + ((smallTile % 0x02) * 0x08) + m;
                                // 绘制像素点
                                graphics.drawLine(x2, y2, x2, y2);
                            }
                        }

                    }
                }
            }
        }
        graphics.dispose();
        return bufferedImage;
    }
}
