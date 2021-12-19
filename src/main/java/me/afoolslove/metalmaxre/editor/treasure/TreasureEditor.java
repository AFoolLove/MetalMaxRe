package me.afoolslove.metalmaxre.editor.treasure;

import me.afoolslove.metalmaxre.SingleMapEntry;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.map.MapCheckPoints;
import me.afoolslove.metalmaxre.editor.map.MapPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 宝藏编辑器
 * <p>
 * 宝藏没有前后顺序
 * <p>
 * 包含世界地图，世界地图宝藏图像会固定自动隐藏
 * <p>
 * 起始：0x39C50
 * 结束：0x39DBB
 * <p>
 * 总共 0x5B(91) 个宝藏
 * 0x39C50-0x39CAA  地图
 * 0x39CAB-0x39D05  X
 * 0x39D06-0x39D60  Y
 * 0x39D61-0x39DBB  宝藏
 * <p>
 * <p>
 * 2021年5月9日：已完成并通过测试基本编辑功能
 *
 * <p>
 * <p>
 * *兼容SuperHack版本
 *
 * @author AFoolLove
 */
public class TreasureEditor extends AbstractEditor<TreasureEditor> {
    /**
     * 宝藏最大数量
     */
    public static final int TREASURE_MAX_COUNT = 0x5B;

    /**
     * 随机宝藏的总数量
     */
    public static final int RANDOM_TREASURE_MAX_COUNT = 0x06;
    /**
     * 地图随机调查获取的宝藏
     * <p>
     * * 所有地图会得到调查的默认宝藏
     * <p>
     * * 世界地图会额外得到其它 {@link #RANDOM_TREASURE_MAX_COUNT} 个宝藏
     */
    public static final int RANDOM_TREASURE_START_OFFSET = 0x35AE4 - 0x10;
    public static final int RANDOM_TREASURE_END_OFFSET = 0x35AE4 - 0x10;

    /**
     * 地图的调查点坐标
     */
    public static final int MAP_CHECK_POINTS_MAX_COUNT = 0x06;
    public static final int MAP_CHECK_POINTS_START_OFFSET = 0x35CB5 - 0x10;
    public static final int MAP_CHECK_POINTS_END_OFFSET = 0x35CC6 - 0x10;

    /**
     * 宝藏数据
     */
    public static final int TREASURE_START_OFFSET = 0x39C50 - 0x10;
    public static final int TREASURE_END_OFFSET = 0x39DBB - 0x10;

    /**
     * 所有宝藏
     */
    private final LinkedHashSet<Treasure> treasures = new LinkedHashSet<>(TREASURE_MAX_COUNT);

    /**
     * 调查的随机宝藏
     * <p>
     * K：宝藏ID
     * <p>
     * V：宝藏概率
     */
    private final List<SingleMapEntry<Byte, Byte>> randomTreasures = new ArrayList<>(RANDOM_TREASURE_MAX_COUNT);
    /**
     * 默认调查的随机宝藏和获得其它随机宝藏的概率
     * <p>
     * K：默认宝藏ID
     * <p>
     * V：其它宝藏概率
     */
    private final SingleMapEntry<Byte, Byte> defaultRandomTreasure = new SingleMapEntry<>((byte) 0x00, (byte) 0x0C);

    /**
     * 地图的调查点，算是"宝藏"吧
     */
    private final MapCheckPoints mapCheckPoints = new MapCheckPoints();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        treasures.clear();
        randomTreasures.clear();

        byte[] maps = new byte[TREASURE_MAX_COUNT];
        byte[] xs = new byte[TREASURE_MAX_COUNT];
        byte[] ys = new byte[TREASURE_MAX_COUNT];
        byte[] items = new byte[TREASURE_MAX_COUNT];

        // 宝藏的数据按顺序存放（地图、X、Y、宝藏
        setPrgRomPosition(TREASURE_START_OFFSET);
        get(buffer, maps);
        get(buffer, xs);
        get(buffer, ys);
        get(buffer, items);

        treasures.clear();
        for (int i = 0; i < TREASURE_MAX_COUNT; i++) {
            Treasure treasure = new Treasure(maps[i], xs[i], ys[i], items[i]);
            boolean add = treasures.add(treasure);
            if (!add) {
                // 重复的宝藏
                System.out.println("读取到重复的宝藏 " + treasure);
                System.out.format("位置：Map:%05X, X:%05X, Y:%05X,Item:%05X\n", treasure.getMap(), treasure.getX(), treasure.getY(), treasure.getItem());
            }
        }

