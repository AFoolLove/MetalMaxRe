package me.afoolslove.metalmaxre.editor.map.tileset;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

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
                byte[] tileComposition = tileCompositions[i] != null ? tileCompositions[i] : new byte[0x40];
                tileCompositions[i] = tileComposition;
                buffer.get(tileComposition);
            }
        }

        // 读取tile的特性和调色板 0x00-0x37
        // 0x01byte = 1tile特性和调色板
        // 0x40tile = 0x40（0x00、0x80、0xC0）
        setChrRomPosition(buffer, TILE_SET_COLOR_INDEX_START_OFFSET);
        for (int count = 0; count < 0x37; count++) {
            byte[] color = colorIndex[count] == null ? colorIndex[count] : new byte[0x40];
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
}
