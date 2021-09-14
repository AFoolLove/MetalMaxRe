package me.afoolslove.metalmaxre.editor.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 地图构建器
 * 不支持世界地图
 * <p>
 * 谨慎使用 {@link #add(MapTile)} 它不会进行地图优化
 * <p>
 * 2021年5月20日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class MapBuilder extends LinkedList<MapTile> {

    public MapBuilder() {
    }

    /**
     * 不会进行优化！
     */
    public MapBuilder(@NotNull Collection<? extends MapTile> c) {
        super(c);
    }

    /**
     * 通过条件读取数据并转换为7bit的地图数据格式
     *
     * @param buffer    被读取的缓存数据
     * @param bitOffset bit的偏移
     * @param condition 循环条件
     * @param listener  每完成一个7bit就会调用一次
     * @return 7bit的地图数据格式
     */
    public static byte[] parseMap(ByteBuffer buffer, int bufferPosition, int bitOffset, @NotNull Predicate<List<Byte>> condition, @Nullable Predicate<Byte> listener) {
        List<Byte> list = new ArrayList<>();
        int index = 0;
        while (condition.test(list)) {
            if (list.size() <= index) {
                // 没有数据就添加
                list.add(index, (byte) 0);
            }

            int data = buffer.get(bufferPosition++) & 0xFF;
            int hBit, lBit;
            // 前bit和后bit的数量，总和不超过 7

            // 前bit数量：通过最大的7位减去当前位(bitOffset)得到
            hBit = 7 - bitOffset;
            // 后bit数量：通过最大的8位减去前bit数量
            lBit = 8 - hBit;

            // 临时储存前bit值和后bit值
            int temp;

            // 前bit值：通过位移移除后bit数量得到
            temp = data >>> lBit;
            // 前bit值直接与上一个数据相加
            list.set(index, (byte) (list.get(index) | (temp & 0x7F)));

            // 写入完成一个数据
            if (listener != null) {
                listener.test(list.get(index));
            }

//            System.out.println(list.get(index));

            // 后bit值储存到新的数据里并向左对齐
            index++;
            // 后bit值：通过位移移除前bit数量后右位移1位得到
            temp = ((data << hBit) & 0xFF) >>> 1;
            list.add(index, (byte) (temp & 0xFF));

            // 如果剩余的bit刚好作为一个数据，索引指向新的数据
            if (lBit == 7) {
                // 写入完成一个数据
                if (listener != null) {
                    listener.test(list.get(index));
                }
                index++;
            }

            // 后bit数量作为下一个offset
            bitOffset = lBit % 7;
        }

        if (bitOffset != 0 && listener != null) {
            // 最后的数据
            listener.test(list.get(index));
        }

        // 转换为byte数组
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    /**
     * @return 当前地图共计多少个图块
     */
    public int getTileCount() {
        return stream().mapToInt(value -> value.getCount() == 0 ? 0x100 : value.getCount()).sum();
    }

    /**
     * 添加自定义数量的图块
     */
    public boolean add(int tile, int count) {
        tile &= 0x7F;
        if (count == 0x00) {
            // 256个图块的大佬直接送走
            return add(new MapTile(tile, 0));
        }
        MapTile last = peekLast();

        // 判断是否需要合并
        if (last != null && last.tile == tile && last.count != 0x00) {
            // 合计
            int sum = last.count + count;

            if (sum <= 0x7F) {
                // 合计不超过一个图块的最大数量 1 - 127
                // 直接合并
                last.count += count;
            } else {
                // 整数256，添加
                // range: 256+
                for (int i = sum / 0x100; i > 0; i--) {
                    // 直接添加，不验证
                    add(new MapTile(tile, 0x00));
                    sum -= 0x100;
                }

                // 大于127时，直接添加一个 0x7F
                if (sum > 0x7F) {
                    last.count = 0x7F;
                    sum -= 0x7F;
                }
                // 小于127时，直接添加
                if (sum > 0x00) {
                    add(new MapTile(tile, sum));
                }
            }
            return true;
        }
        return add(new MapTile(tile, count));
    }

    /**
     * 添加一个图块
     */
    public boolean add(int tile) {
        return add(tile, 1);
    }

    /**
     * @return 构建地图数据
     */
    public byte[] build() {
        index = 0;
        offset = 0;
        // 获取单个图块的数量
        int oneCount = parallelStream()
                .filter(mapTile -> mapTile.count > 0 && mapTile.count < 3)
                .mapToInt(MapTile::getCount)
                .sum();
        // 单个图块占7bit
        int length = oneCount * 7;
        // 自定义数量的图块占3*7bit
        int multipleCount = (int) parallelStream()
                .filter(mapTile -> mapTile.count == 0 || mapTile.count >= 3)
                .count();
        length += multipleCount * 3 * 7;
        // 计算需要的byte长度
        length = length % 8 == 0 ? (length / 8) : (length / 8 + 1);
        byte[] bytes = new byte[length];
        // 开始构建
        for (MapTile mapTile : this) {
            if (mapTile.count > 0 && mapTile.count < 3) {
                // 数量小于3的图块直接存入
                for (int i = 0; i < mapTile.count; i++) {
                    append(bytes, mapTile.tile);
                }
            } else {
                // 复数图块以 0B000_0000、tile、count的方式存入

                // 数量为0时，实际数量为256
                // 非0时，是多少就是多少 1-128
                append(bytes, 0B0000_0000);
                append(bytes, mapTile.tile);
                append(bytes, mapTile.count);
            }
        }
        return bytes;
    }

    /**
     * 记录当前构建器的数据进度
     */
    private int index, offset;

    private void append(byte[] bytes, int tile) {
        // 判断是否可以直接装下一个数据
        if (offset == 0) {
            // 直接写入
            bytes[index] = (byte) (tile << 1);
            offset = 7;
            return;
        } else if (offset == 1) {
            // 直接添加
            bytes[index] |= tile;
            offset = 0;
            index++;
            return;
        }

        // 无法直接装下一个tile，需要两个byte分段储存
        // 左位移一位，左对齐bit
        tile <<= 1;

        // 截取可储存的bit
        int temp = tile >>> offset;
        // 储存截取的可储存bit
        bytes[index] |= temp & 0xFF;


        // 储存后将还未称长度数据单独存入

        // 将剩余未储存的数据左位移对齐后直接添加为一个byte
        temp = tile << (8 - offset);
        // 添加并设置新的offset
        index++;
        bytes[index] = (byte) (temp & 0xFF);
        offset += 7;
        offset %= 8;

    }
}
