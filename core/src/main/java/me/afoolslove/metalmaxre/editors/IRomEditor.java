package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

/**
 * 提供了编辑器部分所需的方法
 *
 * @author AFoolLove
 */
public interface IRomEditor {
    /**
     * 获取主实例
     *
     * @return 主实例
     */
    @NotNull
    MetalMaxRe getMetalMaxRe();

    /**
     * 设置启用或禁用编辑器
     * <p>
     * *禁用后依然会加载，但不会进行写入
     *
     * @param enabled 启用或禁用编辑器
     */
    void setEnabled(boolean enabled);

    /**
     * 当前编辑器是否已启用
     *
     * @return 当前编辑器是否已启用
     */
    boolean isEnabled();

    /**
     * 获取当前编辑器的数据指针位置
     *
     * @return 指针位置
     */
    int position();

    /**
     * 设置当前编辑器的数据指针位置
     *
     * @param position 目标指针位置
     * @return 指针位置
     */
    int position(int position);

    /**
     * 从CHR ROM起始位置开始定位
     *
     * @param chrPosition 目标位置
     * @return 指针位置
     */
    int chrPosition(int chrPosition);

    /**
     * 从PRG ROM起始位置开始定位
     *
     * @param prgPosition 目标位置
     * @return 指针位置
     */
    int prgPosition(int prgPosition);


    /**
     * 根据 {@link DataAddress} 定位到目标位置
     * <p>
     * *通过调用 {@link IRomEditor#prgPosition(int)} 和 {@link IRomEditor#chrPosition(int)} 实现
     *
     * @param position 目标位置
     * @return 指针位置
     */
    default int position(@NotNull DataAddress position) {
        return position(position, 0);
    }

    /**
     * 根据 {@link DataAddress} 定位到目标位置
     * <p>
     * *通过调用 {@link IRomEditor#prgPosition(int)} 和 {@link IRomEditor#chrPosition(int)} 实现
     *
     * @param position 目标位置
     * @param offset   目标位置偏移
     * @return 指针位置
     */
    default int position(@NotNull DataAddress position, int offset) {
        return switch (position.getType()) {
            case PRG -> prgPosition(position.getStartAddress() + offset);
            case CHR -> chrPosition(position.getStartAddress() + offset);
        };
    }

    /**
     * 偏移当前编辑器的数据指针位置
     *
     * @param offset 偏移值
     * @return 指针位置
     */
    default int offsetPosition(int offset) {
        return position(position() + offset);
    }

    @NotNull
    default RomBuffer getBuffer() {
        return getMetalMaxRe().getBuffer();
    }


}
