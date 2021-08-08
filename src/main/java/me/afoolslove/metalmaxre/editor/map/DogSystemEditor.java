package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * 犬系统
 * 在世界地图中设置犬系统的传送的坐标
 * 包含设置城镇
 * <p>
 * 起始：0x3272D
 * 结束：0x32744
 * <p>
 * <p>
 * 修改意向：修改传送代码，让其支持任意地图
 * ---------程序代码入口：0x3270F
 * <p>
 * 2021年5月23日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class DogSystemEditor extends AbstractEditor<DogSystemEditor> {
    /**
     * 目的地最大数量
     */
    public static final int DESTINATION_MAX_COUNT = 0x0C;

    public static final int DESTINATION_POINT_START_OFFSET = 0x3272D - 0x10;
    public static final int DESTINATION_POINT_END_OFFSET = 0x32738 - 0x10;
    public static final int DESTINATION_START_OFFSET = 0x34707 - 0x10;
    public static final int DESTINATION_END_OFFSET = 0x34712 - 0x10;

    public static final int TELEPORT_MAP_MAX_MAP_COUNT = 0x0D;
    public static final int TELEPORT_MAP_START_OFFSET = 0x3538C - 0x10;
    public static final int TELEPORT_MAP_X_START_OFFSET = 0x35399 - 0x10;
    public static final int TELEPORT_MAP_Y_START_OFFSET = 0x353A9 - 0x10;

    /**
     * 犬系统一共就 3*4 个目的地，写死算求
     */
    private final List<MapPoint> destinations = new ArrayList<>(DESTINATION_MAX_COUNT);

    /**
     * 将某个地图设置为城镇
     */
    private final HashMap<Integer, Integer> towns = new HashMap<>(DESTINATION_MAX_COUNT);

    /**
     * 可以将一个地图设置为某一个城镇
     * 只能设置两个！
     */
    private final HashMap<Integer, Integer> townSeries = new HashMap<>(0x02);

    /**
     * 时空隧道的目的地
     */
    private final HashMap<Integer, MapPoint> teleport = new HashMap<>(TELEPORT_MAP_MAX_MAP_COUNT);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        destinations.clear();
        towns.clear();
        townSeries.clear();
        teleport.clear();

        byte[] xs = new byte[DESTINATION_MAX_COUNT];
        byte[] ys = new byte[DESTINATION_MAX_COUNT];

        // (0x30 & 0B1111_1000) >>> 3 // 0x0441的偏移量
        // (0x30 & 0x0000_0111) // 指向的bit位
        // 读取目的地坐标
        setPrgRomPosition(buffer, DESTINATION_POINT_START_OFFSET);
        get(buffer, xs);
        get(buffer, ys);

        // 储存目的地坐标
        for (int i = 0; i < DESTINATION_MAX_COUNT; i++) {
            MapPoint destination = new MapPoint();
            destination.setCamera(xs[i], ys[i]);
            destinations.add(i, destination);
        }

        byte[] towns = new byte[DESTINATION_MAX_COUNT];
        byte[] townValues = new byte[DESTINATION_MAX_COUNT];

        // 读取城镇
        setPrgRomPosition(buffer, DESTINATION_START_OFFSET);
        get(buffer, towns);
        // 读取城镇数据
        // 这个数据就厉害了
        // 可以修改0x0441-0x460的数据，但是只能 或(|)运算，无法移除
        // 有需求再修改
        setPosition(0x3471E);
        get(buffer, townValues);

        // 添加城镇映射
        for (int i = 0; i < DESTINATION_MAX_COUNT; i++) {
            getTowns().put(i, towns[i] & 0xFF);
        }

        // 城镇附属，最多2个，格式和城镇一样，就不重复注释了
        byte[] townSeries = new byte[0x02];
        byte[] townSeriesValues = new byte[0x02];
        setPosition(0x3471B);
        get(buffer, townSeries);
        setPosition(0x34732);
        get(buffer, townSeriesValues);
        for (int i = 0; i < 0x02; i++) {
            getTownSeries().put(townSeries[i] & 0xFF, townSeriesValues[i] & 0xFF);
        }

        // 读取时空隧道目的地
        byte[] maps = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        xs = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        ys = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        // 读取时空隧道所有目的地地图
        setPrgRomPosition(buffer, TELEPORT_MAP_START_OFFSET);
        get(buffer, maps);
        // 读取时空隧道所有目的地地图的X坐标
        setPrgRomPosition(buffer, TELEPORT_MAP_X_START_OFFSET);
        get(buffer, xs);
        // 读取时空隧道所有目的地地图的Y坐标
        setPrgRomPosition(buffer, TELEPORT_MAP_Y_START_OFFSET);
        get(buffer, ys);
        for (int i = 0; i < TELEPORT_MAP_MAX_MAP_COUNT; i++) {
            teleport.put(i, new MapPoint(maps[i], xs[i], ys[i]));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] xs = new byte[DESTINATION_MAX_COUNT];
        byte[] ys = new byte[DESTINATION_MAX_COUNT];

        // 分解目的地坐标

        // 移除多余的目的地坐标
        Iterator<MapPoint> iterator = destinations.iterator();
        limit(iterator, () -> destinations.size() > DESTINATION_MAX_COUNT, removed -> {
            System.out.printf("犬系统编辑器：移除多余的目的地 %s\n", removed);
        });

        int i = 0;
        while (iterator.hasNext()) {
            MapPoint destination = iterator.next();
            xs[i] = destination.getCameraX();
            ys[i] = destination.getCameraY();
            i++;
        }

        if (i < DESTINATION_MAX_COUNT) {
            System.out.printf("犬系统编辑器：警告！！！剩余%d个目的地可能传送到地图坐标 0,0 的位置\n", i);
        }

        // 写入目的地
        setPrgRomPosition(buffer, DESTINATION_POINT_START_OFFSET);
        put(buffer, xs);
        put(buffer, ys);


        // 写入城镇对应的地图

        Iterator<Integer> townIterator = towns.values().iterator();
        // 移除多余的城镇
        limit(townIterator, () -> towns.size() > DESTINATION_MAX_COUNT, removed -> {
            System.out.printf("犬系统编辑器：移除多余的城镇 %02X\n", removed);
        });

        // 写入城镇
        setPrgRomPosition(buffer, DESTINATION_START_OFFSET);
        while (townIterator.hasNext()) {
            Integer next = townIterator.next();
            put(buffer, next.byteValue());
        }

        // 城镇附属
        Iterator<Map.Entry<Integer, Integer>> townSeriesIterator = townSeries.entrySet().iterator();
        // 移除多余的城镇附属
        limit(townSeriesIterator, () -> townSeries.size() > 0x02, removed -> {
            System.out.printf("犬系统编辑器：移除多余的城镇 %02X\n", removed.getKey());
        });

        // 写入城镇附属
        byte[] townSeries = new byte[0x02];
        byte[] townSeriesValues = new byte[0x02];

        int j = 0;
        while (townSeriesIterator.hasNext()) {
            Map.Entry<Integer, Integer> next = townSeriesIterator.next();
            townSeries[j] = next.getKey().byteValue();
            townSeriesValues[j] = next.getValue().byteValue();
            j++;
        }
        // 写入附属地图
        setPosition(0x3471B);
        put(buffer, townSeries);
        // 写入附属地图的所属地图（也可以是其它数据
        setPosition(0x34732);
        put(buffer, townSeriesValues);

        // 写入时空隧道目的地和坐标
        byte[] maps = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        xs = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        ys = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        for (i = 0; i < TELEPORT_MAP_MAX_MAP_COUNT; i++) {
            MapPoint point = teleport.get(i);
            maps[i] = point.getMap();
            xs[i] = point.getX();
            ys[i] = point.getY();
        }
        // 写入时空隧道所有目的地地图
        setPrgRomPosition(buffer, TELEPORT_MAP_START_OFFSET);
        put(buffer, maps);
        // 写入时空隧道所有目的地地图的X坐标
        setPrgRomPosition(buffer, TELEPORT_MAP_X_START_OFFSET);
        put(buffer, xs);
        // 写入时空隧道所有目的地地图的Y坐标
        setPrgRomPosition(buffer, TELEPORT_MAP_Y_START_OFFSET);
        put(buffer, ys);
        return true;
    }

    /**
     * @return 获取所有目的地
     */
    public List<MapPoint> getDestinations() {
        return destinations;
    }

    /**
     * @return 获取指定目的地的坐标
     */
    public MapPoint getDestination(int destination) {
        return destinations.get(destination);
    }

    /**
     * @return 所有城镇和其对应地图
     */
    public HashMap<Integer, Integer> getTowns() {
        return towns;
    }

    /**
     * @return 城镇对应的地图
     */
    public Integer getTown(int index) {
        return towns.get(index);
    }

    /**
     * @return 所有城镇附属和其对应的地图
     */
    public HashMap<Integer, Integer> getTownSeries() {
        return townSeries;
    }

    /**
     * @return 城镇附属和其对应的地图
     */
    public Integer getTownSeries(int index) {
        return townSeries.get(index);
    }

    /**
     * @return 所有时空隧道目的地
     */
    public HashMap<Integer, MapPoint> getTeleport() {
        return teleport;
    }

    /**
     * @return 通过城镇序号（不是地图id）获取传送目的地
     */
    public MapPoint getTeleport(@Range(from = 0x00, to = TELEPORT_MAP_MAX_MAP_COUNT - 1) int index) {
        return teleport.get(index);
    }
}
