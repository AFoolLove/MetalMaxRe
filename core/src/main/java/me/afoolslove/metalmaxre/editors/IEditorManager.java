package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface IEditorManager {
    MetalMaxRe getMetalMaxRe();

    /**
     * 注册一个编辑器
     *
     * @param editorType 编辑器类类型
     * @param builder    编辑器实例的构建方式
     */
    <E extends IRomEditor> void register(@NotNull Class<E> editorType, @NotNull Function<MetalMaxRe, E> builder);

    /**
     * 加载所有编辑器
     */
    <R> R loadEditors();

    /**
     * 程序加载所有编辑器
     */
    <R> R reloadEditors();

    /**
     * 加载指定编辑器
     *
     * @param type 被加载的编辑器类型
     */
    <R> R loadEditor(Class<? extends IRomEditor> type);

    /**
     * 重新加载指定编辑器
     *
     * @param type 被重新加载的编辑器
     */
    <R> R reloadEditor(Class<? extends IRomEditor> type);

    /**
     * 应用指定编辑器
     *
     * @param type 被应用的编辑器类型
     */
    <R> R applyEditor(Class<? extends IRomEditor> type);

    /**
     * 获取编辑器实例
     *
     * @param editor 编辑器类型
     * @return 编辑器
     */
    <E extends IRomEditor> E getEditor(Class<? extends IRomEditor> editor);
}
