package me.afoolslove.metalmaxre.desktop;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.desktop.dialog.DataValueEditorDialog;
import me.afoolslove.metalmaxre.desktop.dialog.MapEntranceEditorDialog;
import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.map.*;
import me.afoolslove.metalmaxre.editors.map.tileset.ITileSetEditor;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.sprite.Sprite;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
import me.afoolslove.metalmaxre.editors.text.TextEditorImpl;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorManagerEvent;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.tiled.TiledMapUtils;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.mapeditor.core.TileSet;
import org.mapeditor.io.TMXMapReader;
import org.mapeditor.io.TMXMapWriter;
import org.mapeditor.util.BasicTileCutter;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main extends JFrame {
    private final LinkedList<MetalMaxRe> metalMaxRes = new LinkedList<>();

    private JPanel contentPane;
    private JList<String> mapList;
    private JLabel mapImage;
    private JRadioButton borderTypeLast;
    private JRadioButton borderTypeFixed;
    private JRadioButton borderTypeDirection;
    private JPanel borderTypes;
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
    private JButton mapHideTile;
    private JButton mapFillTile;
    private JSpinner mapMusic;
    private JSpinner spinner20;
    private JButton applyProperties;
    private JTextField mapFilter;


    private final ExecutorService imageLoaderService = Executors.newCachedThreadPool();
    private final Map<Integer, ImageIcon> mapImages = new HashMap<>();

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
        main.setSize(1280, 720);
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
                    RomBuffer buffer = new RomBuffer(RomVersion.getChinese(), selectedFile.toPath());
                    MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
                    metalMaxRe.useDefault();
                    ((EditorManagerImpl) metalMaxRe.getEditorManager()).registerDefaultEditors();

//                    metalMaxRes.add(metalMaxRe);
                    metalMaxRes.addFirst(metalMaxRe);

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

        // 重新打开
        JMenuItem fileMenuReopen = new JMenuItem("重新加载");
        fileMenuReopen.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }

            int i = JOptionPane.showConfirmDialog(this, "重新加载会丢失已修改的所有数据", "重新加载", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (i == JOptionPane.OK_OPTION) {
                showLoadingDialog(metalMaxRes.getFirst());
            }
        });


        JMenuItem fileMenuSaveAs = new JMenuItem("另存为...");
        // 快捷键：Ctrl + Shift + S
        fileMenuSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK));
        fileMenuSaveAs.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }

            MetalMaxRe metalMaxRe = metalMaxRes.getFirst();
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
            if (metalMaxRes.isEmpty()) {
                return;
            }
            MetalMaxRe metalMaxRe = metalMaxRes.getFirst();
            if (metalMaxRe != null) {
                metalMaxRe.getEditorManager().applyEditors();
            }
        });


        JMenu fileMenuImport = new JMenu("导入");
        JMenuItem fileMenuImportWorld = new JMenuItem("导入世界地图(undone)");
        fileMenuImportWorld.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setFileFilter(new FileNameExtensionFilter("TiledMap", "tmx"));
