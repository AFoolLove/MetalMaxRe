package me.afoolslove.metalmaxre.event.editors.editor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 编辑器应用事件
 *
 * @author AFoolLove
 */
public class EditorApplyEvent extends EditorEvent {

    public EditorApplyEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor) {
        super(metalMaxRe, editor);
    }

    public static class Pre extends EditorApplyEvent {

        public Pre(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor) {
            super(metalMaxRe, editor);
        }
    }

    public static class Post extends EditorApplyEvent {
        private final Exception exception;

        public Post(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, @Nullable Exception exception) {
            super(metalMaxRe, editor);
            this.exception = exception;
        }

        public Post(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor) {
            this(metalMaxRe, editor, null);
        }

        @Nullable
        public Exception getException() {
            return exception;
        }
    }
}
