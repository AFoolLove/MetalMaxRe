package me.afoolslove.metalmaxre.event.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * 有关编辑器的事件
 *
 * @author AFoolLove
 */
public class EditorEvent extends Event {
    @NotNull
    private final IRomEditor editor;

    private long completionTime = -1;

    public EditorEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor) {
        super(metalMaxRe);
        this.editor = editor;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <E extends IRomEditor> E getEditor() {
        return (E) editor;
    }


    public long getCompletionTime() {
        return completionTime;
    }

    public boolean isComplete() {
        return completionTime != -1;
    }

    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }
}
