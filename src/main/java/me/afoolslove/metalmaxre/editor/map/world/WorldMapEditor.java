package me.afoolslove.metalmaxre.editor.map.world;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 世界地图编辑器
 *
 * @author AFoolLove
 */
public class WorldMapEditor extends AbstractEditor {
    /**
     * 世界地图图块索引偏移
     * 0x01 = 0x1000 byte
     */
    public static final int WORLD_MAP_TILES_INDEX_OFFSET_START = 0x00010 - 0x10;
    public static final int WORLD_MAP_TILES_INDEX_OFFSET_END = 0x0040F - 0x10;

    /**
     * 图块组索引
     * 0B0101_0101
     * 0(bit)：使用图块组A
     * 1(bit)：使用图块组B
     */
    public static final int WORLD_MAP_X00410_START = 0x00410 - 0x10;
    public static final int WORLD_MAP_X00410_END = 0x0060F - 0x10;

    /**
     * 图块组A，一组16（byte）个图块
     */
    public static final int WORLD_MAP_INDEX_A_START = 0x0C010 - 0x10;
    public static final int WORLD_MAP_INDEX_A_END = 0x00E50F - 0x10;

    /**
     * 图块组B，一组16（byte）个图块
     */
    public static final int WORLD_MAP_INDEX_B_START = 0x2A010 - 0x10;
    public static final int WORLD_MAP_INDEX_B_END = 0x2E00F - 0x10;

    /**
     * 相对图块索引
     */
    public static final int WORLD_MAP_INDEX_START = 0xBA010 - 0x10;
    public static final int WORLD_MAP_INDEX_END = 0xBB00F - 0x10;

    /**
     * 世界地图的怪物领域
     * 1Byte = 16*16小块 = 256个领域，固定无法变更
     */
    public static final int REALM_INDEX_START = 0x39233;
    public static final int REALM_INDEX_END = 0x39332;

    public byte[] indexOffsets = new byte[0x400]; // WORLD_MAP_TILES_INDEX_OFFSET_END - WORLD_MAP_TILES_INDEX_OFFSET_START + 1
    public byte[] x00410 = new byte[0x200]; // WORLD_MAP_X00410_END - WORLD_MAP_X00410_START + 1
    /**
     * 注：虽然写着0x03*0x100*0x10，但实际上只有 2.5*0x100*0x10
     */
    public byte[][][] indexA = new byte[0x03][0x100][0x10]; // WORLD_MAP_INDEX_A_END - WORLD_MAP_INDEX_A_START + 1
    public byte[][][] indexB = new byte[0x04][0x100][0x10]; // WORLD_MAP_INDEX_B_END - WORLD_MAP_INDEX_B_START + 1
    public byte[] index = new byte[0x1000]; // WORLD_MAP_INDEX_END - WORLD_MAP_INDEX_START + 1

    /**
     * 地图
     * 值为图块索引
     */
    public byte[][] map = new byte[0x100][0x100];
    /**
     * 图块集组合矩形化
     */
    public static final java.util.Map<Rectangle, Integer> DEFAULT_PIECES = java.util.Map.ofEntries(
            java.util.Map.entry(new Rectangle(0x88, 0x08, 0x78, 0x60), 0x84858889),
            java.util.Map.entry(new Rectangle(0x88, 0x68, 0x78, 0x98), 0x84858687),
            java.util.Map.entry(new Rectangle(0x08, 0x08, 0x80, 0x90), 0x848C8D8E),
            java.util.Map.entry(new Rectangle(0x08, 0x98, 0x60, 0x68), 0x84858A8B),
            java.util.Map.entry(new Rectangle(0x68, 0x98, 0x20, 0x68), 0x84858687),
            java.util.Map.entry(new Rectangle(0x88, 0x00, 0x78, 0x08), 0x84858687),
            java.util.Map.entry(new Rectangle(0x08, 0x00, 0x60, 0x08), 0x84858A8B),
            java.util.Map.entry(new Rectangle(0x68, 0x00, 0x20, 0x08), 0x84858687),
            java.util.Map.entry(new Rectangle(0x00, 0x00, 0x08, 0x08), 0x84858687),
            java.util.Map.entry(new Rectangle(0x00, 0x08, 0x08, 0x60), 0x84858889),
            java.util.Map.entry(new Rectangle(0x00, 0x68, 0x08, 0x98), 0x84858687)
    );


    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清除数据
        for (byte[] bytes : map) {
            Arrays.fill(bytes, (byte) 0x00);
        }
        Arrays.fill(indexOffsets, (byte) 0x00);
        Arrays.fill(x00410, (byte) 0x00);
        for (int i = 0; i < indexA.length; i++) {
            if (indexA[i] == null) {
                indexA[i] = new byte[0x100][0x10];
                for (int j = 0; j < indexA[i].length; j++) {
                    indexA[i][j] = new byte[0x10];
                }
            } else {
                for (byte[] bytes : indexA[i]) {
                    Arrays.fill(bytes, (byte) 0x00);
                }
            }
        }
        for (int i = 0; i < indexB.length; i++) {
            if (indexB[i] == null) {
                indexB[i] = new byte[0x100][0x10];
                for (int j = 0; j < indexB[i].length; j++) {
                    indexB[i][j] = new byte[0x10];
                }
            } else {
                for (byte[] bytes : indexB[i]) {
                    Arrays.fill(bytes, (byte) 0x00);
                }
            }
        }
        Arrays.fill(index, (byte) 0x00);

