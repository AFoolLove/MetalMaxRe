package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;
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

    public MapBuilder(@NotNull Collection<? extends MapTile> c) {
        this(c, true);
    }

    /**
     * 通过集合创建地图构建器
     *
     * @param c        集合
     * @param optimize 是否优化传入的数据
     */
    public MapBuilder(@NotNull Collection<? extends MapTile> c, boolean optimize) {
        if (optimize) {
            addAll(c);
        } else {
            // 使用add(int, int)方法添加可以进行优化
            for (MapTile mapTile : c) {
                add(mapTile.intTile(), mapTile.intCount());
            }
        }
    }

    /**
     * 将地图构建器中的数据转换为二维地图数据
     *
     * @param mapBuilder 地图构建器
     * @param width      宽度
     * @param height     高度
     * @param dest       二维地图的大小
     * @param isFill     如果目标的大小超出了地图数据是否需要填充
     * @param fillTile   填充的tile，仅在 {@code isFill}为true时有效
     * @return 二维地图数据
     */
    public static byte[][] toMapData(@NotNull MapBuilder mapBuilder, int width, int height, @Nullable Rectangle dest, boolean isFill, Byte fillTile) {
        if (dest == null) {
            dest = new Rectangle(width, height);
        }

        if (isFill && fillTile == null) {
            fillTile = 0x00;
        }

        byte[][] map = mapBuilder.toMapArray(width, height);
        byte[][] destMap = new byte[dest.height][dest.width];

        for (int y = 0; y < dest.height; y++) {
            for (int x = 0; x < dest.width; x++) {
                // 判断是否超出了目标宽度
                if ((dest.x + x) < width) {
                    destMap[y][x] = map[y][x + dest.x];
                    continue;
                }
                if (isFill) {
                    // 触发填充，这行之后的图块的不用遍历，直接计算数量添加
                    Arrays.fill(destMap[y], x, dest.width, fillTile);
                }
                break;
            }
        }
        return destMap;
    }

    /**
     * 通过二维地图数据创建地图构建器
     *
     * @param mapData  二维地图数据
     * @param dest     二维地图的大小
     * @param isFill   如果目标的大小超出了地图数据是否需要填充
     * @param fillTile 填充的tile，仅在 {@code isFill}为true时有效
     * @return 地图构建器
     */
    public static MapBuilder fromMapData(@NotNull byte[][] mapData, @Nullable Rectangle dest, boolean isFill, Byte fillTile) {
        MapBuilder mapBuilder = new MapBuilder();
        int width = mapData[0].length;
        int height = mapData.length;
        if (dest == null) {
            dest = new Rectangle(width, height);
        }
        if (isFill && fillTile == null) {
            fillTile = 0x00;
        }

        for (int y = dest.y, h = dest.y + dest.height; y < h; y++) {
            // 判断是否超出了目标高度
            if (y > height) {
                if (isFill) {
                    mapBuilder.add(fillTile, dest.width);
                }
                continue;
            }

            for (int x = dest.x, w = dest.x + dest.width; x < w; x++) {
                // 判断是否超出了目标宽度
                if (x < width) {
                    mapBuilder.add(mapData[y][x]);
                    continue;
                }
                if (isFill) {
                    // 触发填充，这行之后的图块的不用遍历，直接计算数量添加
                    mapBuilder.add(fillTile, w - x);
                }
                break;
            }
        }

        return mapBuilder;
    }

    /**
     * 通过条件读取数据并转换为7bit的地图数据格式
     * *使用RomBuffer
     *
     * @param buffer    被读取的缓存数据
     * @param bitOffset bit的偏移
     * @param condition 循环条件
     * @param listener  每完成一个7bit就会调用一次
     * @return 7bit的地图数据格式
     */
    public static byte[] parseMap(RomBuffer buffer, int bufferPosition, int bitOffset, @NotNull Predicate<List<Byte>> condition, @Nullable Predicate<Byte> listener) {
        return parseMap(buffer::get, bufferPosition, bitOffset, condition, listener);
    }

    /**
     * 通过条件读取数据并转换为7bit的地图数据格式
     * *使用字节数组
     *
     * @param buffer    被读取的缓存数据
     * @param bitOffset bit的偏移
     * @param condition 循环条件
     * @param listener  每完成一个7bit就会调用一次
     * @return 7bit的地图数据格式
     */
    public static byte[] parseMap(byte[] buffer, int bufferPosition, int bitOffset, @NotNull Predicate<List<Byte>> condition, @Nullable Predicate<Byte> listener) {
        return parseMap(position -> buffer[position], bufferPosition, bitOffset, condition, listener);
    }

    /**
     * 通过条件读取数据并转换为7bit的地图数据格式
     * *使用泛型
     *
     * @param buffer    被读取的缓存数据
     * @param bitOffset bit的偏移
     * @param condition 循环条件
     * @param listener  每完成一个7bit就会调用一次
     * @return 7bit的地图数据格式
     */
    public static byte[] parseMap(@NotNull Function<Integer, Byte> buffer, int bufferPosition, int bitOffset, @NotNull Predicate<List<Byte>> condition, @Nullable Predicate<Byte> listener) {
        List<Byte> list = new ArrayList<>();
        int index = 0;
        while (condition.test(list)) {
            if (list.size() <= index) {
                // 没有数据就添加
                list.add(index, (byte) 0);
            }

            int data = buffer.apply(bufferPosition++) & 0xFF;
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
     * 将地图数据转换为二维数组
     *
     * @param mapBuilder 地图构建器
     * @param width      地图宽度
     * @param height     地图高度
     * @return 二维地图
     */
    public static byte[][] toMapArray(@NotNull MapBuilder mapBuilder, int width, int height) {
        byte[][] map = new byte[height][width];
        int index = 0;
        for (MapTile mapTile : mapBuilder) {
            // 获取tile
            for (int i = 0, count = mapTile.getCount(); i < count; i++, index++) {
                if (index >= (width * height)) {
                    // 超出地图
                    break;
                }
                // 设置tile
                map[index / width][index % width] = mapTile.getTile();
            }
        }
        return map;
    }

    /**
     * 将地图数据转换为二维数组
     *
     * @param mapBuilder 地图构建器
     * @param properties 地图属性，获取宽高
     * @return 二维地图
     */
    public static byte[][] toMapArray(@NotNull MapBuilder mapBuilder, @NotNull MapProperties properties) {
        return toMapArray(mapBuilder, properties.intWidth(), properties.intHeight());
    }

    /**
     * @return 当前地图共计多少个图块
     */
    public int getTileCount() {
        int sum = 0;
        for (MapTile mapTile : this) {
            if (mapTile.getCount() == 0) {
                return 0x100;
            } else {
                sum += mapTile.getCount();
            }
        }
        return sum;

        // return stream().mapToInt(value -> value.getCount() == 0 ? 0x100 : value.getCount()).sum();
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

        // 使用 0B1 填充未使用的bit，否则部分地图会出现错误
        if (offset != 0) {
            bytes[length - 1] |= (0B1111_1111 >>> offset);
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

    /**
     * 将地图数据转换为二维数组
     *
     * @param width  地图宽度
     * @param height 地图高度
     * @return 二维地图
     */
    public byte[][] toMapArray(int width, int height) {
        return toMapArray(this, width, height);
    }

    /**
     * 将地图数据转换为二维数组
     *
     * @param properties 地图属性，获取宽高
     * @return 二维地图
     */
    public byte[][] toMapArray(@NotNull MapProperties properties) {
        return toMapArray(this, properties);
    }

    /**
     * 将地图构建器中的数据转换为二维地图数据
     *
     * @param width    宽度
     * @param height   高度
     * @param dest     二维地图的大小
     * @param isFill   如果目标的大小超出了地图数据是否需要填充
     * @param fillTile 填充的tile，仅在 {@code isFill}为true时有效
     * @return 二维地图数据
     */
    public byte[][] toMapData(int width, int height, @Nullable Rectangle dest, boolean isFill, Byte fillTile) {
        return toMapData(this, width, height, dest, isFill, fillTile);
    }

    /**
     * 将地图构建器中的数据转换为二维地图数据
     *
     * @param properties 地图属性，获取宽高
     * @param dest       二维地图的大小
     * @param isFill     如果目标的大小超出了地图数据是否需要填充
     * @param fillTile   填充的tile，仅在 {@code isFill}为true时有效
     * @return 二维地图数据
     */
    public byte[][] toMapData(@NotNull MapProperties properties, @Nullable Rectangle dest, boolean isFill, Byte fillTile) {
        return toMapData(this, properties.intWidth(), properties.intHeight(), dest, isFill, fillTile);
    }
}
