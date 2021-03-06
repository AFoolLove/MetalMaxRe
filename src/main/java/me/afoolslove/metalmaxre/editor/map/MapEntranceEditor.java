package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.Version;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.ReadBefore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 地图边界和出入口编辑器<p>
 * 包含世界地图<p>
 * 起始：0x1E990<p>
 * 结束：0x1F999<p>
 * 修改地图的边界类型和地图的出入口<p>
 * 2021年5月31日：已完成并通过测试基本编辑功能
 *
 * <p>
 * <p>
 * TODO: 多版本读取正常，多版本写入未测试
 *
 * @author AFoolLove
 */
@ReadBefore({MapPropertiesEditor.class})
public class MapEntranceEditor extends AbstractEditor<MapEntranceEditor> {
    /**
     * 地图边界和出入口数据
     */
    public static final int MAP_ENTRANCE_START_OFFSET = 0x1E990 - 0x10;
    public static final int MAP_ENTRANCE_END_OFFSET = 0x1F990 - 0x10;

    /**
     * SuperHack版本
     * <p>
     * 世界地图数据无修改，地图数据从该地址
     */
    public static final int SH_MAP_ENTRANCE_START_OFFSET = 0x7E7B0 - 0x10;
//    未知
//    public static final int SH_MAP_ENTRANCE_END_OFFSET = 0x7F7B0 - 0x10;

    /**
     * SuperHack通用版本
     * <p>
     * 世界地图数据无修改，地图数据从该地址
     */
    public static final int SHG_MAP_ENTRANCE_START_OFFSET = 0x527B0 - 0x10;
//    未知
//    public static final int SHG_MAP_ENTRANCE_END_OFFSET = 0x537B0 - 0x10;

    /**
     * K：map
     */
    private final Map<Integer, MapEntrance> mapEntrances = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        mapEntrances.clear();

        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
            Integer map = mapPropertiesEntry.getKey();
            MapProperties mapProperties = mapPropertiesEntry.getValue();

            if (getMapEntrances().containsKey(map)) {
                // 排除已读取的地图
                continue;
            }


            // 索引到数据
            if (map != 0 && getVersion() == Version.SUPER_HACK) {
                setPrgRomPosition(0x7E000 + mapProperties.entrance - 0x8000);
            } else if (map != 0 && getVersion() == Version.SUPER_HACK_GENERAL) {
                setPrgRomPosition(0x52000 + mapProperties.entrance - 0x8000);
            } else {
                setPrgRomPosition(0x1E000 + mapProperties.entrance - 0x8000);
            }

            int temp = getToInt(buffer);

            MapBorder mapBorder;
            switch (MapBorderType.getType(temp)) {
                // 上一个位置，不需要读取额外数据
                case LAST -> mapBorder = new MapBorder(MapBorderType.LAST);

                // 固定位置
                case FIXED -> {
                    mapBorder = new MapBorder(MapBorderType.FIXED);
                    // 只读取1个目标地图位置
                    mapBorder.add(new MapPoint(get(buffer), get(buffer), get(buffer)));
                }
                // 不同的目标地图位置
                default -> {
                    mapBorder = new MapBorder(MapBorderType.DIRECTION);
                    // 回退1byte，该byte属于地图数据
                    skip(-1);
                    // 读取4个目标地图位置
                    // 分别读取：上下左右，的目标地图位置
                    for (int i = 0; i < 0x04; i++) {
                        mapBorder.add(new MapPoint(get(buffer), get(buffer), get(buffer)));
                    }
                }
            }

            MapEntrance mapEntrance = new MapEntrance(mapBorder);

            // 读取地图内的出入口数量
            int count = getToInt(buffer);

            if (count != 0x00) {
                MapPoint[] entrances = new MapPoint[count];
                // 读取入口数据
                for (int i = 0; i < count; i++) {
                    // 读取当前地图入口的 X、Y
                    entrances[i] = new MapPoint(get(buffer), get(buffer));
                }
                // 读取入口对应的出口
                for (int i = 0; i < count; i++) {
                    // 读取出口的 Map、X、Y
                    mapEntrance.getEntrances().put(entrances[i], new MapPoint(get(buffer), get(buffer), get(buffer)));
                }
            }

