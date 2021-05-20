package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.Point;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 犬系统
 * 可以在大地图
 * <p>
 * 起始：0x3272D
 * 结束：0x32744
 * <p>
 * <p>
 * 修改意向：修改传送代码，让其支持任意地图
 *          程序代码入口：0x3270F
 *
 *
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
    public final List<Destination> destinations = new ArrayList<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        byte[] xs = new byte[DESTINATION_MAX_COUNT];
        byte[] ys = new byte[DESTINATION_MAX_COUNT];

        // 读取目的地坐标
        buffer.position(0x3272D);
        buffer.get(xs);
        buffer.get(ys);

        // 储存目的地坐标
        for (int i = 0; i < DESTINATION_MAX_COUNT; i++) {
            destinations.add(i, new Destination(xs[i], ys[i]));
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
        while (destinations.size() > DESTINATION_MAX_COUNT) {
            if (iterator.hasNext()) {
                Destination destination = iterator.next();
                iterator.remove();
                System.out.printf("犬系统编辑器：移除多余的目的地 %s", destination);
            }
        }

        int i = 0;
        while (iterator.hasNext()) {
            Destination destination = iterator.next();
            xs[i] = destination.x;
            ys[i] = destination.y;
            i++;
        }

        if (i < DESTINATION_MAX_COUNT) {
            System.out.printf("犬系统编辑器：警告！！！剩余%d个目的地可能传送到地图坐标 0,0 的位置", i);
        }

        // 写入目的地
        buffer.position(0x3272D);
        buffer.put(xs);
        buffer.put(ys);
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
     * 目的地
     */
    public static class Destination extends Point<Byte> {

        public Destination(Byte x, Byte y) {
            super(x, y);
        }

        public Destination(@NotNull Point<Byte> point) {
            super(point);
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
            super.setX((byte) x);
        }

        /**
         * 此坐标会下偏移7格坐标点
         */
        public void setCameraY(int y) {
            super.setY((byte) y);
        }

        @Override
        public String toString() {
            return String.format("Destination{x=%s, y=%s}", x, y);
        }
    }

}
