package me.afoolslove.metalmaxre.event.editors.computer;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * 计算机编辑器的事件
 * <p>
 * 添加、移除和替换
 *
 * @author AFoolLove
 */
public class EditorComputerEvent extends EditorEvent {
    private final Computer computer;

    private EditorComputerEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {
        super(metalMaxRe, editor);
        this.computer = computer;
    }

    public Computer getComputer() {
        return computer;
    }

    /**
     * 所有计算机进行更新，通常是进行数据替换等操作
     */
    public static class DataUpdateComputer extends EditorComputerEvent {
        private final List<Computer> oldComputers;
        private final List<Computer> newComputers;

        public DataUpdateComputer(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull List<Computer> oldComputers, @NotNull List<Computer> newComputers) {
            super(metalMaxRe, editor, Computer.EMPTY_COMPUTER);
            this.oldComputers = Collections.unmodifiableList(oldComputers);
            this.newComputers = Collections.unmodifiableList(newComputers);
        }

        /**
         * @return 旧的计算机列表，unmodifiableList
         */
        public List<Computer> getOldComputers() {
            return oldComputers;
        }

        /**
         * @return 新的计算机列表，unmodifiableList
         */
        public List<Computer> getNewComputers() {
            return newComputers;
        }

        /**
         * @return 返回固定实例 Computer.EMPTY_COMPUTER
         * @deprecated 请使用 {@link #getNewComputers()} 和 {@link #getOldComputers()}
         */
        @Override
        @Deprecated
        public Computer getComputer() {
            return Computer.EMPTY_COMPUTER;
        }
    }

    /**
     * 更新一个计算机的事件
     */
    public static class UpdateComputer extends EditorComputerEvent {

        public UpdateComputer(@NotNull MetalMaxRe metalMaxRe, @NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {
            super(metalMaxRe, editor, computer);
        }

        public Computer getUpdateComputer() {
            return super.getComputer();
        }
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
