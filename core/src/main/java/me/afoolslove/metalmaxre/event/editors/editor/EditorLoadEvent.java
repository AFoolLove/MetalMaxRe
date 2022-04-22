package me.afoolslove.metalmaxre.event.editors.editor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 编辑器加载事件
 *
 * @author AFoolLove
 */
public class EditorLoadEvent extends EditorEvent {
    private final boolean reload;

    public EditorLoadEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, boolean reload) {
        super(metalMaxRe, editor);
        this.reload = reload;
    }

    /**
     * @return 是否为重新加载编辑器
     */
    public boolean isReload() {
        return reload;
    }

    /**
     * 编辑器加载前的事件
     */
    public static class Pre extends EditorLoadEvent {

        public Pre(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, boolean reload) {
            super(metalMaxRe, editor, reload);
        }
    }

    /**
     * 编辑器加载后的事件
     */
    public static class Post extends EditorLoadEvent {
        private final Exception exception;

        public Post(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, @Nullable Exception exception, boolean reload) {
            super(metalMaxRe, editor, reload);
            this.exception = exception;
        }

        public Post(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, boolean reload) {
            this(metalMaxRe, editor, null, reload);
        }

        @Nullable
        public Exception getException() {
            return exception;
        }
    }
}
