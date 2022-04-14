package me.afoolslove.metalmaxre.editors.computer.listener;

import me.afoolslove.metalmaxre.editors.IEditorListener;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import org.jetbrains.annotations.NotNull;

/**
 * 计算机编辑器的监听器
 *
 * @author AFoolLove
 */
public interface IComputerListener extends IEditorListener {
    default void onAddComputer(@NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {

    }

    default void onRemoveComputer(@NotNull IComputerEditor<Computer> editor, @NotNull Computer computer) {

    }

    default void onReplaceComputer(@NotNull IComputerEditor<Computer> editor, @NotNull Computer source, @NotNull Computer replace) {

    }
}
