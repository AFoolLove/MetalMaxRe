package me.afoolslove.metalmaxre.event.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import org.jetbrains.annotations.NotNull;

/**
 * 犬系统编辑器的事件
 *
 * @author AFoolLove
 */
public class EditorDogSystemEvent extends EditorEvent {
    public EditorDogSystemEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor) {
        super(metalMaxRe, editor);
    }
}
