package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.Point2B;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * 地图边界和出入口编辑器
 * 包含世界地图
 * <p>
 * 修改地图的边界类型和地图的出入口
 * <p>
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class MapEntranceEditorImpl extends RomBufferWrapperAbstractEditor implements IMapEntranceEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapEntranceEditorImpl.class);

    /**
     * 地图边界和出入口数据地址
     */
    public static final String MAP_ENTRANCE_ADDRESS = "mapEntrance";

    public MapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x1E990 - 0x10, 0x1F999 - 0x10));
    }

    public MapEntranceEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                 DataAddress mapEntranceAddress) {
        super(metalMaxRe);
        putDataAddress(MAP_ENTRANCE_ADDRESS, mapEntranceAddress);
    }

    /**
     * K：map
     */
    private final Map<Integer, MapEntrance> mapEntrances = new HashMap<>();

    @Editor.Load
    public void onLoad(IMapPropertiesEditor mapPropertiesEditor) {
        // 读取前清空数据
        getMapEntrances().clear();

        DataAddress mapEntranceAddress = getDataAddress(MAP_ENTRANCE_ADDRESS);

        for (int mapId = 0, maxMapCount = mapPropertiesEditor.getMapProperties().size(); mapId < maxMapCount; mapId++) {
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(mapId);

            if (getMapEntrances().containsKey(mapId)) {
                // 排除已读取的地图
                continue;
            }

            // 索引到数据
            // 兼容SH和SHG，只用0xFF000中的FF，后三个无用
            prgPosition((mapEntranceAddress.getStartAddress() & 0xFF000) + mapProperties.getEntrance() - 0x8000);
//            System.out.printf("%02X. %05X ",map, position());

            MapEntrance mapEntrance = readBorderAndEntrance();
            // 设置所有使用此属性的地图
            // 注：使用parallelStream会导致部分地图未能加载出入口数据，这里使用stream
            mapPropertiesEditor.getMapProperties().entrySet().stream()
                    .filter(entry -> entry.getValue().getEntrance() == mapProperties.getEntrance())
                    .forEach(entry -> {
                        // 添加该地图的边界和出入口数据
                        MapEntrance copy = mapEntrance.copy();
                        getMapEntrances().put(entry.getKey(), copy);
//                        System.out.println("put " + entry.getKey());
                    });
        }
    }

    @Editor.Apply
    public void onApply(@Editor.QuoteOnly IMapEditor mapEditor,
                        @Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
        // 边界和出入口数据
        byte[][] mapEntrances = new byte[mapEditor.getMapMaxCount()][];
        // 边界数据
        byte[][] mapBorders = new byte[mapEditor.getMapMaxCount()][];
        for (Map.Entry<Integer, MapEntrance> entry : getMapEntrances().entrySet()) {
            MapEntrance mapEntrance = entry.getValue();
            mapBorders[entry.getKey()] = mapEntrance.getBorder().toByteArray();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 写入边界数据
            outputStream.writeBytes(mapBorders[entry.getKey()]);
            // 写入入口数量
            outputStream.write(entry.getValue().getEntrances().size());

            // 写入边界目标位置
            if (!entry.getValue().getEntrances().isEmpty()) {
                // 写入地图出入口数据

                // 重新排序，需要顺序写入 出入口的坐标
                List<SingleMapEntry<Point2B, MapPoint>> list = new ArrayList<>(entry.getValue().getEntrances());

                // 写入入口 X、Y
                for (SingleMapEntry<Point2B, MapPoint> mapEntry : list) {
                    outputStream.write(mapEntry.getKey().getX());
                    outputStream.write(mapEntry.getKey().getY());
                }
                // 写入出口 Map、X、Y
                for (SingleMapEntry<Point2B, MapPoint> mapEntry : list) {
                    outputStream.write(mapEntry.getValue().getMap());
                    outputStream.write(mapEntry.getValue().getX());
                    outputStream.write(mapEntry.getValue().getY());
                }
            }
            mapEntrances[entry.getKey()] = outputStream.toByteArray();
        }

        // 将会储存的边界和出入口数据
        List<byte[]> mapEntrancesData = new ArrayList<>();

        DataAddress mapEntranceAddress = getDataAddress(MAP_ENTRANCE_ADDRESS);
        // 边界和出入口数据索引
        char mapEntranceIndex = mapPropertiesEditor.getWorldMapProperties().getEntrance();
        final char endMapEntranceIndex = (char) (mapEntranceIndex + (mapEntranceAddress.length() - 1));
        for (int mapId = 0, count = mapEditor.getMapMaxCount(); mapId < count; mapId++) {
            byte[] mapEntrance = mapEntrances[mapId];
            if (mapEntrance == null) {
                // 已经设置边界和出入口
                continue;
            }

            if (mapEntranceIndex == endMapEntranceIndex
                    || (endMapEntranceIndex - mapEntranceIndex) < (mapEntrance.length - 1)) {
                // 必须保证地图边界和出入口数据的完整性，不能写入部分
                mapEntranceIndex = endMapEntranceIndex;

                LOGGER.error("地图边界和出入口编辑器：没有剩余的空间写入地图{}的边界和出入口数据：{}",
                        NumberR.toHex(mapId),
                        NumberR.toHexString(mapEntrance));
                continue;
            }

            // 将后面相同数据的事件图块索引一同设置
            MapEntrance currentMapEntrance = getMapEntrance(mapId);
            for (int afterMapId = mapId; afterMapId < count; afterMapId++) {
                MapEntrance afterMapEntrance = getMapEntrance(afterMapId);
                if (afterMapEntrance == null) {
                    continue;
                }
                if (afterMapEntrance != currentMapEntrance) {
                    if (!Arrays.equals(mapBorders[afterMapId], mapBorders[mapId])) {
                        // 不一样，下一个
                        continue;
                    }
                }
                // 判断所有出入口坐标是否一致
                if (!Objects.equals(afterMapEntrance.getEntrances(), currentMapEntrance.getEntrances())) {
                    // 不一致
                    continue;
                }

                mapPropertiesEditor.getMapProperties(afterMapId).setEntrance(mapEntranceIndex);
                mapEntrances[afterMapId] = null;
                if (afterMapId != mapId) {
                    LOGGER.info("地图边界和出入口编辑器：地图{}与{}使用相同边界和出入口",
                            NumberR.toHex(afterMapId),
                            NumberR.toHex(mapId));
                }
            }

            mapEntranceIndex += mapEntrance.length;
            mapEntrancesData.add(mapEntrance);
        }

        // 写入出入口
        position(mapEntranceAddress);
        for (byte[] mapEntrancesDatum : mapEntrancesData) {
            getBuffer().put(mapEntrancesDatum);
        }

        int end = mapEntranceAddress.getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            if (end > 0) {
                // 使用0xFF填充未使用的数据
                byte[] fillBytes = new byte[end];
                Arrays.fill(fillBytes, (byte) 0x00);
                getBuffer().put(fillBytes);
            }
            LOGGER.info("地图边界和出入口编辑器：剩余{}个空闲字节", end);
        } else {
            LOGGER.error("地图边界和出入口编辑器：错误！超出了数据上限{}字节", -end);
        }
    }

    protected synchronized MapEntrance readBorderAndEntrance() {
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
            Point2B[] entrances = new Point2B[count];
            // 读取入口数据
            for (int i = 0; i < count; i++) {
                // 读取当前地图入口的 X、Y
                entrances[i] = new Point2B(getBuffer().get(), getBuffer().get());
            }
            // 读取入口对应的出口
            for (int i = 0; i < count; i++) {
                MapPoint outPoint = new MapPoint(getBuffer().get(), getBuffer().get(), getBuffer().get());
                // 读取出口的 Map、X、Y
                mapEntrance.getEntrances().add(SingleMapEntry.create(entrances[i], outPoint));
            }
        }
        return mapEntrance;
    }

    @Override
    public Map<Integer, MapEntrance> getMapEntrances() {
        return mapEntrances;
    }

}
