package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.DogSystemEditor;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import me.afoolslove.metalmaxre.editor.treasure.Treasure;
import me.afoolslove.metalmaxre.editor.treasure.TreasureEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * 主窗口
 *
 * @author AFoolLove
 */
public class MainWindow extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
    private JComboBox<String> comboBox4;
    private JComboBox<String> comboBox5;
    private JComboBox<String> comboBox6;
    private JComboBox<String> comboBox7;
    private JComboBox<String> comboBox8;
    private JComboBox<String> comboBox9;
    private JComboBox<String> maps;
    private JTable treasures;
    private JTextField treasureMap;
    private JTextField treasureX;
    private JTextField treasureY;
    private JButton treasureAdd;
    private JButton treasureRemove;
    private JButton treasureUpdate;
    private JTextField treasureItem;

    public MainWindow() {
        URL url = getClass().getResource("");
        if (url != null && "jar".equalsIgnoreCase(url.getProtocol())) {
            int result = JOptionPane.showConfirmDialog(this,
                    "没有经过修改版的作者同意，禁止使用本程序将修改内容发布到任何地方",
                    "MetalMaxRe",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }

        setTitle("MetalMaxRe");

        URL resource = getClass().getClassLoader().getResource("MetalMax.nes");
        if (resource == null) {
            // 不会真的会读取失败吧？
            JOptionPane.showConfirmDialog(this, "读取初始文件失败！", "加载错误", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }

        createMenuBar();
        createLayout();

        EditorManager.getEditor(DogSystemEditor.class).getListeners().add(new AbstractEditor.Listener<>() {
            @Override
            public void onReadAfter(@NotNull DogSystemEditor editor) {
                JComboBox<String>[] comboBoxes = new JComboBox[]{
                        comboBox1, comboBox2, comboBox3,
                        comboBox4, comboBox5, comboBox6,
                        comboBox7, comboBox8, comboBox9,
                };
                for (int i = 0; i < comboBoxes.length; i++) {
                    comboBoxes[i].setSelectedIndex(editor.getTown(i) & 0xFF);
                }
            }
        });

        EditorManager.getEditor(TreasureEditor.class).getListeners().add(new AbstractEditor.Listener<>() {
            @Override
            public void onReadAfter(@NotNull TreasureEditor editor) {
                treasures.removeAll();
                DefaultTableModel treasuresModel = ((DefaultTableModel) treasures.getModel());
                for (Treasure treasure : editor.getTreasures()) {
                    treasuresModel.addRow(new String[]{
                            String.format("%02X", treasure.getMap()),
                            String.format("%02X", treasure.getX()),
                            String.format("%02X", treasure.getY()),
                            String.format("%02X", treasure.getItem())
                    });
                }
            }
        });

        loadGame(new File(resource.getFile()), true);

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

        JMenuItem fileMenuExit = new JMenuItem("Exit");
        fileMenuExit.addActionListener(e -> {
            // 退出
            System.exit(0);
        });

//        fileMenu.add(fileMenuOpen);
//        fileMenu.addSeparator();
        fileMenu.add(fileMenuReload);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuSave);
        fileMenu.add(fileMenuSaveAs);
        fileMenu.addSeparator();
        fileMenu.add(fileMenuExit);

        JMenu toolsMenu = new JMenu("Tools");

        JMenuItem toolsMenuPalette = new JMenuItem("Palette");
        toolsMenuPalette.addActionListener(e -> {
            PaletteDialog paletteDialog = new PaletteDialog();
            paletteDialog.pack();
            paletteDialog.setVisible(true);
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
     * 创建布局
     */
    private void createLayout() {
        maps.setModel(new DefaultComboBoxModel<>() {
            {
                for (int i = 0; i < MapEditor.MAP_MAX_COUNT; i++) {
                    addElement(String.format("%02X", i));
                }
            }

            @Override
            public int getSize() {
                return MapEditor.MAP_MAX_COUNT;
            }
        });

        JComboBox<String>[] comboBoxes = new JComboBox[]{
                comboBox1, comboBox2, comboBox3,
                comboBox4, comboBox5, comboBox6,
                comboBox7, comboBox8, comboBox9,
        };
        for (JComboBox<String> comboBox : comboBoxes) {
            comboBox.setModel(new DefaultComboBoxModel<>() {
                {
                    for (int i = 0; i < MapEditor.MAP_MAX_COUNT; i++) {
                        addElement(String.format("%02X", i));
                    }
                }

                @Override
                public int getSize() {
                    return MapEditor.MAP_MAX_COUNT;
                }
            });
        }

        treasures.setModel(new DefaultTableModel(0, 4) {
            final String[] columnNames = {"Map", "X", "Y", "Item"};

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }
        });
        treasures.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = treasures.getSelectedRow();
            if (selectedRow != -1) {
                treasureMap.setText(treasures.getValueAt(selectedRow, 0).toString());
                treasureX.setText(treasures.getValueAt(selectedRow, 1).toString());
                treasureY.setText(treasures.getValueAt(selectedRow, 2).toString());
                treasureItem.setText(treasures.getValueAt(selectedRow, 3).toString());
            }
        });
        treasureUpdate.addActionListener(e -> {
            int selectedRow = treasures.getSelectedRow();
            if (selectedRow == -1) {
                return;
            }
            Treasure source = new Treasure(
                    Integer.parseInt(treasures.getValueAt(selectedRow, 0).toString(), 16),
                    Integer.parseInt(treasures.getValueAt(selectedRow, 1).toString(), 16),
                    Integer.parseInt(treasures.getValueAt(selectedRow, 2).toString(), 16),
                    Integer.parseInt(treasures.getValueAt(selectedRow, 3).toString(), 16)
            );

            TreasureEditor treasureEditor = EditorManager.getEditor(TreasureEditor.class);
            Treasure treasure = treasureEditor.find(source);
            if (treasure == null) {
                return;
            }

            int map = Integer.parseInt(treasureMap.getText(), 16);
            int x = Integer.parseInt(treasureX.getText(), 16);
            int y = Integer.parseInt(treasureY.getText(), 16);
            int item = Integer.parseInt(treasureItem.getText(), 16);

            treasureEditor.replace(treasure, new Treasure(map, x, y, item));
            treasures.setValueAt(String.format("%02X", map), selectedRow, 0);
            treasures.setValueAt(String.format("%02X", x), selectedRow, 1);
            treasures.setValueAt(String.format("%02X", y), selectedRow, 2);
            treasures.setValueAt(String.format("%02X", item), selectedRow, 3);
            treasures.validate();
        });
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
