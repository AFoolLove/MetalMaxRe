package me.afoolslove.metalmaxre.event.editors.computer;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 计算机编辑器的事件
 * <p>
 * 添加、移除和替换
 *
 * @author AFoolLove
 */
public class EditorComputerEvent extends EditorEvent {
    private final Computer computer;

    public EditorComputerEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {
        super(metalMaxRe, editor);
        this.computer = computer;
    }

    public Computer getComputer() {
        return computer;
    }

    /**
     * 添加计算机的事件
     */
    public static class AddComputer extends EditorComputerEvent {

        public AddComputer(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {
            super(metalMaxRe, editor, computer);
        }

        public Computer getAddComputer() {
            return super.getComputer();
        }
    }

    /**
     * 移除计算机的事件
     */
    public static class RemoveComputer extends EditorComputerEvent {

        public RemoveComputer(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {
            super(metalMaxRe, editor, computer);
        }

        public Computer getRemoveComputer() {
            return super.getComputer();
        }
    }

    /**
     * 替换计算机的事件
     */
    public static class ReplaceComputer extends EditorComputerEvent {
        private final Computer replace;

        public ReplaceComputer(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull Computer soucre, @NotNull Computer replace) {
            super(metalMaxRe, editor, soucre);
            this.replace = replace;
        }

        public Computer getSourceComputer() {
            return super.getComputer();
        }

        public Computer getReplaceComputer() {
            return replace;
        }
    }
}
