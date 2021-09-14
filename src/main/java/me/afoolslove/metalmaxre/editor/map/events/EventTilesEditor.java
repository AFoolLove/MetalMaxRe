package me.afoolslove.metalmaxre.editor.map.events;

import me.afoolslove.metalmaxre.NumberR;
import me.afoolslove.metalmaxre.Point2B;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.ReadBefore;
import me.afoolslove.metalmaxre.editor.WriteBefore;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import me.afoolslove.metalmaxre.editor.map.MapProperties;
import me.afoolslove.metalmaxre.editor.map.MapPropertiesEditor;
import me.afoolslove.metalmaxre.editor.map.world.WorldMapEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件图块编辑器
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
 * <p>
 * 起始：0x1DCCF
 * 结束：0x1DEAF
 * <p>
 * 2021年5月26日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
@ReadBefore({WorldMapEditor.class, MapPropertiesEditor.class})
@WriteBefore({WorldMapEditor.class})
public class EventTilesEditor extends AbstractEditor<EventTilesEditor> {
    /**
     * 事件图块数据
     */
    public static final int EVENT_TILES_START_OFFSET = 0x1DCCF - 0x10;
    public static final int EVENT_TILES_END_OFFSET = 0x1DEAF - 0x10;

    /**
     * K：Map<p>
     * V：events<p>
     * -- K: event<p>
     * -- V: tile,x,y
     */
    private final HashMap<Integer, Map<Integer, List<EventTile>>> eventTiles = new HashMap<>();

    /**
     * 特殊事件图块<p>
     * 罗克东部涨潮和退潮的4个4*4tile
     */
    @Nullable
    private WorldMapInteractiveEvent worldMapInteractiveEvent;

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        var worldMapEditor = EditorManager.getEditor(WorldMapEditor.class);

        // 读取前清空数据
        eventTiles.clear();
        worldMapInteractiveEvent = null;

        // 排除事件为 0x00 ！！！！
        // setPrgRomPosition(EVENT_TILES_START_OFFSET);

