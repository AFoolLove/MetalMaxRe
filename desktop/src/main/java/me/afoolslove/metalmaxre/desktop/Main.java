package me.afoolslove.metalmaxre.desktop;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.components.FlatTriStateCheckBox;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.desktop.adapter.BoxSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.MapImagePopupMenuMouseAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.MiddleMoveMouseAdapter;
import me.afoolslove.metalmaxre.desktop.dialog.EditorEnabledDialog;
import me.afoolslove.metalmaxre.desktop.formatter.NumberFormatter;
import me.afoolslove.metalmaxre.desktop.frame.*;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.map.*;
import me.afoolslove.metalmaxre.editors.map.events.EventTile;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.map.world.LineDirection;
import me.afoolslove.metalmaxre.editors.map.world.WorldMapEditorImpl;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.sprite.Sprite;
import me.afoolslove.metalmaxre.editors.tank.ITankEditor;
import me.afoolslove.metalmaxre.editors.tank.Tank;
import me.afoolslove.metalmaxre.editors.tank.TankInitialAttribute;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.Treasure;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorManagerEvent;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.tiled.TiledMapUtils;
import me.afoolslove.metalmaxre.utils.*;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXEditorPane;
import org.mapeditor.core.TileSet;
import org.mapeditor.io.TMXMapReader;
import org.mapeditor.io.TMXMapWriter;
import org.mapeditor.util.BasicTileCutter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main extends JFrame {
    private final MultipleMetalMaxRe multipleMetalMaxRe = new MultipleMetalMaxRe();

    private JPanel contentPane;
    private JList<String> mapList;
    private JLabel mapImage;
    private JRadioButton borderTypeLast;
    private JRadioButton borderTypeFixed;
    private JRadioButton borderTypeDirection;
    private JSpinner borderLocation10;
    private JSpinner borderLocation11;
    private JSpinner borderLocation12;
    private JSpinner borderLocation20;
    private JSpinner borderLocation21;
    private JSpinner borderLocation22;
    private JSpinner borderLocation30;
    private JSpinner borderLocation31;
    private JSpinner borderLocation32;
    private JSpinner borderLocation00;
    private JSpinner borderLocation01;
    private JSpinner borderLocation02;
    private JXEditorPane mapDes;
    private JCheckBox mapHeadUnderground;
    private JCheckBox mapHeadEventTile;
    private JCheckBox mapHeadUnknown;
    private JCheckBox mapHeadDyTile;
    private JSpinner mapWidth;
    private JSpinner mapHeight;
    private JSpinner mapMovableWidth;
    private JSpinner mapMovableHeight;
    private JSpinner mapMovableWidthOffset;
    private JSpinner mapMovableHeightOffset;
    private JLabel mapHideTile;
    private JLabel mapFillTile;
    private JSpinner mapMusic;
    private JButton applyProperties;
    private JTextField mapFilter;
    private JSpinner X00;
    private JSpinner X40;
    private JSpinner X80;
    private JSpinner XC0;
    private JSpinner S00;
    private JSpinner S40;
    private JSpinner S80;
    private JSpinner SC0;
    private JSpinner mapDataIndex;
    private JSpinner combinationA;
    private JSpinner combinationB;
    private JSpinner palette;
    private JSpinner mapPropertiesIndex;
    private JSpinner eventTilesIndex;
    private JSpinner mapHideTileValue;
    private JSpinner mapFillTileValue;
    private FlatTriStateCheckBox showSprites;
    private FlatTriStateCheckBox showFillTile;
    private FlatTriStateCheckBox showEntrances;
    private FlatTriStateCheckBox showComputers;
    private FlatTriStateCheckBox showEvents;
    private FlatTriStateCheckBox showTreasures;
    private JLabel mapTileSetImage;
    private JLabel spriteTileSetImage;
    private JButton mapImageRefresh;
    private JSpinner monsterGroupIndex;
    private JComboBox<String> dyDataA;
    private JComboBox<String> dyDataB;
    private JScrollPane mapPropertiesScrollPane;
    private JButton collapsedSpriteTileSetImage;
    private JXCollapsiblePane spriteTileSetImagePane;
    private JXCollapsiblePane mapTileSetImagePane;
    private JButton collapsedMapTileSetImage;
    private JScrollPane mapImageScrollPane;
    private JLabel mapTileSetIndex;
    private JLabel spriteTileSetIndex;
    private FlatTriStateCheckBox showTanks;
    private FlatTriStateCheckBox showLines;
    private JComboBox<String> sharedEventTilesIndex;
    private JSpinner mapImageSelectedPointX;
    private JSpinner mapImageSelectedPointY;
    private JButton gotoPoint;
    private JList<Map.Entry<LineDirection, Byte>> outLineList;
    private JList<Map.Entry<LineDirection, Byte>> backLineList;
    private JButton lineUp;
    private JButton lineDown;
    private JButton lineLeft;
    private JButton lineRight;
    private JSpinner lineMoveCount;
    private JButton lineRemove;
    private JRadioButton outLineSelected;
    private JRadioButton backLineSelected;
    private JSpinner outLineTargetMap;
    private JSpinner outLineTargetX;
    private JSpinner outLineTargetY;
    private JButton outLineTarget;
    private JButton gotoOutLineTarget;
    private JButton saveLine;
    private JButton gotoBackLineTarget;
    private JSpinner backLineTargetMap;
    private JSpinner backLineTargetX;
    private JSpinner backLineTargetY;
    private JButton backLineTarget;


    private final ExecutorService imageLoaderService = Executors.newCachedThreadPool();
    private final Map<Integer, ImageIcon> mapImages = new HashMap<>();
    private BufferedImage worldMapImage;

    /**
     * 0x0000_FFFFFFFF_FFFF，tiles(4byte) + palette(2byte)
     */
    private final Map<Long, SingleMapEntry<BufferedImage, List<BufferedImage>>> mapTileSets = new HashMap<>();
    private final Map<Byte, SingleMapEntry<BufferedImage, List<BufferedImage>>> spriteTileSets = new HashMap<>();

    private MapSelectListModel mapSelectListModel;
    private final List<BoxSelectedAdapter.SelectListener> selectListeners = new ArrayList<>();

    private String currentOpenDirectoryPath;
    private String currentSaveAsDirectoryPath;

    // 世界地图的动态水
    private ScheduledExecutorService dynamicWorld = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        UIManager.getDefaults().put("Spinner.editorAlignment", JTextField.LEFT);

        // 亮主题
        FlatLightLaf.setup();
        Main main = new Main();

        main.setContentPane(main.contentPane);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 窗口默认大小
        main.setSize(1280, 860);
//        pack();
        main.setVisible(true);
//        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
//            private final Stack<String> errors = new Stack<>();
//            private final ScheduledExecutorService showError = Executors.newSingleThreadScheduledExecutor();
//
//            {
//                showError.scheduleWithFixedDelay(() -> {
//                    if (errors.isEmpty()) {
//                        return;
//                    }
//                    String pop = errors.pop();
//                    SwingUtilities.invokeLater(() -> JOptionPane.showConfirmDialog(null, pop, "发生了意外", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE));
//                }, 100, 100, TimeUnit.MILLISECONDS);
//            }
//
//            protected void dispatchEvent(AWTEvent newEvent) {
//                try {
//                    super.dispatchEvent(newEvent);
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    t.printStackTrace(new PrintWriter(out, true));
//                    errors.add(out.toString());
//                }
//            }
//        });
    }

    public Main() {
        if (ResourceManager.isJar()) {
            int result = JOptionPane.showConfirmDialog(this,
                    "未经修改版的作者同意，禁止通过本程序将数据公布到任何地方",
                    "MetalMaxRe",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                System.exit(0);
                return;
            }
        }

        // 设置标题
        setTitle("MetalMaxRe");

        // 创建菜单栏
        createMenuBar();

        createLayout();
    }

    private JCheckBoxMenuItem testMenuAdvancedMap;

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        // 设置菜单栏
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("文件");
        // Alt+F
        fileMenu.setMnemonic(KeyEvent.VK_F);

        // 打开ROM文件
        JMenuItem fileMenuOpen = new JMenuItem("打开...");
        fileMenuOpen.addActionListener(e -> {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser(currentOpenDirectoryPath);
            // 添加一个nes后缀的文件过滤器
            fileChooser.setFileFilter(new FileNameExtensionFilter("NES 文件(*.nes)", "NES"));
            // 开始选择文件
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                // 获取选择的NES文件
                File selectedFile = fileChooser.getSelectedFile();
                currentOpenDirectoryPath = selectedFile.getParent();

                if (selectedFile.length() < 524304) {
                    JOptionPane.showMessageDialog(this, "你怕不是个傻子吧？");
                    return;
                }

                try {
                    MetalMaxRe metalMaxRe = multipleMetalMaxRe.create(RomVersion.getChinese(), selectedFile.toPath(), true);
                    if (testMenuAdvancedMap.isSelected()) {
                        metalMaxRe.getEditorManager().unregister(IMapEditor.class);
                        metalMaxRe.getEditorManager().register(IMapEditor.class, me.afoolslove.metalmaxre.desktop.editors.map.MapEditorImpl::new);
                    }
                    multipleMetalMaxRe.select(metalMaxRe);
                    showLoadingDialog(metalMaxRe);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintWriter printWriter = new PrintWriter(out);
                    ex.printStackTrace(printWriter);
                    JOptionPane.showMessageDialog(this, "加载失败\n" + out);
                }
            }
        });

        // 打开内置ROM
        JMenu fileMenuOpenDefault = new JMenu("打开内置ROM");
        for (RomVersion romVersion : RomVersion.getVersions().values()) {
            JMenuItem version = new JMenuItem(romVersion.getName());
            fileMenuOpenDefault.add(version);

            version.addActionListener(e -> {
                // 如果已经加载了ROM，提示是否覆盖
                int result = 0;
                if (multipleMetalMaxRe.hasInstance()) {
                    result = JOptionPane.showConfirmDialog(this,
                            "加载会丢失已修改的所有数据\n确定要加载吗？",
                            "加载ROM",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                }
                if (result == JOptionPane.OK_OPTION) {
                    // 加载
                    try {
                        MetalMaxRe metalMaxRe = multipleMetalMaxRe.create(romVersion, null, true);
                        if (testMenuAdvancedMap.isSelected()) {
                            metalMaxRe.getEditorManager().unregister(IMapEditor.class);
                            metalMaxRe.getEditorManager().register(IMapEditor.class, me.afoolslove.metalmaxre.desktop.editors.map.MapEditorImpl::new);
                        }
                        multipleMetalMaxRe.select(metalMaxRe);
                        showLoadingDialog(metalMaxRe);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        PrintWriter printWriter = new PrintWriter(out);
                        ex.printStackTrace(printWriter);
                        JOptionPane.showMessageDialog(this, "加载失败\n" + out);
                    }
                }
            });
        }


        // 重新打开
        JMenuItem fileMenuReopen = new JMenuItem("重新加载");
        fileMenuReopen.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            int i = JOptionPane.showConfirmDialog(this, "重新加载会丢失已修改的所有数据", "重新加载", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (i == JOptionPane.OK_OPTION) {
                try {
                    // 移除当前的工程
                    MetalMaxRe oldMetalMaxRe = multipleMetalMaxRe.current();
                    multipleMetalMaxRe.remove(oldMetalMaxRe);
                    // 重新加载工程
                    MetalMaxRe metalMaxRe = multipleMetalMaxRe.create(oldMetalMaxRe.getBuffer().getVersion(), oldMetalMaxRe.getBuffer().getPath(), true);
                    showLoadingDialog(metalMaxRe);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintWriter printWriter = new PrintWriter(out);
                    ex.printStackTrace(printWriter);
                    JOptionPane.showMessageDialog(this, "加载失败\n" + out);
                }
            }
        });


        JMenuItem fileMenuSaveAs = new JMenuItem("另存为...");
        // 快捷键：Ctrl + Shift + S
        fileMenuSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));
        fileMenuSaveAs.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser(currentSaveAsDirectoryPath);
            fileChooser.setFileFilter(new FileNameExtensionFilter("NES 文件(*.nes)", "nes"));
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
                    try {
                        currentSaveAsDirectoryPath = selectedFile.getParent();
                        Files.write(selectedFile.toPath(), metalMaxRe.getBuffer().toByteArray());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        PrintWriter printWriter = new PrintWriter(out);
                        ex.printStackTrace(printWriter);
                        JOptionPane.showMessageDialog(this, "保存失败\n" + out);
                    }
                }
            } // 其它皆为不不保存
        });

        JMenuItem fileMenuApply = new JMenuItem("应用");
        fileMenuApply.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            if (metalMaxRe != null) {
                metalMaxRe.getEditorManager().applyEditors();
            }
        });


        JMenu fileMenuExport = new JMenu("导出");

        JMenuItem fileMenuExportWorldMap = new JMenuItem("导出世界地图");
        fileMenuExportWorldMap.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // selectedDir -> tsx
                //                -> png
                //             -> sprite
                //                -> png
                //             -> world.tmx
                selectedFile.mkdirs();
                final TMXMapWriter tmxMapWriter = new TMXMapWriter();
                Map<Integer, TileSet> collect = WorldMapEditorImpl.DEFAULT_PIECES.values().parallelStream().distinct().map(integer -> {
                            String name = String.format("%08X", integer);
                            File out = new File(selectedFile, "tsx/png/" + name + ".png");
                            if (!out.exists()) {
                                out.getParentFile().mkdirs();
                                try {
                                    if (out.createNewFile()) {
                                        BufferedImage bufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(metalMaxRe, integer));
                                        ImageIO.write(bufferedImage, "PNG", out);
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            File tsxOut = new File(selectedFile, "tsx");
                            tsxOut.mkdirs();
                            try {
                                return Map.entry(integer, TiledMapUtils.createWorldTsx(metalMaxRe, tmxMapWriter, out, integer, tsxOut));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull) // 移除 null
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                String spriteName = String.format("0405%02X%02X", 0x94, 0x95);
                File spritePng = new File(selectedFile, "sprite/png/" + spriteName + ".png");
                try {
                    if (!spritePng.exists()) {
                        spritePng.getParentFile().mkdirs();
                        if (spritePng.createNewFile()) {
                            BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, 0x94));
                            ImageIO.write(spriteBufferedImage, "PNG", spritePng);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                TileSet spriteTileSet = new TileSet();
                spriteTileSet.setName(spriteName);
                try {
                    spriteTileSet.importTileBitmap(spritePng.getAbsolutePath(), new BasicTileCutter(0x10, 0x10, 0, 0));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                org.mapeditor.core.Map world = TiledMapUtils.createWorld(metalMaxRe, WorldMapEditorImpl.DEFAULT_PIECES, collect, spriteTileSet);
                try {
                    File worldTmx = new File(selectedFile, "world.tmx");
                    if (!worldTmx.exists()) {
                        if (!worldTmx.createNewFile()) {
                            return;
                        }
                    }
                    tmxMapWriter.writeMap(world, worldTmx.getAbsolutePath());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.printf("导出世界地图到：%s.\n", selectedFile.getAbsolutePath());
            }
        });

        JMenuItem fileMenuExportMap = new JMenuItem("导出地图");
        fileMenuExportMap.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = fileChooser.showSaveDialog(mapImage);
            if (state == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                String maps = JOptionPane.showInputDialog(mapImage, """
                        导出单个地图：1
                        导出多个地图：1-2
                        导出多个地图：1-2,5-6,9,12
                        地图ID范围： 1-239\
                        """, "导出地图", JOptionPane.QUESTION_MESSAGE);
                if (maps == null || maps.isEmpty()) {
                    return;
                }
                HashSet<Integer> mapIds = new HashSet<>();
                for (String ids : maps.strip().split(",")) {
                    ids = ids.strip();
                    if (ids.contains("-")) {
                        String[] range = ids.split("-");
                        for (int i = Integer.parseInt(range[0]), end = Integer.parseInt(range[1]); i <= end; i++) {
                            mapIds.add(i);
                        }
                    } else {
                        mapIds.add(Integer.parseInt(ids));
                    }
                }

                if (mapIds.isEmpty()) {
                    return;
                }

                // selectedDir -> tsx
                //                -> png
                //             -> sprite
                //                -> png
                //             -> [MapId].tmx
                selectedFile.mkdirs();

                JProgressBar progressBar = new JProgressBar(0, metalMaxRe.getEditorManager().getCount());
                JOptionPane jOptionPane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = jOptionPane.createDialog(mapImage, "正在导出地图");
                progressBar.setMaximum(mapIds.size());
                // 显示导出进度
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(true);
                });

                final TMXMapWriter tmxMapWriter = new TMXMapWriter();
//                        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
                IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);

                for (int mapId : mapIds) {
                    MapProperties mapProperties = mapPropertiesEditor.getMapProperties(mapId);
                    String tileSetName = String.format("%08X-%02X%02X-%04X", mapProperties.getIntTiles(), mapProperties.combinationA, mapProperties.combinationB, (int) mapProperties.palette);

                    try {
                        BufferedImage tileSetBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));
                        File output = new File(selectedFile, String.format("/tsx/png/%s.png", tileSetName));
                        output.getParentFile().mkdirs();
                        ImageIO.write(tileSetBufferedImage, "PNG", output);
                        TileSet tsx = TiledMapUtils.createTsx(metalMaxRe, tmxMapWriter, output, mapProperties, new File(selectedFile, "tsx"));
                        tsx.setName(tileSetName);
                        BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
                        String spriteName = String.format("0405%02X%02X", mapProperties.spriteIndex, (byte) (mapProperties.spriteIndex + 1));
                        output = new File(selectedFile, String.format("/sprite/%s.png", spriteName));
                        output.getParentFile().mkdirs();
                        ImageIO.write(spriteBufferedImage, "PNG", output);

                        TileSet spriteTileSet = new TileSet();
                        spriteTileSet.setName(spriteName);
                        spriteTileSet.importTileBitmap(output.getAbsolutePath(), new BasicTileCutter(0x10, 0x10, 0, 0));

                        tmxMapWriter.writeMap(TiledMapUtils.create(metalMaxRe, mapId, tsx, spriteTileSet), new File(selectedFile, String.format("%02X.tmx", mapId)).getAbsolutePath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        progressBar.setValue(progressBar.getValue() + 1);
                    }
                }
                // 导出完毕，关闭窗口
                SwingUtilities.invokeLater(dialog::dispose);
                System.out.printf("导出多个地图：%s.\n", Arrays.toString(mapIds.toArray()));
            }
        });

        fileMenuExport.add(fileMenuExportWorldMap);
        fileMenuExport.add(fileMenuExportMap);

        JMenu fileMenuImport = new JMenu("导入");
        JMenuItem fileMenuImportWorld = new JMenuItem("导入世界地图");
        fileMenuImportWorld.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TiledMap", "tmx"));
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                int result = 0;
                if (selectedFile.exists()) {
                    result = JOptionPane.showConfirmDialog(this,
                            "导入世界地图会覆盖现有的世界地图数据！", "覆盖数据",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                }
                if (result == JOptionPane.OK_OPTION) {
                    // 导入世界地图
                    try {
                        TMXMapReader tmxMapReader = new TMXMapReader();
                        TiledMapUtils.importWorldMap(multipleMetalMaxRe.current(), tmxMapReader.readMap(selectedFile.getAbsolutePath()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        PrintWriter printWriter = new PrintWriter(out);
                        ex.printStackTrace(printWriter);
                        JOptionPane.showMessageDialog(this, "导入失败\n" + out);
                    }
                    System.out.printf("导入世界地图：%s.\n", selectedFile.getAbsolutePath());
                    worldMapImage = null;
                    mapImages.remove(0x00);
                    if (mapSelectListModel.getSelectedMap() == 0x00) {
                        mapList.removeSelectionInterval(0, 0);
                        mapList.setSelectedIndex(0);
                    }
                }
            }
        });

        JMenuItem fileMenuImportMap = new JMenuItem("导入地图");
        fileMenuImportMap.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("TiledMaps(*.tmx)", "tmx"));
            fileChooser.setMultiSelectionEnabled(true);
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                if (selectedFiles.length == 0) {
                    return;
                }

                List<File> files = Arrays.stream(selectedFiles).filter(File::exists).toList();


                var maps = files.parallelStream().map(value -> {
                            // 移除后缀
                            String name = value.getName();
                            int lastIndexOf = name.lastIndexOf('.');

                            // 将十六进制字符转换为数字
                            int map;
                            if (lastIndexOf == -1) {
                                map = Integer.parseInt(name, 16);
                            } else {
                                map = Integer.parseInt(name.substring(0, lastIndexOf), 16);
                            }
                            return Map.entry(map, value);
                        })
                        .distinct() // 去重
