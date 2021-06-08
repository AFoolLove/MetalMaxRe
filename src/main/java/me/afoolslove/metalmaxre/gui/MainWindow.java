package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 主窗口
 *
 * @author AFoolLove
 */
public class MainWindow extends JFrame {

    private JPanel contentPane;

    public MainWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // 窗口默认大小
        setSize(640, 800);
        URL resource = getClass().getClassLoader().getResource("MetalMax.nes");
        if (resource == null) {
            // 不会真的会读取失败吧？
            JOptionPane.showConfirmDialog(this, "读取初始文件失败！", "加载错误", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadGame(new File(resource.getFile()), true);

        createMenuBar();

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

    /**
     * 创建菜单栏
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileMenuOpen = new JMenuItem("Open ...");
        // 快捷键：Ctrl + O
        fileMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        fileMenuOpen.addActionListener(e -> {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser();
            // 添加一个nes后缀的文件过滤器
            fileChooser.setFileFilter(new FileNameExtensionFilter("NES FILE", "NES"));
            // 开始选择文件
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                // 获取选择的NES文件
                loadGame(fileChooser.getSelectedFile());
            } // 其它皆为不打开文件
        });


        fileMenu.add(fileMenuOpen);

        menuBar.add(fileMenu);
        // 设置菜单栏
        setJMenuBar(menuBar);
    }

    /**
     * 加载游戏文件
     */
    public void loadGame(@NotNull File game) {
        loadGame(game, false);
    }

    private synchronized void loadGame(@NotNull File game, boolean init) {
        MetalMaxRe.getInstance().loadGame(game, new WindowEditorWorker() {
            @Override
            protected void done() {
                try {
                    // 加载NES文件
                    if (get()) {
                        // 加载成功
                        // 设置标题为打开的文件路径
                        if (!init) {
                            setTitle(String.format("MetalMaxRe [%s] - %s", game.getName(), game.getPath()));
                        } else {
                            setTitle(String.format("MetalMaxRe [%s]", game.getName()));
                        }
                    } else {
                        if (!init) {
                            String text = String.format("打开%s文件失败\n路径：%s", game.getName(), game.getPath());
                            JOptionPane.showMessageDialog(MainWindow.this, text, "游戏文件加载失败，请重试", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(MainWindow.this, "打开初始文件失败", "初始游戏文件加载失败，请重新打开一个文件再使用", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
