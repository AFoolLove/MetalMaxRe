package me.afoolslove.metalmaxre.desktop.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.*;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 地图编辑器
 * 与 {@link me.afoolslove.metalmaxre.editors.map.MapEditorImpl} 一致，但地图0xC3及以后的数据不在原本的位置
 *
 * @author AFoolLove
 */
public class MapEditorImpl extends RomBufferWrapperAbstractEditor implements IMapEditor {
    public static final int[] MAP_CONTAINERS = {0x0B6D4 - 0x00610, 0xBF010 - 0xBB010};

    private final HashMap<Integer, MapBuilder> maps = new HashMap<>();

    public MapEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
    }

    @Editor.Load
    public void onLoad(IMapPropertiesEditor mapPropertiesEditor) {
        // 读取前清空数据
        getMaps().clear();

        for (Map.Entry<Integer, MapProperties> entry : mapPropertiesEditor.getMapProperties().entrySet()) {
            int mapId = entry.getKey();
            MapProperties mapProperties = entry.getValue();
            if (mapProperties instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }

            if (mapId >= 0xC3) {
                MapBuilder mapBuilder = new MapBuilder();
                // 获取地图图块所有数量
                int size = mapProperties.intWidth() * mapProperties.intHeight();
                int position = 0x50010 + ((mapId - 0xC3) * 0x1000);

                // 通过地图属性的地图数据索引读取地图数据
                MapBuilder.parseMap(getBuffer(), position, 0, bytes -> mapBuilder.getTileCount() < size, new Predicate<>() {
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
    public void onApply(@Editor.QuoteOnly IMapPropertiesEditor mapPropertiesEditor) {// 获取并创建所有地图的数据
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
                        System.out.printf("地图编辑器：地图%02X与地图%02X使用相同地图\n", entry.getKey(), mapId);
                    }
                }
            }
        }

        int mapIndex = 0xC000;
        int mapIndex2 = 0x0600;
        // 开始写入地图数据
        for (Map.Entry<Integer, byte[]> entry : mapData.entrySet()) {
            Integer mapId = entry.getKey();
            if (mapId >= 0xC3) {
                getBuffer().putPrg(0x50000 + ((mapId - 0xC3) * 0x1000), entry.getValue());
                continue;
            }
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
                        System.err.printf("地图编辑器：没有剩余的空间储存地图%02X\n", mapId);
                        continue;
                    }
                    // 可能还能存下两张半截地图
                    if (mapIndex < 0x10000) {
                        // 塞入一个半截地图
                        mapProperties.mapIndex = (char) mapIndex;
                        getBuffer().putChr(0x2F000 + mapIndex, data, 0, 0x10000 - mapIndex);
                        System.err.format("地图编辑器A：地图%02X剩余%d字节未写入\n", mapId, (mapIndex + data.length) - 0x10000);
                        mapIndex = 0x10000;
                    } else if (mapIndex2 < 0x0B6D3) {
                        // 塞入一个半截地图
                        mapProperties.mapIndex = (char) mapIndex2;
                        getBuffer().putPrg(mapIndex2, data, 0, 0x0B6D3 - mapIndex2);
                        System.err.format("地图编辑器B：地图%02X剩余%d字节未写入\n", mapId, (mapIndex2 + data.length) - 0x0B6D3);

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
