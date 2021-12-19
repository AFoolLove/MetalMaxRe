package me.afoolslove.metalmaxre.editor.computer;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 计算机编辑器
 * 售货机、游戏机、计算机等
 * 计算机需要地图图块属性支持，支持动态图块
 * <p>
 * 起始：0x39DD2
 * 结束：0x39FBD
 * <p>
 * 总共 123(0x7B) 个宝藏
 * <p>
 * 2021年5月10日：已完成并通过测试基本编辑功能
 * <p>
 * <p>
 * *兼容SuperHack版本
 *
 * @author AFoolLove
 */
public class ComputerEditor extends AbstractEditor<ComputerEditor> {
    /**
     * 计算机最大数量
     */
    public static final int COMPUTER_MAX_COUNT = 0x7B;

    /**
     * 计算机数据地址
     */
    public static final int COMPUTER_START_OFFSET = 0x39DD2 - 0x10;
    public static final int COMPUTER_END_OFFSET = 0x39FBD - 0x10;

    /**
     * 所有计算机
     */
    private final LinkedHashSet<Computer> computers = new LinkedHashSet<>(COMPUTER_MAX_COUNT);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        computers.clear();

        byte[] maps = new byte[COMPUTER_MAX_COUNT];
        byte[] types = new byte[COMPUTER_MAX_COUNT];
        byte[] xs = new byte[COMPUTER_MAX_COUNT];
        byte[] ys = new byte[COMPUTER_MAX_COUNT];

        setPrgRomPosition(COMPUTER_START_OFFSET);
        get(buffer, maps);
        get(buffer, types);
        get(buffer, xs);
        get(buffer, ys);

        for (int i = 0; i < COMPUTER_MAX_COUNT; i++) {
            computers.add(new Computer(maps[i], types[i], xs[i], ys[i]));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] maps = new byte[COMPUTER_MAX_COUNT];
        byte[] types = new byte[COMPUTER_MAX_COUNT];
        byte[] xs = new byte[COMPUTER_MAX_COUNT];
        byte[] ys = new byte[COMPUTER_MAX_COUNT];

        // 优先储存后加入的
        ArrayList<Computer> computers = new ArrayList<>(getComputers());
        int fromIndex = Math.max(0, computers.size() - COMPUTER_MAX_COUNT);
        for (int index = fromIndex, size = computers.size(); index < size; index++) {
            Computer computer = computers.get(index);
            maps[index] = computer.getMap();
            types[index] = computer.getType();
            xs[index] = computer.getX();
            ys[index] = computer.getY();
        }

        // 使用 0xFF 填充未使用的计算机空位
        int remain = COMPUTER_MAX_COUNT - computers.size();
        if (remain > 0) {
            int count = COMPUTER_MAX_COUNT - remain;
            Arrays.fill(maps, count, COMPUTER_MAX_COUNT, (byte) 0xFF);
            System.out.printf("计算机编辑器：%d个未使用的计算机\n", remain);
        }

        // 写入
        setPrgRomPosition(COMPUTER_START_OFFSET);
        put(buffer, maps);
        put(buffer, types);
        put(buffer, xs);
        put(buffer, ys);
        return true;
    }

    /**
     * @return 获取所有计算机
     */
    public HashSet<Computer> getComputers() {
        return computers;
    }

    /**
     * 通过地图ID查找计算机
     *
     * @param map 计算机所在的地图
     * @return 该地图的所有计算机
     */
    public Set<Computer> findMap(@Range(from = Byte.MIN_VALUE, to = Byte.MAX_VALUE) int map) {
        final byte m = (byte) (map & 0xFF);
        return find(computer -> computer.getMap() == m);
    }

    /**
     * 自定义过滤寻找计算机
     *
     * @param filter 过滤器
     * @return 符合的计算机
     */
    public Set<Computer> find(@NotNull Predicate<Computer> filter) {
        return computers.stream().filter(filter).collect(Collectors.toSet());
    }


    /**
     * 添加一个计算机
     *
     * @param computer 计算机
     * @return 是否成功添加
     */
    public boolean add(@NotNull Computer computer) {
        if (computers.size() > COMPUTER_MAX_COUNT) {
            return false;
        }
        return !computers.contains(computer) && computers.add(computer);
    }

    /**
     * 移除一个计算机
     *
     * @param remove 被移除的计算机
     * @return 是否移除成功
     */
    public boolean remove(@NotNull Computer remove) {
        return computers.remove(remove);
    }

    /**
     * 条件移除对象
     *
     * @param filter 过滤器
     * @return 是否移除成功
     */
    public boolean removeIf(@NotNull Predicate<Computer> filter) {
        return computers.removeIf(filter);
    }

    /**
     * 移除合集里的所有计算机
     *
     * @param collection 被移除的计算机合集
     * @return 是否移除成功
     */
    public boolean removeAll(Collection<Computer> collection) {
        return computers.removeAll(collection);
    }

    /**
     * 替换计算机
     *
     * @param source  被替换的计算机，为 null 时，替换任意一个计算机
     * @param replace 替换的计算机
     * @return 是否替换成功
     */
    public boolean replace(@Nullable Computer source, @NotNull Computer replace) {
        if (computers.contains(replace)) {
            // 替换的计算机已存在
            return true;
        }
        if (source == null) {
            Iterator<Computer> iterator = computers.iterator();
            if (iterator.hasNext()) {
                iterator.remove();
                return true;
            }
        }

        if (computers.remove(source)) {
            // 替换计算机
            return computers.add(replace);
        } else {
            // 不存在被替换的计算机
            if (computers.size() < COMPUTER_MAX_COUNT) {
                // 如果计算机数量小于最大数量，直接添加
                return computers.add(replace);
            }
        }
        return false;
    }
}
