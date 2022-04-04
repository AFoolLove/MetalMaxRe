package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 提供了编辑器部分所需的方法
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
     * 获取监听器
     *
     * @return 监听器
     */
    @NotNull
    List<? extends IEditorListener> getListeners();

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
     * @return 当前指针位置
     */
    int position(int position);

    /**
     * 偏移当前编辑器的数据指针位置
     *
     * @param offset 偏移值
     * @return 当前指针位置
     */
    default int offsetPosition(int offset) {
        if (position() == offset) {
            return offset;
        }
        synchronized (this) {
            return position(position() + offset);
        }
    }

    @NotNull
    default RomBuffer getBuffer() {
        return getMetalMaxRe().getBuffer();
    }
}
