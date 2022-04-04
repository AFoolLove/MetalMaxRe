package me.afoolslove.metalmaxre.editors;

import org.jetbrains.annotations.NotNull;

/**
 * 基础编辑器监听器接口
 */
public interface IEditorListener {
    void onPreLoad(@NotNull IRomEditor editor);

    void onPostLoad(@NotNull IRomEditor editor, long time);


    void onPreApply(@NotNull IRomEditor editor);

    void onPostApply(@NotNull IRomEditor editor, long time);
}
