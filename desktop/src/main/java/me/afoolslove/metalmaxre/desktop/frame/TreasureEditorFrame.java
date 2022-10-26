package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.desktop.HexJSpinnerEditor;
import me.afoolslove.metalmaxre.desktop.Main;
import me.afoolslove.metalmaxre.desktop.ValueMouseWheelListener;
import me.afoolslove.metalmaxre.desktop.adapter.BoxSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.ComboBoxEnterSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.FocusSelectAllAdapter;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.items.Item;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.Treasure;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class TreasureEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JXList mapList;
    private JXTable treasures;
    private JComboBox<String> treasureItem;
    private JSpinner treasureMap;
    private JSpinner treasureX;
    private JSpinner treasureY;
    private JButton treasureRemove;
    private JButton treasureAdd;
    private JXSearchField searchTreasure;
    private JLabel treasureCount;
    private JButton gotoTreasure;
    private JButton selectTreasure;
    private JButton replaceTreasure;
    private JXSearchField searchMap;

    private final Main main;

    public TreasureEditorFrame(@NotNull Main frame, @NotNull MetalMaxRe metalMaxRe) {
        this(null, frame, metalMaxRe);
    }

    public TreasureEditorFrame(Integer initMap, @NotNull Main frame, @Nullable MetalMaxRe metalMaxRe) {
        super(initMap, frame, metalMaxRe);
        this.main = frame;
        init("宝藏编辑器", contentPane);
    }

    @Override
    protected void createLayout() {
        searchMap.addActionListener(e -> {
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

        searchTreasure.addActionListener(e -> {
            if (e.getActionCommand().isEmpty()) {
                // 如果选中了地图，重新选中一次
                int[] selectedIndices = mapList.getSelectedIndices();
                if (selectedIndices.length > 0) {
                    mapList.removeSelectionInterval(0, mapList.getModel().getSize() - 1);
                    mapList.setSelectedIndices(selectedIndices);
                } else {
                    // 没有选中地图
                    treasures.setRowFilter(null);
                }
            } else {
                treasures.setRowFilter(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                        int[] selectedIndices = mapList.getSelectedIndices();
                        if (selectedIndices.length > 0) {
                            // 得到真实索引
                            for (int i = 0; i < selectedIndices.length; i++) {
                                selectedIndices[i] = mapList.convertIndexToModel(selectedIndices[i]);
                            }
                            // 有选中的地图，过滤不在选中地图的宝藏
                            int map = Integer.parseInt(entry.getValue(0).toString(), 16);
                            if (Arrays.binarySearch(selectedIndices, map) < 0) {
                                return false;
                            }
                        }

                        for (int i = 0; i < entry.getValueCount(); i++) {
                            if (entry.getValue(i).toString().contains(e.getActionCommand())) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        });


        IMapEditor mapEditor = getMetalMaxRe().getEditorManager().getEditor(IMapEditor.class);

        String[] mapItems = new String[mapEditor.getMapMaxCount()];
        for (int i = 0; i < mapItems.length; i++) {
            mapItems[i] = String.format("%02X", i);
        }
        mapList.setListData(mapItems);

        mapList.addListSelectionListener(e -> {
            // 选中的地图
            int[] selectedIndices = mapList.getSelectedIndices();
            if (selectedIndices.length == 0) {
                treasures.setRowFilter(null);
            } else {
                // 得到真实索引
                for (int i = 0; i < selectedIndices.length; i++) {
                    selectedIndices[i] = mapList.convertIndexToModel(selectedIndices[i]);
                }
                treasures.setRowFilter(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                        return Arrays.binarySearch(selectedIndices, Integer.parseInt(entry.getValue(0).toString(), 16)) >= 0;
                    }
                });
            }
        });

        ITreasureEditor treasureEditor = getMetalMaxRe().getEditorManager().getEditor(ITreasureEditor.class);
        String[][] treasureData = new String[treasureEditor.getTreasures().size()][];
        for (int i = 0; i < treasureEditor.getTreasures().size(); i++) {
            Treasure treasure = treasureEditor.getTreasures().get(i);
            treasureData[i] = new String[]{
                    String.format(String.format("%02X", treasure.getMap())),
                    String.format(String.format("%02X", treasure.getX())),
                    String.format(String.format("%02X", treasure.getY())),
                    String.format(String.format("%02X", treasure.getItem()))
            };
        }
        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        IItemEditor iItemEditor = getMetalMaxRe().getEditorManager().getEditor(IItemEditor.class);
        List<Item> items = iItemEditor.getItems();

        final String[] itemNames = new String[0x100];
        for (int i = 0; i < itemNames.length; i++) {
            if (i < items.size()) {
                itemNames[i] = String.format("%02X %s", i, textEditor.getItemName(i));
            } else {
                itemNames[i] = String.format("%02X", i);
            }
        }

        DefaultTableModel treasureDataModel = new DefaultTableModel(
                treasureData,
                new String[]{"地图", "X", "Y", "物品"}
        );
        treasures.setModel(treasureDataModel);
        treasures.getColumn(0x03).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                value = itemNames[Integer.parseInt(value.toString(), 16)];
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        treasures.getColumn(0x03).setCellEditor(new DefaultCellEditor(new JTextField()) {
            private final JComboBox<String> comboBox = new JComboBox<>();

            {
                comboBox.setEditable(true);
                comboBox.setModel(new DefaultComboBoxModel<>(itemNames));

                treasureItem.setModel(new DefaultComboBoxModel<>(itemNames));
                FocusSelectAllAdapter.addAdapter(comboBox.getEditor().getEditorComponent());
                comboBox.getEditor().getEditorComponent().addKeyListener(new ComboBoxEnterSelectedAdapter(comboBox));
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                comboBox.setSelectedIndex(Integer.parseInt(value.toString(), 16));
                return comboBox;
            }

            @Override
            public Object getCellEditorValue() {
                return String.format("%02X", comboBox.getSelectedIndex());
            }
        });
        treasureDataModel.addTableModelListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) e.getSource();

            List<Treasure> treasures = treasureEditor.getTreasures();
            if (e.getType() == TableModelEvent.UPDATE) {
                // 过滤不会影响索引，索引为实际索引
                Vector<?> vector = tableModel.getDataVector().get(e.getLastRow());
                int value = Integer.parseInt(vector.get(e.getColumn()).toString(), 16);
                Treasure treasure = treasures.get(e.getLastRow());
                switch (e.getColumn()) {
                    case 0 -> treasure.setMap(value);
                    case 1 -> treasure.setX(value);
                    case 2 -> treasure.setY(value);
                    case 3 -> treasure.setItem(value);
                }
            }

            updateTreasureCount();
        });

        treasures.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && treasures.getSelectedRowCount() > 0) {
                // 选中项目会设置到下方的数据中
                DefaultTableModel model = (DefaultTableModel) treasures.getModel();
                Vector<?> vector = model.getDataVector().get(treasures.convertRowIndexToModel(treasures.getSelectedRow()));
                treasureMap.setValue(Integer.parseInt(vector.get(0).toString(), 16));
                treasureX.setValue(Integer.parseInt(vector.get(1).toString(), 16));
                treasureY.setValue(Integer.parseInt(vector.get(2).toString(), 16));
                treasureItem.setSelectedIndex(Integer.parseInt(vector.get(3).toString(), 16));
            }
        });

        treasureRemove.addActionListener(e -> {
            if (treasures.getSelectedRowCount() == 0) {
                // 未选中
                return;
            }

            int[] selectedRows = treasures.getSelectedRows();
            // 得到真实索引
            for (int i = 0; i < selectedRows.length; i++) {
                selectedRows[i] = treasures.convertRowIndexToModel(selectedRows[i]);
            }
            DefaultTableModel model = (DefaultTableModel) treasures.getModel();
            Vector<Vector> dataVector = model.getDataVector();
            // 倒序移除，否则会影响索引
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                Vector vector = dataVector.get(selectedRows[i]);

                int map = Integer.parseInt(vector.get(0).toString(), 16);
                int x = Integer.parseInt(vector.get(1).toString(), 16);
                int y = Integer.parseInt(vector.get(2).toString(), 16);
                int item = Integer.parseInt(vector.get(3).toString(), 16);
                treasureEditor.getTreasures().remove(new Treasure(map, x, y, item));
                model.removeRow(selectedRows[i]);
            }
            updateTreasureCount();
        });
        treasureAdd.addActionListener(e -> {
            int map = ((Number) treasureMap.getValue()).intValue();
            int x = ((Number) treasureX.getValue()).intValue();
            int y = ((Number) treasureY.getValue()).intValue();
            int item = treasureItem.getSelectedIndex();
            ((DefaultTableModel) treasures.getModel()).addRow(
                    new String[]{
                            String.format("%02X", map),
                            String.format("%02X", x),
                            String.format("%02X", y),
                            String.format("%02X", item)
                    }
            );
            treasureEditor.getTreasures().add(new Treasure(map, x, y, item));
            updateTreasureCount();
        });

        gotoTreasure.addActionListener(e -> {
            int map = ((Number) treasureMap.getValue()).intValue();
            int x = ((Number) treasureX.getValue()).intValue();
            int y = ((Number) treasureY.getValue()).intValue();
            main.gotoPoint(map, x, y);

            main.setState(ICONIFIED);
            main.setState(NORMAL);
        });

        selectTreasure.addActionListener(e -> {
            selectTreasure.setEnabled(false);
            main.getSelectListeners().add(new BoxSelectedAdapter.SelectListener() {
                @Override
                public void select(int x, int y) {
                    if (!isVisible()) {
                        main.getSelectListeners().remove(this);
                        selectTreasure.setEnabled(true);
                    }
                }

                @Override
                public void selected(int x, int y) {
                    main.getSelectListeners().remove(this);
                    selectTreasure.setEnabled(true);
                    if (isVisible()) {
                        int option = JOptionPane.showConfirmDialog(getFrame(),
                                String.format("Map:%02X X:%02X Y:%02X", main.getSelectedMap(), x, y),
                                "宝藏位置选择", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            treasureMap.setValue(main.getSelectedMap());
                            treasureX.setValue(x);
                            treasureY.setValue(y);
                        }

                        setState(ICONIFIED);
                        setState(NORMAL);
                    }
                }
            });

            main.setState(ICONIFIED);
            main.setState(NORMAL);
        });

        replaceTreasure.addActionListener(e -> {
            if (treasures.getSelectedRowCount() == 0) {
                // 未选中
                return;
            }
//
//            int[] selectedRows = treasures.getSelectedRows();
//            // 得到真实索引
//            for (int i = 0; i < selectedRows.length; i++) {
//                selectedRows[i] = treasures.convertRowIndexToModel(selectedRows[i]);
//            }
//            DefaultTableModel model = (DefaultTableModel) treasures.getModel();
//            Vector<Vector> dataVector = model.getDataVector();
//            // 倒序移除，否则会影响索引
//            for (int i = selectedRows.length - 1; i >= 0; i--) {
//                Vector vector = dataVector.get(selectedRows[i]);
//
//                int map = Integer.parseInt(vector.get(0).toString(), 16);
//                int x = Integer.parseInt(vector.get(1).toString(), 16);
//                int y = Integer.parseInt(vector.get(2).toString(), 16);
//                int item = Integer.parseInt(vector.get(3).toString(), 16);
//                model.removeRow(selectedRows[i]);
//            }
//            model.fireTableRowsUpdated();
//            updateTreasureCount();
        });

        for (JComboBox<?> comboBox : new JComboBox<?>[]{
                treasureItem
        }) {
            treasureItem.setModel(new DefaultComboBoxModel<>(itemNames));
            FocusSelectAllAdapter.addAdapter(comboBox.getEditor().getEditorComponent());
            comboBox.getEditor().getEditorComponent().addKeyListener(new ComboBoxEnterSelectedAdapter(comboBox));
        }

        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                treasureMap, treasureX, treasureY
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }

        // 初始选中的地图
        if (getInitMap() != null) {
            mapList.setSelectedIndex(getInitMap());
        }

        updateTreasureCount();

        // 初始选中的地图
        if (getInitMap() != null) {
            mapList.setSelectedValue(getInitMap(), true);
        }
    }

    private void updateTreasureCount() {
        ITreasureEditor treasureEditor = getMetalMaxRe().getEditorManager().getEditor(ITreasureEditor.class);
        treasureCount.setText(String.format("%d/%d", treasureEditor.getTreasures().size(), treasureEditor.getTreasureMaxCount()));
    }
}