//            int state = fileChooser.showOpenDialog(this);
//            if (state == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//
//                int result = 0;
//                if (selectedFile.exists()) {
//                    result = JOptionPane.showConfirmDialog(this,
//                            "导入世界地图会覆盖现有的世界地图数据！", "覆盖数据",
//                            JOptionPane.OK_CANCEL_OPTION,
//                            JOptionPane.WARNING_MESSAGE);
//                }
//                if (result == JOptionPane.OK_OPTION) {
//                    // 导入世界地图
//                    try {
//                        TiledMap.importWorldMap(new TMXMapReader().readMap(selectedFile.getAbsolutePath()));
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        ByteArrayOutputStream out = new ByteArrayOutputStream();
//                        PrintWriter printWriter = new PrintWriter(out);
//                        ex.printStackTrace(printWriter);
//                        JOptionPane.showMessageDialog(this, "导入失败\n" + out);
//                    }
//                    System.out.printf("Import world map from %s OK.\n", selectedFile.getAbsolutePath());
//                }
//            }
        });

        JMenuItem fileMenuImportMap = new JMenuItem("导入地图");
        fileMenuImportMap.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }
            MetalMaxRe metalMaxRe = metalMaxRes.getFirst();

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
        fileMenu.add(fileMenuOpen);     // 打开
        fileMenu.add(fileMenuReopen);   // 重新加载
        fileMenu.addSeparator();        //----------------
        fileMenu.add(fileMenuSaveAs);   // 另存为...
        fileMenu.addSeparator();        //----------------
        fileMenu.add(fileMenuApply);    // 应用
        fileMenu.addSeparator();        //----------------
        fileMenu.add(fileMenuImport);   // 导入 >

        JMenu editorMenu = new JMenu("编辑器");

        JMenuItem editorMenuText = new JMenuItem("文本编辑器(undone)");
        editorMenuText.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }

            TextEditorImpl textEditor = metalMaxRes.getFirst().getEditorManager().getEditor(ITextEditor.class);

            Path textPath = Path.of("C:\\Users\\AFoolLove\\Desktop\\text.txt");
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Integer, List<TextBuilder>> listEntry : textEditor.getIndexPages().entrySet()) {
                int indexPage = textEditor.getIndexPagexx(listEntry.getKey());
                DataAddress dataAddress = textEditor.getTextAddresses().get(indexPage);

                builder.append(String.format("%02X:%05X-%05X\n", listEntry.getKey(), dataAddress.getKey() + 0x10, dataAddress.getValue() + 0x10));

                for (int i = 0; i < listEntry.getValue().size(); i++) {
                    builder.append(String.format("%02X:%02X  ", listEntry.getKey(), i));
                    builder.append(listEntry.getValue().get(i));
                    builder.append('\n');
                }
            }

            try (OutputStream outputStream = Files.newOutputStream(textPath)) {
                outputStream.write(builder.toString().getBytes());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem editorMenuEntrances = new JMenuItem("出入口编辑器");
        editorMenuEntrances.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }
            MapEntranceEditorDialog mapEntranceEditorDialog = new MapEntranceEditorDialog(metalMaxRes.getFirst());
            mapEntranceEditorDialog.pack();
            mapEntranceEditorDialog.setVisible(true);


        });

        JMenuItem editorMenuDataValue = new JMenuItem("数据值编辑器");
        editorMenuDataValue.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }

            DataValueEditorDialog dataValueEditorDialog = new DataValueEditorDialog(metalMaxRes.getFirst());
            dataValueEditorDialog.pack();
            dataValueEditorDialog.setVisible(true);
        });


        // 编辑器
        editorMenu.add(editorMenuText);         // 文本编辑器
        editorMenu.add(editorMenuEntrances);    // 出入口编辑器
        editorMenu.add(editorMenuDataValue);    // 数据值编辑器


        JMenu paletteMenu = new JMenu("调色板(undone)");
        JMenuItem paletteMenuSystem = new JMenuItem("系统调色板");
        paletteMenu.add(paletteMenuSystem);

        menuBar.add(fileMenu);
        menuBar.add(editorMenu);
        menuBar.add(paletteMenu);
    }

    private void createLayout() {
        mapSelectListModel = new MapSelectListModel(mapList, metalMaxRes, mapFilter);
        mapList.setModel(mapSelectListModel);
        mapList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            final var selectedIndex = mapSelectListModel.getSelectMap();
            if (selectedIndex == 0) {
                System.out.println("E" + selectedIndex);
                return;
            }

            if (!metalMaxRes.isEmpty()) {
                MetalMaxRe metalMaxRe = metalMaxRes.getFirst();
                if (metalMaxRe != null) {
                    MapPropertiesEditorImpl mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
                    IMapEntranceEditor mapEntranceEditor = metalMaxRe.getEditorManager().getEditor(IMapEntranceEditor.class);
                    IComputerEditor<Computer> computerEditor = metalMaxRe.getEditorManager().getEditor(IComputerEditor.class);
                    IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);

                    MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectedIndex);
                    MapEntrance mapEntrance = mapEntranceEditor.getMapEntrance(selectedIndex);

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

                    mapPropertiesData.append(String.format("%04X:", (int) NumberR.toChar(mapIndexRoll[selectedIndex])));
                    for (byte b : mapProperties.toByteArray()) {
                        mapPropertiesData.append(String.format("%02X", b));
                    }
                    mapPropertiesData.append('\n');
                    mapPropertiesData.append(String.format("%04X:", (int) NumberR.toChar(mapProperties.mapIndex)));
                    byte[] build = mapEditor.getMap(selectedIndex).build();
                    for (byte b : build) {
                        mapPropertiesData.append(String.format("%02X", b));
                    }

                    this.mapDes.setText(String.format("""
                            %02X
                            宽:%03d 高:%03d
                            %s
                            """, selectedIndex, mapProperties.intWidth(), mapProperties.intHeight(), mapPropertiesData));

                    // 设置头属性
                    mapHeadUnderground.setSelected(mapProperties.isUnderground());
                    mapHeadEventTile.setSelected(mapProperties.hasEventTile());
                    mapHeadUnknown.setSelected((mapProperties.getHead() & 0B0000_0010) == 0B0000_0010);
                    mapHeadDyTile.setSelected(mapProperties.hasDyTile());

                    // 设置地图大小
                    mapWidth.setValue(mapProperties.intWidth());
                    mapHeight.setValue(mapProperties.intHeight());
                    mapMovableWidthOffset.setValue(mapProperties.movableWidthOffset & 0xFF);
                    mapMovableHeightOffset.setValue(mapProperties.movableHeightOffset & 0xFF);
                    mapMovableWidth.setValue(mapProperties.movableWidth & 0xFF);
                    mapMovableHeight.setValue(mapProperties.movableHeight & 0xFF);

                    // 获取边界类型和目的地
                    switch (mapEntrance.getBorder().getType()) {
                        case LAST -> {
                            borderTypeLast.setSelected(true);
                        }
                        case FIXED -> {
                            borderTypeFixed.setSelected(true);
                            MapPoint first = mapEntrance.getBorder().getFirst();
                            borderLocation00.setValue(first.intMap());
                            borderLocation01.setValue(first.intX());
                            borderLocation02.setValue(first.intY());
                        }
                        case DIRECTION -> {
                            borderTypeDirection.setSelected(true);

                            MapPoint mapPoint = mapEntrance.getBorder().get(0);
                            borderLocation00.setValue(mapPoint.intMap());
                            borderLocation01.setValue(mapPoint.intX());
                            borderLocation02.setValue(mapPoint.intY());

                            mapPoint = mapEntrance.getBorder().get(1);
                            borderLocation10.setValue(mapPoint.intMap());
                            borderLocation11.setValue(mapPoint.intX());
                            borderLocation12.setValue(mapPoint.intY());

                            mapPoint = mapEntrance.getBorder().get(2);
                            borderLocation20.setValue(mapPoint.intMap());
                            borderLocation21.setValue(mapPoint.intX());
                            borderLocation22.setValue(mapPoint.intY());

                            mapPoint = mapEntrance.getBorder().get(3);
                            borderLocation30.setValue(mapPoint.intMap());
                            borderLocation31.setValue(mapPoint.intX());
                            borderLocation32.setValue(mapPoint.intY());
                        }
                    }

                    // 设置其它
                    mapMusic.setValue(mapProperties.intMusic());

                    // 更新地图图片
                    var imageIcon = mapImages.get(selectedIndex);
                    if (imageIcon == null) {
                        if (mapImages.containsKey(selectedIndex)) {
                            // 存在还是 null，正在加载
                            return;
                        }
                        mapImages.put(selectedIndex, null); // 占位，表示正在加载
                        imageLoaderService.submit(() -> {
                            MapBuilder mapBuilder = mapEditor.getMap(selectedIndex);

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

                            BufferedImage tileSet = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));

                            BufferedImage bufferedImage = new BufferedImage(width * 0x10, height * 0x10, BufferedImage.TYPE_INT_ARGB);

                            // 储存1tile(16*16)
                            final Map<Integer, BufferedImage> tmpTiles = new HashMap<>();

                            Graphics2D graphics = bufferedImage.createGraphics();

                            // mapX和mapY的单位是tile，1tile=16*16像素
                            for (int mapY = 0; mapY < height; mapY++) {
                                for (int mapX = 0; mapX < width; mapX++) {
                                    int tile = map[mapY][mapX] & 0xFF;
                                    int y2 = (tile / 0x10) * 0x10;
                                    int x2 = (tile % 0x10) * 0x10;

                                    BufferedImage tileImage = tmpTiles.get(tile);
                                    if (tileImage == null) {
                                        // 复制一个tile出来保存到临时map中
                                        tileImage = tileSet.getSubimage(x2, y2, 0x10, 0x10);
                                        tmpTiles.put(tile, tileImage);
                                    }
                                    // 绘制tile
                                    graphics.drawImage(tileImage, mapX * 0x10, mapY * 0x10, 0x10, 0x10, null);
                                }
                            }
                            // 使用空心矩形显示可移动区域
                            // 黑色标记可移动区域
                            graphics.setColor(Color.BLACK);
                            graphics.drawRect((((mapProperties.movableWidthOffset & 0xFF) * 0x10)),
                                    (((mapProperties.movableHeightOffset & 0xFF) * 0x10)),
                                    (((mapProperties.movableWidth & 0xFF) * 0x10) - 1),
                                    (((mapProperties.movableHeight & 0xFF) * 0x10) - 1));
                            // 红色标记入口
                            graphics.setColor(Color.RED);
                            for (MapPoint mapPoint : mapEntrance.getEntrances().keySet()) {
                                graphics.drawRect(mapPoint.intX() * 0x10, mapPoint.intY() * 0x10, 16 - 1, 16 - 1);
                            }

                            // 蓝色标记计算机
                            graphics.setColor(Color.BLUE);
                            for (Computer computer : computerEditor.getComputers()) {
                                if (computer.intMap() != selectedIndex) {
                                    continue;
                                }
                                if (computer.intX() >= width || computer.intY() >= height) {
                                    // 超出地图的不绘制
                                    continue;
                                }
                                graphics.drawRect(computer.intX() * 0x10, computer.intY() * 0x10, 16 - 1, 16 - 1);
                            }

                            // 绘制精灵
                            ISpriteEditor spriteEditor = metalMaxRe.getEditorManager().getEditor(ISpriteEditor.class);
                            List<Sprite> sprites = spriteEditor.getSprites().get(selectedIndex);
                            if (!sprites.isEmpty()) {
                                BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
                                for (Sprite sprite : sprites) {
                                    if (sprite.intX() >= width || sprite.intY() >= height) {
                                        // 超出地图的不绘制
                                        continue;
                                    }
                                    final int mapX = sprite.intX() * 0x10;
                                    final int mapY = sprite.intY() * 0x10;
                                    int tile = sprite.intType();

                                    int y2 = (tile / 0x10) * 0x10;
                                    int x2 = (tile % 0x10) * 0x10;

                                    graphics.drawImage(spriteBufferedImage.getSubimage(x2, y2, 0x10, 0x10), mapX, mapY, 0x10, 0x10, null);
                                }
                            }
                            graphics.dispose();

                            // 将地图左右和上下分别增加3tile作为填充tile展示
                            BufferedImage finalBufferedImage = new BufferedImage(bufferedImage.getWidth() + (6 * 0x10), bufferedImage.getHeight() + (6 * 0x10), BufferedImage.TYPE_INT_ARGB);
                            graphics = finalBufferedImage.createGraphics();
                            BufferedImage fillTile = tmpTiles.get(mapProperties.intFillTile());
                            if (fillTile == null) {
                                int y = (mapProperties.intFillTile() / 0x10) * 0x10;
                                int x = (mapProperties.intFillTile() % 0x10) * 0x10;
                                fillTile = tileSet.getSubimage(x, y, 0x10, 0x10);
                            }
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


                            ImageIcon icon = new ImageIcon(finalBufferedImage);

                            mapImages.put(selectedIndex, icon);

                            if (mapSelectListModel.getSelectMap() == selectedIndex) {
                                // 如果还是选择了这个地图，更新图片
                                mapImage.setIcon(icon);

                                // 设置填充图块到按钮
                                mapFillTile.setIcon(new ImageIcon(fillTile));

                                // 设置隐藏图块到按钮
                                BufferedImage hideTile = tmpTiles.get(mapProperties.intHideTile());
                                if (hideTile == null) {
                                    int y = (mapProperties.intFillTile() / 0x10) * 0x10;
                                    int x = (mapProperties.intFillTile() % 0x10) * 0x10;
                                    hideTile = tileSet.getSubimage(x, y, 0x10, 0x10);
                                }
                                mapHideTile.setIcon(new ImageIcon(hideTile));
                            }
                        });
                        return; // 还在加载
                    }
                    mapImage.setIcon(imageIcon);
                }
            }
        });

        // 地图右键菜单
        mapImage.addMouseListener(new MouseAdapter() {
            private final JPopupMenu popupMenu = new JPopupMenu();

            {
                JMenuItem copyPng = new JMenuItem("复制图片");
                copyPng.setEnabled(false);
                copyPng.addActionListener(e -> {
                    final Image image = ((ImageIcon) mapImage.getIcon()).getImage();
                    Vector<Object> data = new Vector<>();
                    data.add(image);
                    DataHandler dataHandler = new DataHandler(data, "application/x-java-serialized-object; class=java.util.Vector");
                    getToolkit().getSystemClipboard().setContents(dataHandler, null);
                });
                JMenuItem copyMapData = new JMenuItem("复制地图数据");
                copyMapData.addActionListener(e -> {
                    IMapEditor mapEditor = metalMaxRes.getFirst().getEditorManager().getEditor(IMapEditor.class);
                    StringBuilder builder = new StringBuilder();
                    byte[] build = mapEditor.getMap(mapSelectListModel.getSelectMap()).build();
                    for (byte b : build) {
                        builder.append(String.format("%02X", b));
                    }
                    getToolkit().getSystemClipboard().setContents(new StringSelection(builder.toString()), null);
                });


                JMenuItem exportMap = new JMenuItem("导出地图");
                exportMap.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int state = fileChooser.showOpenDialog(mapImage);
                    if (state == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();

                        String maps = JOptionPane.showInputDialog(mapImage, """
                                导出单个地图：1
                                导出多个地图：1-2
                                导出多个地图：1-2,5-6,9,12
                                地图ID范围： 1-239\
                                """, "导出地图", JOptionPane.QUESTION_MESSAGE);
                        if (maps == null) {
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
                        final TMXMapWriter tmxMapWriter = new TMXMapWriter();
                        MetalMaxRe metalMaxRe = metalMaxRes.getFirst();

                        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
                        IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);


                        JProgressBar progressBar = new JProgressBar(0, metalMaxRe.getEditorManager().getCount());
                        JOptionPane jOptionPane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE);
                        JDialog dialog = jOptionPane.createDialog(mapImage, "正在导出地图");

                        progressBar.setMaximum(maps.length());
                        // 显示导出进度
                        SwingUtilities.invokeLater(() -> {
                            dialog.setVisible(true);
                        });

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
                        SwingUtilities.invokeLater(() -> {
                            dialog.setVisible(false);
                            dialog.dispose();
                        });
                        System.out.printf("Export %s maps OK.\n", Arrays.toString(mapIds.toArray()));
                    }
                });

                JMenuItem importAndReplaceMap = new JMenuItem("导入并替换地图");
                importAndReplaceMap.addActionListener(e -> {
                    // 文件选择器
                    JFileChooser fileChooser = new JFileChooser();
                    // 添加一个nes后缀的文件过滤器
                    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Tiled Map(*.tmx)", "TMX"));
                    // 开始选择文件
                    int state = fileChooser.showOpenDialog(mapImage);
                    if (state == JFileChooser.APPROVE_OPTION) {
                        try {
                            TMXMapReader tmxMapReader = new TMXMapReader();
                            org.mapeditor.core.Map map = tmxMapReader.readMap(fileChooser.getSelectedFile().toString());
                            int selectedIndex = mapSelectListModel.getSelectMap();
                            TiledMapUtils.importMap(metalMaxRes.getFirst(), selectedIndex, map);

                            if (mapImages.containsKey(selectedIndex) && mapImages.get(selectedIndex) != null) {
                                mapImages.remove(selectedIndex);
                                // 重新选中一次，加载图片
                                mapList.setSelectedIndex(0x00);
                                mapList.setSelectedIndex(selectedIndex);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });


                popupMenu.add(copyPng);
                popupMenu.add(copyMapData);
                popupMenu.addSeparator();
                popupMenu.add(exportMap);
                popupMenu.add(importAndReplaceMap);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(mapImage, e.getX(), e.getY());
                    popupMenu.setVisible(true);
                }
            }
        });

        borderTypeLast.addItemListener(e -> {
            if (borderTypeLast.isSelected()) {
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation00, borderLocation01, borderLocation02,
                        borderLocation10, borderLocation11, borderLocation12,
                        borderLocation20, borderLocation21, borderLocation22,
                        borderLocation30, borderLocation31, borderLocation32
                }) {
                    spinner.setVisible(false);
                }
            }
        });
        borderTypeFixed.addItemListener(e -> {
            if (borderTypeFixed.isSelected()) {
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation00, borderLocation01, borderLocation02,
                }) {
                    spinner.setVisible(true);
                }
                for (JSpinner spinner : new JSpinner[]{
                        borderLocation10, borderLocation11, borderLocation12,
                        borderLocation20, borderLocation21, borderLocation22,
                        borderLocation30, borderLocation31, borderLocation32
                }) {
                    spinner.setVisible(false);
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
                    spinner.setVisible(true);
                }
            }
        });

        // 应用修改
        applyProperties.addActionListener(e -> {
            if (metalMaxRes.isEmpty()) {
                return;
            }
            int selectedIndex = mapSelectListModel.getSelectMap();
            if (selectedIndex == -1 || selectedIndex == 0) {
                return;
            }

            MetalMaxRe metalMaxRe = metalMaxRes.getFirst();
            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            IMapEntranceEditor entranceEditor = metalMaxRe.getEditorManager().getEditor(IMapEntranceEditor.class);
            MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectedIndex);
            MapEntrance mapEntrance = entranceEditor.getMapEntrance(selectedIndex);

            // 获取头属性，并去掉需要修改的4个flag
            byte head = (byte) (mapProperties.getHead() & 0xF0);

            // 设置头属性
            if (mapHeadUnderground.isSelected()) {
                head |= MapProperties.FLAG_UNDERGROUND;
            }
            if (mapHeadEventTile.isSelected()) {
                head |= MapProperties.FLAG_EVENT_TILE;
            }
            if (mapHeadUnknown.isSelected()) {
                head |= 0B0000_0010;
            }
            if (mapHeadDyTile.isSelected()) {
                head |= MapProperties.FLAG_DY_TILE;
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
            // 设置其它
            mapProperties.setMusic((byte) (((Number) mapMusic.getValue()).intValue() & 0xFF));
            mapMusic.setValue(mapProperties.intMusic());

            // 更新预览地图的图片
            mapImages.remove(selectedIndex);
            mapList.setSelectedIndex(0);
            mapList.setSelectedIndex(selectedIndex);
        });

        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                mapWidth, mapHeight, mapMovableWidthOffset, mapMovableHeightOffset, mapMovableWidth, mapMovableHeight,
                borderLocation00, borderLocation01, borderLocation02,
                borderLocation10, borderLocation11, borderLocation12,
                borderLocation20, borderLocation21, borderLocation22,
                borderLocation30, borderLocation31, borderLocation32,
                mapMusic
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
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
                lastMapId = mapSelectListModel.getSelectMap();
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

                // 加载完毕，关闭窗口
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(false);
                    dialog.dispose();
                });

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

                mapImages.clear();
                if (lastMapId != -1) {
                    mapList.setSelectedIndex(Math.min(lastMapId, mapList.getModel().getSize() - 1));
                }
                mapList.updateUI(); // 更新地图列表
            }
        });

        // 开始加载
        metalMaxRe.getEditorManager().loadEditors();
    }
}