        // 读取地图的调查点
        setPrgRomPosition(MAP_CHECK_POINTS_START_OFFSET);
        get(buffer, maps, 0, MAP_CHECK_POINTS_MAX_COUNT);
        get(buffer, xs, 0, MAP_CHECK_POINTS_MAX_COUNT);
        get(buffer, ys, 0, MAP_CHECK_POINTS_MAX_COUNT);
        mapCheckPoints.entrance.getKey().set(maps[0x00], xs[0x00], ys[0x00]);
        mapCheckPoints.text.getKey().set(maps[0x01], xs[0x01], ys[0x01]);
        mapCheckPoints.reviveCapsule.getKey().set(maps[0x02], xs[0x02], ys[0x02]);
        mapCheckPoints.urumi.getKey().set(maps[0x03], xs[0x03], ys[0x03]);
        mapCheckPoints.drawers.getKey().set(maps[0x04], xs[0x04], ys[0x04]);
        mapCheckPoints.text2.getKey().set(maps[0x05], xs[0x05], ys[0x05]);

        mapCheckPoints.entrance.getValue().setCamera(
                getPrgRom(buffer, 0x35CDD - 0x10),
                getPrgRom(buffer, 0x35CE1 - 0x10),
                getPrgRom(buffer, 0x35CE5 - 0x10)
        );
        mapCheckPoints.reviveCapsule.setValue(getPrgRom(buffer, 0x35D01 - 0x10));
        mapCheckPoints.drawers.getValue().clear();
        mapCheckPoints.drawers.getValue().addAll(Arrays.asList(
                getPrgRom(buffer, 0x35D48 - 0x10),
                getPrgRom(buffer, 0x35D49 - 0x10)
        ));

        // 读取随机宝藏相关数据

        // 读取默认的随机宝藏和其它随机宝藏的概率
        setPrgRomPosition(RANDOM_TREASURE_START_OFFSET);
        defaultRandomTreasure.setKey(get(buffer)); // 0x35AE4 - 0x10
        defaultRandomTreasure.setValue(getPrgRom(buffer, 0x35ACD - 0x10));
        // 读取其它随机宝藏和概率
        // 读取其它随机宝藏
        get(buffer, items, 0x00, RANDOM_TREASURE_MAX_COUNT);
        // * maps 作为概率使用
        get(buffer, maps, 0x00, RANDOM_TREASURE_MAX_COUNT);
        for (int index = 0; index < RANDOM_TREASURE_MAX_COUNT; index++) {
            randomTreasures.add(SingleMapEntry.create(items[index], maps[index]));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] maps = new byte[TREASURE_MAX_COUNT];
        byte[] xs = new byte[TREASURE_MAX_COUNT];
        byte[] ys = new byte[TREASURE_MAX_COUNT];
        byte[] items = new byte[TREASURE_MAX_COUNT];

        // 优先储存后加入的
        ArrayList<Treasure> treasures = new ArrayList<>(getTreasures());
        int fromIndex = Math.max(0, treasures.size() - TREASURE_MAX_COUNT);
        for (int index = fromIndex, size = treasures.size(); index < size; index++) {
            Treasure treasure = treasures.get(index);
            maps[index] = treasure.getMap();
            xs[index] = treasure.getX();
            ys[index] = treasure.getY();
            items[index] = treasure.getItem();
        }

        // 写入宝藏
        setPrgRomPosition(TREASURE_START_OFFSET);
        put(buffer, maps);
        put(buffer, xs);
        put(buffer, ys);
        put(buffer, items);


        // 写入地图的调查点
        setPrgRomPosition(MAP_CHECK_POINTS_START_OFFSET);
        List<MapPoint> checkPoints = Arrays.asList(mapCheckPoints.entrance.getKey(),
                mapCheckPoints.text.getKey(),
                mapCheckPoints.reviveCapsule.getKey(),
                mapCheckPoints.urumi.getKey(),
                mapCheckPoints.drawers.getKey(),
                mapCheckPoints.text2.getKey());
        for (int index = 0; index < MAP_CHECK_POINTS_MAX_COUNT; index++) {
            MapPoint checkPoint = checkPoints.get(index);
            maps[index] = checkPoint.getMap();
            xs[index] = checkPoint.getX();
            ys[index] = checkPoint.getY();
        }
        put(buffer, maps, 0, MAP_CHECK_POINTS_MAX_COUNT);
        put(buffer, xs, 0, MAP_CHECK_POINTS_MAX_COUNT);
        put(buffer, ys, 0, MAP_CHECK_POINTS_MAX_COUNT);

        putPrgRom(buffer, 0x35CDD - 0x10, mapCheckPoints.entrance.getValue().getMap());
        putPrgRom(buffer, 0x35CE1 - 0x10, mapCheckPoints.entrance.getValue().getCameraX());
        putPrgRom(buffer, 0x35CE5 - 0x10, mapCheckPoints.entrance.getValue().getCameraY());

        putPrgRom(buffer, 0x35D01 - 0x10, mapCheckPoints.reviveCapsule.getValue());

        setPrgRomPosition(0x35D48 - 0x10);
        for (int item : mapCheckPoints.drawers.getValue().stream()
                .mapToInt(value -> value & 0xFF)
                .limit(2)
                .toArray()) {
            put(buffer, item);
        }

        // 写入随机宝藏相关数据

