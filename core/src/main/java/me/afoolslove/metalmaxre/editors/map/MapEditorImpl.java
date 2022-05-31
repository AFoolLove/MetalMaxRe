package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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
            int size = (mapProperties.width & 0xFF) * (mapProperties.height & 0xFF);

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
        var maps = new HashMap<Integer, byte[]>();
        for (Integer map : indexMaps.values()) {
            maps.put(map, getMap(map).build());
        }
        // TODO 将地图数据放入 0x00610-0x0B6D3和0xBB010-0xBF00F 两个地址中（包含值），并将每个byte[]的起始地址记录下来

        for (Map.Entry<Integer, byte[]> entry : maps.entrySet()) {
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties().get(entry.getKey());
            if (mapProperties instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }
            int mapIndex = mapProperties.mapIndex;

            // 定位到地图数据的起始地址
            if (mapIndex >= 0xC000) {
                chrPosition(0x2F000 + mapIndex);
//                int i = buffer.position() + entry.getValue().length;
//                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), buffer.position(), i);
//                if (i >= 0xBF010) {
////                    System.out.print(" >= 0xBF010");
//                    more += i - 0xBF010;
//                }
            } else {
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                prgPosition(mapIndex);
//                int i = buffer.position() + entry.getValue().length;
//                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), buffer.position(), i);
//                if (i >= 0x0B6D4) {
////                    System.out.print(" >= 0x0B6D4");
//                    more += i - 0x0B6D4;
//                }
            }
//            System.out.println();
            // 写入地图数据
            getBuffer().put(entry.getValue());

//            MapBuilder mapTiles = maps.get(i);
//            buffer.put(mapTiles.build());
        }
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