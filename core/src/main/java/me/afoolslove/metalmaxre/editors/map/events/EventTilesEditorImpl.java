package me.afoolslove.metalmaxre.editors.map.events;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件图块编辑器
 * <p>
 * 支持世界地图
 * 根据事件状态可以显示不同的图块，覆盖原图块
 * 注意尽量不要同时作用在同一个图块上，因为没测试会发生啥
 * <p>
 * 需要地图属性中的事件图块启用才会生效
 * 图块图像根据玩家当前的图块组合不同而不同
 * <p>
 * 世界地图：
 * 世界地图的总tile大小为0x100*0x100，其中每4*4为一个小块
 * 当世界地图使用时，单个tile数据控制此4*4的方块
 * 并且X、Y的计算方式变更为 X*4、Y*4，X、Y < 0x40
 *
 * @author AFoolLove
 */
public class EventTilesEditorImpl extends RomBufferWrapperAbstractEditor implements IEventTilesEditor {
    private final DataAddress eventTilesAddress;
    private final HashMap<Integer, Map<Integer, List<EventTile>>> eventTiles = new HashMap<>();
    @Nullable
    private WorldMapInteractiveEvent worldMapInteractiveEvent;

    public EventTilesEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x1DCCF - 0x10, 0x1DEAF - 0x10),
                null);
    }

    public EventTilesEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                @NotNull DataAddress eventTilesAddress,
                                @Nullable WorldMapInteractiveEvent worldMapInteractiveEvent) {
        super(metalMaxRe);
        this.eventTilesAddress = eventTilesAddress;
        this.worldMapInteractiveEvent = worldMapInteractiveEvent;
    }

    @Editor.Load
    public void onLoad(IWorldMapEditor worldMapEditor,
                       IMapPropertiesEditor mapPropertiesEditor,
                       IMapEditor mapEditor) {
        // 读取前清空数据
        getEventTiles().clear();
        worldMapInteractiveEvent = null;

        position(getEventTilesAddress());
        var map = new HashMap<>(mapPropertiesEditor.getMapProperties())
                .entrySet().parallelStream()
                .filter(entry -> entry.getValue().hasEventTile()) // 移除没有事件图块属性的地图
                .collect(
                        // 移除相同的事件图块数据索引
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing((o) -> o.getValue().eventTilesIndex)))
                );

        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : map) {
            final char eventTilesIndex = mapPropertiesEntry.getValue().eventTilesIndex;
            prgPosition(0x1C000 + eventTilesIndex - 0x8000);

            // 一个或多个事件作为一组，一组使用 0x00 作为结尾
            var events = new HashMap<Integer, List<EventTile>>();
            // 事件
            int event = getBuffer().getToInt();
            do {
                // 图块数量
                int count = getBuffer().getToInt();

                List<EventTile> eventTiles = new ArrayList<>();

                if (mapPropertiesEntry.getKey() == 0x00) {
                    // 读取事件图块：X、Y、图块
                    for (int i = count; i > 0; i--) {
                        eventTiles.add(new WorldEventTile(getBuffer().get(), getBuffer().get(), getBuffer().get()));
                    }
                    // 世界地图的图块事件还需要读取图块内容
                    for (EventTile eventTile : eventTiles) {
                        byte[] tiles = worldMapEditor.getIndex(eventTile.intX(), eventTile.intY(), eventTile.intTile());
                        ((WorldEventTile) eventTile).setTiles(tiles);
                    }
                } else {
                    // 读取事件图块：X、Y、图块
                    for (int i = count; i > 0; i--) {
                        eventTiles.add(new EventTile(getBuffer().get(), getBuffer().get(), getBuffer().get()));
                    }
                }

                events.put(event, eventTiles);
            } while ((event = getBuffer().get()) != 0x00);

            // 添加事件图块组
            for (Map.Entry<Integer, MapProperties> propertiesEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                if (propertiesEntry.getValue().eventTilesIndex == eventTilesIndex) {
                    // 添加使用当前事件图块的地图
                    getEventTiles().put(propertiesEntry.getKey(), events);
                }
            }
        }
        // 将没有事件的地图添加一个空的事件Set
        for (int mapId = 0, count = mapEditor.getMapMaxCount(); mapId < count; mapId++) {
            getEventTiles().computeIfAbsent(mapId, HashMap::new);
        }


        // 获取罗克东边涨潮退潮的4个4*4tile
        worldMapInteractiveEvent = new WorldMapInteractiveEvent();
        prgPosition(0x28184);
        int worldMapInteractiveEventA = NumberR.toInt(getBuffer().get(), getBuffer().get());
        worldMapInteractiveEventA -= 0x7000;
        prgPosition(0x2818A);
        int worldMapInteractiveEventB = NumberR.toInt(getBuffer().get(), getBuffer().get());
        worldMapInteractiveEventB -= 0x7000;

        final int aX = worldMapInteractiveEventA % 0x40,
                aY = worldMapInteractiveEventA / 0x40,
                bX = worldMapInteractiveEventB % 0x40,
                bY = worldMapInteractiveEventB / 0x40;
        for (Map.Entry<Integer, List<EventTile>> entry : getWorldEventTile().entrySet()) {
            for (EventTile eventTile : entry.getValue()) {
                if (eventTile.intX() == aX && eventTile.intY() == aY) {
                    worldMapInteractiveEvent.aPoint = new Point2B(aX, aY);
                    worldMapInteractiveEvent.aTrue = worldMapEditor.getIndex(eventTile.intX(), eventTile.intY(), eventTile.intTile());
                    worldMapInteractiveEvent.aFalse = IWorldMapEditor.getTiles4x4(worldMapEditor.getMap(), aX, aY);
                    continue;
                }
                if (eventTile.intX() == bX && eventTile.intY() == bY) {
                    worldMapInteractiveEvent.bPoint = new Point2B(bX, bY);
                    worldMapInteractiveEvent.bTrue = worldMapEditor.getIndex(eventTile.intX(), eventTile.intY(), eventTile.intTile());
                    worldMapInteractiveEvent.bFalse = IWorldMapEditor.getTiles4x4(worldMapEditor.getMap(), bX, bY);
                }
            }
        }
        // 读取worldMapInteractiveEvent的触发坐标和朝向
        prgPosition(0x28165);
        worldMapInteractiveEvent.setCameraX(getBuffer().get());
        prgPosition(0x2816B);
        worldMapInteractiveEvent.setCameraY(getBuffer().get());
        prgPosition(0x28172);
        worldMapInteractiveEvent.setDirection(getBuffer().get());
    }

    @Editor.Apply
    public void onApply(IWorldMapEditor worldMapEditor,
                        @Editor.QuoteOnly IMapEditor mapEditor,
                        @Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
//        // 排除事件为 0x00 ！！！！
//        getEventTiles().values().forEach(each ->
//                each.entrySet().removeIf(entry -> entry.getKey() == 0x00)
//        );

        // 世界地图事件4*4tiles因为重新排序过，所以要更新事件的tile索引
        byte[][][] indexes = worldMapEditor.getIndexes();
        int[] indexesCapacity = worldMapEditor.getIndexesCapacity();
        getWorldEventTile().values().forEach(eventTiles -> {
            for (EventTile eventTile : eventTiles) {
                if (eventTile instanceof WorldEventTile worldEventTile) {
                    tile:
                    for (int i = 0; i < indexes.length; i++) {
                        byte[][] newTile = indexes[i];
                        for (int index = 0; index < indexesCapacity[i]; index++) {
                            if (Arrays.equals(worldEventTile.getTiles(), newTile[index])) {
                                // 设置新的索引
                                worldEventTile.setTile(index % 0x100);
                                break tile;
                            }
                        }
                    }
                }
            }
        });

        byte[][] eventTiles = new byte[mapEditor.getMapMaxCount()][];
        // 事件图块数据
        List<byte[]> eventTilesData = new ArrayList<>();

        for (Map.Entry<Integer, Map<Integer, List<EventTile>>> entry : getEventTiles().entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (Map.Entry<Integer, List<EventTile>> listEntry : entry.getValue().entrySet()) {
                // 写入事件
                outputStream.write(listEntry.getKey());
                // 写入事件数量
                outputStream.write(listEntry.getValue().size());
                // 写入 X、Y、Tile
                for (EventTile eventTile : listEntry.getValue()) {
                    outputStream.writeBytes(eventTile.toByteArray());
                }
            }
            // 写入事件组结束符
            outputStream.write(0x00);
            eventTiles[entry.getKey()] = outputStream.toByteArray();
        }

        // 事件图块索引
        char eventTilesIndex = (char) (getEventTilesAddress().getStartAddress() - 0x1C000 + 0x8000);
        final char endEventTilesIndex = (char) (eventTilesIndex + getEventTilesAddress().length());
        for (int mapId = 0, count = mapEditor.getMapMaxCount(); mapId < count; mapId++) {
            byte[] eventTile = eventTiles[mapId];
            if (eventTile == null || eventTile.length == 0) {
                // 没有或已经设置过
                continue;
            }
            if (eventTilesIndex == endEventTilesIndex) {
                System.err.printf("事件图块编辑器：没有剩余的空间写入地图%02X的事件：%s\n", mapId, NumberR.toHexString(eventTile));
                continue;
            }
            if ((endEventTilesIndex - eventTilesIndex) < eventTile.length) {
                // 剩余空间不能完整的写入了

                if ((endEventTilesIndex - eventTilesIndex) < (1 + 1 + 3 + 1)) {
                    // 剩余的空间已经不能写入任何事件了
                    // 设置为已写满
                    eventTilesIndex = endEventTilesIndex;
                    System.err.printf("事件图块编辑器：没有剩余的空间写入地图%02X的事件：%s\n", mapId, NumberR.toHexString(eventTile));
                    continue;
                }

                // 裁剪后还能尽量塞入一些事件

                // 去除基本数据外，还能塞入的事件图块数量
                int c = ((endEventTilesIndex - eventTilesIndex) - 1 - 1 - 1) / 3;
                eventTile = new byte[1 + 1 + (c * 3) + 1];
                eventTile[0] = eventTiles[mapId][0];    // 复制事件
                eventTile[1] = (byte) (c & 0xFF);       // 设置数量
//                eventTile[eventTile.length - 1] = (byte) 0x00;       // 设置结束符，默认为0x00
                // 复制 X、Y、Tile
                System.arraycopy(eventTiles[mapId], 0x02, eventTile, 0x02, c * 3);

                System.err.printf("事件图块编辑器：%02X写入部分事件图块：%s\n", mapId, NumberR.toHexString(eventTile));
            }
            // 将后面相同数据的事件图块索引一同设置
            byte[] currentData = eventTiles[mapId];
            for (int afterMapId = mapId; afterMapId < count; afterMapId++) {
                if (Arrays.equals(eventTiles[afterMapId], currentData)) {
                    mapPropertiesEditor.getMapProperties(afterMapId).eventTilesIndex = eventTilesIndex;
                    eventTiles[afterMapId] = null;
                    if (afterMapId != mapId) {
                        System.out.printf("事件图块编辑器：地图%02X与%02X使用相同事件图块\n", afterMapId, mapId);
                    }
                }
            }
            eventTilesIndex += eventTile.length;

            eventTilesData.add(eventTile);
        }

        // 写入数据
        position(getEventTilesAddress());
        for (byte[] eventTilesDatum : eventTilesData) {
            getBuffer().put(eventTilesDatum);
        }

        int end = getEventTilesAddress().getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            if (end > 0) {
                // 使用0xFF填充未使用的数据
                byte[] fillBytes = new byte[end];
                Arrays.fill(fillBytes, (byte) 0x00);
                getBuffer().put(fillBytes);
            }
            System.out.printf("事件图块编辑器：剩余%d个空闲字节\n", end);
        } else {
            System.err.printf("事件图块编辑器：错误！超出了数据上限%d字节\n", -end);
        }

        if (worldMapInteractiveEvent != null) {
            // 写入worldMapInteractiveEvent的触发坐标和朝向
            prgPosition(0x28165);
            getBuffer().put(worldMapInteractiveEvent.getCameraX());
            prgPosition(0x2816B);
            getBuffer().put(worldMapInteractiveEvent.getCameraY());
            prgPosition(0x28172);
            getBuffer().put(worldMapInteractiveEvent.getDirection());

            if (worldMapInteractiveEvent.aPoint != null || worldMapInteractiveEvent.bPoint != null) {
                for (int i = 0; i < indexes.length; i++) {
                    byte[][] index = indexes[i];

                    for (int j = 0; j < indexesCapacity[i]; j++) {
                        if (worldMapInteractiveEvent.aPoint != null) {
                            if (Arrays.equals(index[j], worldMapInteractiveEvent.aFalse)) {
                                prgPosition(0x2818D);
                                getBuffer().put(j % 0x100);
                                continue;
                            }
                            if (Arrays.equals(index[j], worldMapInteractiveEvent.aTrue)) {
                                prgPosition(0x2818E);
                                getBuffer().put(j % 0x100);
                                continue;
                            }
                        }
                        if (worldMapInteractiveEvent.bPoint != null) {
                            if (Arrays.equals(index[j], worldMapInteractiveEvent.bFalse)) {
                                prgPosition(0x2818F);
                                getBuffer().put(j % 0x100);
                                continue;
                            }
                            if (Arrays.equals(index[j], worldMapInteractiveEvent.bTrue)) {
                                prgPosition(0x28190);
                                getBuffer().put(j % 0x100);
                                continue;
                            }
                        }
                    }
                }

            }

            if (worldMapInteractiveEvent.aPoint != null) {
                prgPosition(0x28184);
                int worldMapInteractiveEventA = worldMapInteractiveEvent.aPoint.intX() + (worldMapInteractiveEvent.aPoint.intY() * 0x40);
                worldMapInteractiveEventA += 0x7000;
                getBuffer().putChar(NumberR.toChar(worldMapInteractiveEventA));
            }

            if (worldMapInteractiveEvent.bPoint != null) {
                prgPosition(0x2818A);
                int worldMapInteractiveEventB = worldMapInteractiveEvent.bPoint.intX() + (worldMapInteractiveEvent.bPoint.intY() * 0x40);
                worldMapInteractiveEventB += 0x7000;
                getBuffer().putChar(NumberR.toChar(worldMapInteractiveEventB));
            }
        }
    }

    @Override
    public DataAddress getEventTilesAddress() {
        return eventTilesAddress;
    }

    @Override
    public HashMap<Integer, Map<Integer, List<EventTile>>> getEventTiles() {
        return eventTiles;
    }

    @Override
    public Map<Integer, List<EventTile>> getEventTile(int map) {
        return eventTiles.get(map);
    }

    @Nullable
    @Override
    public WorldMapInteractiveEvent getWorldMapInteractiveEvent() {
        return worldMapInteractiveEvent;
    }
}
