package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

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

        // 创建一个临时的map，排除相同的地图
        // K:mapIndex
        // V:MapId
        var indexMaps = new HashMap<Character, Integer>();
        for (Map.Entry<Integer, MapProperties> entry : mapPropertiesEditor.getMapProperties().entrySet()) {
            // 不存在就put
//            indexMaps.computeIfAbsent(entry.getValue().mapIndex, k -> entry.getKey());
            if (entry.getValue() instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }

            Integer map = indexMaps.get(entry.getValue().mapIndex);
            if (map == null) {
                indexMaps.put(entry.getValue().mapIndex, entry.getKey());
            } else {
                System.out.printf("地图编辑器：地图%#04X与地图%#04X使用相同地图\n", entry.getKey(), map);
            }
        }


        // K:mapID
        // V:mapData
        var maps = indexMaps.values().parallelStream()
                .map(m -> SingleMapEntry.create(m, getMap(m).build()))
                .collect(Collectors.toMap(SingleMapEntry::getKey, SingleMapEntry::getValue));

        // TODO 将地图数据放入 0x00610-0x0B6D2和0xBB010-0xBF00F 两个地址中（包含值），并将每个byte[]的起始地址记录下来

        int mapIndex = 0xC000;
        int mapIndex2 = 0x0600;
        for (Map.Entry<Integer, byte[]> entry : maps.entrySet()) {
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(entry.getKey());
            if (mapProperties instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }

            byte[] mapData = entry.getValue();
            if (mapIndex < 0x10000 && mapIndex + mapData.length >= 0x10000) {
                for (Map.Entry<Integer, MapProperties> mapEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                    if (Objects.equals(mapEntry.getKey(), entry.getKey())) {
                        continue;
                    }
                    if (mapEntry.getValue().mapIndex == mapProperties.mapIndex) {
                        mapEntry.getValue().mapIndex = (char) mapIndex;
                    }
                }
                mapProperties.mapIndex = (char) mapIndex;
                // 截取，能写入的部分数据
                System.out.format("地图编辑器：地图%02X剩余%d字节未写入\n", entry.getKey(), (mapIndex + mapData.length) - 0x10000);
                getBuffer().putChr(0x2F000 + mapIndex, mapData, 0, 0x10000 - mapIndex);
                mapIndex = 0x10000;
                continue;
            } else if (mapIndex2 < 0x0B6D3 && mapIndex2 + mapData.length >= 0x0B6D3) {
                for (Map.Entry<Integer, MapProperties> mapEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                    if (Objects.equals(mapEntry.getKey(), entry.getKey())) {
                        continue;
                    }
                    if (mapEntry.getValue().mapIndex == mapProperties.mapIndex) {
                        mapEntry.getValue().mapIndex = (char) mapIndex2;
                    }
                }
                mapProperties.mapIndex = (char) mapIndex2;
                // 截取，能写入的部分数据
                System.out.format("地图编辑器：地图%02X剩余%d字节未写入\n", entry.getKey(), (mapIndex2 + mapData.length) - 0x0B6D3);
                getBuffer().putPrg(mapIndex2, mapData, 0, 0x0B6D3 - mapIndex2);
                mapIndex2 = 0x0B6D3;
                continue;
            }


//            // 定位到地图数据的起始地址
//            if (mapIndex >= 0xC000) {
//                mapIndex = 0x2F000 + mapIndex;
//                chrPosition(mapIndex);
////                int i = position() + entry.getValue().length;
////                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), position(), i);
////                if (i >= 0xBF010) {
////                    System.out.print(" >= 0xBF010");
//////                    more += i - 0xBF010;
////                }
////                System.out.println();
//            } else {
//                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
//                prgPosition(mapIndex);
////                int i = position() + entry.getValue().length;
////                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), position(), i);
////                if (i >= 0x0B6D4) {
////                    System.out.print(" >= 0x0B6D4");
//////                    more += i - 0x0B6D4;
////                }
////                System.out.println();
//            }
////            System.out.println();

            if (mapIndex < 0x10000) {
                for (Map.Entry<Integer, MapProperties> mapEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                    if (Objects.equals(mapEntry.getKey(), entry.getKey())) {
                        continue;
                    }
                    if (mapEntry.getValue().mapIndex == mapProperties.mapIndex) {
                        mapEntry.getValue().mapIndex = (char) mapIndex;
                    }
                }
                mapProperties.mapIndex = (char) mapIndex;
                // 写入地图数据
                getBuffer().putChr(0x2F000 + mapIndex, mapData);
                mapIndex += mapData.length;
            } else {
                for (Map.Entry<Integer, MapProperties> mapEntry : mapPropertiesEditor.getMapProperties().entrySet()) {
                    if (Objects.equals(mapEntry.getKey(), entry.getKey())) {
                        continue;
                    }
                    if (mapEntry.getValue().mapIndex == mapProperties.mapIndex) {
                        mapEntry.getValue().mapIndex = (char) mapIndex2;
                    }
                }
                mapProperties.mapIndex = (char) mapIndex2;
                // 写入地图数据
                getBuffer().putPrg(mapIndex2, mapData);
                mapIndex2 += mapData.length;
            }
        }
        System.out.println(mapIndex);
        System.out.println(mapIndex2);
//        System.out.println("more byte:" + more);
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
