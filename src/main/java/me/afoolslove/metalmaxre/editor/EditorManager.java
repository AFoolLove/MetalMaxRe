package me.afoolslove.metalmaxre.editor;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author AFoolLove
 */
public final class EditorManager {
    private static final HashMap<Class<? extends AbstractEditor>, AbstractEditor> EDITORS = new LinkedHashMap<>();

    public static HashMap<Class<? extends AbstractEditor>, AbstractEditor> getEditors() {
        return EDITORS;
    }

    /**
     * 注册一个编辑器
     */
    public static <T extends AbstractEditor> void register(@NotNull T editor) {
        EDITORS.put(editor.getClass(), editor);
    }

    /**
     * 不考虑 {@code null} 的情况
     *
     * @return 获取相应的编辑器
     */
    public static <T extends AbstractEditor> T getEditor(Class<T> editor) {
        return editor.cast(EDITORS.get(editor));
    }

}
