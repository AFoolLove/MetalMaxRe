package me.afoolslove.metalmaxre.editor.map.tileset;

import me.afoolslove.metalmaxre.NumberR;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.MapProperties;
import me.afoolslove.metalmaxre.editor.palette.PaletteEditor;
import me.afoolslove.metalmaxre.editor.palette.PaletteList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 图块集
 * <p>
 * 2021年6月26日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class TileSetEditor extends AbstractEditor<TileSetEditor> {
    public static final int TILE_SET_START_OFFSET = 0x00000; // CHR
    public static final int TILE_SET_COMPOSITIONS_START_OFFSET = 0x35000; // CHR
    public static final int TILE_SET_COLOR_INDEX_START_OFFSET = 0x38700; // CHR

    public static final int TILE_SET_WORLD_COMPOSITIONS_START_OFFSET = 0x0FBBE - 0x10;
    public static final int TILE_SET_WORLD_COLOR_INDEX_START_OFFSET = 0x0FEAA - 0x10;


    public byte[][][] tiles = new byte[0x100][0x40][0x10]; // 0x04 = CHR表的四分之一
    public byte[][][] compositions = new byte[0x37][0x40][0x04]; // 每4byte一组，0x37个组合，0x40个4byte组
    public byte[][] colorIndex = new byte[0x37][0x40]; // 每0x40byte一组，0x37个组合，每byte对应一个图块的特性和调色板索引

    /**
     * 世界地图的组合数据，全局固定
     */
    public byte[][][] worldCompositions = new byte[0x03][0x40][0x04];
    /**
     * 世界地图的调色板
     */
    public byte[][] worldColorIndexes = new byte[0x03][0x40];


    public byte[] xA597 = new byte[0x04]; // 精灵的朝向帧，全局属性（移动和未移动的图像
    public byte[] xA59B = new byte[0x04]; // 精灵的姿态，全局属性（翻转、调色板等
    public byte[] xA59E = new byte[0x40]; // 精灵图像值 value + spriteId = $0150，该数据大小待验证
    public byte[] xA5DD = new byte[0x40]; // 精灵的姿态和调色板的值，(value + spriteId) | xA59B = $0160，该数据大小待验证

    public byte[] x83F2 = new byte[0x08]; // 精灵上半部分两个图像块的差值
    public byte[] x83FA = new byte[0x08]; // 精灵下半部分两个图像块的差值
    public byte[] x847B = new byte[0x100]; // 精灵图像块差值索引等，该数据大小待验证
    public byte[] x8552 = new byte[0x100]; // 精灵图像上半部分的图像索引，该数据大小待验证
    public byte[] x8629 = new byte[0x100]; // 精灵图像下半部分的图像索引，该数据大小待验证

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
        // 世界地图的tile组合
        for (byte[][] worldComposition : worldCompositions) {
            for (byte[] bytes : worldComposition) {
                Arrays.fill(bytes, (byte) 0x00);
            }
        }
        // 世界地图的特性和调色板
        for (byte[] worldColorIndex : worldColorIndexes) {
            Arrays.fill(worldColorIndex, (byte) 0x00);
        }

        // 精灵相关数据
        Arrays.fill(xA597, (byte) 0x00);
        Arrays.fill(xA59B, (byte) 0x00);
        Arrays.fill(xA59E, (byte) 0x00);
        Arrays.fill(xA5DD, (byte) 0x00);
        Arrays.fill(x83F2, (byte) 0x00);
        Arrays.fill(x83FA, (byte) 0x00);
        Arrays.fill(x847B, (byte) 0x00);
        Arrays.fill(x8552, (byte) 0x00);
        Arrays.fill(x8629, (byte) 0x00);


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
                get(buffer, tile);
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
                get(buffer, tileComposition);
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
            get(buffer, color);
        }

        // 读取世界地图的tile组合
        setPrgRomPosition(buffer, TILE_SET_WORLD_COMPOSITIONS_START_OFFSET);
        for (int count = 0; count < 0x03; count++) {
            // 0x40 tile composition
            byte[][] tileCompositions = worldCompositions[count] != null ? worldCompositions[count] : new byte[0x40][0x04];
            worldCompositions[count] = tileCompositions;
            for (int i = 0; i < 0x40; i++) {
                // tile composition
                byte[] tileComposition = tileCompositions[i] != null ? tileCompositions[i] : new byte[0x04];
                tileCompositions[i] = tileComposition;
                get(buffer, tileComposition);
            }
        }

        // 读取世界地图的特性和调色板
        setPrgRomPosition(buffer, TILE_SET_WORLD_COLOR_INDEX_START_OFFSET);
        for (int count = 0; count < 0x03; count++) {
            byte[] color = worldColorIndexes[count] != null ? worldColorIndexes[count] : new byte[0x40];
            worldColorIndexes[count] = color;
            // 读取 x40 tile特性和调色板
            get(buffer, color);
        }

        // 读取精灵相关数据，太杂乱了，未命名

        // 精灵朝向帧
        setPrgRomPosition(buffer, 0x34597);
        get(buffer, xA597);
        // 精灵的姿态
        get(buffer, xA59B);

        // 精灵图像值
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        setPrgRomPosition(buffer, 0x3459F - 0x01);
        get(buffer, xA59E);

        // 精灵的姿态和调色板值
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        setPrgRomPosition(buffer, 0x345DE - 0x01);
        get(buffer, xA5DD);

        // 精灵的上半部分图像块差值
        setPrgRomPosition(buffer, 0x263F2);
        get(buffer, x83F2);
        // 精灵的下半部分图像块差值
        get(buffer, x83FA);

        // 精灵图像块差值索引等
        setPrgRomPosition(buffer, 0x2647B);
        get(buffer, x847B);

        // 精灵的图像上半部分索引
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        setPrgRomPosition(buffer, 0x26553 - 0x01);
        get(buffer, x8552);
        // 精灵的图像下半部分索引
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        setPrgRomPosition(buffer, 0x2662A - 0x01);
        get(buffer, x8629);
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入所有tile
        setChrRomPosition(buffer, TILE_SET_START_OFFSET);
        for (byte[][] tile : tiles) {
            for (byte[] bytes : tile) {
                put(buffer, bytes);
            }
        }

        // 写入tile组合
        setChrRomPosition(buffer, TILE_SET_COMPOSITIONS_START_OFFSET);
        for (byte[][] composition : compositions) {
            for (byte[] bytes : composition) {
                put(buffer, bytes);
            }
        }

        // 写入tile的特性和调色板
        setChrRomPosition(buffer, TILE_SET_COLOR_INDEX_START_OFFSET);
        for (byte[] bytes : colorIndex) {
            put(buffer, bytes);
        }

        // 写入世界地图tile组合
        setPrgRomPosition(buffer, TILE_SET_WORLD_COMPOSITIONS_START_OFFSET);
        for (byte[][] composition : worldCompositions) {
            for (byte[] bytes : composition) {
                put(buffer, bytes);
            }
        }

        // 写入世界地图tile的特性和调色板
        setPrgRomPosition(buffer, TILE_SET_WORLD_COLOR_INDEX_START_OFFSET);
        for (byte[] bytes : worldColorIndexes) {
            put(buffer, bytes);
        }

        // 写入精灵相关数据

        // 写入精灵朝向帧
        setPrgRomPosition(buffer, 0x34597);
        put(buffer, xA597);
        // 写入精灵的姿态
        put(buffer, xA59B);

        // 写入精灵图像值
        setPrgRomPosition(buffer, 0x3459F);
        put(buffer, xA59E, 1, xA59E.length - 1);
        // 写入精灵的姿态和调色板值
        setPrgRomPosition(buffer, 0x345DE);
        put(buffer, xA5DD, 1, xA5DD.length - 1);

        // 写入精灵的上半部分图像块差值
        setPrgRomPosition(buffer, 0x263F2);
        put(buffer, x83F2);
        // 写入精灵的下半部分图像块差值
        put(buffer, x83FA);

        // 写入精灵图像块差值索引等
        setPrgRomPosition(buffer, 0x2647B);
        put(buffer, x847B);

        // 精灵的图像上半部分索引
        // 起始值最小为1
        // 第一个数据为无效数据
        setPrgRomPosition(buffer, 0x26553);
        put(buffer, x8552, 1, x8552.length - 1);
        // 精灵的图像下半部分索引
        // 起始值最小为1
        // 第一个数据为无效数据
        setPrgRomPosition(buffer, 0x2662A);
        put(buffer, x8629, 1, x8629.length - 1);
        return true;
    }

    /**
     * @return 通过地图属性生成一张 TileSet 图片
     * @see #generateTileSet(int, int, int, int, int, int, Color[][])
     */
    public BufferedImage generateTileSet(@NotNull MapProperties mapProperties, @Nullable Color[][] colors) {
        if (colors == null) {
            PaletteEditor paletteEditor = EditorManager.getEditor(PaletteEditor.class);
            PaletteList palettes = paletteEditor.getPalettes(mapProperties.palette);
            colors = palettes.toColors();
        }
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
            colors = PaletteList.BLACK_WHITE;
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

    /**
     * 生成一个世界地图的 TileSet 图片
     * 使用了3个0x40组合和颜色
     * 图片的大小为 256*192
     *
     * @param xXX 分割为4个，高位到低位分别为 x00、x40、x80、xC0
     */
    public BufferedImage generateWorldTileSet(int xXX,
                                              byte[][][] compositions, byte[][] colorIndexes,
                                              Color[][] colors) {
        int x00 = NumberR.at(xXX, 3);
        int x40 = NumberR.at(xXX, 2);
        int x80 = NumberR.at(xXX, 1);
        int xC0 = NumberR.at(xXX, 0);
        return generate(0x100, 0xC0, x00, x40, x80, xC0, compositions, colorIndexes, colors);
    }

    /**
     * 生成一个世界地图的 TileSet 图片
     * 使用了3个0x40组合和颜色
     * 图片的大小为 256*192
     *
     * @param xXX 分割为4个，高位到低位分别为 x00、x40、x80、xC0
     */
    public BufferedImage generateWorldTileSet(int xXX) {
        int x00 = NumberR.at(xXX, 3);
        int x40 = NumberR.at(xXX, 2);
        int x80 = NumberR.at(xXX, 1);
        int xC0 = NumberR.at(xXX, 0);
        PaletteList palettes = EditorManager.getEditor(PaletteEditor.class).getPalettes(0x9AD0);
        return generate(0x100, 0xC0, x00, x40, x80, xC0, worldCompositions, worldColorIndexes, palettes.toColors());
    }

    public BufferedImage generateSpriteTileSet(byte sprite) {
        return generateSpriteTileSet(sprite & 0xFF);
    }

    /**
     * 生成一张精灵的 TileSet 图片
     * 该算法不完整，所以有部分错误的图像
     * 图片的大小为
     */
    public BufferedImage generateSpriteTileSet(int sprite) {
        // 获取精灵使用的图块表
        byte[][][] spriteTiles = new byte[4][0x40][0x10];
        spriteTiles[0] = this.tiles[0x04]; // $00-$3F
        spriteTiles[1] = this.tiles[0x05]; // $40-$7F
        // 上面两个为固定的精灵表
        spriteTiles[2] = this.tiles[sprite]; // $80-$BF
        spriteTiles[3] = this.tiles[sprite + 1]; // $C0-$FF

        // 获取精灵调色板
        PaletteList palette = EditorManager.getEditor(PaletteEditor.class).getSpritePalette();

        BufferedImage bufferedImage = new BufferedImage(0x100, 0x100, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();

        // 精灵的的图像一共有 0x100 / 0x04 = 0x40 种
        // 0x04 为精灵的四个朝向
        // 精灵的id为0时，不显示精灵，所以直接跳过
        for (int spriteId = 1; spriteId < 0x40; spriteId++) {
            // 顺序为：上、下、左、右
            for (int direction = 0; direction < 0x04; direction++) {
                // 模拟内存地址$0150-$015F的数据
                byte bA597 = xA597[direction];
                byte bA59E = xA59E[spriteId];
                int x0150 = (bA59E & 0xFF) + (bA597 & 0xFF);
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
                                continue; //  精灵的第0颜色为透明
                            }

                            // 翻转保持调色板
                            if ((x0160 & 0B1000_0000) == 0B1000_0000) {
                                // 上下翻转替换调色板
                                if (y2 != 0) {
                                    // 上半部分使用下半部分的调色板
                                    graphics.setColor(palette.get((x0160 & 0B0000_1100) >>> 2).getToColor(l));
                                } else {
                                    // 下半部分使用上半部分的调色板
                                    graphics.setColor(palette.get(x0160 & 0B0000_0011).getToColor(l));
                                }
                            } else {
                                if (y2 != 0) {
                                    // 上半部分调色板
                                    graphics.setColor(palette.get(x0160 & 0B0000_0011).getToColor(l));
                                } else {
                                    // 下半部分调色板
                                    graphics.setColor(palette.get((x0160 & 0B0000_1100) >>> 2).getToColor(l));
                                }
                            }

                            int x3 = x + (x2 * 0x08) + k; // 精灵图像X值绘制位置
                            int y3 = y + (y2 * 0x08) + b; // 精灵图像Y值绘制位置
                            graphics.drawLine(x3, y3, x3, y3);
                        }
                    }
                }
            }
        }


        return bufferedImage;
    }

    /**
     * 生成指定大小的图片，图块集、图块组合、颜色组合
     */
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
