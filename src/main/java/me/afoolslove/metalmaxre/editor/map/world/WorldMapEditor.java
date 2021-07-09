package me.afoolslove.metalmaxre.editor.map.world;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.events.EventTile;
import me.afoolslove.metalmaxre.editor.map.events.EventTilesEditor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;

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
    public byte[][] indexA = new byte[0x250][0x10]; // WORLD_MAP_INDEX_A_END - WORLD_MAP_INDEX_A_START + 1
    public byte[][] indexB = new byte[0x400][0x10]; // WORLD_MAP_INDEX_B_END - WORLD_MAP_INDEX_B_START + 1
    public byte[] index = new byte[0x1000]; // WORLD_MAP_INDEX_END - WORLD_MAP_INDEX_START + 1


    public byte[] AindexOffsets = new byte[0x400]; // WORLD_MAP_TILES_INDEX_OFFSET_END - WORLD_MAP_TILES_INDEX_OFFSET_START + 1
    public byte[] Ax00410 = new byte[0x200]; // WORLD_MAP_X00410_END - WORLD_MAP_X00410_START + 1
    public byte[][] AindexA = new byte[0x250][0x10]; // WORLD_MAP_INDEX_A_END - WORLD_MAP_INDEX_A_START + 1
    public byte[][] AindexB = new byte[0x400][0x10]; // WORLD_MAP_INDEX_B_END - WORLD_MAP_INDEX_B_START + 1
    public byte[] Aindex = new byte[0x1000]; // WORLD_MAP_INDEX_END - WORLD_MAP_INDEX_START + 1

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
                indexA[i] = new byte[0x10];
            } else {
                Arrays.fill(indexA[i], (byte) 0x00);
            }
        }
        for (int i = 0; i < indexB.length; i++) {
            if (indexB[i] == null) {
                indexB[i] = new byte[0x10];
            } else {
                Arrays.fill(indexB[i], (byte) 0x00);
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
        for (byte[] index : indexA) {
            buffer.get(index);
        }
        // 读取图块组B
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_B_START);
        for (byte[] index : indexB) {
            buffer.get(index);
        }
        // 读取图块索引
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_START);
        buffer.get(index);


        AindexOffsets = Arrays.copyOf(indexOffsets, indexOffsets.length);
        Ax00410 = Arrays.copyOf(x00410, x00410.length);
        AindexA = new byte[0x250][0x10];
        AindexB = new byte[0x400][0x10];

        for (int i = 0; i < AindexA.length; i++) {
            AindexA[i] = Arrays.copyOf(indexA[i], indexA[i].length);
        }

        for (int i = 0; i < AindexB.length; i++) {
            AindexB[i] = Arrays.copyOf(indexB[i], indexB[i].length);
        }

        Aindex = Arrays.copyOf(index, index.length);

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
            byte[] tiles = (x410 == 0 ? indexB : indexA)[(indexOffset * 0x100) + offset];

            int x = i % 64;
            int y = i / 64;
            map0(map, x, y, tiles);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {

        // 留下未使用的4*4tile
        var tempIndexA = new HashSet<Integer>();
        var tempIndexB = new HashSet<Integer>();
        // 新的4*4tile
        var newIndex = new HashMap<byte[], Set<Integer>>();

        for (int i = 0; i < indexA.length; i++) {
            tempIndexA.add(i);
        }
        for (int i = 0; i < indexB.length; i++) {
            tempIndexB.add(i);
        }

        for (int i = 0; i < 0x1000; i++) {
            int x = i % 64;
            int y = i / 64;
            // 获取4*4图块
            byte[] bytes = map1(map, x, y);

            // 索引偏移
            Integer indexOffset = null;
            // 使用图块组A还是图块组B
            int x410 = 0;
            // 数据偏移
            int offset = 0;


            for (int index = indexA.length - 1; index >= 0; index--) {
                byte[] tiles = indexA[index];
                if (Arrays.equals(tiles, bytes)) {
                    indexOffset = (index / 0x100) & 0B0000_0011;
                    // 使用图块组A
                    x410 = 0B0000_0001;
                    offset = index % 0x100;

                    tempIndexA.remove(index);
                    break;
                }
            }

            if (indexOffset == null) {
                for (int index = indexB.length - 1; index >= 0; index--) {
                    byte[] tiles = indexB[index];
                    if (Arrays.equals(tiles, bytes)) {
                        indexOffset = (index / 0x100) & 0B0000_0011;
                        // 使用图块组B
                        x410 = 0B0000_0000;
                        offset = index % 0x100;

                        tempIndexB.remove(index);
                        break;
                    }
                }
            }

            if (indexOffset == null) {
                // 添加到新的4*4tile set中
                for (Map.Entry<byte[], Set<Integer>> entry : newIndex.entrySet()) {
                    // 如果是已经添加过了，只添加值
                    if (Arrays.equals(entry.getKey(), bytes)) {
                        bytes = entry.getKey();
                        entry.getValue().add(i);
                        break;
                    }
                }
                // 如果没有添加过，创建一个HashSet并传入值后put
                if (!newIndex.containsKey(bytes)) {
                    HashSet<Integer> set = new HashSet<>();
                    set.add(i);
                    newIndex.put(bytes, set);
                }
                continue;
            }


            // 配置索引偏移，索引与indexOffsets的比例是4:1
            // 1 index = 2 bit indexOffsets
            indexOffset <<= (6 - ((i % 4) * 2));

            int tempBit = 0B0000_0011;
            tempBit <<= (6 - ((i % 4) * 2));
            // 反码，目标2bit为0，其它bit为1
            tempBit = ~tempBit;
            // 将目标bit置0
            indexOffsets[i / 4] &= tempBit;
            // 加入到数据中
            indexOffsets[i / 4] |= indexOffset.byteValue();

            // 图块组索引，索引与x00410的比例是8:1
            x410 <<= (7 - (i % 8));

            tempBit = 0B0000_0001;
            tempBit <<= (7 - (i % 8));
            // 反码，目标1bit为0，其它bit为1
            tempBit = ~tempBit;
            // 将目标bit置0
            x00410[i / 8] &= tempBit;
            // 加入到数据中
            x00410[i / 8] |= x410;

            // 配置数据偏移
            index[i] = (byte) (offset & 0xFF);
        }

        // 移除4*4的事件图块
        // 事件图块只使用indexA，offset = 0x200
        for (List<EventTile> eventTiles : EditorManager.getEditor(EventTilesEditor.class).getWorldEventTile().values()) {
            for (EventTile eventTile : eventTiles) {
                tempIndexA.remove(0x200 + (eventTile.tile & 0xFF));
            }
        }

        // 计算差异，将未使用的4*4tile替换为新的4*4tile，当前无法添加新的4*4tile和移除未使用的4*4tile
        var tempIndexIteratorA = tempIndexA.iterator();
        var tempIndexIteratorB = tempIndexB.iterator();

//        var strs = new HashMap<>();
//        var s = new HashMap<>();
//        var t = new HashMap<>();
//
//        for (int i = 0; i < indexA.length; i++) {
//            var a = Arrays.toString(indexA[i]);
//            if (strs.containsKey(a)) {
//                s.put(i, indexA[i]);
//            } else {
//                strs.put(a, indexA[i]);
//            }
//        }
//
//        for (int i = 0; i < indexB.length; i++) {
//            var a = Arrays.toString(indexB[i]);
//            if (strs.containsKey(a)) {
//                t.put(i, indexB[i]);
//            } else {
//                strs.put(a, indexB[i]);
//            }
//        }
//
//        var te = new ArrayList<>();
//
//        for (int i = 0; i < 0x1000; i++) {
//            int x = i % 64;
//            int y = i / 64;
//            // 获取4*4图块
//            byte[] bytes = map1(map, x, y);
//            Iterator<Integer> iterator = tempIndexA.iterator();
//            while (iterator.hasNext()){
//                Integer a = iterator.next();
//                if (Arrays.equals(indexA[a],bytes)){
//                    te.add(i);
//                    te.add(indexA[a]);
//                    iterator.remove();
////                    break;
//                }
//            }
//            iterator = tempIndexB.iterator();
//            while (iterator.hasNext()){
//                Integer a = iterator.next();
//                if (Arrays.equals(indexB[a],bytes)){
//                    te.add(i);
//                    te.add(indexB[a]);
//                    iterator.remove();
////                    break;
//                }
//            }
//        }

        // 未使用的4*4tile能装下多少新的4*4tile
        int count = Math.min(tempIndexA.size() + tempIndexB.size(), newIndex.size());

        if (count > 0) {
            Iterator<Map.Entry<byte[], Set<Integer>>> newIndexIterator = newIndex.entrySet().iterator();
            while (count > 0 && newIndexIterator.hasNext()) {
                Map.Entry<byte[], Set<Integer>> entry = newIndexIterator.next();

                for (int i : entry.getValue()) {
                    // 索引偏移
                    int indexOffset = 0;
                    // 使用图块组A还是图块组B
                    int x410 = 0;
                    // 数据偏移
                    int offset = 0;

                    if (tempIndexIteratorA.hasNext()) {
                        x410 = 0B0000_0001;
                        int data = tempIndexIteratorA.next();
                        indexOffset = data / 0x100;
                        offset = data % 0x100;
                        count--;

                        indexA[data] = entry.getKey();
                    } else if (tempIndexIteratorB.hasNext()) {
                        x410 = 0B0000_0000;
                        int data = tempIndexIteratorB.next();
                        indexOffset = data / 0x100;
                        offset = data % 0x100;
                        count--;

                        indexB[data] = entry.getKey();
                    }

                    if (count < 0) {
                        // 无法添加剩余的4*4tile
                        break;
                    }

                    // 配置索引偏移，索引与indexOffsets的比例是4:1
                    // 1 index = 2 bit indexOffsets
                    indexOffset <<= (6 - ((i % 4) * 2));

                    int tempBit = 0B0000_0011;
                    tempBit <<= (6 - ((i % 4) * 2));
                    // 反码，目标2bit为0，其它bit为1
                    tempBit = ~tempBit;
                    // 将目标bit置0
                    indexOffsets[i / 4] &= tempBit;
                    // 加入到数据中
                    indexOffsets[i / 4] |= indexOffset;

                    // 图块组索引，索引与x00410的比例是8:1
                    x410 <<= (7 - (i % 8));

                    tempBit = 0B0000_0001;
                    tempBit <<= (7 - (i % 8));
                    // 反码，目标1bit为0，其它bit为1
                    tempBit = ~tempBit;
                    // 将目标bit置0
                    x00410[i / 8] &= tempBit;
                    // 加入到数据中
                    x00410[i / 8] |= x410;

                    // 配置数据偏移
                    index[i] = (byte) (offset & 0xFF);
                }
            }
        }

//        var setsv = new HashSet<>();
//        for (int i = 0; i < indexOffsets.length; i++) {
//            if (indexOffsets[i] != AindexOffsets[i]) {
//                setsv.add(i);
//                System.out.print("");
//            }
//        }

        // 写入世界地图图块索引偏移
        setPrgRomPosition(buffer, WORLD_MAP_TILES_INDEX_OFFSET_START);
        buffer.put(indexOffsets);
        // 写入图块组索引
        setPrgRomPosition(buffer, WORLD_MAP_X00410_START);
        buffer.put(x00410);
        // 写入图块组A
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_A_START);
        for (byte[] tiles : indexA) {
            buffer.put(tiles);
        }
        // 写入图块组B
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_B_START);
        for (byte[] tiles : indexB) {
            buffer.put(tiles);
        }
        // 写入图块索引
        setPrgRomPosition(buffer, WORLD_MAP_INDEX_START);
        buffer.put(index);
        return true;
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
        for (int offsetY = 0; offsetY < 4; offsetY++) {
            for (int offsetX = 0; offsetX < 4; offsetX++) {
                map[y + offsetY][offsetX + x] = bytes[offsetY * 4 + offsetX];
            }
        }
    }

    /**
     * 将4*4的数据组合为0x10的数据
     * e.g：
     * 0,1,2,3
     * 4,5,6,7
     * 8,9,10,11
     * 12,13,14,15
     * to：
     * 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
     */
    private static byte[] map1(byte[][] map, int x, int y) {
        x *= 4;
        y *= 4;
        byte[] bytes = new byte[0x10];
        for (int offsetY = 0; offsetY < 4; offsetY++) {
            for (int offsetX = 0; offsetX < 4; offsetX++) {
                bytes[(offsetY * 4) + offsetX] = map[y + offsetY][x + offsetX];
            }
        }
        return bytes;
    }
}