        // 填充
        for (int i = 0; i < MapEditor.MAP_MAX_COUNT; i++) {
            getEventTiles().put(i, new HashMap<>());
        }

        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        var map = new HashMap<>(mapPropertiesEditor.getMapProperties())
                .entrySet().parallelStream()
                .filter(entry -> entry.getValue().hasEventTile()) // 移除没有事件图块属性的地图
                .collect(
                        // 移除相同的事件图块数据索引
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing((o) -> o.getValue().eventTilesIndex)))
                );

        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : map) {
            char eventTilesIndex = mapPropertiesEntry.getValue().eventTilesIndex;
            setPrgRomPosition(0x1C000 + eventTilesIndex - 0x8000);

            // 一个或多个事件作为一组，一组使用 0x00 作为结尾
            var events = new HashMap<Integer, List<EventTile>>();
            // 事件
            int event = getToInt(buffer);
            do {
                // 图块数量
                int count = getToInt(buffer);

                List<EventTile> eventTiles = new ArrayList<>();

                if (mapPropertiesEntry.getKey() == 0x00) {
                    // 读取事件图块：X、Y、图块
                    for (int i = count; i > 0; i--) {
                        eventTiles.add(new WorldEventTile(get(buffer), get(buffer), get(buffer)));
                    }
                    // 世界地图的图块事件还需要读取图块内容
                    for (EventTile eventTile : eventTiles) {
                        int offset = 0x200 + eventTile.intTile();
                        byte[] tiles = worldMapEditor.getIndexA(offset, true);
                        ((WorldEventTile) eventTile).setTiles(tiles);
                    }
                } else {
                    // 读取事件图块：X、Y、图块
                    for (int i = count; i > 0; i--) {
                        eventTiles.add(new EventTile(get(buffer), get(buffer), get(buffer)));
                    }
                }

                events.put(event, eventTiles);
            } while ((event = get(buffer)) != 0x00);

            // 添加事件图块组
            for (Map.Entry<Integer, MapProperties> propertiesEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                if (propertiesEntry.getValue().eventTilesIndex == eventTilesIndex) {
                    // 添加使用当前事件图块的地图
                    getEventTiles().put(propertiesEntry.getKey(), events);
                }
            }
        }

        // 获取罗克东边涨潮退潮的4个4*4tile
        worldMapInteractiveEvent = new WorldMapInteractiveEvent();
        setPrgRomPosition(0x28184);
        int worldMapInteractiveEventA = NumberR.toInt(get(buffer), get(buffer));
        worldMapInteractiveEventA -= 0x7000;
        setPrgRomPosition(0x2818A);
        int worldMapInteractiveEventB = NumberR.toInt(get(buffer), get(buffer));
        worldMapInteractiveEventB -= 0x7000;
        for (Map.Entry<Integer, List<EventTile>> entry : getWorldEventTile().entrySet()) {
            for (EventTile eventTile : entry.getValue()) {
                int x = worldMapInteractiveEventA % 0x40;
                int y = worldMapInteractiveEventA / 0x40;
                if (eventTile.intX() == x && eventTile.intY() == y) {
                    worldMapInteractiveEvent.aPoint = new Point2B(x, y);
                    worldMapInteractiveEvent.aTrue = worldMapEditor.indexA[0x200 + eventTile.tile];
                    worldMapInteractiveEvent.aFalse = worldMapEditor.getTiles(x, y);
                    continue;
                }
                x = worldMapInteractiveEventB % 0x40;
                y = worldMapInteractiveEventB / 0x40;
                if (eventTile.intX() == x && eventTile.intY() == y) {
                    worldMapInteractiveEvent.bPoint = new Point2B(x, y);
                    worldMapInteractiveEvent.bTrue = worldMapEditor.indexA[0x200 + eventTile.tile];
                    worldMapInteractiveEvent.bFalse = worldMapEditor.getTiles(x, y);
                }
            }
        }
        // 读取worldMapInteractiveEvent的触发坐标和朝向
        setPrgRomPosition(0x28165);
        worldMapInteractiveEvent.setCameraX(get(buffer));
        setPrgRomPosition(0x2816B);
        worldMapInteractiveEvent.setCameraY(get(buffer));
        setPrgRomPosition(0x28172);
        worldMapInteractiveEvent.setDirection(get(buffer));
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        var mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
        var worldMapEditor = EditorManager.getEditor(WorldMapEditor.class);

        // 排除事件为 0x00 ！！！！
        getEventTiles().values().forEach(each ->
                each.entrySet().removeIf(entry -> entry.getKey() == 0x00)
        );

        List<Map<Integer, List<EventTile>>> eventList = getEventTiles().values()
                .parallelStream()
                .filter(entry -> !entry.isEmpty()) // 过滤没有事件图块的地图
                .distinct().collect(Collectors.toList());

        if (worldMapInteractiveEvent != null) {
            // 写入worldMapInteractiveEvent的触发坐标和朝向
            setPrgRomPosition(0x28165);
            put(buffer, worldMapInteractiveEvent.getCameraX());
            setPrgRomPosition(0x2816B);
            put(buffer, worldMapInteractiveEvent.getCameraY());
            setPrgRomPosition(0x28172);
            put(buffer, worldMapInteractiveEvent.getDirection());

            Map<Integer, List<EventTile>> worldEventTile = getWorldEventTile();
            for (int i = 0x200; i < worldMapEditor.indexA.length; i++) {
                if (Arrays.equals(worldMapEditor.indexA[i], worldMapInteractiveEvent.aFalse)) {
                    setPrgRomPosition(0x2818D);
                    put(buffer, i - 0x200);
                    point:
                    for (List<EventTile> value : worldEventTile.values()) {
                        for (EventTile eventTile : value) {
                            if (eventTile.intX() == worldMapInteractiveEvent.aPoint.intX()
                                    && eventTile.intY() == worldMapInteractiveEvent.aPoint.intY()) {
                                eventTile.setTile(i);
                                break point;
                            }
                        }
                    }
                    continue;
                }
                if (Arrays.equals(worldMapEditor.indexA[i], worldMapInteractiveEvent.bFalse)) {
                    setPrgRomPosition(0x2818F);
                    put(buffer, i - 0x200);
                    point:
                    for (List<EventTile> value : worldEventTile.values()) {
                        for (EventTile eventTile : value) {
                            if (eventTile.intX() == worldMapInteractiveEvent.bPoint.intX()
                                    && eventTile.intY() == worldMapInteractiveEvent.bPoint.intY()) {
                                eventTile.setTile((byte) (i - 0x200));
                                break point;
                            }
                        }
                    }
                    continue;
                }
                if (Arrays.equals(worldMapEditor.indexA[i], worldMapInteractiveEvent.aTrue)) {
                    setPrgRomPosition(0x2818E);
                    put(buffer, i - 0x200);
                    point:
                    for (List<EventTile> value : worldEventTile.values()) {
                        for (EventTile eventTile : value) {
                            if (eventTile.intX() == worldMapInteractiveEvent.aPoint.intX()
                                    && eventTile.intY() == worldMapInteractiveEvent.aPoint.intY()) {
                                eventTile.setTile((byte) (i - 0x200));
                                break point;
                            }
                        }
                    }
                    continue;
                }
                if (Arrays.equals(worldMapEditor.indexA[i], worldMapInteractiveEvent.bTrue)) {
                    setPrgRomPosition(0x28190);
                    put(buffer, i - 0x200);
                    point:
                    for (List<EventTile> value : worldEventTile.values()) {
                        for (EventTile eventTile : value) {
                            if (eventTile.intX() == worldMapInteractiveEvent.bPoint.intX()
                                    && eventTile.intY() == worldMapInteractiveEvent.bPoint.intY()) {
                                eventTile.setTile((byte) (i - 0x200));
                                break point;
                            }
                        }
                    }
                    continue;
                }
            }

            setPrgRomPosition(0x28184);
            int worldMapInteractiveEventA = worldMapInteractiveEvent.aPoint.intX() + (worldMapInteractiveEvent.aPoint.intY() * 0x40);
            worldMapInteractiveEventA += 0x7000;
            putChar(buffer, NumberR.toChar(worldMapInteractiveEventA));
            setPrgRomPosition(0x2818A);
            int worldMapInteractiveEventB = worldMapInteractiveEvent.bPoint.intX() + (worldMapInteractiveEvent.bPoint.intY() * 0x40);
            worldMapInteractiveEventB += 0x7000;
            putChar(buffer, NumberR.toChar(worldMapInteractiveEventB));
        }

        setPrgRomPosition(EVENT_TILES_START_OFFSET);
        eventList.forEach(events -> {
            // 计算新的事件图块索引，太长了！简称：索引
            char newEventTilesIndex = (char) (bufferPosition - 0x10 - 0x1C000 + 0x8000);
            // 将旧的索引替换为新的索引
            getEventTiles().entrySet()
                    .parallelStream()
                    .filter(entry1 -> entry1.getValue() == events) // 获取相同事件图块的地图
                    .forEach(mapEntry -> {
                        // 通过相同的事件图块组更新索引
                        mapPropertiesEditor.getMapProperties(mapEntry.getKey()).eventTilesIndex = newEventTilesIndex;
                    });

            // 写入数据
            for (Map.Entry<Integer, List<EventTile>> eventsList : events.entrySet()) {
                // 写入事件
                put(buffer, eventsList.getKey().byteValue());
                // 写入事件数量
                put(buffer, eventsList.getValue().size());
                // 写入 X、Y、Tile
                for (EventTile eventTile : eventsList.getValue()) {
                    put(buffer, eventTile.toByteArray());
                }
            }
            // 写入事件组结束符
            put(buffer, 0x00);
        });

        int end = bufferPosition - 1;
        if (end <= 0x1DEAF) {
            System.out.printf("事件图块编辑器：剩余%d个空闲字节\n", 0x1DEAF - end);
        } else {
            System.out.printf("事件图块编辑器：错误！超出了数据上限%d字节\n", end - 0x1DEAF);
        }
        return true;
    }

    public HashMap<Integer, Map<Integer, List<EventTile>>> getEventTiles() {
        return eventTiles;
    }

    /**
     * @return 获取指定map的事件图块，可能为null，包含世界地图
     */
    public Map<Integer, List<EventTile>> getEventTile(int map) {
        return eventTiles.get(map);
    }

    /**
     * @return 获取世界地图的事件图块
     */
    public Map<Integer, List<EventTile>> getWorldEventTile() {
        return eventTiles.get(0x00);
    }

    @Nullable
    public WorldMapInteractiveEvent getWorldMapInteractiveEvent() {
        return worldMapInteractiveEvent;
    }
}
