package me.afoolslove.metalmaxre.event.editors.editor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.event.Event;
import org.jetbrains.annotations.NotNull;

public class EditorManagerEvent extends Event {
    public EditorManagerEvent(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
    }

    /**
     * 所有编辑器加载前
     */
    public static class Pre extends EditorManagerEvent {

        public Pre(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }
    }

    /**
     * 所有编辑器加载完毕
     */
    public static class Post extends EditorManagerEvent {

        public Post(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }
    }
}
