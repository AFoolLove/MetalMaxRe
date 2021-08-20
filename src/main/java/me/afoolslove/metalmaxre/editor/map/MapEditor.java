package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.Arrays;
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
public class MapEditor extends AbstractEditor<MapEditor> {
    public static final int MAP_MAX_COUNT = 0xF0;
    public static final int[] MAP_CONTAINERS = {0x0B6D4 - 0x00610, 0xBF010 - 0xBB010};
    public static final int MAP_MAX_CAPACITY = Arrays.stream(MAP_CONTAINERS).sum();

    private final HashMap<Integer, MapBuilder> maps = new HashMap<>();

    public static void main(String[] args) {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.add(12, 80);
        mapBuilder.add(12, 0);
        mapBuilder.add(11, 80);
        mapBuilder.add(12, 80);

        for (byte b : mapBuilder.build()) {
            System.out.printf("%02X ", b);
        }
    }

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        maps.clear();

        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        for (MapProperties mapProperties : mapPropertiesEditor.getMapProperties().values()) {
            if (mapProperties instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }

            int mapIndex = mapProperties.mapIndex;
            if (mapIndex >= 0xC000) {
                // 0xBB010
                setChrRomPosition(0x2F000 + mapIndex);
            } else {
                // 0x00000(610)
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                setPrgRomPosition(mapIndex);
            }

            MapBuilder mapBuilder = new MapBuilder();
            // 获取地图图块所有数量
            int size = (mapProperties.width & 0xFF) * (mapProperties.height & 0xFF);

            // 通过地图属性的地图数据索引读取地图数据
            MapBuilder.parseMap(buffer, bufferPosition, 0, bytes -> mapBuilder.getTileCount() < size, new Predicate<>() {
                byte one = 0;

                @Override
                public boolean test(Byte aByte) {
                    switch (one) {
                        default:
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
            maps.put(maps.size() + 1, mapBuilder);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

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


        var maps = new HashMap<Integer, byte[]>();
        for (Integer map : indexMaps.values()) {
            maps.put(map, getMap(map).build());
        }

/*
        // --------- TEST

        // 重合度
        // K：MapId
        // V：与其它地图的重合度
        //      负数为尾部，正数位头部，不包含重合度为的0地图
        var coincidence = new HashMap<Integer, Map<Integer, Integer>>();

        for (Map.Entry<Integer, byte[]> entry : maps.entrySet()) {
            byte[] mapA = entry.getValue();
            HashMap<Integer, Integer> coincidenceMap = new HashMap<>();
            coincidence.put(entry.getKey(), coincidenceMap);
            for (Map.Entry<Integer, byte[]> other : maps.entrySet()) {
                if (!Objects.equals(other.getKey(), entry.getKey())) {
                    byte[] mapB = other.getValue();

                    int coincidenceStart = 0, coincidenceEnd = 0;
                    // 计算尾部
                    for (int i = 0, j = mapA.length - 1; i < mapB.length && j >= 0; i++, j--) {
                        if (mapA[j] != mapB[i]) {
                            coincidenceStart = i + 1;
                            break;
                        }
                    }

                    // 计算头部
                    for (int i = 0, j = mapB.length - 1; i < mapA.length && j >= 0; i++, j--) {
                        if (mapB[j] != mapA[i]) {
                            coincidenceEnd = i + 1;
                            break;
                        }
                    }

                    if ((coincidenceStart | coincidenceEnd) != 0x00) {
                        coincidenceMap.put(other.getKey(), (coincidenceStart << 8) + coincidenceEnd);
                    }
                }
            }
        }

//        for (Map.Entry<Integer, Map<Integer, Integer>> entry : coincidence.entrySet()) {
//            Integer map = entry.getKey();
//            List<Map.Entry<Integer, Integer>> collect = entry.getValue().entrySet().parallelStream().sorted((o1, o2) -> {
//                return ((o1.getValue() & 0xFF00) >>> 8 - (o2.getValue() & 0xFF00) >>> 8) + (o1.getValue() & 0xFF) - (o2.getValue() & 0xFF);
//            }).collect(Collectors.toList());
//
//            //Collections.reverse(collect);
//
//            System.out.println("Map:" + map);
//            Map.Entry<Integer, Integer> integerIntegerEntry = collect.get(0);
//            System.out.printf("%02X,%04d,%04d\n", integerIntegerEntry.getKey(), (integerIntegerEntry.getValue() & 0xFF00) >>> 8, integerIntegerEntry.getValue() & 0xFF);
//        }
*/

        for (Map.Entry<Integer, byte[]> entry : maps.entrySet()) {
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties().get(entry.getKey());
            if (mapProperties instanceof WorldMapProperties) {
                // 排除世界地图
                continue;
            }
            int mapIndex = mapProperties.mapIndex;

            // 定位到地图数据的起始地址
            if (mapIndex >= 0xC000) {
                setChrRomPosition(0x2F000 + mapIndex);
//                int i = buffer.position() + entry.getValue().length;
//                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), buffer.position(), i);
//                if (i >= 0xBF010) {
////                    System.out.print(" >= 0xBF010");
//                    more += i - 0xBF010;
//                }
            } else {
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                setPrgRomPosition(mapIndex);
//                int i = buffer.position() + entry.getValue().length;
//                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), buffer.position(), i);
//                if (i >= 0x0B6D4) {
////                    System.out.print(" >= 0x0B6D4");
//                    more += i - 0x0B6D4;
//                }
            }
//            System.out.println();
            // 写入地图数据
            put(buffer, entry.getValue());

//            MapBuilder mapTiles = maps.get(i);
//            buffer.put(mapTiles.build());
        }
//        System.out.println("more byte:" + more);
        return true;
    }

    /**
     * @return 所有地图的构建器
     */
    public HashMap<Integer, MapBuilder> getMaps() {
        return maps;
    }

    /**
     * @return 指定地图的构建器
     */
    public MapBuilder getMap(@Range(from = 0x01, to = 0xEF) int map) {
        return maps.get(map);
    }
}
