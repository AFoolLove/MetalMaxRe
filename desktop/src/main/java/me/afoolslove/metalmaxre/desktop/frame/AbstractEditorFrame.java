package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public abstract class AbstractEditorFrame extends JFrame {
    private final MetalMaxRe metalMaxRe;
    private final Frame frame;

    private final Integer initMap;

    public AbstractEditorFrame(Integer initMap, @NotNull Frame frame, @Nullable MetalMaxRe metalMaxRe) {
        this.initMap = initMap;
        this.metalMaxRe = metalMaxRe;
        this.frame = frame;
    }

    public AbstractEditorFrame(@NotNull Frame frame, @Nullable MetalMaxRe metalMaxRe) {
        this(null, frame, metalMaxRe);
    }

    protected AbstractEditorFrame(@NotNull Frame frame) {
        this(null, frame, null);
    }

    public Integer getInitMap() {
        return initMap;
    }

    public void init(String title, @NotNull JPanel contentPane) {
        if (metalMaxRe != null) {
            Path path = metalMaxRe.getBuffer().getPath();
            if (path != null) {
                setTitle(String.format("%s [%s] - %s", title, path.getName(path.getNameCount() - 1), path));
            } else {
                setTitle(String.format("%s [%s]", title, metalMaxRe.getBuffer().getVersion().getName()));
            }
        } else {
            setTitle(title);
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
