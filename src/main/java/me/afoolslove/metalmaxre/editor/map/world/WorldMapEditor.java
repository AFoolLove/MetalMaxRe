package me.afoolslove.metalmaxre.editor.map.world;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.events.EventTile;
import me.afoolslove.metalmaxre.editor.map.events.EventTilesEditor;
import me.afoolslove.metalmaxre.editor.map.events.WorldEventTile;
import me.afoolslove.metalmaxre.editor.map.events.WorldMapInteractiveEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.*;

/**
 * 世界地图编辑器
 * <p>
 * TODO 事件数据，事件数据只能读取图块组A的 0x0C010+0x200 - 0x0E510 的地方
 *
 * @author AFoolLove
 */
public class WorldMapEditor extends AbstractEditor<WorldMapEditor> {
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
    public static final int WORLD_MAP_INDEX_START = 0x3A010 - 0x10; // CHR
    public static final int WORLD_MAP_INDEX_END = 0x3B00F - 0x10; // CHR

    /**
     * 世界地图的怪物领域
     * 1Byte = 16*16小块 = 256个领域，固定无法变更
     */
    public static final int WORLD_MAP_REALM_INDEX_START = 0x39233;
    public static final int WORLD_MAP_REALM_INDEX_END = 0x39332;
    public static final int WORLD_MAP_REALM_INDEX_MAX_COUNT = 0x100;

    public List<Byte> realms = new ArrayList<>(WORLD_MAP_REALM_INDEX_MAX_COUNT);

    /**
     * index与indexOffsets的比例是 1byte：2bit
     */
    public byte[] indexOffsets = new byte[0x400]; // WORLD_MAP_TILES_INDEX_OFFSET_END - WORLD_MAP_TILES_INDEX_OFFSET_START + 1
    /**
     * index与x00410的比例是 8byte：1bit
     */
    public byte[] x00410 = new byte[0x200]; // WORLD_MAP_X00410_END - WORLD_MAP_X00410_START + 1
    /**
     * 注：虽然写着0x03*0x100*0x10，但实际上只有 2.5*0x100*0x10
     */
    public byte[][] indexA = new byte[0x250][0x10]; // WORLD_MAP_INDEX_A_END - WORLD_MAP_INDEX_A_START + 1
    public byte[][] indexB = new byte[0x400][0x10]; // WORLD_MAP_INDEX_B_END - WORLD_MAP_INDEX_B_START + 1
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
        realms.clear();

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
        setChrRomPosition(buffer, WORLD_MAP_INDEX_START);
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
            byte[] tiles = (x410 == 0 ? indexB : indexA)[(indexOffset * 0x100) + offset];

