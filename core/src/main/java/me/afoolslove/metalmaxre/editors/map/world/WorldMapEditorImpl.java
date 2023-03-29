package me.afoolslove.metalmaxre.editors.map.world;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.CameraMapPoint;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.editors.map.events.EventTile;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.events.WorldEventTile;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * 世界地图编辑器
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class WorldMapEditorImpl extends RomBufferWrapperAbstractEditor implements IWorldMapEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorldMapEditorImpl.class);
    private final DataAddress worldMapTilesIndexAddress;
    private final DataAddress worldMapX00410Address;
    private final List<DataAddress> worldMapIndexesAddress;
    private final DataAddress worldMapIndexAddress;
    private final DataAddress worldMapMinesAddress;
    private final DataAddress worldMapOutLineAddress;
    private final DataAddress worldMapBackLineAddress;

    /**
     * 图块集组合矩形化
     */
    public static final Map<Rectangle, Integer> DEFAULT_PIECES = Map.ofEntries(
            Map.entry(new Rectangle(0x88, 0x07, 0x78, 0x61), 0x84858889),
            Map.entry(new Rectangle(0x88, 0x68, 0x78, 0x98), 0x84858687),
            Map.entry(new Rectangle(0x08, 0x07, 0x80, 0x91), 0x848C8D8E),
            Map.entry(new Rectangle(0x08, 0x98, 0x60, 0x68), 0x84858A8B),
            Map.entry(new Rectangle(0x68, 0x98, 0x20, 0x68), 0x84858687),
            Map.entry(new Rectangle(0x88, 0x00, 0x78, 0x07), 0x84858687),
            Map.entry(new Rectangle(0x08, 0x00, 0x60, 0x07), 0x84858A8B),
            Map.entry(new Rectangle(0x68, 0x00, 0x20, 0x07), 0x84858687),
            Map.entry(new Rectangle(0x00, 0x00, 0x08, 0x07), 0x84858687),
            Map.entry(new Rectangle(0x00, 0x07, 0x08, 0x61), 0x84858889),
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

    public byte[][][] indexes = {
            new byte[0x200][0x10], // WORLD_MAP_INDEX_A_END - WORLD_MAP_INDEX_A_START + 1
            new byte[0x200][0x10], // WORLD_MAP_INDEX_B_END - WORLD_MAP_INDEX_B_START + 1
            new byte[0x200][0x10], // WORLD_MAP_INDEX_C_END - WORLD_MAP_INDEX_C_START + 1
            new byte[0x200][0x10]  // WORLD_MAP_INDEX_D_END - WORLD_MAP_INDEX_D_START + 1
    };
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
    private final Map.Entry<List<Map.Entry<LineDirection, Byte>>, CameraMapPoint> shippingLineOut = Map.entry(new ArrayList<>(0x10), new CameraMapPoint());
    /**
     * 归航的航线<p>
     * K: line<p>
     * V：目的地
     */
    private final Map.Entry<List<Map.Entry<LineDirection, Byte>>, CameraMapPoint> shippingLineBack = Map.entry(new ArrayList<>(0x10), new CameraMapPoint());

    public WorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x00010 - 0x10, 0x0040F - 0x10),
                DataAddress.fromPRG(0x00410 - 0x10, 0x0060F - 0x10),
                DataAddress.fromCHR(0x3A010 - 0x10, 0x3B00F - 0x10),
                DataAddress.fromPRG(0x35EC1 - 0x10),
                DataAddress.fromPRG(0x258C6 - 0x10),
                DataAddress.fromPRG(0x258EA - 0x10));
    }

    public WorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                              DataAddress worldMapTilesIndexAddress,
                              DataAddress worldMapX00410Address,
                              DataAddress worldMapIndexAddress,
                              DataAddress worldMapMinesAddress,
                              DataAddress worldMapOutLineAddress,
                              DataAddress worldMapBackLineAddress,
                              List<DataAddress> worldMapIndexesAddress) {
        super(metalMaxRe);
        this.worldMapTilesIndexAddress = worldMapTilesIndexAddress;
        this.worldMapX00410Address = worldMapX00410Address;
        this.worldMapIndexAddress = worldMapIndexAddress;
        this.worldMapMinesAddress = worldMapMinesAddress;
        this.worldMapOutLineAddress = worldMapOutLineAddress;
        this.worldMapBackLineAddress = worldMapBackLineAddress;
        this.worldMapIndexesAddress = worldMapIndexesAddress;
    }

    public WorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                              DataAddress worldMapTilesIndexAddress,
                              DataAddress worldMapX00410Address,
                              DataAddress worldMapIndexAddress,
                              DataAddress worldMapMinesAddress,
                              DataAddress worldMapOutLineAddress,
                              DataAddress worldMapBackLineAddress) {
        super(metalMaxRe);
        this.worldMapTilesIndexAddress = worldMapTilesIndexAddress;
        this.worldMapX00410Address = worldMapX00410Address;
        this.worldMapIndexAddress = worldMapIndexAddress;
        this.worldMapMinesAddress = worldMapMinesAddress;
        this.worldMapOutLineAddress = worldMapOutLineAddress;
        this.worldMapBackLineAddress = worldMapBackLineAddress;

        this.worldMapIndexesAddress = new ArrayList<>();
        int position = (metalMaxRe.getBuffer().getHeader().getLastPrgRom() * 0x4000) + 0x01E94;

        worldMapIndexesAddress.add(DataAddress.fromPRG(metalMaxRe.getBuffer().getToInt(position++) * 0x2000));
        worldMapIndexesAddress.add(DataAddress.fromPRG(metalMaxRe.getBuffer().getToInt(position++) * 0x2000));
        worldMapIndexesAddress.add(DataAddress.fromPRG(metalMaxRe.getBuffer().getToInt(position++) * 0x2000));
        worldMapIndexesAddress.add(DataAddress.fromPRG(metalMaxRe.getBuffer().getToInt(position) * 0x2000));
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清除数据
        for (byte[] bytes : map) {
            Arrays.fill(bytes, (byte) 0x00);
        }
        Arrays.fill(indexOffsets, (byte) 0x00);
        Arrays.fill(x00410, (byte) 0x00);
        for (byte[][] bytes : indexes) {
            for (byte[] bs : bytes) {
                Arrays.fill(bs, (byte) 0x00);
            }
        }
        Arrays.fill(index, (byte) 0x00);
        mines.clear();

        // 读取世界地图图块索引偏移
        getBuffer().get(getWorldMapTilesIndexAddress(), indexOffsets);
        // 读取图块组索引
        getBuffer().get(getWorldMapX00410Address(), x00410);
        // 读取图块组
        for (int i = 0; i < getWorldMapIndexesAddress().size(); i++) {
            position(getWorldMapIndexesAddress().get(i));
            for (byte[] bytes : indexes[i]) {
                getBuffer().get(bytes);
            }
        }
        // 读取图块索引
        position(getWorldMapIndexAddress());
        getBuffer().get(index);

        byte[][][] indexes = getIndexes();
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

            byte[][] tempIndex = indexes[x410 << 1 | indexOffset >> 1];
            // indexOffset的D0判断是否需要*0x100，相当于offset的高位
            offset += (indexOffset & 1) * 0x100;

            byte[] tiles = tempIndex[offset];

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
        List<Map.Entry<LineDirection, Byte>> linePoint = getShippingLineOut().getKey();
        linePoint.clear();

        // 读取0x10次方向
        while (linePoint.size() < 0x10) {
            byte action = getBuffer().get();
            LineDirection direction = LineDirection.fromAction(action);
            if (direction == null) {
                // 航线错误
                LOGGER.error("世界地图编辑器：出航航线读取错误：未知的操作码 {}", NumberR.toHex(action));
                continue;
            }
            if (direction == LineDirection.END) {
                // 读取到目的地数据，立即结束
                break;
            }
            linePoint.add(Map.entry(direction, getBuffer().get()));
        }
        if (linePoint.size() == 0x10) {
            getBuffer().get(); // 所有路径点全部使用，跳过 0x5E
        }
        // 读取出航目的地
        getShippingLineOut().getValue().setCamera(getBuffer().get(), getBuffer().get(), getBuffer().get());

        // 读取归航航线
        position(getWorldMapBackLineAddress());
        linePoint = getShippingLineBack().getKey();
        linePoint.clear();

        while (linePoint.size() < 0x10) {
            byte action = getBuffer().get();
            LineDirection direction = LineDirection.fromAction(action);
            if (direction == null) {
                // 航线错误
                LOGGER.error("世界地图编辑器：归航航线读取错误：未知的操作码 {}", NumberR.toHex(action));
                continue;
            }
            if (direction == LineDirection.END) {
                // 读取到目的地数据，立即结束
                break;
            }
            linePoint.add(Map.entry(direction, getBuffer().get()));
        }
        if (linePoint.size() == 0x10) {
            getBuffer().get(); // 所有路径点全部使用，跳过 0xE5
        }
        // 读取归航目的地
        getShippingLineBack().getValue().setCamera(getBuffer().get(), getBuffer().get(), getBuffer().get());
    }

    @Editor.Apply
    public void onApply(@Editor.QuoteOnly IEventTilesEditor eventTilesEditor) {
        // 每个的容量
        int[] indexesCapacity = getIndexesCapacity();

        // key  : hashCode
        // value: 4*4tiles
        Map<Integer, Map<Integer, byte[]>> newIndexes = Map.ofEntries(
                Map.entry(0, new LinkedHashMap<>(indexesCapacity[0])),
                Map.entry(1, new LinkedHashMap<>(indexesCapacity[1])),
                Map.entry(2, new LinkedHashMap<>(indexesCapacity[2])),
                Map.entry(3, new LinkedHashMap<>(indexesCapacity[3]))
        );

        // 格式化地图数据为4*4tile
        byte[][][] mapTiles = new byte[0x40][0x40][0x10];
        for (int y = 0; y < 0x40; y++) {
            for (int x = 0; x < 0x40; x++) {
                mapTiles[y][x] = IWorldMapEditor.getTiles4x4(map, x, y);
            }
        }

        // 储存 罗克东边涨潮退潮的4个4*4tile
//        WorldMapInteractiveEvent worldMapInteractiveEvent = eventTilesEditor.getWorldMapInteractiveEvent();
//        if (worldMapInteractiveEvent != null) {
//            // 与相同的地图4*4tile一起存入
//            if (worldMapInteractiveEvent.aPoint != null) {
//                newIndexes.get(0).put(Arrays.hashCode(worldMapInteractiveEvent.aFalse), worldMapInteractiveEvent.aFalse);
//                byte[] tiles = mapTiles[worldMapInteractiveEvent.aPoint.intY()][worldMapInteractiveEvent.aPoint.intX()];
//                newIndexes.get(0).put(Arrays.hashCode(tiles), tiles);
//
//                newIndexes.get(0).put(Arrays.hashCode(worldMapInteractiveEvent.aTrue), worldMapInteractiveEvent.aTrue);
//                tiles = mapTiles[worldMapInteractiveEvent.aPoint.intY()][worldMapInteractiveEvent.aPoint.intX()];
//                newIndexes.get(0).put(Arrays.hashCode(tiles), tiles);
//            }
//            if (worldMapInteractiveEvent.bPoint != null) {
//                newIndexes.get(0).put(Arrays.hashCode(worldMapInteractiveEvent.bFalse), worldMapInteractiveEvent.bFalse);
//                byte[] tiles = mapTiles[worldMapInteractiveEvent.bPoint.intY()][worldMapInteractiveEvent.bPoint.intX()];
//                newIndexes.get(0).put(Arrays.hashCode(tiles), tiles);
//
//                newIndexes.get(0).put(Arrays.hashCode(worldMapInteractiveEvent.bTrue), worldMapInteractiveEvent.bTrue);
//                tiles = mapTiles[worldMapInteractiveEvent.bPoint.intY()][worldMapInteractiveEvent.bPoint.intX()];
//                newIndexes.get(0).put(Arrays.hashCode(tiles), tiles);
//            }
//
//        }


        // 所有事件图块索引
        Collection<List<EventTile>> worldEventTiles = eventTilesEditor.getWorldEventTile().values();

        // 储存 事件4*4tiles
        for (List<EventTile> eventTileList : worldEventTiles) {
            for (EventTile eventTile : eventTileList) {
                if (eventTile instanceof WorldEventTile worldEventTile) {
                    // 添加事件所在的原4*4tiles
                    byte[] tiles = mapTiles[worldEventTile.intY()][worldEventTile.intX()];
                    newIndexes.get(0).put(Arrays.hashCode(tiles), tiles);
                    // 添加事件的4*4tiles
                    newIndexes.get(0).put(Arrays.hashCode(worldEventTile.getTiles()), worldEventTile.getTiles());
                }
            }
        }
        // 移除null
        for (Map<Integer, byte[]> value : newIndexes.values()) {
            value.entrySet().removeIf(filter -> filter.getValue() == null);
        }


        // 遍历世界地图 4*4tile并储存
        for (int y = 0; y < 0x40; y++) {
            for (int x = 0; x < 0x40; x++) {
                // 当前位置的 4*4tile
                byte[] tiles = mapTiles[y][x];
                final int hashCode = Arrays.hashCode(tiles);

                // 判断是否已经存入
                boolean hasSave = false;
                for (Map<Integer, byte[]> newIndex : newIndexes.values()) {
                    if (newIndex.containsKey(hashCode)) {
                        hasSave = true;
                        break;
                    }
                }

                if (!hasSave) {
                    // 还未存入的tiles，进行存入
                    for (Map.Entry<Integer, Map<Integer, byte[]>> entry : newIndexes.entrySet()) {
                        if (entry.getValue().size() >= indexesCapacity[entry.getKey()]) {
                            // 没有空间存入了，下一个
                            continue;
                        }
                        // 存入
                        entry.getValue().put(hashCode, tiles);
                        hasSave = true;
                        break;
                    }
                }

                if (!hasSave) {
                    // 什么？还是没存进去？等死吧！
                    LOGGER.error("世界地图编辑器：无法储存的世界地图4*4tile数据，可能会导致地图({},{})异常：{}\n",
                            NumberR.toHex(2, x * 0x04), NumberR.toHex(2, y * 0x04),
                            NumberR.toHexString(tiles));
                }
            }
        }

        // 将Set转换为有序的List
        List<List<byte[]>> newTiles = new ArrayList<>();
        for (int i = 0; i < indexesCapacity.length; i++) {
            newTiles.add(i, new ArrayList<>(newIndexes.get(i).values()));
        }

        // 填充空闲空间并计算空闲空间数量
        final byte[] fillTiles = new byte[0x10];
        Arrays.fill(fillTiles, (byte) 0xFF);

        int fillCount = 0;
        for (int i = 0; i < indexesCapacity.length; i++) {
            if (newTiles.get(i).size() >= indexesCapacity[i]) {
                // 没有空闲空间
                continue;
            }
            // 空闲空间数量
            int count = indexesCapacity[i] - newTiles.get(i).size();
            fillCount += count;

            // 填充
            for (int c = 0; c < count; c++) {
                newTiles.get(i).add(fillTiles);
            }
        }

        if (fillCount > 0x00) {
            LOGGER.info("世界地图编辑器：剩余{}个空闲4*4tile", fillCount);
        }

        // 更新index
        for (int i = 0; i < getIndexes().length; i++) {
            byte[][] bytes = newTiles.get(i).toArray(new byte[0x00][]);
            System.arraycopy(bytes, 0x00, getIndexes()[i], 0x00, indexesCapacity[i]);
        }

        // 开始写入
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

            findTiles:
            for (int bankIndex = 0; bankIndex < newTiles.size(); bankIndex++) {
                List<byte[]> bankTiles = newTiles.get(bankIndex);
                for (int index = 0; index < bankTiles.size(); index++) {
                    byte[] tiles = bankTiles.get(index);
                    if (Arrays.equals(tiles, bytes)) {
                        // 设置index为 indexA或indexC
                        x410 = (bankIndex / 0x02) & 0B0000_0001;

                        // indexA或indexC变为 indexB或indexD，并设置到D1
                        indexOffset = (bankIndex % 0x02) & 0B0000_0001;
                        indexOffset <<= 1;
                        // index是否+0x100
                        indexOffset |= (index / 0x100) & 0B0000_0001;

                        // 数据偏移
                        offset = index % 0x100;
                        break findTiles;
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
            indexOffsets[i / 4] |= indexOffset & 0xFF;

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
        getBuffer().put(getWorldMapTilesIndexAddress(), indexOffsets);
        // 写入图块组索引
        getBuffer().put(getWorldMapX00410Address(), x00410);
        // 写入图块组
        for (int i = 0; i < getWorldMapIndexesAddress().size(); i++) {
            position(getWorldMapIndexesAddress().get(i));
            for (int j = 0; j < indexesCapacity[i]; j++) {
                getBuffer().put(getIndexes()[i][j]);
            }
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
            Map.Entry<LineDirection, Byte> linePoint = getShippingLineOut().getKey().get(i);
            getBuffer().put(linePoint.getKey().getAction());
            getBuffer().put(linePoint.getValue());
        }
        // 航线结束标志
        getBuffer().put(LineDirection.END.getAction());
        // 写入出航目的地
        getBuffer().put(getShippingLineOut().getValue().getMap());
        getBuffer().put(getShippingLineOut().getValue().getCameraX());
        getBuffer().put(getShippingLineOut().getValue().getCameraY());

        // 写入归航路径点和目的地
        position(getWorldMapBackLineAddress());
        for (int i = 0, size = Math.min(0x10, getShippingLineBack().getKey().size()); i < size; i++) {
            Map.Entry<LineDirection, Byte> linePoint = getShippingLineBack().getKey().get(i);
            getBuffer().put(linePoint.getKey().getAction());
            getBuffer().put(linePoint.getValue());
        }
        // 航线结束标志
        getBuffer().put(LineDirection.END.getAction());
        // 写入归航目的地
        getBuffer().put(getShippingLineBack().getValue().getMap());
        getBuffer().put(getShippingLineBack().getValue().getCameraX());
        getBuffer().put(getShippingLineBack().getValue().getCameraY());
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
    public List<DataAddress> getWorldMapIndexesAddress() {
        return worldMapIndexesAddress;
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
    public byte[] getIndex(int x, int y, int offset) {
        int i = (y * 0x40) + x;

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
        int x410 = x00410[i / 8] & 0xFF;
        x410 >>>= (7 - (i % 8));
        x410 &= 0B0000_0001;

        byte[][] tempIndex = getIndexes()[x410 << 1 | indexOffset >> 1];
        offset += (indexOffset & 1) * 0x100;
        return tempIndex[offset];
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
    public byte[] getIndex() {
        return index;
    }

    @Override
    public byte[][][] getIndexes() {
        return indexes;
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
    public Map.Entry<List<Map.Entry<LineDirection, Byte>>, CameraMapPoint> getShippingLineOut() {
        return shippingLineOut;
    }

    @Override
    public Map.Entry<List<Map.Entry<LineDirection, Byte>>, CameraMapPoint> getShippingLineBack() {
        return shippingLineBack;
    }

    /**
     * 兼容SH和SHG版本
     */
    @Editor.TargetVersion({"super_hack", "super_hack_general"})
    public static class SHWorldMapEditorImpl extends WorldMapEditorImpl {

        public SHWorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            this(metalMaxRe,
                    DataAddress.fromPRG(0x00010 - 0x10, 0x0040F - 0x10),
                    DataAddress.fromPRG(0x00410 - 0x10, 0x0060F - 0x10),
                    DataAddress.fromCHR(0x3A010 - 0x10, 0x3B00F - 0x10),
                    DataAddress.fromPRG(0x35EC1 - 0x10),
                    DataAddress.fromPRG(0x25C10 - 0x10),
                    DataAddress.fromPRG(0x25C34 - 0x10));
        }

        public SHWorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                    DataAddress worldMapTilesIndexAddress,
                                    DataAddress worldMapX00410Address,
                                    DataAddress worldMapIndexAddress,
                                    DataAddress worldMapMinesAddress,
                                    DataAddress worldMapOutLineAddress,
                                    DataAddress worldMapBackLineAddress,
                                    List<DataAddress> worldMapIndexesAddress) {
            super(metalMaxRe, worldMapTilesIndexAddress, worldMapX00410Address, worldMapIndexAddress, worldMapMinesAddress, worldMapOutLineAddress, worldMapBackLineAddress, worldMapIndexesAddress);
        }

        public SHWorldMapEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                    DataAddress worldMapTilesIndexAddress,
                                    DataAddress worldMapX00410Address,
                                    DataAddress worldMapIndexAddress,
                                    DataAddress worldMapMinesAddress,
                                    DataAddress worldMapOutLineAddress,
                                    DataAddress worldMapBackLineAddress) {
            super(metalMaxRe, worldMapTilesIndexAddress, worldMapX00410Address, worldMapIndexAddress, worldMapMinesAddress, worldMapOutLineAddress, worldMapBackLineAddress);
        }

        @Override
        @Editor.Load
        public void onLoad() {
            super.onLoad();
        }

        @Override
        @Editor.Apply
        public void onApply(IEventTilesEditor eventTilesEditor) {
            super.onApply(eventTilesEditor);
        }
    }
}
