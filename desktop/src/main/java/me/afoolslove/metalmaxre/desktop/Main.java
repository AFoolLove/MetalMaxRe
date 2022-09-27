package me.afoolslove.metalmaxre.desktop;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.desktop.adapter.BoxSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.MapImagePopupMenuMouseAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.MiddleMoveMouseAdapter;
import me.afoolslove.metalmaxre.desktop.dialog.EditorEnabledDialog;
import me.afoolslove.metalmaxre.desktop.frame.DataValueEditorFrame;
import me.afoolslove.metalmaxre.desktop.frame.MapEntranceEditorFrame;
import me.afoolslove.metalmaxre.desktop.frame.TextEditorFrame;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.map.*;
import me.afoolslove.metalmaxre.editors.map.events.EventTile;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.map.world.WorldMapEditorImpl;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.sprite.Sprite;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.Treasure;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorManagerEvent;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.tiled.TiledMapUtils;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jdesktop.swingx.JXCollapsiblePane;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
    private JTextArea mapDes;
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
    private JCheckBox showSprites;
    private JCheckBox showFillTile;
    private JCheckBox showEntrances;
    private JCheckBox showComputers;
    private JCheckBox showEvents;
    private JCheckBox showTreasures;
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
    private JLabel mapImageSelectedPoint;
    private JLabel mapTileSetIndex;
    private JLabel spriteTileSetIndex;
    private ButtonGroup borderTypeGroup;


    private final ExecutorService imageLoaderService = Executors.newCachedThreadPool();
    private final Map<Integer, ImageIcon> mapImages = new HashMap<>();
    private BufferedImage worldMapImage;

    /**
     * 0x0000_FFFFFFFF_FFFF，tiles(4byte) + palette(2byte)
     */
    private final Map<Long, SingleMapEntry<BufferedImage, List<BufferedImage>>> mapTileSets = new HashMap<>();
    private final Map<Byte, SingleMapEntry<BufferedImage, List<BufferedImage>>> spriteTileSets = new HashMap<>();

    private MapSelectListModel mapSelectListModel;

    private String currentOpenDirectoryPath;
    private String currentSaveAsDirectoryPath;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Main main = new Main();

        main.setContentPane(main.contentPane);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 窗口默认大小
        main.setSize(1280, 860);
//        pack();
        main.setVisible(true);
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
        for (RomVersion romVersion : RomVersion.getVersions().values().stream()
                .filter(filter -> filter == RomVersion.getChinese() || filter == RomVersion.getJapanese())
                .toList()) {
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
                showLoadingDialog(multipleMetalMaxRe.current());
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

        JMenuItem editorMenuText = new JMenuItem("文本编辑器(undone)");
        editorMenuText.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            TextEditorFrame textEditorFrame = new TextEditorFrame(this, multipleMetalMaxRe);
            textEditorFrame.pack();
            textEditorFrame.setVisible(true);
        });

        JMenuItem editorMenuEntrances = new JMenuItem("出入口编辑器");
        editorMenuEntrances.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MapEntranceEditorFrame mapEntranceEditorFrame = new MapEntranceEditorFrame(this, multipleMetalMaxRe);
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


        // 编辑器
        editorMenu.add(editorMenuEnabledStates);// 启用/禁用编辑器
        editorMenu.add(editorMenuText);         // 文本编辑器
        editorMenu.add(editorMenuEntrances);    // 出入口编辑器
        editorMenu.add(editorMenuDataValue);    // 数据值编辑器


        JMenu paletteMenu = new JMenu("调色板(undone)");
        JMenuItem paletteMenuSystem = new JMenuItem("系统调色板");
        paletteMenu.add(paletteMenuSystem);

        JMenu testMenu = new JMenu("实验功能");

        testMenuAdvancedMap = new JCheckBoxMenuItem("使用扩容地图");
        testMenuAdvancedMap.addItemListener(e -> {
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
        });

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
        helpMenuAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, """
                    这是一个基于java开发的FC游戏 MetalMax ROM编辑器
                    Copyright 2022 AFoolLove
                    """);
        });
        helpMenu.add(helpMenuGithub);
        helpMenu.add(helpMenuAbout);

        menuBar.add(fileMenu);
        menuBar.add(editorMenu);
        menuBar.add(paletteMenu);
