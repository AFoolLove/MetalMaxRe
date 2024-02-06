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
    public static class LoadPre extends EditorManagerEvent {

        public LoadPre(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }
    }

    /**
     * 所有编辑器加载完毕
     */
    public static class LoadPost extends EditorManagerEvent {

        public LoadPost(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }
    }

    /**
     * 所有编辑加载时出现错误，无法正常等待编辑器加载完毕
     */
    public static class LoadError extends EditorManagerEvent {
        private final Throwable throwable;

        public LoadError(@NotNull MetalMaxRe metalMaxRe, @NotNull Throwable throwable) {
            super(metalMaxRe);
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    /**
     * 所有编辑器应用前
     */
    public static class ApplyPre extends EditorManagerEvent {

        public ApplyPre(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }
    }

    /**
     * 所有编辑器应用完毕
     */
    public static class ApplyPost extends EditorManagerEvent {

        public ApplyPost(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }
    }

    /**
     * 所有编辑器应用时出现错误，无法进行正常等待应用编辑器完毕
     */
    public static class ApplyError extends EditorManagerEvent {
        private final Throwable throwable;

        public ApplyError(@NotNull MetalMaxRe metalMaxRe, @NotNull Throwable throwable) {
            super(metalMaxRe);
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

}
