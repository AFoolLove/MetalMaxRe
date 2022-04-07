package me.afoolslove.metalmaxre.editors;

import org.jetbrains.annotations.NotNull;

/**
 * 基础编辑器监听器接口
 *
 * @author AFoolLove
 */
public interface IEditorListener {
    default void onPreLoad(@NotNull IRomEditor editor) {
    }

    default void onPostLoad(@NotNull IRomEditor editor, long time) {
    }


    default void onPreApply(@NotNull IRomEditor editor) {
    }

    default void onPostApply(@NotNull IRomEditor editor, long time) {
    }
}