//                        .filter(entry -> entry.getKey() > 0x00 && entry.getKey() < MapEditor.MAP_MAX_COUNT) // 过滤大于最大地图ID的数据
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                int result = JOptionPane.showConfirmDialog(this, String.format("""
                                这些地图的数据将会被覆盖
                                %s
                                """, Arrays.toString(maps.keySet().toArray(new Integer[0]))),
                        "覆盖数据",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    // 已确认覆盖
                    try {
                        TMXMapReader tmxMapReader = new TMXMapReader();
                        for (Map.Entry<Integer, File> entry : maps.entrySet()) {
                            try {
                                TiledMapUtils.importMap(metalMaxRe, entry.getKey(), tmxMapReader.readMap(entry.getValue().getAbsolutePath()));
                            } catch (Exception ex) {
                                System.out.println("导入失败：" + entry.getKey());
                                ex.printStackTrace();
                            }
                        }
                    } catch (JAXBException ex) {
                        ex.printStackTrace();
                    }
                    System.out.printf("导入地图 %s 完成.\n", Arrays.toString(maps.keySet().toArray()));
                }
            }
        });

        fileMenuImport.add(fileMenuImportWorld);
        fileMenuImport.add(fileMenuImportMap);

        // 文件
        fileMenu.add(fileMenuOpen);         // 打开
        fileMenu.add(fileMenuOpenDefault);  // 打开内置ROM    >
        fileMenu.add(fileMenuReopen);       // 重新加载
        fileMenu.addSeparator();            //----------------
        fileMenu.add(fileMenuSaveAs);       // 另存为...
        fileMenu.addSeparator();            //----------------
        fileMenu.add(fileMenuApply);        // 应用
        fileMenu.addSeparator();            //----------------
        fileMenu.add(fileMenuExport);       // 导出           >
        fileMenu.add(fileMenuImport);       // 导入           >

        JMenu editorMenu = new JMenu("编辑器");
        JMenuItem editorMenuEnabledStates = new JMenuItem("启用/禁用编辑器(undone)");
        editorMenuEnabledStates.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            EditorEnabledDialog editorEnabledDialog = new EditorEnabledDialog(this, multipleMetalMaxRe);
            editorEnabledDialog.pack();
            editorEnabledDialog.setVisible(true);
        });

        JMenuItem editorMenuText = new JMenuItem("文本编辑器(bata)");
        editorMenuText.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            TextEditorFrame textEditorFrame = new TextEditorFrame(this, multipleMetalMaxRe.current());
            textEditorFrame.pack();
            textEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuItem = new JMenuItem("物品编辑器(beta)");
        editorMenuItem.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            ItemEditorFrame itemEditorFrame = new ItemEditorFrame(this, multipleMetalMaxRe.current());
            itemEditorFrame.pack();
            itemEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuEntrances = new JMenuItem("出入口编辑器");
        editorMenuEntrances.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MapEntranceEditorFrame mapEntranceEditorFrame;
            if (mapList.isSelectionEmpty()) {
                mapEntranceEditorFrame = new MapEntranceEditorFrame(this, multipleMetalMaxRe.current());
            } else {
                int map = Integer.parseInt(mapList.getSelectedValue(), 16);
                mapEntranceEditorFrame = new MapEntranceEditorFrame(map, this, multipleMetalMaxRe.current());
            }
            mapEntranceEditorFrame.pack();
            mapEntranceEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuDataValue = new JMenuItem("数据值编辑器");
        editorMenuDataValue.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            DataValueEditorFrame dataValueEditorFrame = new DataValueEditorFrame(this, multipleMetalMaxRe.current());
            dataValueEditorFrame.pack();
            dataValueEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuPlayer = new JMenuItem("玩家编辑器");
        editorMenuPlayer.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            PlayerEditorFrame playerEditorFrame = new PlayerEditorFrame(this, multipleMetalMaxRe.current());
            playerEditorFrame.pack();
            playerEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuTank = new JMenuItem("坦克编辑器");
        editorMenuTank.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            TankEditorFrame tankEditorFrame = new TankEditorFrame(this, multipleMetalMaxRe.current());
            tankEditorFrame.pack();
            tankEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuDogSystem = new JMenuItem("犬系统编辑器(beta)");
        editorMenuDogSystem.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            DogSystemEditorFrame dogSystemEditorFrame = new DogSystemEditorFrame(this, multipleMetalMaxRe.current());
            dogSystemEditorFrame.pack();
            dogSystemEditorFrame.setVisible(true);
        });
        JMenuItem editorMenuShop = new JMenuItem("商店编辑器(undone)");
        editorMenuShop.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            ShopEditorFrame shopEditorFrame = new ShopEditorFrame(this, multipleMetalMaxRe.current());
            shopEditorFrame.pack();
            shopEditorFrame.setVisible(true);
        });
        JMenuItem editorMenuTreasure = new JMenuItem("宝藏编辑器(beta)");
        editorMenuTreasure.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            TreasureEditorFrame treasureEditorFrame;
            if (mapList.isSelectionEmpty()) {
                treasureEditorFrame = new TreasureEditorFrame(this, multipleMetalMaxRe.current());
            } else {
                int map = Integer.parseInt(mapList.getSelectedValue(), 16);
                treasureEditorFrame = new TreasureEditorFrame(map, this, multipleMetalMaxRe.current());
            }
            treasureEditorFrame.pack();
            treasureEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuMonster = new JMenuItem("怪物编辑器(undone)");
        editorMenuMonster.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            MonsterModelEditorFrame monsterModelEditorFrame = new MonsterModelEditorFrame(this, multipleMetalMaxRe.current());
            monsterModelEditorFrame.pack();
            monsterModelEditorFrame.setVisible(true);
        });


        // 编辑器
        editorMenu.add(editorMenuEnabledStates);// 启用/禁用编辑器
        editorMenu.add(editorMenuText);         // 文本编辑器
        editorMenu.add(editorMenuItem);         // 物品编辑器
        editorMenu.add(editorMenuEntrances);    // 出入口编辑器
        editorMenu.add(editorMenuDataValue);    // 数据值编辑器
        editorMenu.add(editorMenuPlayer);       // 玩家编辑器
        editorMenu.add(editorMenuTank);         // 坦克编辑器
        editorMenu.add(editorMenuDogSystem);    // 犬系统编辑器
        editorMenu.add(editorMenuShop);         // 商店编辑器
        editorMenu.add(editorMenuTreasure);     // 宝藏编辑器
        editorMenu.add(editorMenuMonster);      // 怪物编辑器


        JMenu paletteMenu = new JMenu("调色板(undone)");
        JMenuItem paletteMenuSystem = new JMenuItem("系统调色板");
        paletteMenu.add(paletteMenuSystem);

        JMenu themeMenu = new JMenu("主题");

        FlatLaf[] flatLafs = {null, new FlatDarculaLaf(), new FlatDarkLaf(), new FlatIntelliJLaf(), new FlatLightLaf()};

        for (FlatLaf flatLaf : flatLafs) {
            JRadioButtonMenuItem theme;
            if (flatLaf == null) {
                theme = new JRadioButtonMenuItem("跟随系统", false);
            } else {
                theme = new JRadioButtonMenuItem(flatLaf.getName(), false);
            }

            themeMenu.add(theme);

            theme.addItemListener(e -> {
                for (Component component : themeMenu.getPopupMenu().getComponents()) {
                    if (theme == component) {
                        // 不设置自己
                        continue;
                    }
                    // 设置未选中
                    if (component instanceof JRadioButtonMenuItem t) {
                        t.setSelected(false);
                    }
                }
                if (flatLaf != null) {
                    FlatLaf.setup(flatLaf);
                } else {
                    // 跟随系统
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                             UnsupportedLookAndFeelException ex) {
                        ex.printStackTrace();
                    }
                }
                SwingUtilities.updateComponentTreeUI(Main.this);
            });
        }

        JMenuItem toolsMenu = new JMenuItem("工具");
        toolsMenu.addActionListener(e -> {
            ToolsFrame toolsFrame = new ToolsFrame(this);
            toolsFrame.pack();
            toolsFrame.setVisible(true);
        });

        JMenu testMenu = new JMenu("实验功能");

        testMenuAdvancedMap = new JCheckBoxMenuItem("使用扩容地图");
        testMenuAdvancedMap.addItemListener(this::itemStateChanged);

        testMenu.add(testMenuAdvancedMap);

        JMenu helpMenu = new JMenu("帮助");
        JMenuItem helpMenuGithub = new JMenuItem("Github");
        helpMenuGithub.setToolTipText("打开开源地址");
        helpMenuGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/AFoolLove/MetalMaxRe"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        JMenuItem helpMenuAbout = new JMenuItem("关于");
        helpMenuAbout.addActionListener(e -> JOptionPane.showMessageDialog(this, """
                这是一个基于java开发的FC游戏 MetalMax ROM编辑器
                Copyright 2022 AFoolLove
                """));
        helpMenu.add(helpMenuGithub);
        helpMenu.add(helpMenuAbout);


        // debug菜单
        JMenu debugMenu = new JMenu("Debug");
        JMenuItem debugMenuBreakpoint = new JMenuItem("Breakpoint");
        debugMenuBreakpoint.addActionListener(e -> Optional.of(Main.this));
        JMenuItem debugMenuGC = new JMenuItem("GC");
        debugMenuGC.addActionListener(e -> System.gc());
        debugMenu.add(debugMenuBreakpoint);
        debugMenu.add(debugMenuGC);

        menuBar.add(fileMenu);
        menuBar.add(editorMenu);
        menuBar.add(paletteMenu);
        menuBar.add(themeMenu);