        // 读取世界地图图块索引偏移
        setPrgRomPosition(buffer, WORLD_MAP_TILES_INDEX_OFFSET_START);
        buffer.get(indexOffsets);
        // 读取图块组索引
        setPrgRomPosition(buffer, WORLD_MAP_X00410_START);
        buffer.get(x00410);
        // 读取图块组A
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_A_START);
        for (byte[][] index : indexA) {
            for (byte[] bytes : index) {
                buffer.get(bytes);
            }
        }
        // 读取图块组B
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_B_START);
        for (byte[][] index : indexB) {
            for (byte[] bytes : index) {
                buffer.get(bytes);
            }
        }
        // 读取图块索引
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_START);
        buffer.get(index);

        for (int i = 0; i < 0x1000; i++) {
            // 得到索引偏移，索引与indexOffsets的比例是4:1
            // 1 index = 2 bit indexOffsets
            int indexOffset = indexOffsets[i / 4] & 0xFF;

            // 得到 indexOffset 对应的2bit
            // i = 0
            // 0B1011_1111 -> 0B0000_0010
            // 0B1011_1111 >> (((4 * 2) - 2) - ((i % 4) * 2))
            // 0B1011_1111 >> (6 - (0 * 2))
            // 0B1011_1111 >> (6 - 0)
            // 0B1011_1111 >> 6 = 0B0000_0010 & 0B0000_0011
            // = 0B0000_0010
            indexOffset >>>= (6 - ((i % 4) * 2));
            // 只要D0和D1
            indexOffset &= 0B0000_0011;

            // 图块组索引，索引与x00410的比例是8:1
            int x410 = x00410[i / 8] & 0xFF;

            // 得到 x00410 对应的1bit
            // i = 0
            // 0B1000_0000 -> 0B0000_0001
            // 0B1000_0000 >> (8 - 1) - (i % 8)
            // 0B1000_0000 >> 7 - 0
            // 0B1000_0000 >> 7 = 0B0000_0001 & 0B0000_0001
            // = 0B0000_0001
            x410 >>>= (7 - (i % 8));
            // 只要D0
            x410 &= 0B0000_0001;

            // 得到数据偏移
            int offset = index[i] & 0xFF;
            // 判断使用图块组A还是图块组B
            byte[] tiles = (x410 == 0 ? indexB : indexA)[indexOffset][offset];

            int x = i % 64;
            int y = i / 64;
            map0(map, x, y, tiles);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {

        return false;
    }

    /**
     * 将0x10个byte以4*4的顺序写入map
     * e.g：
     * 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
     * to：
     * 0,1,2,3
     * 4,5,6,7
     * 8,9,10,11
     * 12,13,14,15
     */
    private static void map0(byte[][] map, int x, int y, byte[] bytes) {
        x *= 4;
        y *= 4;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                map[y + i][j + x] = bytes[i * 4 + j];
            }
        }
    }
}
