package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.Point;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

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
public class DogSystemEditor extends AbstractEditor {
    /**
     * 目的地最大数量
     */
    public static final int DESTINATION_MAX_COUNT = 0x0C;

    /**
     * 犬系统一共就 3*4 个目的地，写死算求
     */
    private final List<Destination> destinations = new ArrayList<>(DESTINATION_MAX_COUNT);

    /**
     * 将某个地图设置为城镇
     */
    private final HashMap<Integer, Integer> towns = new HashMap<>(DESTINATION_MAX_COUNT);

    /**
     * 可以将一个地图设置为某一个城镇
     * 只能设置两个！
     */
    private final HashMap<Integer, Integer> townSeries = new HashMap<>(0x02);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        byte[] xs = new byte[DESTINATION_MAX_COUNT];
        byte[] ys = new byte[DESTINATION_MAX_COUNT];

        // (0x30 & 0B1111_1000) >>> 3 // 0x0441的偏移量
        // (0x30 & 0x0000_0111) // 指向的bit位
        // 读取目的地坐标
        buffer.position(0x3272D);
        buffer.get(xs);
        buffer.get(ys);

        // 储存目的地坐标
        for (int i = 0; i < DESTINATION_MAX_COUNT; i++) {
            destinations.add(i, new Destination(xs[i], ys[i]));
        }

        byte[] towns = new byte[DESTINATION_MAX_COUNT];
        byte[] townValues = new byte[DESTINATION_MAX_COUNT];

        // 读取城镇
        buffer.position(0x34707);
        buffer.get(towns);
        // 读取城镇数据
        // 这个数据就厉害了
        // 可以修改0x0441-0x460的数据，但是只能 或(|)运算，无法移除
        // 有需求再修改
        buffer.position(0x3471E);
        buffer.get(townValues);

        // 添加城镇映射
        for (int i = 0; i < DESTINATION_MAX_COUNT; i++) {
            getTowns().put(i, towns[i] & 0xFF);
        }

        // 城镇附属，最多2个，格式和城镇一样，就不重复注释了
        byte[] townSeries = new byte[0x02];
        byte[] townSeriesValues = new byte[0x02];
        buffer.position(0x3471B);
        buffer.get(townSeries);
        buffer.position(0x34732);
        buffer.get(townSeriesValues);
        for (int i = 0; i < 0x02; i++) {
            getTownSeries().put(i, townSeries[i] & 0xFF);
        }

        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] xs = new byte[DESTINATION_MAX_COUNT];
        byte[] ys = new byte[DESTINATION_MAX_COUNT];

        // 分解目的地坐标

        // 移除多余的目的地坐标
        Iterator<Destination> iterator = destinations.iterator();
        limit(iterator, () -> destinations.size() > DESTINATION_MAX_COUNT, removed -> {
            System.out.printf("犬系统编辑器：移除多余的目的地 %s\n", removed);
        });

        int i = 0;
        while (iterator.hasNext()) {
            Destination destination = iterator.next();
            xs[i] = destination.x;
            ys[i] = destination.y;
            i++;
        }

        if (i < DESTINATION_MAX_COUNT) {
            System.out.printf("犬系统编辑器：警告！！！剩余%d个目的地可能传送到地图坐标 0,0 的位置\n", i);
        }

        // 写入目的地
        buffer.position(0x3272D);
        buffer.put(xs);
        buffer.put(ys);


        // 写入城镇对应的地图

        Iterator<Integer> townIterator = towns.values().iterator();
        // 移除多余的城镇
        limit(townIterator, () -> towns.size() > DESTINATION_MAX_COUNT, removed -> {
            System.out.printf("犬系统编辑器：移除多余的城镇 %02X\n", removed);
        });

        // 写入城镇
        buffer.position(0x34707);
        while (townIterator.hasNext()) {
            Integer next = townIterator.next();
            buffer.put(next.byteValue());
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
        buffer.position(0x3471B);
        buffer.put(townSeries);
        // 写入附属地图的所属地图（也可以是其它数据
        buffer.position(0x34732);
        buffer.put(townSeriesValues);
        return true;
    }

    /**
     * @return 获取所有目的地
     */
    public List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * @return 获取指定目的地的坐标
     */
    public Destination getDestination(int destination) {
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
     * 目的地
     */
    public static class Destination extends Point<Byte> {

        public Destination(Byte x, Byte y) {
            super(x, y);
        }

        public Destination(@NotNull Point<Byte> point) {
            super(point);
        }

        @Override
        public void setX(Byte x) {
            super.setX((byte) (x - 0x08));
        }

        @Override
        public void setY(Byte y) {
            super.setY((byte) (y - 0x07));
        }

        public void setX(int x) {
            super.setX((byte) (x - 0x08));
        }

        public void setY(int y) {
            super.setY((byte) (y - 0x07));
        }

        /**
         * 此坐标会右偏移8格坐标点
         */
        public void setCameraX(int x) {
            super.setX((byte) (x & 0xFF));
        }

        /**
         * 此坐标会下偏移7格坐标点
         */
        public void setCameraY(int y) {
            super.setY((byte) (y & 0xFF));
        }

        public void setPoint(int x, int y) {
            super.setPoint((byte) x, (byte) y);
        }

        public void setCameraPoint(int x, int y) {
            setCameraX(x);
            setCameraY(y);
        }

        @Override
        public String toString() {
            return String.format("Destination{x=%s, y=%s}", x, y);
        }
    }

}
