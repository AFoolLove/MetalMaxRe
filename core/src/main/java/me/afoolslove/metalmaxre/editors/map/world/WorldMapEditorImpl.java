package me.afoolslove.metalmaxre.editors.map.world;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.editors.map.events.EventTile;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.events.WorldEventTile;
import me.afoolslove.metalmaxre.editors.map.events.WorldMapInteractiveEvent;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.*;

/**
 * 世界地图编辑器
 *
 * @author AFoolLove
 */
public class WorldMapEditorImpl extends RomBufferWrapperAbstractEditor implements IWorldMapEditor {
    private final DataAddress worldMapTilesIndexAddress;
    private final DataAddress worldMapX00410Address;
    private final DataAddress worldMapIndexAAddress;
    private final DataAddress worldMapIndexBAddress;
    private final DataAddress worldMapIndexCAddress;
    private final DataAddress worldMapIndexAddress;
    private final DataAddress worldMapMinesAddress;
    private final DataAddress worldMapOutLineAddress;
    private final DataAddress worldMapBackLineAddress;

    /**
     * 图块集组合矩形化
     */
    public static final Map<Rectangle, Integer> DEFAULT_PIECES = Map.ofEntries(
            Map.entry(new Rectangle(0x88, 0x08, 0x78, 0x60), 0x84858889),
            Map.entry(new Rectangle(0x88, 0x68, 0x78, 0x98), 0x84858687),
            Map.entry(new Rectangle(0x08, 0x08, 0x80, 0x90), 0x848C8D8E),
            Map.entry(new Rectangle(0x08, 0x98, 0x60, 0x68), 0x84858A8B),
            Map.entry(new Rectangle(0x68, 0x98, 0x20, 0x68), 0x84858687),
            Map.entry(new Rectangle(0x88, 0x00, 0x78, 0x08), 0x84858687),
            Map.entry(new Rectangle(0x08, 0x00, 0x60, 0x08), 0x84858A8B),
            Map.entry(new Rectangle(0x68, 0x00, 0x20, 0x08), 0x84858687),
            Map.entry(new Rectangle(0x00, 0x00, 0x08, 0x08), 0x84858687),
            Map.entry(new Rectangle(0x00, 0x08, 0x08, 0x60), 0x84858889),
            Map.entry(new Rectangle(0x00, 0x68, 0x08, 0x98), 0x84858687)
    );

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
    public byte[][] indexC = new byte[0x400][0x10]; // WORLD_MAP_INDEX_C_END - WORLD_MAP_INDEX_C_START + 1
    public byte[] index = new byte[0x1000]; // WORLD_MAP_INDEX_END - WORLD_MAP_INDEX_START + 1

    /**
     * 地图，值为图块索引
     */
    public byte[][] map = new byte[0x100][0x100];

    /**
     * 地图中的4个地雷
     */
    private final List<MapPoint> mines = new ArrayList<>();

    /**
     * 出航的航线<p>
     * K: line<p>
     * V：目的地
     */
    private final Map.Entry<List<MapPoint>, MapPoint> shippingLineOut = Map.entry(new ArrayList<>(0x1), new MapPoint());
    /**
     * 归航的航线<p>
     * K: line<p>
     * V：目的地
     */
    private final Map.Entry<List<MapPoint>, MapPoint> shippingLineBack = Map.entry(new ArrayList<>(0x10), new MapPoint());

