package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 地图编辑器
 * <p>
 * 不包含世界地图
 * 不要使用 0 作为索引
 * <p>
 * 起始：0x00610、0xBB010
 * 结束：0x0B6D3、0xBF00F
 * <p>
 * 地图构成方式：
 * 每7bit作为为一段数据
 * 如果7bit为0，则接下来的两个7bit分别为tile和count
 * 如果7bit非0，则单独为一个tile
 *
 * <p>
 * 注：正常读取地图数据，写入地图需要完成地图重合度优化，并修改地图属性中的地图数据索引，否则装不下地图数据！！！！
 *
 * @author AFoolLove
 */
public class MapEditorImpl extends RomBufferWrapperAbstractEditor implements IMapEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapEditorImpl.class);
    public static final int[] MAP_CONTAINERS = {0x0B6D4 - 0x00610, 0xBF010 - 0xBB010};

    private final HashMap<Integer, MapBuilder> maps = new HashMap<>();

    public MapEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
    }

    @Editor.Load
    public void onLoad(IMapPropertiesEditor mapPropertiesEditor) {
        // 读取前清空数据
        getMaps().clear();

        for (MapProperties mapProperties : mapPropertiesEditor.getMapProperties().values()) {
            if (mapProperties instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }

            int mapIndex = mapProperties.mapIndex;
            if (mapIndex >= 0xC000) {
                // 0xBB010
                chrPosition(0x2F000 + mapIndex);
            } else {
                // 0x00000(610)
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                prgPosition(mapIndex);
            }

            MapBuilder mapBuilder = new MapBuilder();
            // 获取地图图块所有数量
            int size = mapProperties.intWidth() * mapProperties.intHeight();

            // 通过地图属性的地图数据索引读取地图数据
            MapBuilder.parseMap(getBuffer(), position(), 0, bytes -> mapBuilder.getTileCount() < size, new Predicate<>() {
                byte one = 0;

                @Override
                public boolean test(Byte aByte) {
                    switch (one) {
                        default: // 故意放这的
                        case 0:
                            if (aByte == 0B0000_0000) {
                                one++;
                            }
                            mapBuilder.add(aByte);
                            return true;
                        case 1:
                            one++;
                            mapBuilder.getLast().setTile(aByte);
                            break;
                        case 2:
                            one = 0;
                            mapBuilder.getLast().setCount(aByte & 0x7F);
                            break;
                    }
                    return true;
                }
            });
            // 按序添加地图
            getMaps().put(getMaps().size() + 1, mapBuilder);
        }
    }

    @Editor.Apply
    public void onApply(@Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {
        // TODO 将地图数据放入 0x00610-0x0B6D2和0xBB010-0xBF00F 两个地址中（包含值），并将每个byte[]的起始地址记录下来

        // 获取并创建所有地图的数据
        // K:mapID
        // V:mapData
        Map<Integer, byte[]> mapData = mapPropertiesEditor.getMapProperties().keySet().parallelStream()
                .filter(p -> p > 0x00)
                .map(m -> SingleMapEntry.create(m, getMap(m).build()))
                .collect(Collectors.toMap(SingleMapEntry::getKey, SingleMapEntry::getValue));
        // 排除世界地图
        mapData.remove(0x00);
        // 副本，用于遍历
        Map<Integer, byte[]> mapDataCopy = new HashMap<>(mapData);

        // 保存该地图指向的是哪张地图
        // 如果指向自己则代表这是一个新的地图
        // 如果指向其它地图则代表这是一张共享地图
        Map<Integer, Integer> mapState = new HashMap<>();
        for (Integer mapId : mapData.keySet()) {
            if (mapState.get(mapId) != null) {
                // 这个地图已经被设置了
                continue;
            }
            // 这是一个全新的地图

            // 获取地图数据
            byte[] data = mapData.get(mapId);

            // 判断其它地图是否与当前地图一致
            for (Map.Entry<Integer, byte[]> entry : mapDataCopy.entrySet()) {
                if (Arrays.equals(data, entry.getValue())) {
                    mapState.put(entry.getKey(), mapId);
                    if (!Objects.equals(mapId, entry.getKey())) {
                        LOGGER.info("地图编辑器：地图{}与地图{}使用相同地图",
                                NumberR.toHex(entry.getKey()),
                                NumberR.toHex(mapId));
                    }
                }
            }
        }

        int mapIndex = 0xC000;
        int mapIndex2 = 0x0600;
        // 开始写入地图数据
        for (Map.Entry<Integer, byte[]> entry : mapData.entrySet()) {
            Integer mapId = entry.getKey();
            if (mapState.get(mapId) == null) {
                // 已经写入，跳过
                continue;
            }
            byte[] data = entry.getValue();

            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(mapId);
            if (mapIndex == 0x10000 || (mapIndex + data.length) >= 0x10000) {
                // 没有空间储存了，储存到mapIndex2
                if (mapIndex2 == 0x0B6D3 || (mapIndex2 + data.length) >= 0x0B6D3) {
                    // 寄咯，存不下了
                    if (mapIndex == 0x10000 && mapIndex2 == 0x0B6D3) {
                        LOGGER.error("地图编辑器：没有剩余的空间储存地图{}", NumberR.toHex(mapId));
                        continue;
                    }
                    // 可能还能存下两张半截地图
                    if (mapIndex < 0x10000) {
                        // 塞入一个半截地图
                        mapProperties.mapIndex = (char) mapIndex;
                        getBuffer().putChr(0x2F000 + mapIndex, data, 0, 0x10000 - mapIndex);
                        LOGGER.warn("地图编辑器A：地图{}剩余{}字节未写入",
                                NumberR.toHex(mapId),
                                (mapIndex + data.length) - 0x10000);
                        mapIndex = 0x10000;
                    } else if (mapIndex2 < 0x0B6D3) {
                        // 塞入一个半截地图
                        mapProperties.mapIndex = (char) mapIndex2;
                        getBuffer().putPrg(mapIndex2, data, 0, 0x0B6D3 - mapIndex2);
                        LOGGER.warn("地图编辑器B：地图{}剩余{}字节未写入",
                                NumberR.toHex(mapId),
                                (mapIndex2 + data.length) - 0x0B6D3);
                        mapIndex2 = 0x0B6D3;
                    }
                } else {
                    // 储存到mapIndex2
                    mapProperties.mapIndex = (char) mapIndex2;
                    // 写入地图数据
                    getBuffer().putPrg(mapIndex2, data);
                    mapIndex2 += data.length;
                }
            } else {
                // 储存到mapIndex
                mapProperties.mapIndex = (char) mapIndex;
                // 写入地图数据
                getBuffer().putChr(0x2F000 + mapIndex, data);
                mapIndex += data.length;
            }

            // 将其它指向当前地图的地图更新为同一个索引
            for (Map.Entry<Integer, Integer> state : mapState.entrySet()) {
                if (Objects.equals(mapId, state.getValue())) {
                    mapPropertiesEditor.getMapProperties(state.getKey()).mapIndex = mapProperties.mapIndex;
                    // 设置为null，表示已经设置
                    mapState.replace(state.getKey(), null);
                }
            }
        }
    }

    @Override
    public HashMap<Integer, MapBuilder> getMaps() {
        return maps;
    }

    @Override
    public MapBuilder getMap(int map) {
        return maps.get(map);
    }
}