        // 写入默认的随机宝藏和其它随机宝藏的概率
        setPrgRomPosition(RANDOM_TREASURE_START_OFFSET);
        put(buffer, defaultRandomTreasure.getKey()); // 0x35AE4 - 0x10
        putPrgRom(buffer, 0x35ACD - 0x10, defaultRandomTreasure.getValue());
        // 写入其它随机宝藏和概率
        // 写入其它随机宝藏
        Arrays.fill(items, 0x00, RANDOM_TREASURE_MAX_COUNT, (byte) 0x00);
        // 优先储存后加入的
        Arrays.fill(maps, 0x00, RANDOM_TREASURE_MAX_COUNT, (byte) 0x00);
        fromIndex = Math.max(0, randomTreasures.size() - RANDOM_TREASURE_MAX_COUNT);
        for (int index = fromIndex, size = randomTreasures.size(); index < size; index++) {
            SingleMapEntry<Byte, Byte> randomTreasure = randomTreasures.get(index);
            items[index] = randomTreasure.getKey();
            // * maps 作为概率使用
            maps[index] = randomTreasure.getValue();
        }
        // 写入宝藏
        put(buffer, items, 0x00, RANDOM_TREASURE_MAX_COUNT);
        // 写入概率
        put(buffer, maps, 0x00, RANDOM_TREASURE_MAX_COUNT);
        return true;
    }

    /**
     * @return 所有宝藏
     */
    public HashSet<Treasure> getTreasures() {
        return treasures;
    }

    /**
     * 通过地图ID查找宝藏
     *
     * @param map 宝藏所在的地图
     * @return 该地图的所有宝藏
     */
    public Set<Treasure> findMap(@Range(from = Byte.MIN_VALUE, to = Byte.MAX_VALUE) int map) {
        final byte m = (byte) (map & 0xFF);
        return find(treasure -> treasure.getMap() == m);
    }

    /**
     * 自定义过滤寻找宝藏
     *
     * @param filter 过滤器
     * @return 符合的宝藏
     */
    public Set<Treasure> find(@NotNull Predicate<Treasure> filter) {
        return treasures.stream().filter(filter).collect(Collectors.toSet());
    }

    /**
     * 通过另一个宝藏对象获取已储存的宝藏对象
     *
     * @param treasure 宝藏对象
     * @return 符合的宝藏
     */
    public Treasure find(@NotNull Treasure treasure) {
        for (Treasure t : treasures) {
            if (t.equals(treasure)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 添加一个宝藏
     *
     * @param treasure 宝藏
     * @return 是否成功添加
     */
    public boolean add(@NotNull Treasure treasure) {
        if (treasures.size() > TREASURE_MAX_COUNT) {
            return false;
        }
        return !treasures.contains(treasure) && treasures.add(treasure);
    }

    /**
     * 移除一个宝藏
     *
     * @param remove 被移除的宝藏
     * @return 是否移除成功
     */
    public boolean remove(@NotNull Treasure remove) {
        return treasures.remove(remove);
    }

    /**
     * 条件移除对象
     *
     * @param filter 过滤器
     * @return 是否移除成功
     */
    public boolean removeIf(@NotNull Predicate<Treasure> filter) {
        return treasures.removeIf(filter);
    }

    /**
     * 移除合集里的所有宝藏
     *
     * @param collection 被移除的宝藏合集
     * @return 是否移除成功
     */
    public boolean removeAll(Collection<Treasure> collection) {
        return treasures.removeAll(collection);
    }

    /**
     * 替换宝藏
     *
     * @param source  被替换的宝藏
     * @param replace 替换的宝藏
     * @return 是否替换成功
     */
    public boolean replace(@Nullable Treasure source, @NotNull Treasure replace) {
        if (treasures.contains(replace)) {
            // 替换的宝藏已存在
            return true;
        }

        if (source == null) {
            Iterator<Treasure> iterator = treasures.iterator();
            if (iterator.hasNext()) {
                iterator.remove();
                return true;
            }
        }

        if (treasures.remove(source)) {
            // 替换宝藏
            return treasures.add(replace);
        } else {
            // 不存在被替换的宝藏
            if (treasures.size() < TREASURE_MAX_COUNT) {
                // 如果宝藏数量小于最大数量，直接添加
                return treasures.add(replace);
            }
        }
        return false;
    }

    /**
     * 随机进行调查获取的宝藏和宝藏的概率
     * <p>
     * * 仅世界地图
     *
     * @return 随机宝藏和概率
     */
    public List<SingleMapEntry<Byte, Byte>> getRandomTreasures() {
        return randomTreasures;
    }

    /**
     * 随机进行调查获取的默认宝藏和获取其它宝藏的概率（仅世界地图）
     * <p>
     * K：默认宝藏ID
     * <p>
     * V：其它宝藏的概率（仅世界地图）
     *
     * @return 默认和其它宝藏的概率
     */
    public SingleMapEntry<Byte, Byte> getDefaultRandomTreasure() {
        return defaultRandomTreasure;
    }

    /**
     * @return 地图的调查点
     */
    public MapCheckPoints getMapCheckPoints() {
        return mapCheckPoints;
    }
}
