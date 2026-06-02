package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.AbstractEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class RomBufferWrapperAbstractEditor extends AbstractEditor {
    public static final Logger LOGGER = LoggerFactory.getLogger(RomBufferWrapperAbstractEditor.class);

    private final EditorRomBufferWrapper romBufferWrapper;

    public RomBufferWrapperAbstractEditor(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
        this.romBufferWrapper = new EditorRomBufferWrapper(metalMaxRe.getBuffer(), this);
    }

    public RomBufferWrapperAbstractEditor(@NotNull MetalMaxRe metalMaxRe, boolean enabled) {
        super(metalMaxRe, enabled);
        this.romBufferWrapper = new EditorRomBufferWrapper(metalMaxRe.getBuffer(), this);
    }

    @Override
    public @NotNull EditorRomBufferWrapper getBuffer() {
        return romBufferWrapper;
    }


    /**
     * 加载索引式数据（通用方法）
     * <p>
     * 通过索引表读取变长数据块到Map中
     *
     * @param indexAddressKey 索引地址key
     * @param dataAddressKey  数据地址key
     * @param dataMap         存储结果的Map
     * @param maxCount        最大数量
     */
    protected void loadIndexedData(@NotNull String indexAddressKey,
                                   @NotNull String dataAddressKey,
                                   @NotNull Map<Integer, byte[]> dataMap,
                                   int maxCount) {
        // 读取原始索引
        char[] indexes = new char[maxCount];
        position(getDataAddress(indexAddressKey));
        getBuffer().getCharArray(indexes);
        DataAddress dataAddress = getDataAddress(dataAddressKey);
        int bankOffset = dataAddress.getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] == 0xFFFF) {
                // 跳过0xFFFF的索引
                continue;
            }
            indexes[i] -= (char) (0x8000 + bankOffset);
        }
        byte[] data = new byte[dataAddress.length()];
        getBuffer().get(dataAddress, data);

        loadIndexedData(indexes, data, dataMap);
    }

    /**
     * 加载索引式数据（通用方法）
     * <p>
     * 通过索引表读取变长数据块到Map中
     *
     * @param indexAddressKey 索引地址key
     * @param dataAddressKey  数据地址key
     * @param dataMap         存储结果的Map
     * @param maxCount        最大数量
     */
    protected void loadIndexedData(@NotNull String indexAddressKey,
                                   @NotNull String dataAddressKey,
                                   boolean isA000,
                                   @NotNull Map<Integer, byte[]> dataMap,
                                   int maxCount) {
        // 读取原始索引
        char[] indexes = new char[maxCount];
        position(getDataAddress(indexAddressKey));
        getBuffer().getCharArray(indexes);
        DataAddress dataAddress = getDataAddress(dataAddressKey);
        int bankOffset = dataAddress.getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] == 0xFFFF) {
                // 跳过0xFFFF的索引
                continue;
            }
            indexes[i] -= (char) ((isA000 ? 0xA000 : 0x8000) + bankOffset);
        }
        byte[] data = new byte[dataAddress.length()];
        getBuffer().get(dataAddress, data);

        loadIndexedData(indexes, data, dataMap);
    }

    /**
     * 加载索引式数据（通用方法）
     * <p>
     * 通过索引表读取变长数据块到Map中
     *
     * @param indexes 索引
     * @param data    数据
     * @param dataMap 存储结果的Map
     */
    protected void loadIndexedData(char[] indexes,
                                   byte[] data,
                                   @NotNull Map<Integer, byte[]> dataMap) {
        // 排序后的索引副本
        char[] sorted = Arrays.copyOf(indexes, indexes.length);
        Arrays.sort(sorted);

        // 计算每个原始索引在排序数组中的排名
        int[] rank = new int[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            rank[i] = findSortedRank(sorted, indexes[i]);
        }

        // 根据相邻排序索引的差值计算每个脚本的长度并读取数据
        int endOffset = data.length;

        IntStream.range(0x00, indexes.length).parallel()
                .forEach(i -> {
                    int nextOffset = (rank[i] + 1 < indexes.length) ? sorted[rank[i] + 1] : endOffset;
                    char index = sorted[rank[i]];
                    if (nextOffset == 0xFFFF || index == 0xFFFF) {
                        // 可能是有什么心事吧，不读
                        dataMap.put(i, new byte[0x00]);
                        return;
                    }
                    int length = nextOffset - index;
                    if (length >= 0) {
                        byte[] bytes = new byte[length];
                        System.arraycopy(data, indexes[i], bytes, 0x00, length);
                        dataMap.put(i, bytes);
                    } else {
                        LOGGER.error("{} 读取第{}个索引时出现负数据。", getClass().getSimpleName(), i);
                    }
                });
    }

    /**
     * 应用索引式数据（通用方法）
     * <p>
     * 将Map中的变长数据块打包写入ROM，并重建索引表
     *
     * @param indexAddressKey 索引地址key
     * @param dataAddressKey  数据地址key
     * @param dataMap         脚本数据Map
     * @param canEmptyIndex   数据长度为0时，是否允许索引为空，为空时索引值为0xFFFF，否者可能为任意值
     * @param maxCount        最大数量
     * @param compact         是否压缩数据，开启后会复用已写入的数据
     * @return 剩余空闲数据长度，如果是负数代表超出了地址
     */
    protected int applyIndexedData(@NotNull String indexAddressKey,
                                   @NotNull String dataAddressKey,
                                   boolean isA000,
                                   @NotNull Map<Integer, byte[]> dataMap,
                                   boolean canEmptyIndex,
                                   int maxCount) {
        final int maxLength = dataMap.values().stream().mapToInt(bytes -> bytes.length).sum();

        // 储存新的索引
        Character[] indexes = new Character[maxCount];

        // 用于储存新的数据
        ByteBuffer dataBuffer = ByteBuffer.allocate(maxLength);

        byte[][] sortData = new byte[maxCount][];
        for (Map.Entry<Integer, byte[]> entry : dataMap.entrySet()) {
            sortData[entry.getKey()] = entry.getValue();
        }
        // 按数组长度大到小排序
        Arrays.sort(sortData, (o1, o2) -> o1 == null || o2 == null ? -1 : o2.length - o1.length);
        DataAddress dataAddress = getDataAddress(dataAddressKey);
        int bankOffset = dataAddress.getBankOffset();

        Map<Integer, byte[]> dataMapCopy = new HashMap<>(dataMap); // 仅用于遍历

        // 通过遍历数据获取对应的索引，将索引目标值设置为数据起始地址
        for (int i = 0; i < maxCount; i++) {
            for (Map.Entry<Integer, byte[]> entry : dataMap.entrySet()) {
                Integer key = entry.getKey();
                if (indexes[key] != null) {
                    continue;
                }
                byte[] value = entry.getValue();
                if (canEmptyIndex && value.length == 0x00) {
                    // 数据为0，索引改为为0xFFFF
                    indexes[key] = 0xFFFF;
                    continue;
                }
                if (value == sortData[i]) {
                    indexes[key] = (char) (dataBuffer.position() + (isA000 ? 0xA000 : 0x8000) + bankOffset);
                    dataBuffer.put(value);


                    for (Map.Entry<Integer, byte[]> entryCopy : dataMapCopy.entrySet()) {
                        if (indexes[entryCopy.getKey()] != null) {
                            continue;
                        }
                        if (Arrays.equals(entryCopy.getValue(), value)) {
                            indexes[entryCopy.getKey()] = indexes[key];
                        }
                    }
                    break;
                }
            }
        }
        position(getDataAddress(indexAddressKey));
        for (Character index : indexes) {
            if (index == null) {
                index = 0xFFFF;
            }
            getBuffer().putCharR(index);
        }

        position(dataAddress);
        getBuffer().put(dataBuffer.array(), 0, Math.min(dataBuffer.position(), dataAddress.length()));

        return dataAddress.getEndAddress(-position() + 0x10 + 1);
    }

    /**
     * 应用索引式数据（通用方法）
     * <p>
     * 将Map中的变长数据块打包写入ROM，并重建索引表
     *
     * @param indexAddressKey 索引地址key
     * @param dataAddressKey  数据地址key
     * @param dataMap         脚本数据Map
     * @param canEmptyIndex   数据长度为0时，是否允许索引为空，为空时索引值为0xFFFF，否者可能为任意值
     * @param maxCount        最大数量
     * @param fillVal         将剩余的空间使用该值填充
     * @return 剩余空闲数据长度，如果是负数代表超出了地址
     */
    protected int applyIndexedData(@NotNull String indexAddressKey,
                                   @NotNull String dataAddressKey,
                                   @NotNull Map<Integer, byte[]> dataMap,
                                   boolean canEmptyIndex,
                                   int maxCount, byte fillVal) {
        int end = applyIndexedData(indexAddressKey, dataAddressKey, false, dataMap, canEmptyIndex, maxCount);
        if (end > 0) {
            getBuffer().fill(fillVal, end);
        }
        return end;
    }

    /**
     * 应用索引式数据（通用方法）
     * <p>
     * 将Map中的变长数据块打包写入ROM，并重建索引表
     *
     * @param indexAddressKey 索引地址key
     * @param dataAddressKey  数据地址key
     * @param dataMap         脚本数据Map
     * @param maxCount        最大数量
     * @param fillVal         将剩余的空间使用该值填充
     * @return 剩余空闲数据长度，如果是负数代表超出了地址
     */
    protected int applyIndexedData(@NotNull String indexAddressKey,
                                   @NotNull String dataAddressKey,
                                   @NotNull Map<Integer, byte[]> dataMap,
                                   int maxCount, byte fillVal) {
        return applyIndexedData(indexAddressKey, dataAddressKey, dataMap, false, maxCount, fillVal);
    }
}
