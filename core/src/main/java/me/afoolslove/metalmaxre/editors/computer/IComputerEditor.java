package me.afoolslove.metalmaxre.editors.computer;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 计算机编辑器
 * <p>
 * *需要地图属性支持，并且支持动态图块
 *
 * @param <C> 计算机的属性
 * @author AFoolLove
 */
public interface IComputerEditor<C extends Computer> extends IRomEditor {
    @Override
    default String getId() {
        return "computerEditor";
    }

    /**
     * 获取计算机的最大数量
     *
     * @return 计算机的最大数量
     */
    default int getMaxCount() {
        return 0x7B;
    }

    /**
     * 获取所有的计算机。售货机、游戏机、计算机等
     *
     * @return 所有的计算机
     */
    List<C> getComputers();

    /**
     * 清空当前所有计算机，更换为新的计算机列表
     *
     * @param newComputers 新的计算机列表
     * @return 旧的计算机列表
     */
    List<C> dataUpdate(@Nullable List<C> newComputers);

    /**
     * 更新一个计算机的数据
     */
    void updateComputer(@NotNull C computer);

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
    boolean replaceComputer(@NotNull C source, @NotNull C replace);

    /**
     * 判断是否包含指定的计算机
     *
     * @param computer 被包含的计算机
     */
    boolean containsComputer(@NotNull C computer);

    /**
     * 获取计算机数据的起始地址
     *
     * @return 计算机数据的起始地址
     */
    DataAddress getComputerAddress();
}
