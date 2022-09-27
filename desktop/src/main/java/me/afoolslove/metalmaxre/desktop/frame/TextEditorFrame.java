package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class TextEditorFrame extends JFrame {
    private JPanel contentPane;
    private JList textPages;
    private JButton save;
    private JList list1;
    private JTextArea textArea1;
    private JTextArea textBytes;
    private JTextArea textWord;

    private final MultipleMetalMaxRe multipleMetalMaxRe;

    public TextEditorFrame(@NotNull Frame frame, @NotNull MultipleMetalMaxRe multipleMetalMaxRe) {
        this.multipleMetalMaxRe = multipleMetalMaxRe;

        MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
        Path path = metalMaxRe.getBuffer().getPath();
        if (path != null) {
            setTitle(String.format("文本编辑器 [%s] - %s", path.getName(path.getNameCount() - 1), path));
        } else {
            setTitle(String.format("文本编辑器 [%s]", metalMaxRe.getBuffer().getVersion().getName()));
        }

        setContentPane(contentPane);
        setLocation(frame.getX(), frame.getY());
    }

}
