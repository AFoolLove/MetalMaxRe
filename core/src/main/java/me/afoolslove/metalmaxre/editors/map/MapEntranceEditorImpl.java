package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 地图边界和出入口编辑器
 * 包含世界地图
 * <p>
 * 修改地图的边界类型和地图的出入口
 * <p>
 * TODO: 多版本读取正常，多版本写入未测试
 *
 * @author AFoolLove
 */
public class MapEntranceEditorImpl extends RomBufferWrapperAbstractEditor implements IMapEntranceEditor {
    private final DataAddress mapEntranceAddress;

    public MapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x1E990 - 0x10, 0x1F990 - 0x10));
    }

    public MapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                 DataAddress mapEntranceAddress) {
        super(metalMaxRe);
        this.mapEntranceAddress = mapEntranceAddress;
    }

    /**
     * K：map
     */
    private final Map<Integer, MapEntrance> mapEntrances = new HashMap<>();

    @Editor.Load
    public void onLoad(IMapPropertiesEditor mapPropertiesEditor) {
        // 读取前清空数据
        getMapEntrances().clear();

        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
            Integer map = mapPropertiesEntry.getKey();
            MapProperties mapProperties = mapPropertiesEntry.getValue();

            if (getMapEntrances().containsKey(map)) {
                // 排除已读取的地图
                continue;
            }


            // 索引到数据
            // 兼容SH和SHG，只用0xFF000中的FF，后三个无用
            prgPosition((getMapEntranceAddress().getStartAddress() & 0xFF000) + mapProperties.entrance - 0x8000);

            int temp = getBuffer().getToInt();

            MapBorder mapBorder;
            switch (MapBorderType.getType(temp)) {
                // 上一个位置，不需要读取额外数据
                case LAST -> mapBorder = new MapBorder(MapBorderType.LAST);

                // 固定位置
                case FIXED -> {
                    mapBorder = new MapBorder(MapBorderType.FIXED);
                    // 只读取1个目标地图位置
                    mapBorder.add(new MapPoint(getBuffer().get(), getBuffer().get(), getBuffer().get()));
                }
                // 不同的目标地图位置
                default -> {
                    mapBorder = new MapBorder(MapBorderType.DIRECTION);
                    // 回退1byte，该byte属于地图数据
                    offsetPosition(-1);
                    // 读取4个目标地图位置
                    // 分别读取：上下左右，的目标地图位置
                    for (int i = 0; i < 0x04; i++) {
                        mapBorder.add(new MapPoint(getBuffer().get(), getBuffer().get(), getBuffer().get()));
                    }
                }
            }

            MapEntrance mapEntrance = new MapEntrance(mapBorder);

            // 读取地图内的出入口数量
            int count = getBuffer().getToInt();

            if (count != 0x00) {
                MapPoint[] entrances = new MapPoint[count];
                // 读取入口数据
                for (int i = 0; i < count; i++) {
                    // 读取当前地图入口的 X、Y
                    entrances[i] = new MapPoint(0x00, getBuffer().get(), getBuffer().get());
                }
                // 读取入口对应的出口
                for (int i = 0; i < count; i++) {
                    // 读取出口的 Map、X、Y
                    mapEntrance.getEntrances().put(entrances[i], new MapPoint(getBuffer().get(), getBuffer().get(), getBuffer().get()));
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
    }

    @Editor.Apply
    public void onApply(@Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
        Consumer<MapEntrance> consumer = mapEntrance -> {
            // 计算新的出入口索引
            // 计算版本差异
            char diff = (char) (getMapEntranceAddress().getStartAddress() & 0xFF000);
            char newEntrance = (char) (position() - (getBuffer().getHeader().isTrained() ? 0x200 : 0x000) - (0x10 + diff + 0x8000));
            // 更新所有使用此数据的地图
            getMapEntrances().entrySet().parallelStream()
                    .filter(entry -> entry.getValue() == mapEntrance)
                    .forEach(entry ->
                            mapPropertiesEditor.getMapProperties(entry.getKey()).entrance = newEntrance
                    );

            // 写入边界数据
            getBuffer().put(mapEntrance.getBorder().toByteArray());

            // 写入地图出入口数量
            getBuffer().put(mapEntrance.getEntrances().size());

            if (!mapEntrance.getEntrances().isEmpty()) {
                // 写入地图出入口数据

                // 重新排序，需要顺序写入
                LinkedHashMap<MapPoint, MapPoint> linkedEntrances = new LinkedHashMap<>(mapEntrance.getEntrances());
                // 写入入口 X、Y
                for (MapPoint mapPoint : linkedEntrances.keySet()) {
                    getBuffer().put(mapPoint.getX());
                    getBuffer().put(mapPoint.getY());
                }
                // 写入出口 Map、X、Y
                for (MapPoint mapPoint : linkedEntrances.values()) {
                    getBuffer().put(mapPoint.getMap());
                    getBuffer().put(mapPoint.getX());
                    getBuffer().put(mapPoint.getY());
                }
            }
        };

        var mapEntrances = getMapEntrances();
        // 优先解析世界地图
        var worldMapEntrances = mapEntrances.get(0);
        if (worldMapEntrances != null) {
            position(getMapEntranceAddress());
            consumer.accept(worldMapEntrances);
        }

//        if (getVersion() == Version.SUPER_HACK) {
//            setPrgRomPosition(SH_MAP_ENTRANCE_START_OFFSET);
//        } else if (getVersion() == Version.SUPER_HACK_GENERAL) {
//            setPrgRomPosition(SHG_MAP_ENTRANCE_START_OFFSET);
//        }
        // 地图
        mapEntrances.values().parallelStream()
                .distinct() // 去重
                .forEachOrdered(consumer);
//        int end = position() - 1;
//        if (end <= 0x1F999) {
//            System.out.printf("地图边界和出入口编辑器：剩余%d个空闲字节\n", 0x1F999 - end);
//        } else {
//            System.out.printf("地图边界和出入口编辑器：错误！超出了数据上限%d字节\n", end - 0x1F999);
//        }
    }

    @Override
    public DataAddress getMapEntranceAddress() {
        return mapEntranceAddress;
    }

    @Override
    public Map<Integer, MapEntrance> getMapEntrances() {
        return mapEntrances;
    }

    /**
     * SuperHack版本
     */
    public static class SHMapEntranceEditorImpl extends MapEntranceEditorImpl {
        public SHMapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, DataAddress.fromPRG(0x7E7B0 - 0x10));
        }
    }

    /**
     * SuperHack通用版本
     */
    public static class SHGMapEntranceEditorImpl extends MapEntranceEditorImpl {
        public SHGMapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, DataAddress.fromPRG(0x527B0 - 0x10));
        }
    }
}
