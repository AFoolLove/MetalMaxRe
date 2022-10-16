package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public abstract class AbstractEditorFrame extends JFrame {
    private final MetalMaxRe metalMaxRe;
    private final Frame frame;

    public AbstractEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        this.metalMaxRe = metalMaxRe;
        this.frame = frame;
    }

    public void init(String name, @NotNull JPanel contentPane) {
        Path path = metalMaxRe.getBuffer().getPath();
        if (path != null) {
            setTitle(String.format("%s [%s] - %s", name, path.getName(path.getNameCount() - 1), path));
        } else {
            setTitle(String.format("%s [%s]", name, metalMaxRe.getBuffer().getVersion().getName()));
        }
        setContentPane(contentPane);
        setLocation(frame.getX(), frame.getY());

        // 创建并设置菜单栏
        JMenuBar menuBar = new JMenuBar();
        createMenu(menuBar);
        setJMenuBar(menuBar);
        // 创建布局
        createLayout();
    }

    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

    public Frame getFrame() {
        return frame;
    }

    protected void createMenu(@NotNull JMenuBar menuBar) {

    }

    protected void createLayout() {

    }


}