//        menuBar.add(testMenu);
        menuBar.add(debugMenu);
        menuBar.add(helpMenu);
    }

    private void createLayout() {
//        dynamicWorld.scheduleWithFixedDelay(() -> {
//            if (!multipleMetalMaxRe.hasInstance() || mapSelectListModel.getSelectedMap() != 0x00) {
//                return;
//            }
//        }, 1, 1, TimeUnit.SECONDS); // 1秒一次

        mapSelectListModel = new MapSelectListModel(mapList, multipleMetalMaxRe, mapFilter);
        mapList.setModel(mapSelectListModel);
        mapList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            final var selectMap = mapSelectListModel.getSelectedMap();
            if (selectMap == -1) {
                return;
            }

            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            // 记录地图滚动条位置
            final Rectangle mapImageScrollPoint = ((JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, mapImage)).getViewRect();

//            // 显示正在加载
//            // 不显示图标
//            mapImage.setText("正在加载...");
//            mapImage.setIcon(null);

            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            MapPropertiesEditorImpl mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            IMapEntranceEditor mapEntranceEditor = metalMaxRe.getEditorManager().getEditor(IMapEntranceEditor.class);
            IComputerEditor<Computer> computerEditor = metalMaxRe.getEditorManager().getEditor(IComputerEditor.class);
            IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);

            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectMap);
            MapEntrance mapEntrance = mapEntranceEditor.getMapEntrance(selectMap);

            showLines.setEnabled(selectMap == 0x00);

            if (selectMap == 0x00) {
                mapDes.setText("00");
                // 设置头属性
                mapHeadUnderground.setSelected(mapProperties.isUnderground());
                mapHeadEventTile.setSelected(mapProperties.hasEventTile());
                mapHeadUnknown.setSelected((mapProperties.getHead() & 0B0000_0010) == 0B0000_0010);
                mapHeadDyTile.setSelected(mapProperties.hasDyTile());
                if (mapProperties.hasEventTile()) {
                    eventTilesIndex.setValue(mapProperties.eventTilesIndex & 0xFFFF);
                }
                if (mapProperties.hasDyTile()) {
                    dyDataA.setSelectedItem(String.format("%02X", mapProperties.dyTileSpeed));
                    dyDataB.setSelectedItem(String.format("%02X", mapProperties.dyTile));
                }

                // 设置地图大小
                setMapSize(mapProperties);

                // 边界类型和目的地
                setMapBorder(mapEntrance.getBorder());

                // 固定调色板
                palette.setValue(0x9AD0);

                ImageIcon imageIcon = mapImages.get(selectMap);
                if (imageIcon == null) {
                    if (mapImages.containsKey(selectMap)) {
                        // 存在还是 null，正在加载
                        return;
                    }
                    mapImages.put(selectMap, null); // 占位，表示正在加载
                    imageLoaderService.submit(() -> {
                        if (worldMapImage == null) {
                            // 显示正在加载
                            // 不显示图标
                            mapImage.setText("正在加载...");
                            mapImage.setIcon(null);

                            // 生成世界地图
                            IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);
                            // 复制一份
                            byte[][] map = Arrays.copyOfRange(worldMapEditor.getMap(), 0x00, worldMapEditor.getMap().length);
                            for (int y = 0; y < map.length; y++) {
                                map[y] = Arrays.copyOfRange(map[y], 0x00, map[y].length);
                            }

                            // 连续山脉
                            {
                                // 连续山脉的计数器
                                int continuous = 0;
                                for (int index = 0; index < (0x100 * 0x100); index++) {
                                    int x = index % 0x100;
                                    int y = index / 0x100;

//                                int tile = map[y][x] & 0xFF;

                                    int offset = (y & 1) == 1 ? 0 : 1;

                                    // 在新行中，如果上一行的山脉未结束，立即结束并重置连续山脉计数器
                                    if (x == 0x00) {
                                        if (continuous > 1) {
                                            // 上一行未结束，立即结束山脉
                                            map[(index - 1) / 0x100][(index - 1) % 0x100] = 0x0B;
                                        }
                                        // 重置连续山脉计数器
                                        continuous = 0x00;
                                    }

                                    // 获取当前图块是否为山脉
                                    // 如果山脉刚开始计数，设置山脉起始
                                    // 否则判断山脉是否结束，设置山脉结束
                                    if (map[y][x] == 0x02) {
                                        if (continuous == 0x00) {
                                            // 设置山脉起始
                                            map[y][x] = 0x0A;
                                        } else {
                                            map[y][x] = (byte) (0x02 + ((x + offset) % 2));
                                        }
                                        continuous++;
                                    } else {
                                        if (continuous <= 1) {
                                            continue;
                                        }
                                        // 设置山脉结束
                                        map[(index - 1) / 0x100][(index - 1) % 0x100] = 0x0B;
                                        continuous = 0;
                                    }
                                }
                            }

                            Map<Integer, List<BufferedImage>> tileSetMap = WorldMapEditorImpl.DEFAULT_PIECES.values().parallelStream().distinct().map(integer -> {
                                        BufferedImage bufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(metalMaxRe, integer));
                                        return Map.entry(integer, TileSetHelper.diced(bufferedImage));
                                    })
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                            worldMapImage = new BufferedImage(0x100 * 0x10, 0x100 * 0x10, BufferedImage.TYPE_INT_ARGB);

                            Graphics graphics = worldMapImage.getGraphics();

                            WorldMapEditorImpl.DEFAULT_PIECES.entrySet().parallelStream().forEach(entry -> {
                                Rectangle rectangle = entry.getKey();
                                List<BufferedImage> tileSets = tileSetMap.get(entry.getValue());
                                int x = (int) rectangle.getX();
                                int y = (int) rectangle.getY();
                                int width = (int) rectangle.getWidth();
                                int height = (int) rectangle.getHeight();

                                for (int offsetY = 0; offsetY < height; offsetY++) {
                                    for (int offsetX = 0; offsetX < width; offsetX++) {
                                        int mapX = x + offsetX;
                                        int mapY = y + offsetY;

                                        // 绘制16*16的tile
                                        int tile = map[mapY][mapX] & 0xFF;
                                        graphics.drawImage(tileSets.get(tile), mapX * 0x10, mapY * 0x10, null);
                                    }
                                }
                            });
                            graphics.dispose();
                        }
                        if (mapSelectListModel.getSelectedMap() == 0x00) {
                            BufferedImage bufferedImage = new BufferedImage(worldMapImage.getWidth(), worldMapImage.getHeight(), worldMapImage.getType());
                            Graphics2D graphics = bufferedImage.createGraphics();

                            graphics.drawImage(worldMapImage, 0x00, 0x00, null);
                            // 显示事件图块
                            {
                                IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);
                                IEventTilesEditor eventTilesEditor = metalMaxRe.getEditorManager().getEditor(IEventTilesEditor.class);
                                // 获取世界地图的事件图块
                                Map<Integer, List<EventTile>> worldEventTile = eventTilesEditor.getWorldEventTile();
                                // 生成图块集
                                Map<Rectangle, BufferedImage> tileSetMap = WorldMapEditorImpl.DEFAULT_PIECES.entrySet().parallelStream()
                                        .distinct()
                                        .map(entry -> Map.entry(entry, BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(metalMaxRe, entry.getValue()))))
                                        .collect(Collectors.toMap(t -> t.getKey().getKey(), Map.Entry::getValue));

                                // 将图块集切割为0x10*0x10的小块
                                Map<Rectangle, List<BufferedImage>> tileSetMaps = tileSetMap.entrySet().parallelStream()
                                        .map(tsm -> Map.entry(tsm.getKey(), TileSetHelper.diced(tsm.getValue())))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                Color selected = Color.MAGENTA;
                                Color unselected = new Color(0x9932CD);
                                Collection<List<EventTile>> events = worldEventTile.values();
                                for (Map.Entry<Rectangle, List<BufferedImage>> entry : tileSetMaps.entrySet()) {
                                    for (List<EventTile> event : events) {
                                        for (EventTile tile : event) {
                                            if (entry.getKey().contains(tile.intX() * 0x04, tile.intY() * 0x04)) {
                                                // 框出事件图块
                                                // 根据显示与否设置不同颜色
                                                if (showEvents.isSelected()) {
                                                    BufferedImage event4x4TileImage = new BufferedImage(0x10 * 0x04, 0x10 * 0x04, BufferedImage.TYPE_INT_ARGB);
                                                    Graphics2D eventTileGraphics = event4x4TileImage.createGraphics();
                                                    // 获取动态4*4tiles
                                                    byte[] tiles = worldMapEditor.getIndex(tile.intX(), tile.intY(), tile.intTile());
                                                    for (int i = 0; i < tiles.length; i++) {
                                                        int x = i % 4;
                                                        int y = i / 4;
                                                        int t = tiles[i] & 0xFF;
                                                        if (t == 0x02) {
                                                            // 山脉暂时不绘制，后面需要格式化
                                                            continue;
                                                        }
                                                        BufferedImage eventTileImage = entry.getValue().get(t);
                                                        eventTileGraphics.drawImage(eventTileImage, x * 0x10, y * 0x10, null);
                                                    }
                                                    eventTileGraphics.dispose();

                                                    graphics.drawImage(event4x4TileImage, tile.intX() * 0x10 * 0x04, tile.intY() * 0x10 * 0x04, null);

                                                    if (!showEvents.isIndeterminate()) {
                                                        // 选中为深紫色
                                                        // 未选中为紫色
                                                        graphics.setColor(showEvents.isSelected() ? selected : unselected);
                                                        graphics.drawRect(tile.intX() * 0x10 * 0x04, tile.intY() * 0x10 * 0x04, 0x10 * 0x04 - 1, 0x10 * 0x04 - 1);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // 显示可移动区域
                            boxMovable(graphics, mapProperties);
                            if (showEntrances.isSelected()) {
                                boxEntrances(graphics, mapProperties, mapEntrance.getEntrances());
                            }
                            // 显示精灵
                            if (showSprites.isSelected()) {
                                ISpriteEditor spriteEditor = metalMaxRe.getEditorManager().getEditor(ISpriteEditor.class);
                                SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets = this.spriteTileSets.get((byte) 0x94);
                                if (spriteTileSets == null) {
                                    BufferedImage key = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, 0x94));
                                    List<BufferedImage> value = TileSetHelper.diced(key);
                                    spriteTileSets = SingleMapEntry.create(key, value);
                                    this.spriteTileSets.put(mapProperties.spriteIndex, spriteTileSets);
                                    spriteTileSetImage.setIcon(new ImageIcon(key));
                                }
                                boxSprites(graphics, mapProperties, spriteEditor.getSprites().get(selectMap), spriteTileSets);
                            }
                            // 显示宝藏
                            if (showTreasures.isSelected()) {
                                ITreasureEditor treasureEditor = metalMaxRe.getEditorManager().getEditor(ITreasureEditor.class);
                                boxTreasures(graphics, showTreasures.isIndeterminate(), treasureEditor.getTreasures().stream().filter(treasure -> treasure.intMap() == selectMap).toList(), null);
                            }

                            // 显示坦克，无法加载世界地图
                            if (showTanks.isSelected()) {
                                SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets = this.spriteTileSets.get((byte) 0x94);

                                ITankEditor tankEditor = metalMaxRe.getEditorManager().getEditor(ITankEditor.class);
                                boxTanks(graphics, 0x00, tankEditor.getTankInitAttributes(), spriteTileSets);
                            }

                            // 显示航线
                            if (showLines.isSelected()) {
                                IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);

                                graphics.setColor(Color.GREEN);
                                // 出航路线
                                int lineX = 0x6B * 0x10;
                                int lineY = 0x47 * 0x10;
                                graphics.fillRect(lineX + 0x02, lineY + 6, 0x04, 0x04);
                                List<Map.Entry<LineDirection, Byte>> line = new ArrayList<>(worldMapEditor.getShippingLineOut().getKey());
                                if (!line.isEmpty()) {
                                    LineDirection firstDir = line.get(0).getKey();
                                    switch (firstDir) {
                                        case UP -> {

                                        }
                                        case DOWN -> {
                                        }
                                        case LEFT -> {
                                        }
                                        case RIGHT -> {
                                        }
                                    }
                                }


                                LineDirection last = null; // 上一个位置
                                for (int i = 0; i < line.size(); i++) {
                                    // ↑↓，左边
                                    // ←→，上边
                                    Map.Entry<LineDirection, Byte> lineDirection = line.get(i);
                                    int value = lineDirection.getValue() & 0xFF;
                                    value *= 0x10;

                                    LineDirection nextDir = (i + 1) < line.size() ? line.get(i + 1).getKey() : null;
                                    if (i == 0) {
                                        // 起点，从上半中心开始画
                                        if (nextDir == null) {
                                            // 起点，也是终点
                                            break;
                                        }
//                                        switch (nextDir) {
//                                            case UP -> {
//                                                graphics.fillRect(lineX + 0x02, lineY + 0x08, 0x02, value - 0x08);
//                                                lineY -= value;
//                                            }
//                                            case DOWN -> {
//                                                graphics.fillRect(lineX + 0x02, lineY + 0x08, 0x02, value - 0x08);
//                                                lineY += value;
//                                            }
//                                            case LEFT -> {
//                                                graphics.fillRect(lineX + 0x08, lineY + 0x02, value - 0x08, 0x02);
//                                                lineX -= value;
//                                            }
//                                            case RIGHT -> {
//                                                graphics.fillRect(lineX + 0x08, lineY + 0x02, value - 0x08, 0x02);
//                                                lineX += value;
//                                            }
//                                        }
                                    }
//                                    switch (last == null ? lineDirection.getKey() : last) {
//                                        case UP -> {
//                                            graphics.fillRect(lineX + 0x02, lineY + 0x08, 0x02, value);
//                                            lineY -= value;
//                                        }
//                                        case DOWN -> {
//                                            graphics.fillRect(lineX + 0x02, lineY + 0x08, 0x02, value);
//                                            lineY += value;
//                                        }
//                                        case LEFT -> {
//                                            graphics.fillRect(lineX + 0x08, lineY + 0x02, value, 0x02);
//                                            lineX -= value;
//                                        }
//                                        case RIGHT -> {
//                                            graphics.fillRect(lineX + 0x08, lineY + 0x02, value, 0x02);
//                                            lineX += value;
//                                        }
//                                    }
//                                    last = lineDirection.getKey();

                                }


                                graphics.setColor(Color.RED);
                                // 归航路线
                                lineX = 0x81 * 0x10;
                                lineY = 0x38 * 0x10;
                                graphics.fillRect(lineX + 0x08 + 0x02, lineY + 6, 0x04, 0x04);

                                line = new ArrayList<>(worldMapEditor.getShippingLineBack().getKey());
                                last = null; // 上一个位置
                                for (int i = 0; i < line.size(); i++) {
                                    // ↑↓，左边
                                    // ←→，上边
                                    Map.Entry<LineDirection, Byte> lineDirection = line.get(i);
                                    LineDirection nextDir = (i + 1) < line.size() ? line.get(i + 1).getKey() : null;
                                    if (i == 0) {

                                        // 起点，从上半中心开始画
                                        if (nextDir == null) {
                                            // 起点，也是终点
                                            break;
                                        }
                                    }
                                }

                            }

                            graphics.dispose();
                            ImageIcon icon = new ImageIcon(bufferedImage);
                            mapImages.put(selectMap, icon);

                            mapImage.setText(null);
                            mapImage.setIcon(icon);
                            SwingUtilities.invokeLater(() -> mapImage.scrollRectToVisible(mapImageScrollPoint));
                        }
                    });
                    return;
                }
                mapImage.setText(null);
                mapImage.setIcon(imageIcon);
                SwingUtilities.invokeLater(() -> mapImage.scrollRectToVisible(mapImageScrollPoint));
                return;
            }

            StringBuilder mapPropertiesData = new StringBuilder();

            char[] mapIndexRoll = new char[0x40 + 0xB0];
            mapPropertiesEditor.position(mapPropertiesEditor.getMapPropertiesIndexUpRollAddress());
            for (int i = 0; i < 0x40; i++) {
                mapIndexRoll[i] = mapPropertiesEditor.getBuffer().getChar();
            }
            mapPropertiesEditor.position(mapPropertiesEditor.getMapPropertiesIndexDownRollAddress());
            for (int i = 0; i < 0xB0; i++) {
                mapIndexRoll[0x40 + i] = mapPropertiesEditor.getBuffer().getChar();
            }

            // 获取地图属性绝对地址
            DataAddress mapPropertiesAddress = mapPropertiesEditor.getMapPropertiesAddress();
            mapPropertiesAddress = DataAddress.fromPRG(mapPropertiesAddress.getStartAddress(-0x08500 + mapIndexRoll[selectMap]));
            mapPropertiesData.append(String.format("%04X(0x%05X):", (int) NumberR.toChar(mapIndexRoll[selectMap]), mapPropertiesAddress.getAbsStartAddress(metalMaxRe.getBuffer())));
            mapPropertiesData.append(NumberR.toPlainHexString(mapProperties.toByteArray()));
            mapPropertiesData.append('\n');

            DataAddress mapDataAddress;
            int mapIndex = mapProperties.mapIndex;
            if (mapIndex >= 0xC000) {
                // 0xBB010
                mapDataAddress = DataAddress.fromCHR(0x2F000 + mapIndex);
            } else {
                // 0x00000(610)
                mapIndex = (((mapIndex & 0xE000) >> ((8 * 2) - 3)) * 0x2000) + (mapIndex & 0x1FFF);
                mapDataAddress = DataAddress.fromCHR(mapIndex);
            }
            mapPropertiesData.append(String.format("%04X(0x%05X):", (int) NumberR.toChar(mapProperties.mapIndex), mapDataAddress.getAbsStartAddress(mapEditor.getBuffer())));

            byte[] build = mapEditor.getMap(selectMap).build();
            mapPropertiesData.append(NumberR.toPlainHexString(build));

            this.mapDes.setText(String.format("""
                    %02X (%d bytes)
                    %s\
                    """, selectMap, build.length, mapPropertiesData));

            // 设置隐藏图块和填充图块
            mapHideTileValue.setValue(mapProperties.intHideTile());
            mapFillTileValue.setValue(mapProperties.intFillTile());

            // 设置头属性
            mapHeadUnderground.setSelected(mapProperties.isUnderground());
            mapHeadEventTile.setSelected(mapProperties.hasEventTile());
            mapHeadUnknown.setSelected((mapProperties.getHead() & 0B0000_0010) == 0B0000_0010);
            mapHeadDyTile.setSelected(mapProperties.hasDyTile());
            if (mapProperties.hasEventTile()) {
                eventTilesIndex.setValue(mapProperties.eventTilesIndex & 0xFFFF);

                // 自定义
                sharedEventTilesIndex.setSelectedIndex(0);
                for (int i = 0; i < selectMap; i++) {
                    if (mapPropertiesEditor.getMapProperties(i).eventTilesIndex == mapProperties.eventTilesIndex) {
                        // 共享
                        sharedEventTilesIndex.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (mapProperties.hasDyTile()) {
                dyDataA.setSelectedItem(String.format("%02X", mapProperties.dyTileSpeed));
                dyDataB.setSelectedItem(String.format("%02X", mapProperties.dyTile));
            }

            // 设置地图大小
            setMapSize(mapProperties);

            setMapBorder(mapEntrance.getBorder());

            // 地图图块集
            X00.setValue(mapProperties.tilesIndexA);
            X40.setValue(mapProperties.tilesIndexB);
            X80.setValue(mapProperties.tilesIndexC);
            XC0.setValue(mapProperties.tilesIndexD);
            // 设置图块集组合
            combinationA.setValue(mapProperties.combinationA);
            combinationB.setValue(mapProperties.combinationB);
            // 设置调色板索引
            palette.setValue(mapProperties.palette & 0xFFFF);

            // 地图精灵图块集
            S80.setValue(mapProperties.spriteIndex);
            SC0.setValue(mapProperties.spriteIndex + 1);

            // 设置其它

            // 设置音乐
            mapMusic.setValue(mapProperties.intMusic());
            // 设置该地图的怪物组合
            if (selectMap >= 0x80) {
                monsterGroupIndex.setEnabled(true);
                monsterGroupIndex.setValue(mapProperties.monsterGroupIndex);
            } else {
                monsterGroupIndex.setEnabled(false);
            }
            // 设置地图数据索引
            mapDataIndex.setValue(mapProperties.mapIndex & 0xFFFF);
            // 设置地图属性索引
            mapPropertiesIndex.setValue(mapIndexRoll[selectMap] & 0xFFFF);


            final long mapTileSetKey = ((long) mapProperties.getIntTiles() << 16) + mapProperties.palette;
            final byte spriteTileSetKey = mapProperties.spriteIndex;

            SingleMapEntry<BufferedImage, List<BufferedImage>> tmpTileSets = mapTileSets.get(mapTileSetKey);

            if (tmpTileSets != null) {
                // 更新隐藏图块和填充图块
                mapHideTile.setIcon(new ImageIcon(tmpTileSets.getValue().get(mapProperties.intHideTile())));
                mapFillTile.setIcon(new ImageIcon(tmpTileSets.getValue().get(mapProperties.intFillTile())));
                // 更新地图图块集
                mapTileSetImage.setIcon(new ImageIcon(tmpTileSets.getKey()));
            }
            // 更新精灵图块集
            tmpTileSets = spriteTileSets.get(spriteTileSetKey);
            if (tmpTileSets != null) {
                spriteTileSetImage.setIcon(new ImageIcon(tmpTileSets.getKey()));
            }

            // 更新地图图片
            ImageIcon imageIcon = mapImages.get(selectMap);
            if (imageIcon == null) {
                if (mapImages.containsKey(selectMap)) {
                    // 存在还是 null，正在加载
                    return;
                }
                mapImages.put(selectMap, null); // 占位，表示正在加载
                imageLoaderService.submit(() -> {
                    // 显示正在加载
                    // 不显示图标
                    mapImage.setText("正在加载...");
                    mapImage.setIcon(null);
                    MapBuilder mapBuilder = mapEditor.getMap(selectMap);

                    int width = mapProperties.intWidth();
                    int height = mapProperties.intHeight();

                    byte[][] map = new byte[height][width];

                    int index = 0;
                    for (MapTile mapTile : mapBuilder) {
                        // 获取tile
                        for (int i = 0, count = mapTile.getCount(); i < count; i++, index++) {
                            if (index >= (width * height)) {
                                // 超出地图
                                break;
                            }
                            // 设置tile
                            map[index / width][index % width] = mapTile.getTile();
                        }
                    }

                    SingleMapEntry<BufferedImage, List<BufferedImage>> mapTileSets = this.mapTileSets.get(mapTileSetKey);
                    if (mapTileSets == null) {
                        BufferedImage key = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));
                        List<BufferedImage> value = TileSetHelper.diced(key);
                        mapTileSets = SingleMapEntry.create(key, value);
                        this.mapTileSets.put(mapTileSetKey, mapTileSets);
                        mapTileSetImage.setIcon(new ImageIcon(key));
                    }

                    BufferedImage bufferedImage = new BufferedImage(width * 0x10, height * 0x10, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = bufferedImage.createGraphics();

                    // mapX和mapY的单位是tile，1tile=16*16像素
                    for (int mapY = 0; mapY < height; mapY++) {
                        for (int mapX = 0; mapX < width; mapX++) {
                            int tile = map[mapY][mapX] & 0x7F;
                            BufferedImage tileImage = mapTileSets.getValue().get(tile);
                            // 绘制tile
                            graphics.drawImage(tileImage, mapX * 0x10, mapY * 0x10, null);
                        }
                    }

                    // 显示动态图块
                    if (mapProperties.hasEventTile()) {
                        IEventTilesEditor eventTilesEditor = metalMaxRe.getEditorManager().getEditor(IEventTilesEditor.class);

                        Color selected = Color.MAGENTA;
                        // 小地图事件图块未选中时透明一些，否则看着很密集
                        Color unselected = new Color(0x809932CD, true);
                        for (Map.Entry<Integer, List<EventTile>> entry : eventTilesEditor.getEventTile(selectMap).entrySet()) {
                            for (EventTile eventTile : entry.getValue()) {
                                if (showEvents.isSelected()) {
                                    BufferedImage eventTileImage = mapTileSets.getValue().get(eventTile.intTile());
                                    graphics.drawImage(eventTileImage, eventTile.intX() * 0x10, eventTile.intY() * 0x10, null);

                                    if (!showEvents.isIndeterminate()) {
                                        // 选中为深紫色
                                        // 未选中为紫色
                                        graphics.setColor(showEvents.isSelected() ? selected : unselected);
                                        graphics.drawRect(eventTile.intX() * 0x10, eventTile.intY() * 0x10, 0x10 - 1, 0x10 - 1);
                                    }
                                }
                            }
                        }
                    }

                    // 显示宝藏
                    if (showTreasures.isSelected()) {
                        ITreasureEditor treasureEditor = metalMaxRe.getEditorManager().getEditor(ITreasureEditor.class);
                        boxTreasures(graphics, !showTreasures.isIndeterminate(), treasureEditor.getTreasures().stream().filter(treasure -> treasure.intMap() == selectMap).toList(), mapTileSets);
                    }

                    // 显示精灵
                    if (showSprites.isSelected()) {
                        ISpriteEditor spriteEditor = metalMaxRe.getEditorManager().getEditor(ISpriteEditor.class);

                        SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets = this.spriteTileSets.get(mapProperties.spriteIndex);
                        if (spriteTileSets == null) {
                            BufferedImage key = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
                            List<BufferedImage> value = TileSetHelper.diced(key);
                            spriteTileSets = SingleMapEntry.create(key, value);
                            this.spriteTileSets.put(mapProperties.spriteIndex, spriteTileSets);
                            spriteTileSetImage.setIcon(new ImageIcon(key));
                        }
                        boxSprites(graphics, mapProperties, spriteEditor.getSprites().get(selectMap), spriteTileSets);
                    }

                    // 显示坦克
                    if (showTanks.isSelected()) {
                        SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets = this.spriteTileSets.values().stream().findAny().get();

                        ITankEditor tankEditor = metalMaxRe.getEditorManager().getEditor(ITankEditor.class);
                        boxTanks(graphics, selectMap, tankEditor.getTankInitAttributes(), spriteTileSets);
                    }

                    // 显示可移动区域
                    boxMovable(graphics, mapProperties);
                    // 显示入口
                    if (showEntrances.isSelected()) {
                        boxEntrances(graphics, mapProperties, mapEntrance.getEntrances());
                    }
                    // 显示计算机
                    if (showComputers.isSelected()) {
                        boxComputer(graphics, mapProperties, computerEditor.getComputers().stream().filter(computer -> computer.intMap() == selectMap).toList());
                    }

                    graphics.dispose();

                    // 填充图块
                    BufferedImage fillTile = mapTileSets.getValue().get(mapProperties.intFillTile());
                    // 隐藏图块
                    BufferedImage hideTile = mapTileSets.getValue().get(mapProperties.intHideTile());

                    ImageIcon icon;
                    if (showFillTile.isSelected()) {
                        // 将地图左右和上下分别增加3tile作为填充tile展示
                        BufferedImage finalBufferedImage = new BufferedImage(bufferedImage.getWidth() + (6 * 0x10), bufferedImage.getHeight() + (6 * 0x10), BufferedImage.TYPE_INT_ARGB);
                        graphics = finalBufferedImage.createGraphics();
                        for (int fillTileX = 0; fillTileX < (3 + width + 3); fillTileX++) {
                            for (int fillTileY = 0; fillTileY < (3 + height + 3); fillTileY++) {
                                if (fillTileX >= 3 && fillTileX < (width + 3)
                                    && fillTileY >= 3 && fillTileY < (height + 3)) {
                                    continue;
                                }
                                graphics.drawImage(fillTile, fillTileX * 0x10, fillTileY * 0x10, 0x10, 0x10, null);
                            }
                        }

                        // 绘制实际地图
                        graphics.drawImage(bufferedImage, 3 * 0x10, 3 * 0x10, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                        // 用红线框出实际地图
                        graphics.setColor(Color.RED);
                        graphics.drawRect((3 * 0x10) - 1, (3 * 0x10) - 1, bufferedImage.getWidth() + 1, bufferedImage.getHeight() + 1);
                        graphics.dispose();
                        icon = new ImageIcon(finalBufferedImage);
                    } else {
                        icon = new ImageIcon(bufferedImage);
                    }

                    mapImages.put(selectMap, icon);

                    if (mapSelectListModel.getSelectedMap() == selectMap) {
                        mapImage.setText(null);
                        // 如果还是选择了这个地图，更新图片
                        mapImage.setIcon(icon);
                        SwingUtilities.invokeLater(() -> mapImage.scrollRectToVisible(mapImageScrollPoint));
                        // 设置填充图块到按钮
                        mapFillTile.setIcon(new ImageIcon(fillTile));
                        // 设置隐藏图块到按钮
                        mapHideTile.setIcon(new ImageIcon(hideTile));
                    }
                });
                return; // 还在加载
            }
            mapImage.setText(null);
            mapImage.setIcon(imageIcon);
            SwingUtilities.invokeLater(() -> mapImage.scrollRectToVisible(mapImageScrollPoint));
        });
        mapList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.isControlDown()) {
                    if (e.getKeyCode() == KeyEvent.VK_F) {
                        // Ctrl + F
                        // 焦点设置到搜索里
                        mapFilter.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_G) {
                        // Ctrl + G
                        // 打开窗口跳转
                        String map = JOptionPane.showInputDialog(Main.this,
                                null, "地图跳转",
                                JOptionPane.INFORMATION_MESSAGE,
                                null, null, "FF").toString();
                        mapList.setSelectedValue(map, true);
                    }
                }
            }
        });

        mapHideTileValue.addChangeListener(e -> {
            if (!multipleMetalMaxRe.hasInstance() || mapList.isSelectionEmpty()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            int selectedMap = mapSelectListModel.getSelectedMap();
            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectedMap);
            long mapTileSetKey = ((long) mapProperties.getIntTiles() << 16) + mapProperties.palette;
            SingleMapEntry<BufferedImage, List<BufferedImage>> entry = mapTileSets.get(mapTileSetKey);
            if (entry != null) {
                mapHideTile.setIcon(new ImageIcon(entry.getValue().get(((int) mapHideTileValue.getValue()) & 0x7F)));
            }
        });
        mapFillTileValue.addChangeListener(e -> {
            if (!multipleMetalMaxRe.hasInstance() || mapList.isSelectionEmpty()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            int selectedMap = mapSelectListModel.getSelectedMap();
            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectedMap);
            long mapTileSetKey = ((long) mapProperties.getIntTiles() << 16) + mapProperties.palette;
            SingleMapEntry<BufferedImage, List<BufferedImage>> entry = mapTileSets.get(mapTileSetKey);
            if (entry != null) {
                mapFillTile.setIcon(new ImageIcon(entry.getValue().get(((int) mapFillTileValue.getValue()) & 0x7F)));
            }

        });

        gotoPoint.addActionListener(e -> {
            // Ctrl + G
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, mapImage);
            if (viewPort != null) {
                int deltaX = ((Number) mapImageSelectedPointX.getValue()).intValue() * 0x10;
                int deltaY = ((Number) mapImageSelectedPointY.getValue()).intValue() * 0x10;

                Rectangle view = viewPort.getViewRect();
                // 居中
                view.x = Math.min(deltaY, deltaX - (view.width / 2));
                view.y = Math.min(deltaY, deltaY - (view.height / 2));

                mapImage.scrollRectToVisible(view);
            }
        });
        MiddleMoveMouseAdapter middleMoveMouseAdapter = new MiddleMoveMouseAdapter(mapImage);
        mapImage.addMouseListener(middleMoveMouseAdapter);
        mapImage.addMouseMotionListener(middleMoveMouseAdapter);
        mapImage.addMouseListener(new MouseAdapter() {
            private int selectedX;
            private int selectedY;

            private Integer lastSelectedMap;
            private Integer lastSelectedX;
            private Integer lastSelectedY;
            private final BoxSelectedAdapter.SelectListener selectListener = new BoxSelectedAdapter.SelectListener() {
                @Override
                public void select(int x, int y) {
                    selectedX = x;
                    selectedY = y;
                }
            };

            {
                getSelectListeners().add(selectListener);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 4) {
                    // 后退键
                    if (lastSelectedMap != null && lastSelectedX != null && lastSelectedY != null) {
                        gotoPoint(lastSelectedMap, lastSelectedX, lastSelectedY);
                    }
                    return;
                }

                if (e.isControlDown() && e.getButton() == MouseEvent.BUTTON1) {
                    // Ctrl + 左键
                    IMapEntranceEditor treasureEditor = multipleMetalMaxRe.current().getEditorManager().getEditor(IMapEntranceEditor.class);
                    int selectedMap = mapSelectListModel.getSelectedMap();
                    MapEntrance mapEntrance = treasureEditor.getMapEntrance(selectedMap);

                    int x = selectedX;
                    int y = selectedY;

                    for (Map.Entry<MapPoint, MapPoint> pointEntry : mapEntrance.getEntrances().entrySet()) {
                        MapPoint key = pointEntry.getKey();
                        if (key.intX() == x && key.intY() == y) {
                            // 记录跳转前的坐标
                            lastSelectedMap = mapSelectListModel.getSelectedMap();
                            lastSelectedX = x;
                            lastSelectedY = y;

                            // 跳转
                            MapPoint value = pointEntry.getValue();
                            gotoPoint(value.intMap(), value.intX(), value.intY());
                            return;
                        }
                    }

                }
            }
        });

        selectListeners.add(new BoxSelectedAdapter.SelectListener() {
            @Override
            public void select(int x, int y) {
                int width = mapImage.getIcon().getIconWidth() / 0x10;
                int height = mapImage.getIcon().getIconHeight() / 0x10;
                if (showFillTile.isSelected()) {
                    // 世界地图没有填充块
                    if (mapSelectListModel.getSelectedMap() == 0x00) {
                        // 世界地图，根据鼠标位置右边显示不同的tiles
                        for (Map.Entry<Rectangle, Integer> entry : WorldMapEditorImpl.DEFAULT_PIECES.entrySet()) {
                            if (entry.getKey().contains(x, y)) {
                                X00.setValue(NumberR.at(entry.getValue(), 3) & 0xFF);
                                X40.setValue(NumberR.at(entry.getValue(), 2) & 0xFF);
                                X80.setValue(NumberR.at(entry.getValue(), 1) & 0xFF);
                                XC0.setValue(NumberR.at(entry.getValue(), 0) & 0xFF);

                                final long mapTileSetKey = ((long) entry.getValue() << 16) + 0x9AD0;
                                SingleMapEntry<BufferedImage, List<BufferedImage>> worldTileSets = mapTileSets.get(mapTileSetKey);
                                if (worldTileSets == null) {
                                    BufferedImage worldTileSet = BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(multipleMetalMaxRe.current(), entry.getValue()));
                                    List<BufferedImage> value = TileSetHelper.diced(worldTileSet);
                                    mapTileSets.put(mapTileSetKey, SingleMapEntry.create(worldTileSet, value));
                                    mapTileSetImage.setIcon(new ImageIcon(worldTileSet));
                                } else {
                                    mapTileSetImage.setIcon(new ImageIcon(worldTileSets.getKey()));
                                }
                                break;
                            }
                        }
                    }
                }
                mapImageSelectedPointX.setValue(x);
                mapImageSelectedPointY.setValue(y);
            }
        });
        BoxSelectedAdapter mapSelectedTileBox = new BoxSelectedAdapter(mapImage);
        mapSelectedTileBox.addListener(new BoxSelectedAdapter.SelectListener() {
            @Override
            public void selected(int x, int y) {
                if (mapSelectListModel.getSelectedMap() != 0x00 && showFillTile.isSelected()) {
                    x -= 0x03;
                    y -= 0x03;
                }
                for (BoxSelectedAdapter.SelectListener selectListener : new ArrayList<>(selectListeners)) {
                    selectListener.selected(x, y);
                }
            }

            @Override
            public void select(int x, int y) {
                if (mapSelectListModel.getSelectedMap() != 0x00 && showFillTile.isSelected()) {
                    x -= 0x03;
                    y -= 0x03;
                }
                for (BoxSelectedAdapter.SelectListener selectListener : new ArrayList<>(selectListeners)) {
                    selectListener.select(x, y);
                }
            }
        });
        mapImage.addMouseListener(mapSelectedTileBox);
        mapImage.addMouseMotionListener(mapSelectedTileBox);
        mapImageScrollPane.addMouseWheelListener(mapSelectedTileBox);
        mapImage.setDropTarget(new DropTarget(mapImage, new DropTargetAdapter() {
            private File targetFile;

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
//                if (mapSelectListModel.getSelectedMap() <= 0x00) {
//                    // 拒绝世界地图
//                    dtde.rejectDrag();
//                    return;
//                }
                // 每次只接收一个
                if (dtde.getCurrentDataFlavors().length == 1) {
                    DataFlavor currentDataFlavor = dtde.getCurrentDataFlavors()[0];
                    if (currentDataFlavor.equals(DataFlavor.javaFileListFlavor)) {
                        try {
                            List<File> transferData = (List<File>) dtde.getTransferable().getTransferData(currentDataFlavor);
                            targetFile = transferData.get(0);
                            if (targetFile.getName().endsWith(".tmx")) {
                                System.out.println("接收文件:" + targetFile.getPath());
                                return;
                            }
                        } catch (UnsupportedFlavorException | IOException e) {
                            e.printStackTrace();
                        }
                    } else if (currentDataFlavor.equals(DataFlavor.stringFlavor)) {
                        System.out.println("接收文本");
                        return;
                    }
                }
                // 不接受
                dtde.rejectDrag();
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                targetFile = null;
                System.out.println("取消接收");
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (targetFile == null) {
                    return;
                }
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                if (!multipleMetalMaxRe.hasInstance()) {
                    return;
                }
                MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
                int selectMap = mapSelectListModel.getSelectedMap();

                if (selectMap == 0x00) {
                    // 导入世界地图
                    int option = JOptionPane.showConfirmDialog(null, "请确认这是世界地图！\n导入世界地图会覆盖现有的世界地图数据！", "导入确认", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // 已确认
                        try {
                            TMXMapReader tmxMapReader = new TMXMapReader();
                            TiledMapUtils.importWorldMap(multipleMetalMaxRe.current(), tmxMapReader.readMap(targetFile.getAbsolutePath()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            PrintWriter printWriter = new PrintWriter(out);
                            ex.printStackTrace(printWriter);
                            JOptionPane.showMessageDialog(null, String.format("导入世界地图失败。\n%s", out));
                        }
                        System.out.printf("导入世界地图：%s.\n", targetFile);
                        worldMapImage = null;
                        mapImages.remove(0x00);
                        mapList.removeSelectionInterval(0, 0);
                        mapList.setSelectedIndex(0);
                    }
                    return;
                }

                try {
                    TMXMapReader tmxMapReader = new TMXMapReader();
                    org.mapeditor.core.Map map = tmxMapReader.readMap(targetFile.toString());
                    TiledMapUtils.importMap(metalMaxRe, selectMap, map);

                    if (mapImages.containsKey(selectMap) && mapImages.get(selectMap) != null) {
                        mapImages.remove(selectMap);
                        // 重新选中一次，加载图片
                        mapList.removeSelectionInterval(selectMap, selectMap);
                        mapList.setSelectedIndex(selectMap);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintWriter printWriter = new PrintWriter(out);
                    ex.printStackTrace(printWriter);
                    JOptionPane.showMessageDialog(null, String.format("导入地图%02X失败。\n%s", selectMap, out));
                }
            }
        }));

        // 地图右键菜单
        mapImage.addMouseListener(new MapImagePopupMenuMouseAdapter(mapImage, mapList, multipleMetalMaxRe, mapSelectListModel, mapImages));

        ItemListener showItemListener = e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            int selectedIndex = mapList.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }
            mapImages.clear();
            mapList.removeSelectionInterval(selectedIndex, selectedIndex);
            mapList.setSelectedIndex(selectedIndex);
        };
        // 图片刷新按钮
        mapImageRefresh.addActionListener(e -> showItemListener.itemStateChanged(null));
        // 显示选项
        showSprites.addItemListener(showItemListener);
        showFillTile.addItemListener(showItemListener);
        showEntrances.addItemListener(showItemListener);
        showComputers.addItemListener(showItemListener);
        showEvents.addItemListener(showItemListener);
        showTreasures.addItemListener(showItemListener);
        showTanks.addItemListener(showItemListener);
        showLines.addItemListener(showItemListener);

        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                mapImageSelectedPointX, mapImageSelectedPointY,
                mapHideTileValue, mapFillTileValue,
                mapWidth, mapHeight, mapMovableWidthOffset, mapMovableHeightOffset, mapMovableWidth, mapMovableHeight,
                borderLocation00, borderLocation01, borderLocation02,
                borderLocation10, borderLocation11, borderLocation12,
                borderLocation20, borderLocation21, borderLocation22,
                borderLocation30, borderLocation31, borderLocation32,
                X00, X40, X80, XC0,
                S00, S40, S80, SC0,
                combinationA, combinationB,
                mapMusic, monsterGroupIndex,

                outLineTargetMap, outLineTargetX, outLineTargetY,
                backLineTargetMap, backLineTargetX, backLineTargetY
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
        // 对JSpinner添加两个十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                eventTilesIndex,
                palette, mapDataIndex, mapPropertiesIndex
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner, TwoHexFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }

        setMapPropertiesPane();
        setLinePane();
    }

    /**
     * 初始化右边的地图属性数据
     */
    private void setMapPropertiesPane() {
        // 固定值
        S00.setValue(0x04);
        S40.setValue(0x06);

        collapsedMapTileSetImage.addActionListener(e -> {
            mapTileSetImagePane.setCollapsed(!mapTileSetImagePane.isCollapsed());
            collapsedMapTileSetImage.setText(mapTileSetImagePane.isCollapsed() ? "预览" : "隐藏");
        });
        collapsedSpriteTileSetImage.addActionListener(e -> {
            spriteTileSetImagePane.setCollapsed(!spriteTileSetImagePane.isCollapsed());
            collapsedSpriteTileSetImage.setText(spriteTileSetImagePane.isCollapsed() ? "预览" : "隐藏");
        });

        // 地图预览的滚动条，滚轮向下滑动一次的距离变大
        mapImageScrollPane.getVerticalScrollBar().setUnitIncrement(12);
        mapImageScrollPane.getHorizontalScrollBar().setUnitIncrement(12);
        // 地图属性的滚动条，滚轮向下滑动一次的距离变大
        mapPropertiesScrollPane.getVerticalScrollBar().setUnitIncrement(12);

        // 头属性 事件图块 和 动态图块
        mapHeadEventTile.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                eventTilesIndex.setEnabled(true);
                sharedEventTilesIndex.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                eventTilesIndex.setEnabled(false);
                sharedEventTilesIndex.setEnabled(false);
            }
        });

        // 共享事件
        sharedEventTilesIndex.addItemListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);

            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (sharedEventTilesIndex.getSelectedIndex() == 0x00) {
                    // 读取读取
                    eventTilesIndex.setValue(mapPropertiesEditor.getMapProperties(mapSelectListModel.getSelectedMap()).eventTilesIndex & 0xFFFF);
                } else {
                    eventTilesIndex.setValue(mapPropertiesEditor.getMapProperties(sharedEventTilesIndex.getSelectedIndex()).eventTilesIndex & 0xFFFF);
                }
            }
        });
        mapHeadDyTile.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                dyDataA.setEnabled(true);
                dyDataB.setEnabled(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                dyDataA.setEnabled(false);
                dyDataB.setEnabled(false);
            }
        });

        dyDataA.setModel(new DefaultComboBoxModel<>(new String[]{"FF", "00", "01"}));
        dyDataA.setSelectedIndex(0x01);
        dyDataB.setModel(new DefaultComboBoxModel<>(new String[]{"00", "01"}));
        dyDataB.setSelectedIndex(0x00);


        borderTypeLast.addItemListener(e -> {
            if (borderTypeLast.isSelected()) {
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation00, borderLocation01, borderLocation02,
                        borderLocation10, borderLocation11, borderLocation12,
                        borderLocation20, borderLocation21, borderLocation22,
                        borderLocation30, borderLocation31, borderLocation32
                }) {
                    spinner.setEnabled(false);
                }
            }
        });
        borderTypeFixed.addItemListener(e -> {
            if (borderTypeFixed.isSelected()) {
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation00, borderLocation01, borderLocation02,
                }) {
                    spinner.setEnabled(true);
                }
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation10, borderLocation11, borderLocation12,
                        borderLocation20, borderLocation21, borderLocation22,
                        borderLocation30, borderLocation31, borderLocation32
                }) {
                    spinner.setEnabled(false);
                }
            }
        });
        borderTypeDirection.addItemListener(e -> {
            if (borderTypeDirection.isSelected()) {
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation00, borderLocation01, borderLocation02,
                        borderLocation10, borderLocation11, borderLocation12,
                        borderLocation20, borderLocation21, borderLocation22,
                        borderLocation30, borderLocation31, borderLocation32
                }) {
                    spinner.setEnabled(true);
                }
            }
        });

        BoxSelectedAdapter mapTileSetIndexAdapter = new BoxSelectedAdapter(mapTileSetImage);
        mapTileSetIndexAdapter.addListener(new BoxSelectedAdapter.SelectListener() {
            @Override
            public void select(int x, int y) {
                mapTileSetIndex.setText(String.format("%02X", y * 0x10 + x));
            }
        });
        mapTileSetImage.addMouseListener(mapTileSetIndexAdapter);
        mapTileSetImage.addMouseMotionListener(mapTileSetIndexAdapter);
        mapPropertiesScrollPane.addMouseWheelListener(mapTileSetIndexAdapter);

        BoxSelectedAdapter spriteTileSetIndexAdapter = new BoxSelectedAdapter(spriteTileSetImage);
        spriteTileSetIndexAdapter.addListener(new BoxSelectedAdapter.SelectListener() {
            @Override
            public void select(int x, int y) {
                spriteTileSetIndex.setText(String.format("%02X", y * 0x10 + x));
            }
        });
        spriteTileSetImage.addMouseListener(spriteTileSetIndexAdapter);
        spriteTileSetImage.addMouseMotionListener(spriteTileSetIndexAdapter);
        mapPropertiesScrollPane.addMouseWheelListener(spriteTileSetIndexAdapter);

        // 应用修改
        applyProperties.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            int selectMap = mapSelectListModel.getSelectedMap();
            if (selectMap <= 0) {
                return;
            }

            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            IMapEntranceEditor entranceEditor = metalMaxRe.getEditorManager().getEditor(IMapEntranceEditor.class);
            IEventTilesEditor eventTilesEditor = metalMaxRe.getEditorManager().getEditor(IEventTilesEditor.class);

            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectMap);
            MapEntrance mapEntrance = entranceEditor.getMapEntrance(selectMap);

            // 获取隐藏和填充图块
            mapProperties.setHideTile(((Number) mapHideTileValue.getValue()).intValue());
            mapProperties.setFillTile(((Number) mapFillTileValue.getValue()).intValue());

            // 获取头属性，并去掉需要修改的4个flag
            byte head = (byte) (mapProperties.getHead() & 0xF0);

            // 设置头属性
            if (mapHeadUnderground.isSelected()) {
                head |= MapProperties.FLAG_UNDERGROUND;
            }
            if (mapHeadEventTile.isSelected()) {
                head |= MapProperties.FLAG_EVENT_TILE;
                mapProperties.eventTilesIndex = ((char) (((Number) eventTilesIndex.getValue()).intValue() & 0xFFFF));

                // 判断其它地图属性使用使用了该索引
                // 如果索引一致，将其它地图的数据复制过来
                boolean has = false;
                for (Map.Entry<Integer, MapProperties> entry : mapPropertiesEditor.getMapProperties().entrySet()) {
                    if (entry.getValue().eventTilesIndex == mapProperties.eventTilesIndex) {
                        has = true;
                        Map<Integer, List<EventTile>> eventTile = eventTilesEditor.getEventTile(entry.getKey());
                        eventTilesEditor.getEventTiles().put(selectMap, eventTile);
                        System.out.printf("将地图%02X的事件数据复制到地图%02X\n", entry.getKey(), selectMap);
                        break;
                    }
                }

                if (!has) {
                    // 从数据里重新读取
                }
            }
            if (mapHeadUnknown.isSelected()) {
                head |= 0B0000_0010;
            }
            if (mapHeadDyTile.isSelected()) {
                head |= MapProperties.FLAG_DY_TILE;
                int dyDataA = Integer.parseInt(this.dyDataA.getSelectedItem().toString(), 16);
                int dyDataB = Integer.parseInt(this.dyDataB.getSelectedItem().toString(), 16);
                mapProperties.dyTileSpeed = (byte) (dyDataA & 0xFF);
                mapProperties.dyTile = (byte) (dyDataB & 0xFF);
            }
            mapProperties.setHead(head);

            // 设置地图大小
            mapProperties.setWidth(((Number) mapWidth.getValue()).intValue());
            mapProperties.setHeight(((Number) mapHeight.getValue()).intValue());
            mapProperties.movableWidthOffset = (byte) (((Number) mapMovableWidthOffset.getValue()).intValue() & 0xFF);
            mapProperties.movableHeightOffset = (byte) (((Number) mapMovableHeightOffset.getValue()).intValue() & 0xFF);
            mapProperties.movableWidth = (byte) (((Number) mapMovableWidth.getValue()).intValue() & 0xFF);
            mapProperties.movableHeight = (byte) (((Number) mapMovableHeight.getValue()).intValue() & 0xFF);

            // 获取边界类型和目的地
            MapBorder border = mapEntrance.getBorder();
            if (borderTypeLast.isSelected()) {
                border.setType(MapBorderType.LAST);
                border.clear();
            } else if (borderTypeFixed.isSelected()) {
                border.setType(MapBorderType.FIXED);
                border.clear();
                int map = ((Number) borderLocation00.getValue()).intValue();
                int x = ((Number) borderLocation01.getValue()).intValue();
                int y = ((Number) borderLocation02.getValue()).intValue();
                border.add(new MapPoint(map, x, y));
            } else if (borderTypeDirection.isSelected()) {
                border.setType(MapBorderType.DIRECTION);
                border.clear();
                int map, x, y;

                map = ((Number) borderLocation00.getValue()).intValue();
                x = ((Number) borderLocation01.getValue()).intValue();
                y = ((Number) borderLocation02.getValue()).intValue();
                border.add(new MapPoint(map, x, y));

                map = ((Number) borderLocation10.getValue()).intValue();
                x = ((Number) borderLocation11.getValue()).intValue();
                y = ((Number) borderLocation12.getValue()).intValue();
                border.add(new MapPoint(map, x, y));

                map = ((Number) borderLocation20.getValue()).intValue();
                x = ((Number) borderLocation21.getValue()).intValue();
                y = ((Number) borderLocation22.getValue()).intValue();
                border.add(new MapPoint(map, x, y));

                map = ((Number) borderLocation30.getValue()).intValue();
                x = ((Number) borderLocation31.getValue()).intValue();
                y = ((Number) borderLocation32.getValue()).intValue();
                border.add(new MapPoint(map, x, y));
            }
            // 设置地图图块集
            mapProperties.tilesIndexA = (byte) (((Number) X00.getValue()).intValue() & 0XFF);
            mapProperties.tilesIndexB = (byte) (((Number) X40.getValue()).intValue() & 0XFF);
            mapProperties.tilesIndexC = (byte) (((Number) X80.getValue()).intValue() & 0XFF);
            mapProperties.tilesIndexD = (byte) (((Number) XC0.getValue()).intValue() & 0XFF);
            // 设置图块集组合
            mapProperties.combinationA = ((byte) (((Number) combinationA.getValue()).intValue() & 0xFF));
            mapProperties.combinationB = ((byte) (((Number) combinationB.getValue()).intValue() & 0xFF));
            // 设置调色板
            mapProperties.palette = ((char) (((Number) palette.getValue()).intValue() & 0xFFFF));

            // 设置精灵图块集
            mapProperties.spriteIndex = (byte) (((Number) S80.getValue()).intValue() & 0XFF);

            // 设置其它

            // 设置音乐
            mapProperties.setMusic((byte) (((Number) mapMusic.getValue()).intValue() & 0xFF));
            mapMusic.setValue(mapProperties.intMusic());

            if (selectMap >= 0x80) {
                mapProperties.monsterGroupIndex = (byte) (((Number) monsterGroupIndex.getValue()).intValue() & 0xFF);
            }

            int selectedIndex = mapList.getSelectedIndex();
            // 更新预览地图的图片
            mapImages.remove(selectMap);
            mapList.removeSelectionInterval(selectedIndex, selectedIndex);
            mapList.setSelectedIndex(selectedIndex);
        });
    }

    /**
     * 初始化航线界面
     */
    private void setLinePane() {
        // 点击list也会跟着选中相应的按钮
        outLineList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                outLineSelected.setSelected(true);
            }
        });
        backLineList.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                backLineSelected.setSelected(true);
            }
        });

        DefaultListCellRenderer lineListCellRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Map.Entry<LineDirection, Byte> entry = (Map.Entry<LineDirection, Byte>) value;
                value = String.format("%s%s %d", index > 0x10 ? "(无效) " : "",
                        switch (entry.getKey()) {
                            case UP -> "向上移动";
                            case DOWN -> "向下移动";
                            case LEFT -> "向左移动";
                            case RIGHT -> "向右移动";
                            case END -> "结束";
                        }, entry.getValue());
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        };
        outLineList.setCellRenderer(lineListCellRenderer);
        backLineList.setCellRenderer(lineListCellRenderer);

        outLineList.setModel(new DefaultListModel<>());
        backLineList.setModel(new DefaultListModel<>());

        gotoOutLineTarget.addActionListener(e -> {
            int map = ((Number) outLineTargetMap.getValue()).intValue();
            int x = ((Number) outLineTargetX.getValue()).intValue();
            int y = ((Number) outLineTargetY.getValue()).intValue();
            gotoPoint(map, x, y);
        });
        gotoBackLineTarget.addActionListener(e -> {
            int map = ((Number) backLineTargetMap.getValue()).intValue();
            int x = ((Number) backLineTargetX.getValue()).intValue();
            int y = ((Number) backLineTargetY.getValue()).intValue();
            gotoPoint(map, x, y);
        });
        outLineTarget.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }

            outLineTarget.setEnabled(false);
            getSelectListeners().add(new BoxSelectedAdapter.SelectListener() {
                @Override
                public void selected(int x, int y) {
                    getSelectListeners().remove(this);
                    outLineTarget.setEnabled(true);

                    int option = JOptionPane.showConfirmDialog(Main.this,
                            String.format("Map:%02X X:%02X Y:%02X", mapSelectListModel.getSelectedMap(), x, y),
                            "出航目的地设置", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        outLineTargetMap.setValue(mapSelectListModel.getSelectedMap());
                        outLineTargetX.setValue(x);
                        outLineTargetY.setValue(y);
                    }
                }
            });
        });

        backLineTarget.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }

            backLineTarget.setEnabled(false);
            getSelectListeners().add(new BoxSelectedAdapter.SelectListener() {
                @Override
                public void selected(int x, int y) {
                    getSelectListeners().remove(this);
                    backLineTarget.setEnabled(true);

                    int option = JOptionPane.showConfirmDialog(Main.this,
                            String.format("Map:%02X X:%02X Y:%02X", mapSelectListModel.getSelectedMap(), x, y),
                            "归航目的地设置", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        backLineTargetMap.setValue(mapSelectListModel.getSelectedMap());
                        backLineTargetX.setValue(x);
                        backLineTargetY.setValue(y);
                    }
                }
            });
        });


        lineUp.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }
            DefaultListModel model = (DefaultListModel) selectedLineList.getModel();
            model.addElement(Map.entry(LineDirection.UP, ((Number) lineMoveCount.getValue()).byteValue()));
        });
        lineDown.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }
            DefaultListModel model = (DefaultListModel) selectedLineList.getModel();
            model.addElement(Map.entry(LineDirection.DOWN, ((Number) lineMoveCount.getValue()).byteValue()));
        });
        lineLeft.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }
            DefaultListModel model = (DefaultListModel) selectedLineList.getModel();
            model.addElement(Map.entry(LineDirection.LEFT, ((Number) lineMoveCount.getValue()).byteValue()));
        });
        lineRight.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }
            DefaultListModel model = (DefaultListModel) selectedLineList.getModel();
            model.addElement(Map.entry(LineDirection.RIGHT, ((Number) lineMoveCount.getValue()).byteValue()));
        });

        lineRemove.addActionListener(e -> {
            JList selectedLineList = getSelectedLineList();
            if (selectedLineList == null) {
                return;
            }
            DefaultListModel model = (DefaultListModel) selectedLineList.getModel();
            int[] selectedIndices = selectedLineList.getSelectedIndices();
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                model.remove(selectedIndices[i]);
            }
        });

        saveLine.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);
            Map.Entry<List<Map.Entry<LineDirection, Byte>>, CameraMapPoint> shippingLineOut = worldMapEditor.getShippingLineOut();
            Map.Entry<List<Map.Entry<LineDirection, Byte>>, CameraMapPoint> shippingLineBack = worldMapEditor.getShippingLineBack();
            // 清除现有的航线
            shippingLineOut.getKey().clear();
            shippingLineBack.getKey().clear();

            // 读取出航和归航的路线
            DefaultListModel outLineListModel = (DefaultListModel) outLineList.getModel();
            DefaultListModel backLineListModel = (DefaultListModel) backLineList.getModel();
            for (Object line : outLineListModel.toArray()) {
                shippingLineOut.getKey().add((Map.Entry<LineDirection, Byte>) line);
            }
            for (Object line : backLineListModel.toArray()) {
                shippingLineBack.getKey().add((Map.Entry<LineDirection, Byte>) line);
            }

            // 设置出航目的地
            shippingLineOut.getValue().setMap(((Number) outLineTargetMap.getValue()).intValue());
            shippingLineOut.getValue().setX(((Number) outLineTargetX.getValue()).intValue());
            shippingLineOut.getValue().setY(((Number) outLineTargetY.getValue()).intValue());
            // 设置归航目的地
            shippingLineBack.getValue().setMap(((Number) backLineTargetMap.getValue()).intValue());
            shippingLineBack.getValue().setX(((Number) backLineTargetX.getValue()).intValue());
            shippingLineBack.getValue().setY(((Number) backLineTargetY.getValue()).intValue());

        });

        // 对JSpinner添加鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                lineMoveCount
        }) {
            spinner.setModel(new SpinnerNumberModel(0, 0x00, 0xFF, 1));
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, NumberFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
    }

    private JList getSelectedLineList() {
        JList list = null;
        if (outLineSelected.isSelected()) {
            list = outLineList;
        }
        if (list == null && backLineSelected.isSelected()) {
            list = backLineList;
        }
        return list;
    }

    private synchronized void showLoadingDialog(MetalMaxRe metalMaxRe) {
        JProgressBar progressBar = new JProgressBar(0, metalMaxRe.getEditorManager().getCount());
        JOptionPane jOptionPane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jOptionPane.createDialog(this, "正在加载游戏文件");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // 更新进度条的监听器
        metalMaxRe.getEventHandler().register(new EventListener() {
            private int lastMapId = 0;

            public void onLoadPre(EditorManagerEvent.Pre event) {
                // 记录加载前选中的地图
                lastMapId = mapSelectListModel.getSelectedMap();
                // 显示正在加载
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(true);
                });
            }

            public void onEditorLoadPost(EditorLoadEvent.Post event) {
                progressBar.setValue(progressBar.getValue() + 1);
            }

            public void onLoadPost(EditorManagerEvent.Post event) {
                metalMaxRe.getEventHandler().unregister(this);
                // 设置标题为打开的文件路径
                Path path = metalMaxRe.getBuffer().getPath();
                if (path != null) {
                    setTitle(String.format("MetalMaxRe [%s] - %s", path.getName(path.getNameCount() - 1), path));
                } else {
                    setTitle(String.format("MetalMaxRe [%s]", metalMaxRe.getBuffer().getVersion().getName()));
                }

//                JPopupMenu fileMenu = getJMenuBar().getMenu(0x00).getPopupMenu();
//                for (Component component : fileMenu.getComponents()) {
//                    if (component instanceof JMenuItem jMenuItem) {
//                        if (Objects.equals(jMenuItem.getText(), "Save")) {
//                            // 对菜单 File->Save 按钮启用或禁用
//                            if (path == null) {
//                                jMenuItem.setEnabled(false);
//                                jMenuItem.setToolTipText("打开ROM后才能储存");
//                            } else {
//                                jMenuItem.setEnabled(true);
//                                jMenuItem.setToolTipText(null);
//                            }
//                            break;
//                        }
//                    }
//                }

                worldMapImage = null;
                mapImages.clear();
                mapTileSets.clear();
                spriteTileSets.clear();

                if (lastMapId != -1) {
                    int selectMap = Math.min(lastMapId, mapList.getModel().getSize() - 1);
                    mapList.removeSelectionInterval(selectMap, selectMap);
                    mapList.setSelectedIndex(selectMap);
                }


                IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);
                String[] maps = new String[mapEditor.getMapMaxCount()];
                maps[0] = "自定义";
                for (int i = 1; i < maps.length; i++) {
                    maps[i] = String.format("%02X", i);
                }
                sharedEventTilesIndex.setModel(new DefaultComboBoxModel<>(maps));

                IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);

                DefaultListModel<Map.Entry<LineDirection, Byte>> outLineListModel = (DefaultListModel<Map.Entry<LineDirection, Byte>>) outLineList.getModel();
                DefaultListModel<Map.Entry<LineDirection, Byte>> backLineListModel = (DefaultListModel<Map.Entry<LineDirection, Byte>>) backLineList.getModel();
                // 清空列表，设置新的航线
                outLineListModel.removeAllElements();
                backLineListModel.removeAllElements();

                // 将出航和归航航线添加到列表中
                outLineListModel.addAll(worldMapEditor.getShippingLineOut().getKey());
                backLineListModel.addAll(worldMapEditor.getShippingLineBack().getKey());

                // 设置出目的地
                CameraMapPoint outPoint = worldMapEditor.getShippingLineBack().getValue();
                outLineTargetMap.setValue(outPoint.intMap());
                outLineTargetX.setValue(outPoint.intX());
                outLineTargetY.setValue(outPoint.intY());
                // 设置归航目的地
                CameraMapPoint backPoint = worldMapEditor.getShippingLineBack().getValue();
                backLineTargetMap.setValue(backPoint.intMap());
                backLineTargetX.setValue(backPoint.intX());
                backLineTargetY.setValue(backPoint.intY());

                // 加载完毕，关闭窗口
                SwingUtilities.invokeLater(() -> {
                    dialog.dispose();

                    mapList.updateUI(); // 更新地图列表
                });
            }
        });

        // 开始加载
        metalMaxRe.getEditorManager().loadEditors();
    }

    /**
     * 地图大小
     */
    private void setMapSize(MapProperties mapProperties) {
        mapWidth.setValue(mapProperties.intWidth());
        mapHeight.setValue(mapProperties.intHeight());
        mapMovableWidthOffset.setValue(mapProperties.movableWidthOffset & 0xFF);
        mapMovableHeightOffset.setValue(mapProperties.movableHeightOffset & 0xFF);
        mapMovableWidth.setValue(mapProperties.movableWidth & 0xFF);
        mapMovableHeight.setValue(mapProperties.movableHeight & 0xFF);
    }

    /**
     * 边界类型和目的地
     */
    private void setMapBorder(MapBorder mapBorder) {
        switch (mapBorder.getType()) {
            case LAST -> {
                borderTypeLast.setSelected(true);
            }
            case FIXED -> {
                borderTypeFixed.setSelected(true);
                MapPoint first = mapBorder.getFirst();
                borderLocation00.setValue(first.intMap());
                borderLocation01.setValue(first.intX());
                borderLocation02.setValue(first.intY());
            }
            case DIRECTION -> {
                borderTypeDirection.setSelected(true);

                MapPoint mapPoint = mapBorder.get(0);
                borderLocation00.setValue(mapPoint.intMap());
                borderLocation01.setValue(mapPoint.intX());
                borderLocation02.setValue(mapPoint.intY());

                mapPoint = mapBorder.get(1);
                borderLocation10.setValue(mapPoint.intMap());
                borderLocation11.setValue(mapPoint.intX());
                borderLocation12.setValue(mapPoint.intY());

                mapPoint = mapBorder.get(2);
                borderLocation20.setValue(mapPoint.intMap());
                borderLocation21.setValue(mapPoint.intX());
                borderLocation22.setValue(mapPoint.intY());

                mapPoint = mapBorder.get(3);
                borderLocation30.setValue(mapPoint.intMap());
                borderLocation31.setValue(mapPoint.intX());
                borderLocation32.setValue(mapPoint.intY());
            }
        }
    }

    public int getSelectedMap() {
        return mapSelectListModel.getSelectedMap();
    }

    public List<BoxSelectedAdapter.SelectListener> getSelectListeners() {
        return selectListeners;
    }

    public synchronized void gotoPoint(int map, int x, int y) {
        mapList.setSelectedIndex(map);
        SwingUtilities.invokeLater(() -> {
            mapImageSelectedPointX.setValue(x);
            mapImageSelectedPointY.setValue(y);
            gotoPoint.doClick();
        });
    }

    private static void boxMovable(Graphics graphics, MapProperties mapProperties) {
        // 使用空心矩形显示可移动区域
        // 黑色标记可移动区域
        graphics.setColor(Color.BLACK);
        graphics.drawRect((((mapProperties.movableWidthOffset & 0xFF) * 0x10)),
                (((mapProperties.movableHeightOffset & 0xFF) * 0x10)),
                (((mapProperties.movableWidth & 0xFF) * 0x10) - 1),
                (((mapProperties.movableHeight & 0xFF) * 0x10) - 1));
    }

    private static void boxEntrances(Graphics graphics, MapProperties mapProperties, Map<MapPoint, MapPoint> mapEntrances) {
        // 红色标记入口
        graphics.setColor(Color.RED);
        for (MapPoint mapPoint : mapEntrances.keySet()) {
            if (!(mapProperties instanceof WorldMapProperties) && (mapPoint.intX() >= mapProperties.getWidth() || mapPoint.intY() >= mapProperties.getHeight())) {
                // 超出地图的不绘制
                continue;
            }
            graphics.drawRect(mapPoint.intX() * 0x10, mapPoint.intY() * 0x10, 16 - 1, 16 - 1);
        }
    }

    private static void boxComputer(Graphics graphics, MapProperties mapProperties, List<Computer> computers) {
        // 蓝色标记计算机
        graphics.setColor(Color.BLUE);
        for (Computer computer : computers) {
            if (computer.intX() >= mapProperties.getWidth() || computer.intY() >= mapProperties.getHeight()) {
                // 超出地图的不绘制
                continue;
            }
            graphics.drawRect(computer.intX() * 0x10, computer.intY() * 0x10, 16 - 1, 16 - 1);
        }
    }

    private static void boxSprites(Graphics graphics, MapProperties mapProperties, List<Sprite> sprites, SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets) {
        // 绘制精灵
        for (Sprite sprite : sprites) {
            if (!(mapProperties instanceof WorldMapProperties) && (sprite.intX() >= mapProperties.getWidth() || sprite.intY() >= mapProperties.getHeight())) {
                // 超出地图的不绘制
                continue;
            }
            BufferedImage bufferedImage = spriteTileSets.getValue().get(sprite.intType());
            graphics.drawImage(bufferedImage, sprite.intX() * 0x10, sprite.intY() * 0x10, null);
        }
    }

    private static void boxTreasures(Graphics graphics, boolean showBox, List<Treasure> treasures, SingleMapEntry<BufferedImage, List<BufferedImage>> mapTileSets) {
        // 显示宝藏
        for (Treasure treasure : treasures) {
            if (mapTileSets != null || treasure.intMap() != 0x00) {
                // 世界地图不显示宝藏
                BufferedImage treasureImage = mapTileSets.getValue().get(0x01);
                graphics.drawImage(treasureImage, treasure.intX() * 0x10, treasure.intY() * 0x10, null);
            }
            if (showBox || mapTileSets == null || treasure.intMap() == 0x00) {
                // 画一个黄框
                graphics.setColor(Color.YELLOW);
                graphics.drawRect(treasure.intX() * 0x10, treasure.intY() * 0x10, 16 - 1, 16 - 1);
            }
        }
    }

    private static void boxTanks(Graphics graphics, int mapId, EnumMap<Tank, TankInitialAttribute> tanks, SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets) {
        // 显示坦克
        for (Map.Entry<Tank, TankInitialAttribute> entry : tanks.entrySet()) {
            TankInitialAttribute tankInitialAttribute = entry.getValue();
            if (entry.getKey().getId() >= Tank.TAX_1.getId() || tankInitialAttribute.intMap() != mapId) {
                // 出租坦克和不在这个地图不显示
                continue;
            }

            BufferedImage treasureImage = spriteTileSets.getValue().get((entry.getKey().getId() + 1) * 0x04);
            graphics.drawImage(treasureImage, tankInitialAttribute.intX() * 0x10, tankInitialAttribute.intY() * 0x10, null);
        }
    }

    private void itemStateChanged(ItemEvent e) {
        if (!multipleMetalMaxRe.hasInstance()) {
            return;
        }

        MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

        IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // 使用特殊地图编辑器
            if (mapEditor instanceof me.afoolslove.metalmaxre.desktop.editors.map.MapEditorImpl) {
                return;
            } else {
                metalMaxRe.getEditorManager().unregister(IMapEditor.class);
                metalMaxRe.getEditorManager().register(IMapEditor.class, me.afoolslove.metalmaxre.desktop.editors.map.MapEditorImpl::new);
            }
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            // 使用默认地图编辑器
            if (mapEditor instanceof MapEditorImpl) {
                return;
            } else {
                metalMaxRe.getEditorManager().unregister(IMapEditor.class);
                metalMaxRe.getEditorManager().register(IMapEditor.class, MapEditorImpl::new);
            }
        }
        try {
            ((Future<?>) metalMaxRe.getEditorManager().loadEditor(IMapEditor.class)).get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
        mapImages.entrySet().removeIf(entry -> entry.getKey() >= 0xC3);
        int selectMap = mapSelectListModel.getSelectedMap();
        if (selectMap >= 0xC3) {
            mapList.removeSelectionInterval(selectMap, selectMap);
            mapList.setSelectedIndex(selectMap);
        }
    }
}
