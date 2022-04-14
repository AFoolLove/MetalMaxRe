package me.afoolslove.metalmaxre.editors.computer;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 计算机编辑器
 * <p>
 * *需要地图属性支持，并且支持动态图块
 *
 * @param <C> 计算机的属性
 * @author AFoolLove
 */
public interface IComputerEditor<C extends Computer> extends IRomEditor {
    /**
     * 获取计算机的最大数量
     *
     * @return 计算机的最大数量
     */
    int getMaxCount();

    /**
     * 获取所有的计算机。售货机、游戏机、计算机等
     *
     * @return 所有的计算机
     */
    Set<C> getComputers();

    /**
     * 添加一个计算机
     *
     * @param computer 被添加的计算机
     */
    void addComputer(@NotNull C computer);

    /**
     * 移除一个计算机
     *
     * @param computer 被移除的计算机
     */
    void removeComputer(@NotNull C computer);

    /**
     * 替换计算机
     *
     * @param source  被替换的计算机
     * @param replace 替换后的计算机
     * @return 是否替换成功
     */
    boolean replaceComputer(@NotNull Computer source, @NotNull Computer replace);

    /**
     * 获取计算机数据的起始地址
     *
     * @return 计算机数据的起始地址
     */
    DataAddress getComputerAddress();
}