    public WorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x00010 - 0x10, 0x0040F - 0x10),
                DataAddress.fromPRG(0x00410 - 0x10, 0x0060F - 0x10),
                DataAddress.fromPRG(0x0C010 - 0x10, 0x00E50F - 0x10),
                DataAddress.fromPRG(0x2A010 - 0x10, 0x2E00F - 0x10),
                DataAddress.fromPRG(0x50510 - 0x10),
                DataAddress.fromCHR(0x3A010 - 0x10, 0x3B00F - 0x10),
                DataAddress.fromPRG(0x35EC1 - 0x10),
                DataAddress.fromPRG(0x258C6 - 0x10),
                DataAddress.fromPRG(0x258EA - 0x10));
    }

    public WorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                              DataAddress worldMapTilesIndexAddress,
                              DataAddress worldMapX00410Address,
                              DataAddress worldMapIndexAAddress,
                              DataAddress worldMapIndexBAddress,
                              DataAddress worldMapIndexCAddress,
                              DataAddress worldMapIndexAddress,
                              DataAddress worldMapMinesAddress,
                              DataAddress worldMapOutLineAddress,
                              DataAddress worldMapBackLineAddress) {
        super(metalMaxRe);
        this.worldMapTilesIndexAddress = worldMapTilesIndexAddress;
        this.worldMapX00410Address = worldMapX00410Address;
        this.worldMapIndexAAddress = worldMapIndexAAddress;
        this.worldMapIndexBAddress = worldMapIndexBAddress;
        this.worldMapIndexCAddress = worldMapIndexCAddress;
        this.worldMapIndexAddress = worldMapIndexAddress;
        this.worldMapMinesAddress = worldMapMinesAddress;
        this.worldMapOutLineAddress = worldMapOutLineAddress;
        this.worldMapBackLineAddress = worldMapBackLineAddress;
    }

    @Editor.Load
    public void onLoad(@NotNull ByteBuffer buffer) {
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
        for (int i = 0; i < indexC.length; i++) {
            if (indexC[i] == null) {
                indexC[i] = new byte[0x10];
            } else {
                Arrays.fill(indexC[i], (byte) 0x00);
            }
        }
        Arrays.fill(index, (byte) 0x00);
        mines.clear();

        // 读取世界地图图块索引偏移
        position(getWorldMapTilesIndexAddress());
        getBuffer().get(indexOffsets);
        // 读取图块组索引
        position(getWorldMapX00410Address());
        getBuffer().get(x00410);
        // 读取图块组A
        position(getWorldMapIndexAAddress());
        for (byte[] index : indexA) {
            getBuffer().get(index);
        }
        // 读取图块组B
        position(getWorldMapIndexBAddress());
        for (byte[] index : indexB) {
            getBuffer().get(index);
        }
        // 读取图块组C
        position(getWorldMapIndexCAddress());
        for (byte[] index : indexC) {
            getBuffer().get(index);
        }
        // 读取图块索引
        position(getWorldMapIndexAddress());
        getBuffer().get(index);

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
            byte[][] tempIndex = x410 == 0 ? indexB : indexA;
            offset += indexOffset * 0x100;

            byte[] tiles;
            if (offset >= tempIndex.length) {
                // 超出安全可编辑的数据
                if (tempIndex == indexA) {
                    tiles = indexC[offset - tempIndex.length];
                } else {
                    position(getWorldMapIndexBAddress(), offset * 0x10);
                    tiles = new byte[0x10];
                    getBuffer().get(tiles);
                    System.out.println("世界地图编辑器：警告！使用了安全编辑范围外的地图数据 " + Arrays.toString(tiles));
                }
            } else {
                tiles = tempIndex[offset];
            }

            int x = i % 0x40;
            int y = i / 0x40;
            IWorldMapEditor.map0(map, x, y, tiles);
        }

        // 读取地雷
        position(getWorldMapMinesAddress());
        byte[] mineXs = new byte[0x04];
        byte[] mineYs = new byte[0x04];
        getBuffer().get(mineXs);
        getBuffer().get(mineYs);
        for (int i = 0; i < 0x04; i++) {
            getMines().add(new MapPoint(0x00, mineXs[i], mineYs[i]));
        }

        // 读取出航航线
        position(getWorldMapOutLineAddress());
        // 坐标是相对路径，进入世界地图的坐标开始算起
        List<MapPoint> linePoint = getShippingLineOut().getKey();
        linePoint.clear();

        while (linePoint.size() < 0x10) {
            byte action = getBuffer().get();
            if (action == 0x5E) {
                // 读取到目的地数据，立即结束
                break;
            }
            switch (action) {
                case 0x50 -> { // 上
                    // 移动格数
                    linePoint.add(new MapPoint(0x00, 0x00, -getBuffer().getToInt()));
                }
                case 0x51 -> { // 下
                    linePoint.add(new MapPoint(0x00, 0x00, getBuffer().getToInt()));
                }
                case 0x52 -> { // 左
                    linePoint.add(new MapPoint(0x00, -getBuffer().getToInt(), 0x00));
                }
                case 0x53 -> { // 右
                    linePoint.add(new MapPoint(0x00, getBuffer().getToInt(), 0x00));
                }
            }
        }
        if (linePoint.size() == 0x10) {
            getBuffer().get(); // 所有路径点全部使用，跳过 0xE5
        }
        // 读取出航目的地
        getShippingLineOut().getValue().set(getBuffer().get(), getBuffer().get(), getBuffer().get());

        // 读取归航航线
        position(getWorldMapBackLineAddress());
        linePoint = getShippingLineBack().getKey();
        linePoint.clear();

        while (linePoint.size() < 0x10) {
            byte action = getBuffer().get();
            if (action == 0x5E) {
                // 读取到目的地数据，立即结束
                break;
            }
            switch (action) {
                case 0x50 -> { // 上
                    // 移动格数
                    linePoint.add(new MapPoint(0x00, 0x00, -getBuffer().getToInt()));
                }
                case 0x51 -> { // 下
                    linePoint.add(new MapPoint(0x00, 0x00, getBuffer().getToInt()));
                }
                case 0x52 -> { // 左
                    linePoint.add(new MapPoint(0x00, -getBuffer().getToInt(), 0x00));
                }
                case 0x53 -> { // 右
                    linePoint.add(new MapPoint(0x00, getBuffer().getToInt(), 0x00));
                }
            }
        }
        if (linePoint.size() == 0x10) {
            getBuffer().get(); // 所有路径点全部使用，跳过 0xE5
        }
        // 读取归航目的地
        getShippingLineBack().getValue().set(getBuffer().get(), getBuffer().get(), getBuffer().get());
    }

    @Editor.Apply
    public void onApply(@Editor.QuoteOnly IEventTilesEditor eventTilesEditor) {
        // 读取世界地图，使用set记录需要保留数据
        // 通过set设置未使用的数据为null

        // 对indexA、indexB去重，并将重复的数据设置为null，与x200重复时保留x200的数据

        // 将世界地图的事件和罗克东边涨潮退潮4个数据添加进 x200
        // 储存x200，如果储存不下，移动非必须的x200数据到x200以外的地方，再次存入

        // 如果还是存不下，那没救了，等死吧

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
                mapTiles[y][x] = IWorldMapEditor.getTiles4x4(map, x, y);
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
                    byte[] tiles = IWorldMapEditor.getTiles4x4(map, worldEventTile.intX(), worldEventTile.intY());
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


        if (!newTiles.isEmpty()) {
            // 将剩下的新的4*4tiles找地方存入
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
        }

        // 因为重新排序过，所以要更新事件的tile索引
        for (int index = 0x200; index < indexA.length; index++) {
            byte[] tempIndexA = indexA[index];
            final int tempIndex = index;
            // 将所有使用该4*4tiles的事件全部更新
            eventTilesEditor.getWorldEventTile().values().forEach(eventTiles -> {
                for (EventTile eventTile : eventTiles) {
                    if (eventTile instanceof WorldEventTile worldEventTile) {
                        if (Arrays.equals(worldEventTile.getTiles(), tempIndexA)) {
                            worldEventTile.setTile(tempIndex);
                        }
                    }
                }
            });
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
            // 获取4*4tiles
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
        position(getWorldMapTilesIndexAddress());
        getBuffer().put(indexOffsets);
        // 写入图块组索引
        position(getWorldMapX00410Address());
        getBuffer().put(x00410);
        // 写入图块组A
        position(getWorldMapIndexAAddress());
        for (byte[] tiles : indexA) {
            getBuffer().put(tiles);
        }
        // 写入图块组B
        position(getWorldMapIndexBAddress());
        for (byte[] tiles : indexB) {
            getBuffer().put(tiles);
        }
        // 写入图块索引
        position(getWorldMapIndexAddress());
        getBuffer().put(index);

        // 写入4个地雷坐标
        position(getWorldMapMinesAddress());
        byte[] mineXs = new byte[0x04];
        byte[] mineYs = new byte[0x04];
        for (int i = 0, size = Math.min(0x04, mines.size()); i < size; i++) {
            MapPoint minePoint = mines.get(i);
            mineXs[i] = minePoint.getX();
            mineYs[i] = minePoint.getY();
        }
        getBuffer().put(mineXs);
        getBuffer().put(mineYs);

        // 写入出航路径点和目的地
        position(getWorldMapOutLineAddress());
        for (int i = 0, size = Math.min(0x10, getShippingLineOut().getKey().size()); i < size; i++) {
            MapPoint linePoint = getShippingLineOut().getKey().get(i);
            // X或Y等于0，就是另一个的方向
            if (linePoint.getX() == 0x00) {
                // 上或下移动
                if ((linePoint.getY() & 0B1000_0000) == 0x00) {
                    // 不是负数，下方移动
                    getBuffer().put(0x51);
                } else {
                    // 是负数，上方移动
                    getBuffer().put(0x50);
                }
                getBuffer().put(Math.abs(linePoint.getY())); // 得到正数的移动格数
            } else {
                // 左或右移动
                if ((linePoint.getX() & 0B1000_0000) == 0x00) {
                    // 不是负数，右方移动
                    getBuffer().put(0x53);
                } else {
                    // 是负数，左方移动
                    getBuffer().put(0x52);
                }
                getBuffer().put(Math.abs(linePoint.getX())); // 得到正数的移动格数
            }
        }
        // 写入出航目的地
        getBuffer().put(0x5E);
        getBuffer().put(getShippingLineOut().getValue().getMap());
        getBuffer().put(getShippingLineOut().getValue().getX());
        getBuffer().put(getShippingLineOut().getValue().getY());

        // 写入归航路径点和目的地
        position(getWorldMapBackLineAddress());
        for (int i = 0, size = Math.min(0x10, getShippingLineBack().getKey().size()); i < size; i++) {
            MapPoint linePoint = getShippingLineBack().getKey().get(i);
            // X或Y等于0，就是另一个的方向
            if (linePoint.getX() == 0x00) {
                // 上或下移动
                if ((linePoint.getY() & 0B1000_0000) == 0x00) {
                    // 不是负数，下方移动
                    getBuffer().put(0x51);
                } else {
                    // 是负数，上方移动
                    getBuffer().put(0x50);
                }
                getBuffer().put(Math.abs(linePoint.getX())); // 得到正数的移动格数
            } else {
                // 左或右移动
                if ((linePoint.getX() & 0B1000_0000) == 0x00) {
                    // 不是负数，右方移动
                    getBuffer().put(0x53);
                } else {
                    // 是负数，左方移动
                    getBuffer().put(0x52);
                }
                getBuffer().put(Math.abs(linePoint.getY())); // 得到正数的移动格数
            }
        }
        // 写入归航目的地
        getBuffer().put(0x5E);
        getBuffer().put(getShippingLineBack().getValue().getMap());
        getBuffer().put(getShippingLineBack().getValue().getX());
        getBuffer().put(getShippingLineBack().getValue().getY());
    }

    @Override
    public DataAddress getWorldMapTilesIndexAddress() {
        return worldMapTilesIndexAddress;
    }

    @Override
    public DataAddress getWorldMapX00410Address() {
        return worldMapX00410Address;
    }

    @Override
    public DataAddress getWorldMapIndexAAddress() {
        return worldMapIndexAAddress;
    }

    @Override
    public DataAddress getWorldMapIndexBAddress() {
        return worldMapIndexBAddress;
    }

    @Override
    public DataAddress getWorldMapIndexCAddress() {
        return worldMapIndexCAddress;
    }

    @Override
    public DataAddress getWorldMapIndexAddress() {
        return worldMapIndexAddress;
    }

    @Override
    public DataAddress getWorldMapMinesAddress() {
        return worldMapMinesAddress;
    }

    @Override
    public DataAddress getWorldMapOutLineAddress() {
        return worldMapOutLineAddress;
    }

    @Override
    public DataAddress getWorldMapBackLineAddress() {
        return worldMapBackLineAddress;
    }

    @Override
    public byte[][] getIndexA() {
        return indexA;
    }

    /**
     * 获取索引A的图块数据
     *
     * @param canOut 是否可以越界获取
     * @return indexA的4*4tile
     * @see #getIndexA(int)
     */
    @Override
    public byte[] getIndexA(int offset, boolean canOut) {
        if (offset < indexA.length) {
            return indexA[offset];
        } else if (canOut) {
            position(getWorldMapIndexAAddress(), offset * 0x10);
            byte[] bytes = new byte[0x10];
            getBuffer().get(bytes);
            return bytes;
        }
        return null;
    }

    public byte[] getIndexA(int offset) {
        return getIndexA(offset, false);
    }

    @Override
    public byte[] getIndexOffsets() {
        return indexOffsets;
    }

    @Override
    public byte getIndexOffset(int index) {
        return indexOffsets[index];
    }

    @Override
    public byte[] getX00410() {
        return x00410;
    }

    @Override
    public byte getX00410(int index) {
        return x00410[index];
    }

    @Override
    public byte[][] getIndexB() {
        return indexB;
    }

    @Override
    public byte[] getIndexB(int index) {
        return indexB[index];
    }

    @Override
    public byte[][] getIndexC() {
        return indexC;
    }

    @Override
    public byte[] getIndexC(int index) {
        return indexC[index];
    }

    @Override
    public byte[] getIndex() {
        return index;
    }

    @Override
    public byte getIndex(int index) {
        return this.index[index];
    }

    @Override
    public byte[][] getMap() {
        return map;
    }

    @Override
    public List<MapPoint> getMines() {
        return mines;
    }

    @Override
    public Map.Entry<List<MapPoint>, MapPoint> getShippingLineOut() {
        return shippingLineOut;
    }

    @Override
    public Map.Entry<List<MapPoint>, MapPoint> getShippingLineBack() {
        return shippingLineBack;
    }
}
