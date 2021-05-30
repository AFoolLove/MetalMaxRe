package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 地图边界和出入口编辑器
 * <p>
 * 包含世界地图
 * <p>
 * 起始：0x1E990
 * 结束：0x1F999
 *
 * @author AFoolLove
 */
public class MapEntranceEditor extends AbstractEditor {
    private final Map<Integer, MapEntrance> mapEntrances = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        getMapEntrances().clear();

        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
            Integer map = mapPropertiesEntry.getKey();
            MapProperties mapProperties = mapPropertiesEntry.getValue();

            if (getMapEntrances().containsKey(map)) {
                // 排除已读取的地图
                continue;
            }

            MapBorder mapBorder;

            // 索引到数据
            buffer.position(0x10 + 0x1E000 + mapProperties.entrance - 0x8000);
            int temp = buffer.get() & 0xFF;

            switch (MapBorderType.getType(temp)) {
                case LAST:
                    mapBorder = new MapBorder(MapBorderType.LAST);
                    // 上一个位置，不需要读取额外数据
                    break;
                case FIXED:
                    mapBorder = new MapBorder(MapBorderType.FIXED);
                    // 固定位置，读取1个目标地图位置
                    mapBorder.add(new MapPoint(buffer.get(), buffer.get(), buffer.get()));
                    break;
                default:
                    mapBorder = new MapBorder(MapBorderType.DIRECTION);
                    // 不同的目标地图位置，读取4个目标地图位置

                    // 回退1byte，该byte属于地图数据
                    buffer.position(buffer.position() - 1);
                    // 分别读取：上下左右，的目标地图位置
                    for (int i = 0; i < 0x04; i++) {
                        mapBorder.add(new MapPoint(buffer.get(), buffer.get(), buffer.get()));
                    }
                    break;
            }

            MapEntrance mapEntrance = new MapEntrance(mapBorder);

            // 读取地图内的出入口数量
            int count = buffer.get() & 0xFF;

            if (count != 0x00) {
                MapPoint[] entrances = new MapPoint[count];
                // 读取入口数据
                for (int i = 0; i < count; i++) {
                    // 读取当前地图入口的 X、Y
                    entrances[i] = new MapPoint(buffer.get(), buffer.get());
                }
                // 读取入口对应的出口
                for (int i = 0; i < count; i++) {
                    // 读取出口的 Map、X、Y
                    mapEntrance.getEntrances().put(entrances[i], new MapPoint(buffer.get(), buffer.get(), buffer.get()));
                }
            }

            // 设置所有使用此属性的地图
            mapPropertiesEditor.getMapProperties().entrySet().stream().parallel()
                    .filter(entry -> entry.getValue().entrance == mapProperties.entrance)
                    .forEach(entry -> {
                        // 添加该地图的边界和出入口数据
                        getMapEntrances().put(entry.getKey(), mapEntrance);
                    });
        }

        return true;
    }

    /* TODO 错误的写入数据 */
    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        buffer.position(0x1E990);
        getMapEntrances().values().stream().parallel()
                .distinct()
                .forEachOrdered(mapEntrance -> {
                    // 计算新的出入口索引
                    char newEntrance = (char) (buffer.position() - 0x10 + 0x1E000 + 0x8000);

                    // 更新所有使用此数据的地图
                    getMapEntrances().entrySet().stream().parallel()
                            .filter(entry -> entry.getValue() == mapEntrance)
                            .forEach(entry -> {
                                mapPropertiesEditor.getMapProperties(entry.getKey()).entrance = newEntrance;
                            });

                    // 写入边界数据
                    buffer.put(mapEntrance.getBorder().toByteArray());

                    // 写入地图出入口数量
                    buffer.put((byte) mapEntrance.getEntrances().size());

                    if (!mapEntrance.getEntrances().isEmpty()) {
                        // 写入地图出入口数据

                        // 重新排序，需要顺序写入
                        LinkedHashMap<MapPoint, MapPoint> linkedEntrances = new LinkedHashMap<>(mapEntrance.getEntrances());
                        // 写入入口 X、Y
                        for (MapPoint mapPoint : linkedEntrances.keySet()) {
                            buffer.put(mapPoint.x);
                            buffer.put(mapPoint.y);
                        }
                        // 写入出口 Map、X、Y
                        for (MapPoint mapPoint : linkedEntrances.values()) {
                            buffer.put(mapPoint.map);
                            buffer.put(mapPoint.x);
                            buffer.put(mapPoint.y);
                        }
                    }
                });


        int end = buffer.position() - 1;
        if (end < 0x1F999) {
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
    public MapEntrance getMapEntrance(int map) {
        return mapEntrances.get(map);
    }
}
