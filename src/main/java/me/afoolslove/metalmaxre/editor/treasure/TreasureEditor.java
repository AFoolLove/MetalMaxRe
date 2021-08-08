package me.afoolslove.metalmaxre.editor.treasure;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
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
 * 0x39D61-0x39DBB  物品
 * <p>
 * <p>
 * 2021年5月9日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class TreasureEditor extends AbstractEditor<TreasureEditor> {
    public static final int TREASURE_MAX_COUNT = 0x5B;

    public static final int TREASURE_START_OFFSET = 0x39C50 - 0x10;
    public static final int TREASURE_END_OFFSET = 0x39DBB - 0x10;

    private final LinkedHashSet<Treasure> treasures = new LinkedHashSet<>(TREASURE_MAX_COUNT);

    public static void main(String[] args) {
        var editor = EditorManager.getEditor(TreasureEditor.class);
        HashSet<Treasure> treasures = editor.getTreasures();
        // 添加一个古币宝藏到家楼下的售货机旁边
        treasures.add(new Treasure(0x02, 0x04, 0x06, 0xB2));
    }

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        treasures.clear();

        byte[] maps = new byte[TREASURE_MAX_COUNT];
        byte[] xs = new byte[TREASURE_MAX_COUNT];
        byte[] ys = new byte[TREASURE_MAX_COUNT];
        byte[] items = new byte[TREASURE_MAX_COUNT];

        // 宝藏的数据按顺序存放（地图、X、Y、物品
        setPrgRomPosition(buffer, TREASURE_START_OFFSET);
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
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] maps = new byte[TREASURE_MAX_COUNT];
        byte[] xs = new byte[TREASURE_MAX_COUNT];
        byte[] ys = new byte[TREASURE_MAX_COUNT];
        byte[] items = new byte[TREASURE_MAX_COUNT];

        // 写入前修正宝藏数量

        Iterator<Treasure> iterator = treasures.iterator();
        // 移除多余的宝藏
        limit(iterator, () -> treasures.size() > TREASURE_MAX_COUNT, removed -> {
            System.out.println("移除多余的宝藏：" + removed);
        });

        int i = 0;
        while (iterator.hasNext()) {
            Treasure treasure = iterator.next();
            if (i >= TREASURE_MAX_COUNT) {
                break;
            }

            maps[i] = treasure.getMap();
            xs[i] = treasure.getX();
            ys[i] = treasure.getY();
            items[i] = treasure.getItem();
            i++;
        }

        setPrgRomPosition(buffer, TREASURE_START_OFFSET);
        put(buffer, maps);
        put(buffer, xs);
        put(buffer, ys);
        put(buffer, items);
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
}
