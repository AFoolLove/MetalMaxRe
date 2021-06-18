package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.AttackRange;
import me.afoolslove.metalmaxre.ColorTool;
import me.afoolslove.metalmaxre.DataValues;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.items.ItemsEditor;
import me.afoolslove.metalmaxre.editor.map.tileset.TileSetEditor;
import me.afoolslove.metalmaxre.editor.tank.TankShellCapacity;
import me.afoolslove.metalmaxre.editor.tank.TankWeaponSlot;
import me.afoolslove.metalmaxre.editor.text.TextEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * 主窗口
 *
 * @author AFoolLove
 */
public class MainWindow extends JFrame {

    private JPanel contentPane;
    private JTabbedPane tabbedPane1;

    public MainWindow() {
        setTitle("MetalMaxRe");

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
        // 窗口默认大小
        setSize(854, 480);
//        pack();
        setVisible(true);

    }

    /**
     * 创建菜单栏
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileMenuOpen = new JMenuItem("Open...");
        // 快捷键：Ctrl + O
        fileMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        fileMenuOpen.addActionListener(e -> {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser();
            // 添加一个nes后缀的文件过滤器
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("NES FILE(*.nes)", "NES"));
            // 开始选择文件
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                // 获取选择的NES文件
                loadGame(fileChooser.getSelectedFile());
            } // 其它皆为不打开文件
        });

        JMenuItem fileMenuReload = new JMenuItem("Reload");
        fileMenuReload.addActionListener(e -> {
            // 重新加载前提示放弃已修改的数据
            int result = JOptionPane.showConfirmDialog(this,
                    "重新加载将会丢失已修改的数据！"
                    , "重新加载"
                    , JOptionPane.OK_CANCEL_OPTION
                    , JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // 已确认重新加载
                loadGame(MetalMaxRe.getInstance().getTarget(), MetalMaxRe.getInstance().isIsInitTarget());
            }
        });

        JMenuItem fileMenuSave = new JMenuItem("Save");
        // 快捷键：Ctrl + S
        fileMenuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        fileMenuSave.addActionListener(e -> {
            // 目标文件有效才能保存
            if (!MetalMaxRe.getInstance().isIsInitTarget()) {
                // 保存修改
                String path = MetalMaxRe.getInstance().getTarget().getPath();
                boolean saveAs = MetalMaxRe.getInstance().saveAs(path);
                if (!saveAs) {
                    // 保存失败
                    JOptionPane.showMessageDialog(this,
                            String.format("保存到：%s\n失败！", path), "保存失败",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem fileMenuSaveAs = new JMenuItem("Save As...");
        // 快捷键：Ctrl + Shift + S
        fileMenuSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));
        fileMenuSaveAs.addActionListener(e -> {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser();
            // 选择保存的路径或替换的文件
            int state = fileChooser.showSaveDialog(this);
            // 只关心是否选中了导出的目标文件
            if (state == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // 如果存在则提示是否覆盖
                int result = 0;
                if (selectedFile.exists()) {
                    result = JOptionPane.showConfirmDialog(this,
                            String.format("目标文件已存在：%s！\n替换目标文件吗？", selectedFile.getName()),
                            "目标文件已存在",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                }
                if (result == JOptionPane.OK_OPTION) {
                    // 保存
                    MetalMaxRe.getInstance().saveAs(selectedFile.getPath());
                }
            } // 其它皆为不不保存
        });


        fileMenu.add(fileMenuOpen);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuReload);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuSave);
        fileMenu.add(fileMenuSaveAs);

        JMenu toolsMenu = new JMenu("Tools");

        JMenuItem toolsMenuPalette = new JMenuItem("Palette");
        toolsMenuPalette.addActionListener(e -> {

        });


        toolsMenu.add(toolsMenuPalette);


        JMenu helpMenu = new JMenu("Help");

        JMenuItem helpMenuGithub = new JMenuItem("Github");
        helpMenuGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/AFoolLove/MetalMaxRe"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        JMenuItem helpMenuAbout = new JMenuItem("About");
        helpMenuAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "这是一个FC游戏 MetalMax 的编辑器");
        });


        // 测试功能的选项
        JMenuItem helpMenuTest = new JMenuItem("Test");
        // 快捷键：Ctrl + Shift + T
        helpMenuTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));
        helpMenuTest.addActionListener(e -> {
            var editor = EditorManager.getEditor(TileSetEditor.class);
            byte[] bytes = editor.tiles[0x00][0x00];
            bytes[0x00] = (byte) 0B0001_1000;
            bytes[0x01] = (byte) 0B0001_1000;
            bytes[0x02] = (byte) 0B0010_0100;
            bytes[0x03] = (byte) 0B0100_0010;
            bytes[0x04] = (byte) 0B0001_1000;
            bytes[0x05] = (byte) 0B1000_0001;
            bytes[0x06] = (byte) 0B0000_0000;
            bytes[0x07] = (byte) 0B1111_1111;


            System.out.println("test.");
        });

        helpMenu.add(helpMenuGithub);
        helpMenu.add(helpMenuAbout);
        helpMenu.add(helpMenuTest);


        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
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
        MetalMaxRe.getInstance().loadGame(init, game, new WindowEditorWorker(this, game, init));
    }
}
