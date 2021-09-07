package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.EditorProcess;
import me.afoolslove.metalmaxre.EditorWorker;
import me.afoolslove.metalmaxre.editor.EditorManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * 窗口版
 *
 * @author AFoolLove
 */
public class WindowEditorWorker extends EditorWorker {
    private final MainWindow window;
    private final File game;
    private final boolean init;
    private final JProgressBar progressBar;
    private JDialog dialog;

    public WindowEditorWorker(@NotNull MainWindow window, @NotNull File game, boolean init) {
        this.window = window;
        this.game = game;
        this.init = init;
        progressBar = new JProgressBar(0, EditorManager.getEditors().size());

        // 显示正在加载
        SwingUtilities.invokeLater(() -> {
            JOptionPane jOptionPane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE);
            dialog = jOptionPane.createDialog(window, "正在加载游戏文件");
            dialog.setVisible(true);
        });
    }

    @Override
    protected void process(List<Map.Entry<EditorProcess, Object>> chunks) {
        for (Map.Entry<EditorProcess, Object> chunk : chunks) {
            if (chunk.getKey() == EditorProcess.RESULT) {
                progressBar.setValue(progressBar.getValue() + 1);
            }
            if (chunk.getKey() == EditorProcess.MESSAGE) {
                System.out.println(chunk.getValue());
            }
        }
    }

    @Override
    protected void done() {
        try {
            // 关闭提示正在加载的Dialog
            dialog.setVisible(false);
            dialog.dispose();
            // 加载NES文件
            if (get()) {
                // 加载成功
                // 设置标题为打开的文件路径
                if (!init) {
                    window.setTitle(String.format("MetalMaxRe [%s] - %s", game.getName(), game.getPath()));
                } else {
                    window.setTitle(String.format("MetalMaxRe [%s]", game.getName()));
                }

                JPopupMenu fileMenu = window.getJMenuBar().getMenu(0x00).getPopupMenu();
                for (Component component : fileMenu.getComponents()) {
                    if (component instanceof JMenuItem) {
                        if (Objects.equals(((JMenuItem) component).getText(), "Save")) {
                            // 对菜单 File->Save 按钮启用或禁用
                            component.setEnabled(!init);
                            break;
                        }
                    }
                }
            } else {
                if (!init) {
                    String text = String.format("打开%s文件失败\n路径：%s", game.getName(), game.getPath());
                    JOptionPane.showMessageDialog(window, text, "游戏文件加载失败，请重试", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(window, "打开初始文件失败", "初始游戏文件加载失败，请重新打开一个文件再使用", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
