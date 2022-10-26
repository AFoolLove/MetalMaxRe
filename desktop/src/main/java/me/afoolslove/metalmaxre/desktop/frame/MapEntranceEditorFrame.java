package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.map.*;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public class MapEntranceEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JTable entrances;
    private JXList mapList;
    private JTextField inX;
    private JTextField inY;
    private JTextField outMap;
    private JTextField outX;
    private JTextField outY;
    private JButton addEntrance;
    private JXSearchField mapFilter;
    private JLabel hint;
    private JButton removeEntrance;

    private String currentOpenDirectoryPath;
    private String currentSaveAsDirectoryPath;

    // [FF]{FF} or [FF]
    private final Pattern MAP_INDEX_PATTERN = Pattern.compile("^\\[[0-9a-fA-F]{1,2}](\\{[0-9a-fA-F]{1,2}})?$");
    private final Pattern MAP_ENTRANCE_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2} [0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}:[0-9a-fA-F]{1,2}$");

    public MapEntranceEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        this(null, frame, metalMaxRe);
    }

    public MapEntranceEditorFrame(Integer initMap, @NotNull Frame frame, @Nullable MetalMaxRe metalMaxRe) {
        super(initMap, frame, metalMaxRe);
        init("出入口编辑器", contentPane);
    }

    @Override
    protected void createMenu(@NotNull JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("文件");

        JMenuItem fileMenuExport = new JMenuItem("导出");
        fileMenuExport.addActionListener(e -> {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser(currentSaveAsDirectoryPath);
            fileChooser.setFileFilter(new FileNameExtensionFilter("出入口文件(*.txt)", "txt"));
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

                        StringBuilder entranceTxt = new StringBuilder();

                        IMapEntranceEditor mapEntranceEditor = getMetalMaxRe().getEditorManager().getEditor(IMapEntranceEditor.class);
                        for (Map.Entry<Integer, MapEntrance> entry : mapEntranceEditor.getMapEntrances().entrySet()) {
                            entranceTxt.append(String.format("[%02X]%s\n", entry.getKey(), ""));
                            MapEntrance mapEntrance = entry.getValue();

                            // 边界
                            switch (mapEntrance.getBorder().getType()) {
                                case LAST -> {
                                    entranceTxt.append("border=\n");
                                }
                                case FIXED -> {
                                    MapPoint first = mapEntrance.getBorder().getFirst();
                                    entranceTxt.append(String.format("border=%02X:%02X:%02X\n", first.intMap(), first.intX(), first.intY()));
                                }
                                case DIRECTION -> {
                                    entranceTxt.append("border=");
                                    for (MapPoint mapPoint : mapEntrance.getBorder()) {
                                        entranceTxt.append(String.format("%02X:%02X:%02X ", mapPoint.intMap(), mapPoint.intX(), mapPoint.intY()));
                                    }
                                    entranceTxt.setCharAt(entranceTxt.length() - 1, '\n');
                                }
                            }

                            // 出入口
                            for (Map.Entry<MapPoint, MapPoint> pointEntry : mapEntrance.getEntrances().entrySet()) {
                                MapPoint inPoint = pointEntry.getKey();
                                MapPoint outPoint = pointEntry.getValue();
                                entranceTxt.append(String.format("%02X:%02X %02X:%02X:%02X\n", inPoint.intX(), inPoint.intY(), outPoint.intMap(), outPoint.intX(), outPoint.intY()));
                            }
                        }

                        Files.write(selectedFile.toPath(), entranceTxt.toString().getBytes());
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
        JMenuItem fileMenuImport = new JMenuItem("导入");
        fileMenuImport.addActionListener(e -> {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser(currentOpenDirectoryPath);
            // 添加一个nes后缀的文件过滤器
            fileChooser.setFileFilter(new FileNameExtensionFilter("出入口文件(*.txt)", "txt"));
            // 开始选择文件
            int state = fileChooser.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                // 获取选择的NES文件
                File selectedFile = fileChooser.getSelectedFile();
                currentOpenDirectoryPath = selectedFile.getParent();
                try {
                    List<String> entranceTxtLines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
                    IMapEntranceEditor mapEntranceEditor = getMetalMaxRe().getEditorManager().getEditor(IMapEntranceEditor.class);

                    MapEntrance mapEntrance = null;
                    for (int line = 0; line < entranceTxtLines.size(); line++) {
                        String entranceTxtLine = entranceTxtLines.get(line);
                        if (entranceTxtLine.startsWith("#")) {
                            // 不解析注释
                            continue;
                        }
                        if (MAP_INDEX_PATTERN.matcher(entranceTxtLine).find()) {
                            // 切换地图
                            // [FF]{FF}
                            int mapId = Integer.parseInt(entranceTxtLine.substring(1, 3), 16);
                            mapEntrance = mapEntranceEditor.getMapEntrance(mapId);
                            mapEntrance.getEntrances().clear();

                            if (entranceTxtLine.length() == 8) {
                                // [FF]{FF}
                                // 映射
                                int targetMapId = Integer.parseInt(entranceTxtLine.substring(5, 7), 16);
                                if (targetMapId != mapId) {
                                    mapEntrance = mapEntranceEditor.getMapEntrance(mapId).copy();
                                }
                            }
                            continue;
                        }

                        if (mapEntrance == null) {
                            // 没有读取到目标地图
                            System.err.printf("边界和出入口导入：未读取到目标地图，无效的数据：line=%d：%s", line, entranceTxtLine);
                            continue;
                        }

                        if (entranceTxtLine.startsWith("border=")) {
                            // 边界
                            // FF:FF:FF FF:FF:FF FF:FF:FF FF:FF:FF
                            // 上一个位置
                            entranceTxtLine = entranceTxtLine.substring(7);
                            if (entranceTxtLine.isEmpty()) {
                                mapEntrance.getBorder().setType(MapBorderType.LAST);
                                continue;
                            }

                            // 固定位置
                            String[] split = entranceTxtLine.split(" ");
                            if (split.length == 0x01) {
                                mapEntrance.getBorder().setType(MapBorderType.FIXED);
                                int map = Integer.parseInt(split[0].substring(0, 2), 16);
                                int x = Integer.parseInt(split[0].substring(3, 5), 16);
                                int y = Integer.parseInt(split[0].substring(6, 8), 16);
                                mapEntrance.getBorder().add(0, new MapPoint(map, x, y));
                                continue;
                            }

                            if (split.length < 4) {
                                System.err.printf("边界和出入口导入：不完整的边界目标位置，无效的数据：line=%d：%s", line, entranceTxtLine);
                                continue;
                            }

                            // 不同方向不同位置
                            mapEntrance.getBorder().setType(MapBorderType.DIRECTION);
                            for (int i = 0; i < 4; i++) {
                                int map = Integer.parseInt(split[i].substring(0, 2), 16);
                                int x = Integer.parseInt(split[i].substring(3, 5), 16);
                                int y = Integer.parseInt(split[i].substring(6, 8), 16);
                                mapEntrance.getBorder().add(i, new MapPoint(map, x, y));
                            }
                        }
                        if (MAP_ENTRANCE_PATTERN.matcher(entranceTxtLine).find()) {
                            // FF:FF FF:FF:FF
                            MapPoint inPoint = new MapPoint(0x00,
                                    Integer.parseInt(entranceTxtLine.substring(0, 2), 16),
                                    Integer.parseInt(entranceTxtLine.substring(3, 5), 16)
                            );
                            MapPoint outPoint = new MapPoint(
                                    Integer.parseInt(entranceTxtLine.substring(6, 8), 16),
                                    Integer.parseInt(entranceTxtLine.substring(9, 11), 16),
                                    Integer.parseInt(entranceTxtLine.substring(12, 14), 16)
                            );
                            mapEntrance.getEntrances().put(inPoint, outPoint);
                        }
                    }

                    // 导入完成，更新一下列表
                    int selectMap = mapList.getSelectedIndex();
                    if (selectMap != -1) {
                        mapList.removeSelectionInterval(selectMap, selectMap);
                        mapList.setSelectedIndex(selectMap);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintWriter printWriter = new PrintWriter(out);
                    ex.printStackTrace(printWriter);
                    JOptionPane.showMessageDialog(this, "加载失败\n" + out);
                }
            }
        });

        fileMenu.add(fileMenuExport);
        fileMenu.add(fileMenuImport);

        menuBar.add(fileMenu);
    }

    @Override
    protected void createLayout() {
        mapFilter.addActionListener(e -> {
            if (e.getActionCommand().isEmpty()) {
                mapList.setRowFilter(null);
            } else {
                mapList.setRowFilter(new RowFilter<ListModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                        return entry.getValue(0).toString().contains(e.getActionCommand());
                    }
                });
            }
        });

        IMapEntranceEditor mapEntranceEditor = getMetalMaxRe().getEditorManager().getEditor(IMapEntranceEditor.class);
        Map<Integer, MapEntrance> mapEntrances = mapEntranceEditor.getMapEntrances();

        IMapEditor mapEditor = getMetalMaxRe().getEditorManager().getEditor(IMapEditor.class);
        String[] maps = new String[mapEditor.getMapMaxCount()];
        for (int i = 0; i < mapEditor.getMapMaxCount(); i++) {
            maps[i] = String.format("%02X", i);
        }
        mapList.setListData(maps);

        mapList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || mapList.isSelectionEmpty()) {
                return;
            }
            int selectedMap = mapList.convertIndexToModel(mapList.getSelectedIndex());

            final MapEntrance mapEntrance = mapEntrances.get(selectedMap);

            EntranceTableModel dataModel = new EntranceTableModel(mapEntrance);
            entrances.setModel(dataModel);

            dataModel.addTableModelListener(tableModelEvent -> {
                if (tableModelEvent.getType() == TableModelEvent.UPDATE) {
                    mapEntrance.getEntrances().clear();
                    for (Vector vector : dataModel.getDataVector()) {
                        int inPointX = Integer.parseInt(vector.get(0).toString(), 16);
                        int inPointY = Integer.parseInt(vector.get(1).toString(), 16);
                        int outPointMap = Integer.parseInt(vector.get(2).toString(), 16);
                        int outPointX = Integer.parseInt(vector.get(3).toString(), 16);
                        int outPointY = Integer.parseInt(vector.get(4).toString(), 16);
                        mapEntrance.getEntrances().put(new MapPoint(selectedMap, inPointX, inPointY), new MapPoint(outPointMap, outPointX, outPointY));
                    }
                }
            });
            updateHint();
        });

        addEntrance.addActionListener(e -> {
            if (mapList.isSelectionEmpty()) {
                return;
            }
            int selectedMap = mapList.convertIndexToModel(mapList.getSelectedIndex());

            MapPoint inMapPoint = new MapPoint();
            MapPoint outMapPoint = new MapPoint();
            String text;

            inMapPoint.setMap(selectedMap);
            text = inX.getText();
            if (text.isEmpty()) {
                text = "00";
            }
            inMapPoint.setX(Integer.parseInt(text, 16));

            text = inY.getText();
            if (text.isEmpty()) {
                text = "00";
            }
            inMapPoint.setY(Integer.parseInt(text, 16));

            text = outMap.getText();
            if (text.isEmpty()) {
                text = "00";
            }
            outMapPoint.setMap(Integer.parseInt(text, 16));

            text = outX.getText();
            if (text.isEmpty()) {
                text = "00";
            }
            outMapPoint.setX(Integer.parseInt(text, 16));

            text = outY.getText();
            if (text.isEmpty()) {
                text = "00";
            }
            outMapPoint.setY(Integer.parseInt(text, 16));

            ((DefaultTableModel) entrances.getModel()).addRow(new String[]{
                    inX.getText(),
                    inY.getText(),
                    outMap.getText(),
                    outX.getText(),
                    outY.getText()
            });

            final MapEntrance mapEntrance = mapEntrances.get(selectedMap);
            mapEntrance.getEntrances().put(inMapPoint, outMapPoint);

            entrances.updateUI();
            updateHint();
        });

        removeEntrance.addActionListener(e -> {
            if (entrances.getSelectedRow() == -1 || mapList.isSelectionEmpty()) {
                return;
            }
            if (entrances.isEditing()) {
                // 如果正在编辑，取消编辑
                entrances.getCellEditor().cancelCellEditing();
            }

            int selectedMap = mapList.convertIndexToModel(mapList.getSelectedIndex());
            final MapEntrance mapEntrance = mapEntrances.get(selectedMap);

            DefaultTableModel model = (DefaultTableModel) entrances.getModel();

            int[] selectedRows = entrances.getSelectedRows();
            for (int i = 0; i < selectedRows.length; i++) {
                selectedRows[i] = entrances.convertRowIndexToModel(selectedRows[i]);
            }
            // 倒序移除，否则会影响索引
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                Vector vector = model.getDataVector().get(selectedRows[i]);

                int inPointX = Integer.parseInt(vector.get(0).toString(), 16);
                int inPointY = Integer.parseInt(vector.get(1).toString(), 16);
                int outPointMap = Integer.parseInt(vector.get(2).toString(), 16);
                int outPointX = Integer.parseInt(vector.get(3).toString(), 16);
                int outPointY = Integer.parseInt(vector.get(4).toString(), 16);

                MapPoint inPoint = new MapPoint(selectedMap, inPointX, inPointY);
                MapPoint outPoint = new MapPoint(outPointMap, outPointX, outPointY);

                for (Map.Entry<MapPoint, MapPoint> pointEntry : new ArrayList<>(mapEntrance.getEntrances().entrySet())) {
                    if (Objects.equals(pointEntry.getKey(), inPoint) && Objects.equals(pointEntry.getValue(), outPoint)) {
                        mapEntrance.getEntrances().remove(pointEntry.getKey());
                        break;
                    }
                }
                model.removeRow(selectedRows[i]);
            }
            updateHint();
        });

        // 初始选中的地图
        if (getInitMap() != null) {
            mapList.setSelectedIndex(getInitMap());
        }
    }

    private void updateHint() {
        hint.setText(String.format("数量：%d", entrances.getModel().getRowCount()));
    }


    public static class EntranceTableModel extends DefaultTableModel {
        private static final String[] COLUMN_NAMES = {"入口X", "入口Y", "出口Map", "出口X", "出口Y"};

        public EntranceTableModel(@NotNull MapEntrance mapEntrance) {
            super(mapEntrance.getEntrances().entrySet().stream()
                    .map(m -> {
                        MapPoint inMapPoint = m.getKey();
                        MapPoint outMapPoint = m.getValue();
                        return new String[]{
                                String.format("%02X", inMapPoint.getX()),
                                String.format("%02X", inMapPoint.getY()),
                                String.format("%02X", outMapPoint.getMap()),
                                String.format("%02X", outMapPoint.getX()),
                                String.format("%02X", outMapPoint.getY())
                        };
                    }).toList().toArray(new String[0][]), COLUMN_NAMES);
        }
    }
}
