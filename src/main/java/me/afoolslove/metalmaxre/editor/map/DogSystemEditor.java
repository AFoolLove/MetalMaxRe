package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
     * 使用犬系统传送城镇的目的地<p>
     * 只支持，X和Y坐标，地图为固定的世界地图
     */
    public static final int TOWN_MAX_COUNT = 0x0C;
    public static final int TOWN_LOCATION_START_OFFSET = 0x3272D - 0x10;
    public static final int TOWN_LOCATION_END_OFFSET = 0x32738 - 0x10;
    public static final int TOWN_START_OFFSET = 0x34707 - 0x10;
    public static final int TOWN_END_OFFSET = 0x34712 - 0x10;

    /**
     * 使用传送机器传送时的目的地<p>
     * 支持地图和X、Y坐标修改
     */
    public static final int TELEPORT_MAP_MAX_MAP_COUNT = 0x0D;
    public static final int TELEPORT_MAP_START_OFFSET = 0x3538C - 0x10;
    public static final int TELEPORT_MAP_X_START_OFFSET = 0x35399 - 0x10;
    public static final int TELEPORT_MAP_Y_START_OFFSET = 0x353A9 - 0x10;

    /**
     * 犬系统一共就 3*4 个目的地，写死算求
     */
    private final MapPoint[] townLocations = new MapPoint[TOWN_MAX_COUNT];

    /**
     * 将某个地图设置为城镇
     */
    private final byte[] towns = new byte[TOWN_MAX_COUNT];

    /**
     * 可以将一个地图设置为某一个城镇<p>
     * 只能设置两个！
     */
    private final HashMap<Integer, Integer> townSeries = new HashMap<>(0x02);

    /**
     * 时空隧道的目的地
     */
    private final MapPoint[] teleport = new MapPoint[TELEPORT_MAP_MAX_MAP_COUNT];

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        for (int i = 0; i < townLocations.length; i++) {
            townLocations[i] = new MapPoint();
        }
        Arrays.fill(towns, (byte) 0x00);
        townSeries.clear();
        for (int i = 0; i < teleport.length; i++) {
            teleport[i] = new MapPoint();
        }

        byte[] xs = new byte[TOWN_MAX_COUNT];
        byte[] ys = new byte[TOWN_MAX_COUNT];

        // (0x30 & 0B1111_1000) >>> 3 // 0x0441的偏移量
        // (0x30 & 0x0000_0111) // 指向的bit位
        // 读取目的地坐标
        setPrgRomPosition(TOWN_LOCATION_START_OFFSET);
        get(buffer, xs);
        get(buffer, ys);

        // 储存目的地坐标
        for (int i = 0; i < TOWN_MAX_COUNT; i++) {
            townLocations[i].setCamera(xs[i], ys[i]);
        }

        byte[] townValues = new byte[TOWN_MAX_COUNT];

        // 读取城镇
        setPrgRomPosition(TOWN_START_OFFSET);
        get(buffer, towns);
        // 读取城镇数据
        // 这个数据就厉害了
        // 可以修改0x0441-0x460的数据，但是只能 或(|)运算，无法移除
        // 有需求再修改
        getPrgRom(buffer, 0x3471E - 0x10, townValues);

        // 城镇附属，最多2个，格式和城镇一样，就不重复注释了
        byte[] townSeries = new byte[0x02];
        byte[] townSeriesValues = new byte[0x02];
        getPrgRom(buffer, 0x3471B - 0x10, townSeries);
        getPrgRom(buffer, 0x34732 - 0x10, townSeriesValues);
        for (int i = 0; i < 0x02; i++) {
            getTownSeries().put(townSeries[i] & 0xFF, townSeriesValues[i] & 0xFF);
        }

        // 读取时空隧道目的地
        byte[] maps = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        xs = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        ys = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        // 读取时空隧道所有目的地地图
        setPrgRomPosition(TELEPORT_MAP_START_OFFSET);
        get(buffer, maps);
        // 读取时空隧道所有目的地地图的X坐标
        setPrgRomPosition(TELEPORT_MAP_X_START_OFFSET);
        get(buffer, xs);
        // 读取时空隧道所有目的地地图的Y坐标
        setPrgRomPosition(TELEPORT_MAP_Y_START_OFFSET);
        get(buffer, ys);
        for (int i = 0; i < TELEPORT_MAP_MAX_MAP_COUNT; i++) {
            teleport[i].set(maps[i], xs[i], ys[i]);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] xs = new byte[TOWN_MAX_COUNT];
        byte[] ys = new byte[TOWN_MAX_COUNT];

        // 目的地坐标
        for (int index = 0; index < townLocations.length; index++) {
            xs[index] = townLocations[index].getCameraX();
            ys[index] = townLocations[index].getCameraY();
        }

        // 写入目的地
        setPrgRomPosition(TOWN_LOCATION_START_OFFSET);
        put(buffer, xs);
        put(buffer, ys);


        // 写入城镇对应的地图

        // 写入城镇
        setPrgRomPosition(TOWN_START_OFFSET);
        put(buffer, towns);

        // 写入城镇附属
        byte[] townSeries = new byte[0x02];
        byte[] townSeriesValues = new byte[0x02];
        var townSeriesEntry = new ArrayList<>(getTownSeries().entrySet());
        townSeries[0x00] = (byte) (townSeriesEntry.get(0x00).getKey() & 0xFF);
        townSeries[0x01] = (byte) (townSeriesEntry.get(0x01).getKey() & 0xFF);
        townSeriesValues[0x00] = (byte) (townSeriesEntry.get(0x00).getValue() & 0xFF);
        townSeriesValues[0x01] = (byte) (townSeriesEntry.get(0x01).getValue() & 0xFF);

        // 写入附属地图
        putPrgRom(buffer, 0x3471B - 0x10, townSeries);
        // 写入附属地图的所属地图（也可以是其它数据
        putPrgRom(buffer, 0x34732 - 0x10, townSeriesValues);

        // 写入时空隧道目的地和坐标
        byte[] maps = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        xs = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        ys = new byte[TELEPORT_MAP_MAX_MAP_COUNT];
        for (int i = 0; i < TELEPORT_MAP_MAX_MAP_COUNT; i++) {
            MapPoint teleport = getTeleport(i);
            maps[i] = teleport.getMap();
            xs[i] = teleport.getX();
            ys[i] = teleport.getY();
        }
        // 写入时空隧道所有目的地地图
        setPrgRomPosition(TELEPORT_MAP_START_OFFSET);
        put(buffer, maps);
        // 写入时空隧道所有目的地地图的X坐标
        setPrgRomPosition(TELEPORT_MAP_X_START_OFFSET);
        put(buffer, xs);
        // 写入时空隧道所有目的地地图的Y坐标
        setPrgRomPosition(TELEPORT_MAP_Y_START_OFFSET);
        put(buffer, ys);
        return true;
    }

    /**
     * @return 获取所有目的地
     */
    public MapPoint[] getTownLocations() {
        return townLocations;
    }

    /**
     * @return 获取指定目的地的坐标
     */
    public MapPoint getTownLocation(@Range(from = 0x00, to = TOWN_MAX_COUNT) int town) {
        return townLocations[town];
    }

    /**
     * @return 所有城镇和其对应地图
     */
    public byte[] getTowns() {
        return towns;
    }

    /**
     * @return 城镇对应的地图
     */
    public byte getTown(@Range(from = 0x00, to = TOWN_MAX_COUNT) int index) {
        return towns[index];
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
    public MapPoint[] getTeleport() {
        return teleport;
    }

    /**
     * @return 通过城镇序号（不是地图id）获取传送目的地
     */
    public MapPoint getTeleport(@Range(from = 0x00, to = TELEPORT_MAP_MAX_MAP_COUNT - 1) int index) {
        return teleport[index];
    }
}
