package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * 编辑器管理器
 *
 * @author AFoolLove
 */
public interface IEditorManager {
    MetalMaxRe getMetalMaxRe();

    /**
     * 获取编辑器数量
     *
     * @return 编辑器数量
     */
    int getCount();

    /**
     * 注册一个编辑器
     *
     * @param editorType 编辑器类类型
     * @param builder    编辑器实例的构建方式
     */
    <E extends IRomEditor> void register(@NotNull Class<E> editorType, @NotNull Function<MetalMaxRe, E> builder);

    /**
     * 注销一个编辑器
     *
     * @param editorType 编辑器类类型
     */
    <E extends IRomEditor> IRomEditor unregister(@NotNull Class<E> editorType);

    /**
     * 加载所有编辑器
     */
    Future<?> loadEditors();

    /**
     * 应用所有编辑器的修改
     */
    Future<?> applyEditors();

    /**
     * 重新加载所有编辑器
     */
    Future<?> reloadEditors();

    /**
     * 加载指定编辑器
     *
     * @param type 被加载的编辑器类型
     */
    Object loadEditor(@NotNull Class<? extends IRomEditor> type);

    /**
     * 重新加载指定编辑器
     *
     * @param type 被重新加载的编辑器
     */
    Object reloadEditor(@NotNull Class<? extends IRomEditor> type);

    /**
     * 应用指定编辑器
     *
     * @param type 被应用的编辑器类型
     */
    Object applyEditor(@NotNull Class<? extends IRomEditor> type);

    /**
     * 获取编辑器实例
     *
     * @param editor 编辑器类型
     * @return 编辑器
     */
    <E extends IRomEditor> E getEditor(Class<? extends IRomEditor> editor);
}
