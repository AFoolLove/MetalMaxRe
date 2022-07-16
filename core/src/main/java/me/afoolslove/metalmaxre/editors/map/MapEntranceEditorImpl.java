package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 地图边界和出入口编辑器
 * 包含世界地图
 * <p>
 * 修改地图的边界类型和地图的出入口
 * <p>
 * TODO: 写入错误
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class MapEntranceEditorImpl extends RomBufferWrapperAbstractEditor implements IMapEntranceEditor {
    private final DataAddress mapEntranceAddress;

    public MapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x1E990 - 0x10, 0x1F999 - 0x10));
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

        for (int map = 0, maxMapCount = mapPropertiesEditor.getMapProperties().size(); map < maxMapCount; map++) {
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(map);

            if (getMapEntrances().containsKey(map)) {
                // 排除已读取的地图
                continue;
            }


            // 索引到数据
            // 兼容SH和SHG，只用0xFF000中的FF，后三个无用
            prgPosition((getMapEntranceAddress().getStartAddress() & 0xFF000) + mapProperties.entrance - 0x8000);
//            System.out.printf("%02X. %05X ",map, position());

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
                    entrances[i] = new MapPoint(map, getBuffer().get(), getBuffer().get());
                }
                // 读取入口对应的出口
                for (int i = 0; i < count; i++) {
                    // 读取出口的 Map、X、Y
                    mapEntrance.getEntrances().put(entrances[i], new MapPoint(getBuffer().get(), getBuffer().get(), getBuffer().get()));
                }
            }
//            System.out.format("%05X\n", position());
//            if (map != 0) {
//                System.out.format("%02X. %02X\n", map, (mapProperties.entrance - mapPropertiesEditor.getMapProperties(map - 1).entrance) & 0xFF);
//            }

//            System.out.format("%02X. %02X, %s\n", map, temp, Arrays.toString(mapEntrance.getEntrances().values().stream().map(Object::toString).toArray()));

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
    public void onApply(@Editor.QuoteOnly IMapEditor mapEditor,
                        @Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
        position(getMapEntranceAddress());
        // 将每个地图添加一个是否已经写入入口相关数据的标志
        List<SingleMapEntry<Boolean, MapEntrance>> mapEntries = new ArrayList<>(mapEditor.getMapMaxCount());
        for (MapEntrance mapEntrance : getMapEntrances().values()) {
            mapEntries.add(SingleMapEntry.create(Boolean.FALSE, mapEntrance));
        }

        char newEntrance = mapPropertiesEditor.getWorldMapProperties().entrance;

        for (int i = 0, size = mapEntries.size(); i < size; i++) {
            var singleMapEntry = mapEntries.get(i);
            if (singleMapEntry.getKey()) {
                // 为true表示已处理，跳过
                continue;
            }
            singleMapEntry.setKey(Boolean.TRUE);

            var mapEntrance = singleMapEntry.getValue();

            final int currentPosition = position();
            // 写入边界数据
            getBuffer().put(mapEntrance.getBorder().toByteArray());

            // 写入地图出入口数量
            getBuffer().put(mapEntrance.getEntrances().size());

            if (!mapEntrance.getEntrances().isEmpty()) {
                // 写入地图出入口数据

                // 重新排序，需要顺序写入 出入口的坐标
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

            // 计算新的出入口索引
            // 计算版本差异
//            char diff = (char) (getMapEntranceAddress().getStartAddress() & 0xFF000);
//            char newEntrance = (char) (position() - (getBuffer().getHeader().isTrained() ? 0x200 : 0x000) - (0x10 + diff + 0x8000));
//            char newEntrance = (char) (position() - getBuffer().getHeader().getPrgRomStart(diff + 0x8000));
            newEntrance += position() - currentPosition;

            // 更新所有使用此数据的地图
            for (int j = i + 1; j < size; j++) {
                // 更新后面相同地址的地图，并修改为已处理
                var entry = mapEntries.get(j);
                if (!entry.getKey() && entry.getValue() == mapEntrance) {
                    mapPropertiesEditor.getMapProperties(j).entrance = newEntrance;
                    entry.setKey(Boolean.TRUE);
                }
            }
        }
        int end = position() - 1;
        if (end <= 0x1F999) {
            System.out.printf("地图边界和出入口编辑器：剩余%d个空闲字节\n", 0x1F999 - end);
        } else {
            System.out.printf("地图边界和出入口编辑器：错误！超出了数据上限%d字节\n", end - 0x1F999);
        }
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
    @Editor.TargetVersion("super_hack")
    public static class SHMapEntranceEditorImpl extends MapEntranceEditorImpl {
        public SHMapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, DataAddress.fromPRG(0x7E7B0 - 0x10));
        }

        @Override
        @Editor.Load
        public void onLoad(IMapPropertiesEditor mapPropertiesEditor) {
            super.onLoad(mapPropertiesEditor);
        }

        @Override
        @Editor.Apply
        public void onApply(@Editor.QuoteOnly IMapEditor mapEditor, @Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
            super.onApply(mapEditor, mapPropertiesEditor);
        }
    }

    /**
     * SuperHack通用版本
     */
    @Editor.TargetVersion("super_hack_general")
    public static class SHGMapEntranceEditorImpl extends MapEntranceEditorImpl {
        public SHGMapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, DataAddress.fromPRG(0x527B0 - 0x10));
        }

        @Override
        @Editor.Load
        public void onLoad(IMapPropertiesEditor mapPropertiesEditor) {
            super.onLoad(mapPropertiesEditor);
        }

        @Override
        @Editor.Apply
        public void onApply(@Editor.QuoteOnly IMapEditor mapEditor, @Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
            super.onApply(mapEditor, mapPropertiesEditor);
        }
    }
}
