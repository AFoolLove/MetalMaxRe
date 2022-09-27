package me.afoolslove.metalmaxre.event.editors.editor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 编辑器被启用
 */
public class EditorEnabledEvent extends EditorEvent {

    public EditorEnabledEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor) {
        super(metalMaxRe, editor);
    }
}