//        menuBar.add(testMenu);
        menuBar.add(helpMenu);
    }

    private void createLayout() {
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

            // 显示正在加载
            // 不显示图标
            mapImage.setText("正在加载...");
            mapImage.setIcon(null);

            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            MapPropertiesEditorImpl mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            IMapEntranceEditor mapEntranceEditor = metalMaxRe.getEditorManager().getEditor(IMapEntranceEditor.class);
            IComputerEditor<Computer> computerEditor = metalMaxRe.getEditorManager().getEditor(IComputerEditor.class);
            IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);

            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectMap);
            MapEntrance mapEntrance = mapEntranceEditor.getMapEntrance(selectMap);

            if (selectMap == 0x00) {
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

                ImageIcon imageIcon = mapImages.get(selectMap);
                if (imageIcon == null) {
                    if (mapImages.containsKey(selectMap)) {
                        // 存在还是 null，正在加载
                        return;
                    }
                    mapImages.put(selectMap, null); // 占位，表示正在加载
                    imageLoaderService.submit(() -> {
                        if (worldMapImage == null) {
                            worldMapImage = TileSetHelper.generateWorldMapImage(metalMaxRe);
                        }
                        if (mapSelectListModel.getSelectedMap() == 0x00) {
                            BufferedImage bufferedImage = new BufferedImage(worldMapImage.getWidth(), worldMapImage.getHeight(), worldMapImage.getType());
                            Graphics2D graphics = bufferedImage.createGraphics();

                            graphics.drawImage(worldMapImage, 0x00, 0x00, null);
                            boxMovable(graphics, mapProperties);
                            if (showEntrances.isSelected()) {
                                boxEntrances(graphics, mapProperties, mapEntrance.getEntrances());
                            }
//                            if (showSprites.isSelected()) {
//                                ISpriteEditor spriteEditor = metalMaxRe.getEditorManager().getEditor(ISpriteEditor.class);
//
//                                SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets = this.spriteTileSets.get(mapProperties.spriteIndex);
//                                if (spriteTileSet == null) {
//                                    spriteTileSet = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
//                                    spriteTileSets.put(mapProperties.spriteIndex, spriteTileSet);
//                                    spriteTileSetImage.setIcon(new ImageIcon(spriteTileSet));
//                                }
//                                boxSprites(graphics, mapProperties, spriteEditor.getSprites().get(selectMap), spriteTileSets);
//                            }
//                             显示动态图块
                            if (showEvents.isSelected()) {
                                IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);
                                IEventTilesEditor eventTilesEditor = metalMaxRe.getEditorManager().getEditor(IEventTilesEditor.class);
                                Map<Integer, List<EventTile>> worldEventTile = eventTilesEditor.getWorldEventTile();

                                Map<Rectangle, BufferedImage> tileSetMap = WorldMapEditorImpl.DEFAULT_PIECES.entrySet().parallelStream()
                                        .distinct()
                                        .map(entry -> Map.entry(entry, BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(metalMaxRe, entry.getValue()))))
                                        .collect(Collectors.toMap(t -> t.getKey().getKey(), Map.Entry::getValue));

                                Map<Rectangle, List<BufferedImage>> tileSetMaps = tileSetMap.entrySet().parallelStream()
                                        .map(tsm -> Map.entry(tsm.getKey(), diced(tsm.getValue())))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


                                Collection<List<EventTile>> events = worldEventTile.values();
                                for (Map.Entry<Rectangle, List<BufferedImage>> entry : tileSetMaps.entrySet()) {
                                    for (List<EventTile> event : events) {
                                        for (EventTile tile : event) {
                                            if (entry.getKey().contains(tile.intX(), tile.intY())) {
                                                BufferedImage event4x4TileImage = new BufferedImage(0x10 * 0x04, 0x10 * 0x04, BufferedImage.TYPE_INT_ARGB);
                                                Graphics2D eventTileGraphics = event4x4TileImage.createGraphics();
                                                byte[] tiles = worldMapEditor.getIndexA()[0x200 + tile.intTile()];
                                                for (int i = 0; i < tiles.length; i++) {
                                                    int x = i % 4;
                                                    int y = i / 4;
                                                    BufferedImage eventTileImage = entry.getValue().get(tiles[i] & 0xFF);
                                                    eventTileGraphics.drawImage(eventTileImage, x * 0x10, y * 0x10, null);
                                                }
                                                eventTileGraphics.dispose();

                                                graphics.drawImage(event4x4TileImage, tile.intX() * 0x10 * 0x04, tile.intY() * 0x10 * 0x04, null);
                                            }
                                        }
                                    }
                                }
                            }

                            // 显示宝藏
                            if (showTreasures.isSelected()) {
                                ITreasureEditor treasureEditor = metalMaxRe.getEditorManager().getEditor(ITreasureEditor.class);
                                boxTreasures(graphics, treasureEditor.getTreasures().stream().filter(treasure -> treasure.intMap() == selectMap).toList(), null);
                            }

                            graphics.dispose();
                            ImageIcon icon = new ImageIcon(bufferedImage);
                            mapImages.put(selectMap, icon);

                            mapImage.setText(null);
                            mapImage.setIcon(icon);
                        }
                    });
                    return;
                }
                mapImage.setText(null);
                mapImage.setIcon(imageIcon);
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

            mapPropertiesData.append(String.format("%04X:", (int) NumberR.toChar(mapIndexRoll[selectMap])));
            for (byte b : mapProperties.toByteArray()) {
                mapPropertiesData.append(String.format("%02X", b));
            }
            mapPropertiesData.append('\n');
            mapPropertiesData.append(String.format("%04X:", (int) NumberR.toChar(mapProperties.mapIndex)));
            byte[] build = mapEditor.getMap(selectMap).build();
            for (byte b : build) {
                mapPropertiesData.append(String.format("%02X", b));
            }

            this.mapDes.setText(String.format("""
                    %02X
                    %s
                    """, selectMap, mapPropertiesData));

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
                        List<BufferedImage> value = diced(key);
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
                    if (showEvents.isSelected() && mapProperties.hasEventTile()) {
                        IEventTilesEditor eventTilesEditor = metalMaxRe.getEditorManager().getEditor(IEventTilesEditor.class);
                        boxEventTile(graphics, eventTilesEditor.getEventTile(selectMap), mapTileSets);
                    }

                    // 显示宝藏
                    if (showTreasures.isSelected()) {
                        ITreasureEditor treasureEditor = metalMaxRe.getEditorManager().getEditor(ITreasureEditor.class);
                        boxTreasures(graphics, treasureEditor.getTreasures().stream().filter(treasure -> treasure.intMap() == selectMap).toList(), mapTileSets);
                    }

                    // 显示精灵
                    if (showSprites.isSelected()) {
                        ISpriteEditor spriteEditor = metalMaxRe.getEditorManager().getEditor(ISpriteEditor.class);

                        SingleMapEntry<BufferedImage, List<BufferedImage>> spriteTileSets = this.spriteTileSets.get(mapProperties.spriteIndex);
                        if (spriteTileSets == null) {
                            BufferedImage key = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
                            List<BufferedImage> value = diced(key);
                            spriteTileSets = SingleMapEntry.create(key, value);
                            this.spriteTileSets.put(mapProperties.spriteIndex, spriteTileSets);
                            spriteTileSetImage.setIcon(new ImageIcon(key));
                        }
                        boxSprites(graphics, mapProperties, spriteEditor.getSprites().get(selectMap), spriteTileSets);
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
        });

        MiddleMoveMouseAdapter middleMoveMouseAdapter = new MiddleMoveMouseAdapter(mapImage);
        mapImage.addMouseListener(middleMoveMouseAdapter);
        mapImage.addMouseMotionListener(middleMoveMouseAdapter);

        BoxSelectedAdapter mapSelectedTileBox = new BoxSelectedAdapter(mapImage);
        mapSelectedTileBox.addListener(new BoxSelectedAdapter.SelectListener() {
            @Override
            public void select(int x, int y) {
                int width = mapImage.getIcon().getIconWidth() / 0x10;
                int height = mapImage.getIcon().getIconHeight() / 0x10;
                if (showFillTile.isSelected()) {
                    // 世界地图没有填充块
                    if (mapSelectListModel.getSelectedMap() != 0x00) {
                        x -= 0x03;
                        y -= 0x03;

                        width -= 0x06;
                        height -= 0x06;
                    }
                }
                if (x < 0 || x >= width || y < 0 || y >= height) {
                    mapImageSelectedPoint.setText("-- --");
                } else {
                    mapImageSelectedPoint.setText(String.format("%02X %02X", x, y));
                }
            }

            @Override
            public void selected(int x, int y) {

            }
        });
        mapImage.addMouseListener(mapSelectedTileBox);
        mapImage.addMouseMotionListener(mapSelectedTileBox);
        mapImageScrollPane.addMouseWheelListener(mapSelectedTileBox);
        mapImage.setDropTarget(new DropTarget(mapImage, new DropTargetAdapter() {
            private File targetFile;

            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                if (mapSelectListModel.getSelectedMap() <= 0x00) {
                    // 拒绝世界地图
                    dtde.rejectDrag();
                    return;
                }
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
                try {
                    TMXMapReader tmxMapReader = new TMXMapReader();
                    org.mapeditor.core.Map map = tmxMapReader.readMap(targetFile.toString());
                    int selectMap = mapSelectListModel.getSelectedMap();
                    TiledMapUtils.importMap(metalMaxRe, selectMap, map);

                    if (mapImages.containsKey(selectMap) && mapImages.get(selectMap) != null) {
                        mapImages.remove(selectMap);
                        // 重新选中一次，加载图片
                        mapList.removeSelectionInterval(selectMap, selectMap);
                        mapList.setSelectedIndex(selectMap);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
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


        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                mapHideTileValue, mapFillTileValue,
                mapWidth, mapHeight, mapMovableWidthOffset, mapMovableHeightOffset, mapMovableWidth, mapMovableHeight,
                borderLocation00, borderLocation01, borderLocation02,
                borderLocation10, borderLocation11, borderLocation12,
                borderLocation20, borderLocation21, borderLocation22,
                borderLocation30, borderLocation31, borderLocation32,
                X00, X40, X80, XC0,
                S00, S40, S80, SC0,
                combinationA, combinationB,
                mapMusic, monsterGroupIndex
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
        // 对JSpinner添加两个十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                eventTilesIndex,
                palette, mapDataIndex, mapPropertiesIndex
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner, true));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }

        setMapPropertiesPane();
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
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                eventTilesIndex.setEnabled(false);
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

                JPopupMenu fileMenu = getJMenuBar().getMenu(0x00).getPopupMenu();
                for (Component component : fileMenu.getComponents()) {
                    if (component instanceof JMenuItem jMenuItem) {
                        if (Objects.equals(jMenuItem.getText(), "Save")) {
                            // 对菜单 File->Save 按钮启用或禁用
                            if (path == null) {
                                jMenuItem.setEnabled(false);
                                jMenuItem.setToolTipText("打开ROM后才能储存");
                            } else {
                                jMenuItem.setEnabled(true);
                                jMenuItem.setToolTipText(null);
                            }
                            break;
                        }
                    }
                }

                worldMapImage = null;
                mapImages.clear();
                mapTileSets.clear();
                spriteTileSets.clear();

                if (lastMapId != -1) {
                    int selectMap = Math.min(lastMapId, mapList.getModel().getSize() - 1);
                    mapList.removeSelectionInterval(selectMap, selectMap);
                    mapList.setSelectedIndex(selectMap);
                }

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

    private static List<BufferedImage> diced(BufferedImage bufferedImage) {
        List<BufferedImage> value = new ArrayList<>();
        int w = bufferedImage.getWidth() / 0x10;
        int h = bufferedImage.getHeight() / 0x10;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                value.add(bufferedImage.getSubimage(x * 0x10, y * 0x10, 0x10, 0x10));
            }
        }
        return value;
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

    private static void boxEventTile(Graphics graphics, Map<Integer, List<EventTile>> eventTiles, SingleMapEntry<BufferedImage, List<BufferedImage>> mapTileSets) {
        // 显示动态图块
        for (Map.Entry<Integer, List<EventTile>> entry : eventTiles.entrySet()) {
            for (EventTile eventTile : entry.getValue()) {
                BufferedImage eventTileImage = mapTileSets.getValue().get(eventTile.intTile());
                graphics.drawImage(eventTileImage, eventTile.intX() * 0x10, eventTile.intY() * 0x10, null);
            }
        }
    }

    private static void boxTreasures(Graphics graphics, List<Treasure> treasures, SingleMapEntry<BufferedImage, List<BufferedImage>> mapTileSets) {
        // 显示宝藏
        for (Treasure treasure : treasures) {
            if (mapTileSets == null || treasure.intMap() == 0x00) {
                // 世界地图画一个黄框
                graphics.setColor(Color.YELLOW);
                graphics.drawRect(treasure.intX() * 0x10, treasure.intY() * 0x10, 16 - 1, 16 - 1);
            } else {
                BufferedImage treasureImage = mapTileSets.getValue().get(0x01);
                graphics.drawImage(treasureImage, treasure.intX() * 0x10, treasure.intY() * 0x10, null);
            }
        }
    }
}