            // 设置所有使用此属性的地图
            mapPropertiesEditor.getMapProperties().entrySet().parallelStream()
                    .filter(entry -> entry.getValue().entrance == mapProperties.entrance)
                    .forEach(entry -> {
                        // 添加该地图的边界和出入口数据
                        getMapEntrances().put(entry.getKey(), mapEntrance);
                    });
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        Consumer<MapEntrance> consumer = mapEntrance -> {
            // 计算新的出入口索引
            char diff; // 计算版本差异
            var version = getVersion();
            if (version == Version.SUPER_HACK) {
                diff = (char) 0x7E000;
            } else if (version == Version.SUPER_HACK_GENERAL) {
                diff = (char) 0x52000;
            } else {
                diff = (char) 0x1E000;
            }
            char newEntrance = (char) (bufferPosition - (getHeader().isTrained() ? 0x200 : 0x000) - (0x10 + diff + 0x8000));
            // 更新所有使用此数据的地图
            getMapEntrances().entrySet().parallelStream()
                    .filter(entry -> entry.getValue() == mapEntrance)
                    .forEach(entry ->
                            mapPropertiesEditor.getMapProperties(entry.getKey()).entrance = newEntrance
                    );

            // 写入边界数据
            put(buffer, mapEntrance.getBorder().toByteArray());

            // 写入地图出入口数量
            put(buffer, mapEntrance.getEntrances().size());

            if (!mapEntrance.getEntrances().isEmpty()) {
                // 写入地图出入口数据

                // 重新排序，需要顺序写入
                LinkedHashMap<MapPoint, MapPoint> linkedEntrances = new LinkedHashMap<>(mapEntrance.getEntrances());
                // 写入入口 X、Y
                for (MapPoint mapPoint : linkedEntrances.keySet()) {
                    put(buffer, mapPoint.getX());
                    put(buffer, mapPoint.getY());
                }
                // 写入出口 Map、X、Y
                for (MapPoint mapPoint : linkedEntrances.values()) {
                    put(buffer, mapPoint.getMap());
                    put(buffer, mapPoint.getX());
                    put(buffer, mapPoint.getY());
                }
            }
        };

        var mapEntrances = getMapEntrances();
        // 优先解析世界地图
        var worldMapEntrances = mapEntrances.remove(0);
        if (worldMapEntrances != null) {
            setPrgRomPosition(MAP_ENTRANCE_START_OFFSET);
            consumer.accept(worldMapEntrances);
        }

        if (getVersion() == Version.SUPER_HACK) {
            setPrgRomPosition(SH_MAP_ENTRANCE_START_OFFSET);
        } else if (getVersion() == Version.SUPER_HACK_GENERAL) {
            setPrgRomPosition(SHG_MAP_ENTRANCE_START_OFFSET);
        }
        // 地图
        mapEntrances.values().parallelStream()
                .distinct() // 去重
                .forEachOrdered(consumer);
        int end = bufferPosition - 1;
        if (end <= 0x1F999) {
            System.out.printf("地图边界和出入口编辑器：剩余%d个空闲字节\n", 0x1F999 - end);
        } else {
            System.out.printf("地图边界和出入口编辑器：错误！超出了数据上限%d字节\n", end - 0x1F999);
        }
        return true;
    }

    /**
     * @return 所有地图的边界和出入口
     */
    public Map<Integer, MapEntrance> getMapEntrances() {
        return mapEntrances;
    }

    /**
     * @return 指定地图的边界和出入口
     */
    public MapEntrance getMapEntrance(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
        return mapEntrances.get(map);
    }

    /**
     * @return 世界地图的边界和出入口
     */
    public MapEntrance getWorldMapEntrance() {
        return mapEntrances.get(0x00);
    }
}
