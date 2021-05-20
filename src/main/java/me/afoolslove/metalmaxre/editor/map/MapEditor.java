package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.*;
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
public class MapEditor extends AbstractEditor {
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
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        for (MapProperties mapProperties : mapPropertiesEditor.getMapProperties().values()) {
            int mapIndex = mapProperties.mapIndex;
            if (mapIndex >= 0xC000) {
                // 0xBB010
                buffer.position(getMetalMaxRe().getVROMOffset() + 0x2F000 + mapIndex);
            } else {
                // 0x00000(610)
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                buffer.position(getMetalMaxRe().getPROMOffset() + mapIndex);
            }

            MapBuilder mapBuilder = new MapBuilder();
            // 获取地图图块所有数量
            int size = (mapProperties.width & 0xFF) * (mapProperties.height & 0xFF);

            // 通过地图属性的地图数据索引读取地图数据
            MapBuilder.parseMap(buffer, 0, bytes -> mapBuilder.getTileCount() < size, new Predicate<>() {
                int one = 0;

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
                            int count = aByte & 0x7F;
                            mapBuilder.getLast().setCount(count);
                            break;
                    }
                    return true;
                }
            });
            // 按序添加地图
            maps.put(maps.size() + 1, mapBuilder);
        }
        /*
         * ArrayList<Integer> list = mapPropertiesEditor.getMapProperties().values().stream().mapToInt(value -> value.mapIndex).filter(value -> value >= 0xC000).boxed().distinct().collect(Collectors.toCollection(ArrayList::new));
         * Collections.sort(list);
         *
         * list
         */
        var list1 = mapPropertiesEditor.getMapProperties().entrySet().stream().filter(entry -> entry.getValue().mapIndex < 0xC000).sorted(Comparator.comparingInt(o -> o.getValue().mapIndex)).collect(Collectors.toList());
        var list2 = mapPropertiesEditor.getMapProperties().entrySet().stream().filter(entry -> entry.getValue().mapIndex >= 0xC000).sorted(Comparator.comparingInt(o -> o.getValue().mapIndex)).collect(Collectors.toList());

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

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : coincidence.entrySet()) {
            Integer map = entry.getKey();
            List<Map.Entry<Integer, Integer>> collect = entry.getValue().entrySet().stream().parallel().sorted((o1, o2) -> {
                return ((o1.getValue() & 0xFF00) >>> 8 - (o2.getValue() & 0xFF00) >>> 8) + (o1.getValue() & 0xFF) - (o2.getValue() & 0xFF);
            }).collect(Collectors.toList());

            //Collections.reverse(collect);

            System.out.println("Map:" + map);
            Map.Entry<Integer, Integer> integerIntegerEntry = collect.get(0);
            System.out.printf("%02X,%04d,%04d\n", integerIntegerEntry.getKey(), (integerIntegerEntry.getValue() & 0xFF00) >>> 8, integerIntegerEntry.getValue() & 0xFF);
        }
//
//

        int more = 0, c = 0;
        for (Map.Entry<Integer, byte[]> entry : maps.entrySet()) {
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties().get(entry.getKey());
            int mapIndex = mapProperties.mapIndex;

            if (mapIndex >= 0xC000) {
                buffer.position(getMetalMaxRe().getVROMOffset() + 0x2F000 + mapIndex);
                int i = buffer.position() + entry.getValue().length;
                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), buffer.position(), i);
                if (i >= 0xBF010) {
                    System.out.print(" >= 0xBF010");
                    more += i - 0xBF010;
                }
            } else {
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                buffer.position(getMetalMaxRe().getPROMOffset() + mapIndex);
                int i = buffer.position() + entry.getValue().length;
                System.out.printf("0x%02X, 0x%05X-0x%05X", entry.getKey(), buffer.position(), i);
                if (i >= 0x0B6D4) {
                    System.out.print(" >= 0x0B6D4");
                    more += i - 0x0B6D4;
                }
            }
            System.out.println();
            buffer.put(entry.getValue());