            int x = i % 64;
            int y = i / 64;
            map0(map, x, y, tiles);
        }

        // 读取领域索引
        setPrgRomPosition(buffer, WORLD_MAP_REALM_INDEX_START);
        for (int i = 0; i < WORLD_MAP_REALM_INDEX_MAX_COUNT; i++) {
            realms.add(buffer.get());
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 读取世界地图，使用set记录需要保留数据
        // 通过set设置未使用的数据为null

        // 对indexA、indexB去重，并将重复的数据设置为null，与x200重复时保留x200的数据

        // 将世界地图的事件和罗克东边涨潮退潮4个数据添加进 x200
        // 储存x200，如果储存不下，移动非必须的x200数据到x200以外的地方，再次存入

        // 如果还是存不下，那没救了，等死吧

        var eventTilesEditor = EditorManager.getEditor(EventTilesEditor.class);

        // 必须存入x200的4*4tiles
        Map<String, byte[]> x200 = new HashMap<>();
        // 获取罗克东边涨潮退潮的4个4*4tile
        WorldMapInteractiveEvent worldMapInteractiveEvent = eventTilesEditor.getWorldMapInteractiveEvent();
        if (worldMapInteractiveEvent != null) {
            x200.put(Arrays.toString(worldMapInteractiveEvent.aFalse), worldMapInteractiveEvent.aFalse);
            x200.put(Arrays.toString(worldMapInteractiveEvent.aTrue), worldMapInteractiveEvent.aTrue);
            x200.put(Arrays.toString(worldMapInteractiveEvent.bFalse), worldMapInteractiveEvent.bFalse);
            x200.put(Arrays.toString(worldMapInteractiveEvent.bTrue), worldMapInteractiveEvent.bTrue);
        }
        x200.remove("null");

        // 格式化地图数据
        byte[][][] mapTiles = new byte[0x40][0x40][0x10];
        for (int y = 0; y < 0x40; y++) {
            for (int x = 0; x < 0x40; x++) {
                mapTiles[y][x] = map1(map, x, y);
            }
        }
        // 新的4*4tile
        List<byte[]> newTiles = new ArrayList<>();
        // 可以保留的4*4tile
        Set<Integer> keepTilesA = new HashSet<>();
        Set<Integer> keepTilesB = new HashSet<>();
        for (int y = 0; y < 0x40; y++) {
            keepTilesX:
            for (int x = 0; x < 0x40; x++) {
                byte[] tiles = mapTiles[y][x];
                for (int i = 0; i < indexA.length; i++) {
                    if (Arrays.equals(indexA[i], tiles)) {
                        keepTilesA.add(i);
                        // 读取下一个
                        continue keepTilesX;
                    }
                }
                // 没有在indexA中找到，从indexB找
                for (int i = 0; i < indexB.length; i++) {
                    if (Arrays.equals(indexB[i], tiles)) {
                        keepTilesB.add(i);
                        continue keepTilesX;
                    }
                }

                // 都没有找到，添加到newTiles中
                // 添加之前判断是否已经添加过了
                for (byte[] newTile : newTiles) {
                    if (Arrays.equals(tiles, newTile)) {
                        // 已经添加过了
                        continue keepTilesX;
                    }
                }
                // 添加到newTiles
                newTiles.add(tiles);
            }
        }

        // 将keepTiles以外的数据设置为null
        for (int index = 0; index < indexA.length; index++) {
            if (!keepTilesA.contains(index)) {
                // 设置为null
                indexA[index] = null;
            }
        }
        for (int index = 0; index < indexB.length; index++) {
            if (!keepTilesB.contains(index)) {
                // 设置为null
                indexB[index] = null;
            }
        }

        // indexA、indexB 去重，与x200重复时保留x200的数据
        for (int i = 0, length = indexA.length + indexB.length; i < length; i++) {
            byte[][] index = i < indexA.length ? indexA : indexB;
            int i1 = i < indexA.length ? i : i - indexA.length;
            if (index[i1] == null) {
                continue;
            }
            for (int j = i + 1; j < length; j++) {
                byte[][] index1 = j < indexA.length ? indexA : indexB;
                int j1 = j < indexA.length ? j : j - indexA.length;
                if (index1[j1] == null) {
                    continue;
                }
                if (Arrays.equals(index[i1], index1[j1])) {
                    if (j1 >= 0x200) {
                        // 0xC010+0x2000 的数据不变
                        index[i1] = null;
                    } else {
                        index1[j1] = null;
                    }
                }
            }
        }

        // 添加世界事件图块（4*4tile）到x200
        for (List<EventTile> eventTiles : eventTilesEditor.getWorldEventTile().values()) {
            for (EventTile eventTile : eventTiles) {
                if (eventTile instanceof WorldEventTile worldEventTile) {
                    x200.put(Arrays.toString(worldEventTile.getTiles()), worldEventTile.getTiles());
                    // 将事件对应的4*4tile一并储存到x200
                    byte[] tiles = getTiles(worldEventTile.intX(), worldEventTile.intY());
                    x200.put(Arrays.toString(tiles), tiles);
                }
            }
        }

        // 储存x200
        List<byte[]> tempX200 = new ArrayList<>(x200.values());
        Iterator<byte[]> x200Iterator = tempX200.iterator();
        x200:
        while (x200Iterator.hasNext()) {
            byte[] tiles = x200Iterator.next();
            // 判断x200是否已经存在，如果不存在就找位置添加
            for (int index = 0x200; index < indexA.length; index++) {
                if (Arrays.equals(indexA[index], tiles)) {
                    // 已经存在
                    x200Iterator.remove();
                    continue x200;
                }
            }
            // 不存在就找地方存入
            for (int index = 0x200; index < indexA.length; index++) {
                if (indexA[index] == null) {
                    // 存入
                    indexA[index] = tiles;
                    x200Iterator.remove();
                    continue x200;
                }
            }
            // 没地方存了，跳出
            break;
        }

        // 没能完全储存
        // 将非必须存入x200的数据移动到其它地方
        if (!tempX200.isEmpty()) {
            // 需要被移动的数量
            int count = tempX200.size();
            x200:
            for (int index = 0x200; index < indexA.length; index++) {
                if (count == 0 || !x200Iterator.hasNext()) {
                    break;
                }
                for (byte[] tiles : x200.values()) {
                    if (Arrays.equals(indexA[index], tiles)) {
                        // 跳过必须存入的4*4tile
                        continue x200;
                    }
                }
                // 非必须存入的4*4tile
                // 移动到newTiles中
                newTiles.add(indexA[index]);
                // 替换
                indexA[index] = x200Iterator.next();
                x200Iterator.remove();
                // 下一个
                count--;
            }
        }

        if (!tempX200.isEmpty()) {
            // 什么？还没存完？解决不了，等死吧
            for (byte[] tiles : tempX200) {
                System.out.println("无法储存的世界地图4*4tile数据，可能会导致地图异常：" + Arrays.toString(tiles));
            }
        }

        // 此处可能需要再次去重


        // 将新的4*4tiles找地方存入
        Iterator<byte[]> newTilesIterator = newTiles.iterator();
        for (int index = 0; index < indexA.length; index++) {
            if (indexA[index] == null && newTilesIterator.hasNext()) {
                indexA[index] = newTilesIterator.next();
            }
        }
        for (int index = 0; index < indexB.length; index++) {
            if (indexB[index] == null && newTilesIterator.hasNext()) {
                indexB[index] = newTilesIterator.next();
            }
        }


        // 将indexA和indexB中值为null的数据替换为fillTiles
        // 用来填充null的4*4tile
        byte[] fillTiles = new byte[0x10];
        Arrays.fill(fillTiles, (byte) 0xFF);
        // 空闲的4*4tile数量
        int fillCount = 0;
        for (int i = 0; i < indexA.length; i++) {
            if (indexA[i] == null) {
                indexA[i] = fillTiles;
                fillCount++;
            }
        }
        for (int i = 0; i < indexB.length; i++) {
            if (indexB[i] == null) {
                indexB[i] = fillTiles;
                fillCount++;
            }
        }

        System.out.printf("世界地图编辑器：剩余%d个空闲4*4tile\n", fillCount);

        for (int i = 0; i < 0x1000; i++) {
            int x = i % 0x40;
            int y = i / 0x40;
            // 获取4*4图块
            byte[] bytes = mapTiles[y][x];

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
                        break;
                    }
                }
            }

            if (indexOffset == null) {
                // 找不到？不存在的
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
        setChrRomPosition(buffer, WORLD_MAP_INDEX_START);
        buffer.put(index);
        // 写入领域索引
        setPrgRomPosition(buffer, WORLD_MAP_REALM_INDEX_START);
        for (int i = 0; i < WORLD_MAP_REALM_INDEX_MAX_COUNT; i++) {
            buffer.put(realms.get(i));
        }
        return true;
    }

    /**
     * @return 4*4tile
     */
    public byte[] getTiles(int x, int y) {
        return map1(map, x, y);
    }

    public List<Byte> getRealms() {
        return realms;
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
