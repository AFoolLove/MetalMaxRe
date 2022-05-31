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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // 排除事件为 0x00 ！！！！
        position(getEventTilesAddress());

        // 填充
        for (int i = 0; i < mapEditor.getMapMaxCount(); i++) {
            getEventTiles().put(i, new HashMap<>());
        }

        var map = new HashMap<>(mapPropertiesEditor.getMapProperties())
                .entrySet().parallelStream()
                .filter(entry -> entry.getValue().hasEventTile()) // 移除没有事件图块属性的地图
                .collect(
                        // 移除相同的事件图块数据索引
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing((o) -> o.getValue().eventTilesIndex)))
                );

        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : map) {
            char eventTilesIndex = mapPropertiesEntry.getValue().eventTilesIndex;
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
                        int offset = 0x200 + eventTile.intTile();
                        byte[] tiles = worldMapEditor.getIndexA(offset, true);
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

        // 获取罗克东边涨潮退潮的4个4*4tile
        worldMapInteractiveEvent = new WorldMapInteractiveEvent();
        prgPosition(0x28184);
        int worldMapInteractiveEventA = NumberR.toInt(getBuffer().get(), getBuffer().get());
        worldMapInteractiveEventA -= 0x7000;
        prgPosition(0x2818A);
        int worldMapInteractiveEventB = NumberR.toInt(getBuffer().get(), getBuffer().get());
        worldMapInteractiveEventB -= 0x7000;
        for (Map.Entry<Integer, List<EventTile>> entry : getWorldEventTile().entrySet()) {
            for (EventTile eventTile : entry.getValue()) {
                int x = worldMapInteractiveEventA % 0x40;
                int y = worldMapInteractiveEventA / 0x40;
                if (eventTile.intX() == x && eventTile.intY() == y) {
                    worldMapInteractiveEvent.aPoint = new Point2B(x, y);
                    worldMapInteractiveEvent.aTrue = worldMapEditor.getIndexA()[0x200 + eventTile.tile];
                    worldMapInteractiveEvent.aFalse = IWorldMapEditor.getTiles4x4(worldMapEditor.getMap(), x, y);
                    continue;
                }
                x = worldMapInteractiveEventB % 0x40;
                y = worldMapInteractiveEventB / 0x40;
                if (eventTile.intX() == x && eventTile.intY() == y) {
                    worldMapInteractiveEvent.bPoint = new Point2B(x, y);
                    worldMapInteractiveEvent.bTrue = worldMapEditor.getIndexA()[0x200 + eventTile.tile];
                    worldMapInteractiveEvent.bFalse = IWorldMapEditor.getTiles4x4(worldMapEditor.getMap(), x, y);
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
                           @Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
        // 排除事件为 0x00 ！！！！
        getEventTiles().values().forEach(each ->
                each.entrySet().removeIf(entry -> entry.getKey() == 0x00)
        );

        List<Map<Integer, List<EventTile>>> eventList = getEventTiles().values()
                .parallelStream()
                .filter(entry -> !entry.isEmpty()) // 过滤没有事件图块的地图
                .distinct().toList();

        if (worldMapInteractiveEvent != null) {
            // 写入worldMapInteractiveEvent的触发坐标和朝向
            prgPosition(0x28165);
            getBuffer().put(worldMapInteractiveEvent.getCameraX());
            prgPosition(0x2816B);
            getBuffer().put(worldMapInteractiveEvent.getCameraY());
            prgPosition(0x28172);
            getBuffer().put(worldMapInteractiveEvent.getDirection());

            Map<Integer, List<EventTile>> worldEventTile = getWorldEventTile();
            for (int i = 0x200; i < worldMapEditor.getIndexA().length; i++) {
                if (Arrays.equals(worldMapEditor.getIndexA()[i], worldMapInteractiveEvent.aFalse)) {
                    prgPosition(0x2818D);
                    getBuffer().put(i - 0x200);
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
                if (Arrays.equals(worldMapEditor.getIndexA()[i], worldMapInteractiveEvent.bFalse)) {
                    prgPosition(0x2818F);
                    getBuffer().put(i - 0x200);
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
                if (Arrays.equals(worldMapEditor.getIndexA()[i], worldMapInteractiveEvent.aTrue)) {
                    prgPosition(0x2818E);
                    getBuffer().put(i - 0x200);
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
                if (Arrays.equals(worldMapEditor.getIndexA()[i], worldMapInteractiveEvent.bTrue)) {
                    prgPosition(0x28190);
                    getBuffer().put(i - 0x200);
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

            prgPosition(0x28184);
            int worldMapInteractiveEventA = worldMapInteractiveEvent.aPoint.intX() + (worldMapInteractiveEvent.aPoint.intY() * 0x40);
            worldMapInteractiveEventA += 0x7000;
            getBuffer().putChar(NumberR.toChar(worldMapInteractiveEventA));
            prgPosition(0x2818A);
            int worldMapInteractiveEventB = worldMapInteractiveEvent.bPoint.intX() + (worldMapInteractiveEvent.bPoint.intY() * 0x40);
            worldMapInteractiveEventB += 0x7000;
            getBuffer().putChar(NumberR.toChar(worldMapInteractiveEventB));
        }

        position(getEventTilesAddress());
        eventList.forEach(events -> {
            // 计算新的事件图块索引，太长了！简称：索引
            char newEventTilesIndex = (char) (position() - (getBuffer().getHeader().isTrained() ? 0x200 : 0x000) - 0x10 - 0x1C000 + 0x8000);
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
                getBuffer().put(eventsList.getKey().byteValue());
                // 写入事件数量
                getBuffer().put(eventsList.getValue().size());
                // 写入 X、Y、Tile
                for (EventTile eventTile : eventsList.getValue()) {
                    getBuffer().put(eventTile.toByteArray());
                }
            }
            // 写入事件组结束符
            getBuffer().put(0x00);
        });

//        int end = position() - 1;
//        if (end <= 0x1DEAF) {
//            System.out.printf("事件图块编辑器：剩余%d个空闲字节\n", 0x1DEAF - end);
//        } else {
//            System.out.printf("事件图块编辑器：错误！超出了数据上限%d字节\n", end - 0x1DEAF);
//        }
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