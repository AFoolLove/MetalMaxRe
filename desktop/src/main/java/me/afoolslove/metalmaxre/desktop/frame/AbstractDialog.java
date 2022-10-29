package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class AbstractDialog extends JDialog {
    private final Frame frame;
    private final MetalMaxRe metalMaxRe;

    public AbstractDialog(Frame frame, MetalMaxRe metalMaxRe) {
        this.frame = frame;
        this.metalMaxRe = metalMaxRe;
    }

    public AbstractDialog(Frame frame) {
        this(frame, null);
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

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 创建并设置菜单栏
        JMenuBar menuBar = new JMenuBar();
        createMenu(menuBar);
        setJMenuBar(menuBar);
        // 创建布局
        createLayout();
    }

    public void createMenu(JMenuBar menuBar) {
    }

    public void createLayout() {
    }
}