//            MapBuilder mapTiles = maps.get(i);
//            buffer.put(mapTiles.build());
        }
        System.out.println("more byte:" + more);
        return true;
    }
    // 0094400564128056212A50080480EA0C
    // 02B06012040094220400412048041240
    // 20BAF556AD2244A0120400801A21E290
    // A410550881A8502B5690025080412000
    // 8828040255AB008029114840220C9123
    // 740E12A548AD5A2020802008011074AD
    // 91A244D92A456AD11314840200601204
    // 448922500A02A500AC1A244010040090
    // 22B548952B560138300AC2A244041204
    // 868D9C64AC03308D802706D802506AD5
    // A218290A43A78FBC0088224500138350
    // 0128356D0022068100C1235900441002
    // 40C0128356D1A22448103300883AC580
    // 11075601A0300AC200441D62C0088380
    // 561402227F0080676CB97B83001C404E
    // 1C3C7EFDE428014202F0
    //

    public HashMap<Integer, MapBuilder> getMaps() {
        return maps;
    }

    public MapBuilder getMap(@Range(from = 0x01, to = 0xEF) int map) {
        return maps.get(map);
    }

    /*
     *
     *  没错，也失败了
     * public boolean add(int tile, int count) {
     *         tile &= 0x7F;
     *         count &= 0xFF;
     *         MapTile last = peekLast();
     *         // 判断是否需要合并
     *         if (last != null && last.tile == tile && last.count != 0x00 && (last.count & count) != 0x7F) {
     *             if (last.count + count < 0x80) {
     *                 // 与末尾图块合并
     *                 last.count += count;
     *                 return true;
     *             } else if (last.count + count < 0xFF) {
     *                 // 填满末尾图块，并添加一个图块数量为剩余的图块
     *                 int y = count + last.count - 0x7F;
     *                 last.count = 0x7F;
     *                 return y == 0 || add(tile, y);
     *             } else if (last.count + count > 0xFF){
     *                 // 填满末尾图块，并添加一个图块数量为剩余的图块
     *                 int y = count + last.count - 0x100;
     *                 // 没错就是0xFF
     *                 last.count = 0x00;
     *                 return y == 0 || add(tile, y);
     *             }
     *         }
     *         return add(new MapTile(tile, count));
     *     }
     *
     * 尘封的代码，太麻烦了，只有256 128的数量没有优化好
     *          已移步到MapBuilder中实现
     *         System.out.println("地图编辑器：开始优化地图");
     *         for (Map.Entry<Integer, MapBuilder> entry : maps.entrySet()) {
     *             MapTile[] mapTiles = entry.getValue().toArray(new MapTile[0]);
     *             // 合计省下的bit数量，1=7bit
     *             int bits = 0;
     *
     *             for (int i = 0; i < mapTiles.length; i++) {
     *                 MapTile mapTile = mapTiles[i];
     *                 if (mapTile.count == 0) {
     *                     // 数量为 0 的图块实际数量为 256
     *                     // 不可以与任何图块合并
     *                     continue;
     *                 }
     *
     *                 // 判断接下来的数据是否一致，是否需要合并
     *                 // 条件满足任意一条即可
     *                 // 合并条件一：数量合计大于或等于3
     *                 // 合并条件二：三个图块相同
     *                 int j = i + 1;
     *                 for (; j < mapTiles.length; j++) {
     *                     if (mapTiles[j].count == 0) {
     *                         // 数量为 0 的图块实际数量为 256
     *                         // 不可以与任何图块合并
     *                         break;
     *                     }
     *                     if (mapTiles[j].tile != mapTile.tile) {
     *                         // 计算相同的图块
     *                         break;
     *                     }
     *                 }
     *                 // 计算结束
     *
     *                 // 计算一共多少个图块
     *                 int count = 0;
     *                 for (int k = j - 1; k >= i; k--) {
     *                     count += mapTiles[k].count;
     *                 }
     *
     *                 // 如果图块数量或相同的图块不超过3个
     *                 // 就跳过这些相同的图块
     *                 if (count < 3 || j - i < 3) {
     *                     i = j;
     *                     continue;
     *                 }
     *
     *                 // 合并前计算总数量在128以下或128以上256以下
     *                 // 128以下时，直接加入
     *                 // 128以上256以下时，分成两个图块存放
     *                 // 256不合并
     *                 int finalCount = mapTile.count + count;
     *                 if (finalCount <= 0x7F) {
     *                     for (int k = j - 1; k > i; k--) {
     *                         mapTile.count += mapTiles[k].count;
     *                         bits += mapTiles[k].count;
     *                         // 置空表示已合并
     *                         mapTiles[k] = null;
     *                     }
     *                 } else if (finalCount >= 0xFF) {
     *                     // 分成多个图块存放
     *
     *                     // 多个图块的数量
     *                     int tileCount = finalCount / 0x100;
     *                     for (int k = 0; k < tileCount; k++) {
     *                         mapTile.count = 0xFF;
     *                         i++;
     *                         mapTile = mapTiles[i];
     *                     }
     *                     if (finalCount % 0x80 == 0) {
     *
     *                     }
     *
     *                 }
     *                 i = j;
     *             }
     *
     *             if (bits != 0) {
     *                 System.out.format("地图编辑器：%#02X 优化了%d...%d个byte\n", entry.getKey(), (bits * 7) / 8, (bits * 7) % 8);
     *             }
     *             entry.getValue().clear();
     *             entry.getValue().addAll(Arrays.stream(mapTiles).filter(Objects::nonNull).collect(Collectors.toList()));
     *         }
     *
     *         System.out.println("地图编辑器：优化地图结束");
     */
}
