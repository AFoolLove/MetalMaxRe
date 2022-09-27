package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.desktop.MapSelectListModel;
import me.afoolslove.metalmaxre.editors.map.IMapEntranceEditor;
import me.afoolslove.metalmaxre.editors.map.MapEntrance;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapEntranceEditorFrame extends JFrame {
    private JPanel contentPane;
    private JTable entrances;
    private JList<String> mapList;
    private JTextField inX;
    private JTextField inY;
    private JTextField outMap;
    private JTextField outX;
    private JTextField outY;
    private JButton addEntrance;
    private JTextField mapFilter;
    private JLabel hint;
    private JButton removeEntrance;


    private final MultipleMetalMaxRe multipleMetalMaxRe;

    private MapSelectListModel mapSelectListModel;

    public MapEntranceEditorFrame(@NotNull Frame frame, @NotNull MultipleMetalMaxRe multipleMetalMaxRe) {
        this.multipleMetalMaxRe = multipleMetalMaxRe;

        MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
        Path path = metalMaxRe.getBuffer().getPath();
        if (path != null) {
            setTitle(String.format("出入口编辑器 [%s] - %s", path.getName(path.getNameCount() - 1), path));
        } else {
            setTitle(String.format("出入口编辑器 [%s]", metalMaxRe.getBuffer().getVersion().getName()));
        }

        setContentPane(contentPane);
//        setModal(true);

        setLocation(frame.getX(), frame.getY());

        IMapEntranceEditor mapEntranceEditor = metalMaxRe.getEditorManager().getEditor(IMapEntranceEditor.class);

        Map<Integer, MapEntrance> mapEntrances = mapEntranceEditor.getMapEntrances();

        mapSelectListModel = new MapSelectListModel(mapList, multipleMetalMaxRe, mapFilter);
        mapList.setModel(mapSelectListModel);
        mapList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            final MapEntrance mapEntrance = mapEntrances.get(mapSelectListModel.getSelectedMap());

            entrances.setModel(new EntranceTableModel(mapEntrance));

            updateHint();
        });

        addEntrance.addActionListener(e -> {
            MapPoint inMapPoint = new MapPoint();
            MapPoint outMapPoint = new MapPoint();
            String text;

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

            ((DefaultTableModel) entrances.getModel()).addRow(new Object[]{inMapPoint, outMapPoint});

            entrances.updateUI();

            updateHint();
        });

        removeEntrance.addActionListener(e -> {
            if (entrances.getSelectedRow() == -1) {
                return;
            }
            if (entrances.isEditing()) {
                // 如果正在编辑，取消编辑
                entrances.getCellEditor().cancelCellEditing();
            }
            DefaultTableModel model = (DefaultTableModel) entrances.getModel();
            model.removeRow(entrances.getSelectedRow());
            updateHint();
        });
    }

    private void updateHint() {
        hint.setText(String.format("数量：%d", entrances.getModel().getRowCount()));
    }


    public static class EntranceTableModel extends DefaultTableModel {
        private static final String[] COLUMN_NAMES = {"入口X", "入口Y", "出口Map", "出口X", "出口Y"};
        private final MapEntrance mapEntrance;
        private final List<Map.Entry<MapPoint, MapPoint>> entries;

        public EntranceTableModel(@NotNull MapEntrance mapEntrance) {
            this.mapEntrance = mapEntrance;
            this.entries = new ArrayList<>(mapEntrance.getEntrances().entrySet());
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public int getRowCount() {
            return entries == null ? 0 : entries.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public void addRow(Object[] rowData) {
            entries.add(Map.entry((MapPoint) rowData[0], (MapPoint) rowData[1]));

            mapEntrance.getEntrances().clear();
            for (Map.Entry<MapPoint, MapPoint> entry : entries) {
                mapEntrance.getEntrances().put(entry.getKey(), entry.getValue());
            }

            fireTableRowsInserted(entries.size(), entries.size());
        }

        @Override
        public void removeRow(int row) {
            fireTableRowsDeleted(row, row);
            entries.remove(row);

            mapEntrance.getEntrances().clear();
            for (Map.Entry<MapPoint, MapPoint> entry : entries) {
                mapEntrance.getEntrances().put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            fireTableCellUpdated(row, column);
            Map.Entry<MapPoint, MapPoint> pointEntry = entries.get(row);

            MapPoint inMapPoint = pointEntry.getKey();
            MapPoint outMapPoint = pointEntry.getValue();
            switch (column) {
                case 0x00 -> // inX
                        inMapPoint.setX(Integer.parseInt(aValue.toString(), 16));
                case 0x01 -> // inY
                        inMapPoint.setY(Integer.parseInt(aValue.toString(), 16));
                case 0x02 -> // ouMap
                        outMapPoint.setMap(Integer.parseInt(aValue.toString(), 16));
                case 0x03 -> // outY
                        outMapPoint.setX(Integer.parseInt(aValue.toString(), 16));
                case 0x04 -> // outY
                        outMapPoint.setY(Integer.parseInt(aValue.toString(), 16));
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Map.Entry<MapPoint, MapPoint> pointEntry = entries.get(row);

            MapPoint inMapPoint = pointEntry.getKey();
            MapPoint outMapPoint = pointEntry.getValue();
            return switch (column) {
                case 0x00 -> String.format("%02X", inMapPoint.getX());
                case 0x01 -> String.format("%02X", inMapPoint.getY());
                case 0x02 -> String.format("%02X", outMapPoint.getMap());
                case 0x03 -> String.format("%02X", outMapPoint.getX());
                case 0x04 -> String.format("%02X", outMapPoint.getY());
                default -> null;
            };
        }
    }
}
