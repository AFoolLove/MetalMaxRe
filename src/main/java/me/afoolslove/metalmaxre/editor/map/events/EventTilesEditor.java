package me.afoolslove.metalmaxre.editor.map.events;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import me.afoolslove.metalmaxre.editor.map.MapProperties;
import me.afoolslove.metalmaxre.editor.map.MapPropertiesEditor;
import me.afoolslove.metalmaxre.editor.map.world.WorldMapEditor;
import org.jetbrains.annotations.NotNull;

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
public class EventTilesEditor extends AbstractEditor {
    public static final int EVENT_TILES_START_OFFSET = 0x1DCCF - 0x10;
    public static final int EVENT_TILES_END_OFFSET = 0x1DEAF - 0x10;

    /**
     * K：Map
     * V：events
     * -- K: event
     * -- V: tile,x,y
     */
    private final HashMap<Integer, Map<Integer, List<EventTile>>> eventTiles = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        var worldMapEditor = EditorManager.getEditor(WorldMapEditor.class);

        // 读取前清空数据
        eventTiles.clear();

        // 排除事件为 0x00 ！！！！
        // setPrgRomPosition(buffer, EVENT_TILES_START_OFFSET);

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
            setPrgRomPosition(buffer, 0x1C000 + eventTilesIndex - 0x8000);

            // 一个或多个事件作为一组，一组使用 0x00 作为结尾
            var events = new HashMap<Integer, List<EventTile>>();
            // 事件
            int event = buffer.get();
            do {
                // 图块数量
                int count = buffer.get();

                List<EventTile> eventTiles = new ArrayList<>();

                if (mapPropertiesEntry.getKey() == 0x00) {
                    // 读取事件图块：X、Y、图块
                    for (int i = count; i > 0; i--) {
                        eventTiles.add(new WorldEventTile(buffer.get(), buffer.get(), buffer.get()));
                    }
                    // 世界地图的图块事件还需要读取图块内容
                    for (EventTile eventTile : eventTiles) {
                        byte[] tiles = worldMapEditor.indexA[0x200 + eventTile.intTile()];
                        ((WorldEventTile) eventTile).setTiles(tiles);
                    }
                } else {
                    // 读取事件图块：X、Y、图块
                    for (int i = count; i > 0; i--) {
                        eventTiles.add(new EventTile(buffer.get(), buffer.get(), buffer.get()));
                    }
                }

                events.put(event, eventTiles);
            } while ((event = buffer.get()) != 0x00);

            // 添加事件图块组
            for (Map.Entry<Integer, MapProperties> propertiesEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                if (propertiesEntry.getValue().eventTilesIndex == eventTilesIndex) {
                    // 添加使用当前事件图块的地图
                    getEventTiles().put(propertiesEntry.getKey(), events);
                }
            }
        }

//        // 如果存在
//        // 写入罗克东边涨潮退潮4*4 + 4*4图块
//        for (Map.Entry<Integer, List<EventTile>> entry : getWorldEventTile().entrySet()) {
//            if (entry.getKey() == ((0x0452 - 0x0441) << 3)) { // 涨潮退潮事件
//                for (EventTile eventTile : entry.getValue()) {
//                    if (eventTile.x == 0x3A && eventTile.y == 0x17) {
//                        // 上部分
//                        setPrgRomPosition(buffer, 0x2818D);
//                        buffer.put(map[])
//                    } else if (eventTile.x == 0x3A && eventTile.y == 0x18) {
//                        // 下部分
//                        setPrgRomPosition(buffer, 0x2818E);
//                    }
//                }
//            }
//        }
//        for (List<EventTile> eventTiles : getWorldEventTile().values()) {
//            for (EventTile eventTile : eventTiles) {
//                if (eventTile.)
//            }
//        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        // 排除事件为 0x00 ！！！！
        getEventTiles().values().forEach(each -> {
            each.entrySet().removeIf(entry -> entry.getKey() == 0x00);
        });

        setPrgRomPosition(buffer, EVENT_TILES_START_OFFSET);
        getEventTiles().values()
                .parallelStream()
                .filter(entry -> !entry.isEmpty()) // 过滤没有事件图块的地图
                .distinct()
                .forEachOrdered(events -> {
                    // 计算新的事件图块索引，太长了！简称：索引
                    char newEventTilesIndex = (char) (buffer.position() - 0x10 - 0x1C000 + 0x8000);
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
                        buffer.put(eventsList.getKey().byteValue());
                        // 写入事件数量
                        buffer.put(((byte) eventsList.getValue().size()));
                        // 写入 X、Y、Tile
                        for (EventTile eventTile : eventsList.getValue()) {
                            buffer.put(eventTile.toByteArray());
                        }
                    }
                    // 写入事件组结束符
                    buffer.put((byte) 0x00);
                });

        int end = buffer.position() - 1;
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
}
