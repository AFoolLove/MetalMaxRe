package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.Treasure;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class TreasureEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private org.jdesktop.swingx.JXList mapList;
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

    public TreasureEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("宝藏编辑器", contentPane);
    }

    @Override
    protected void createLayout() {
        IMapEditor mapEditor = getMetalMaxRe().getEditorManager().getEditor(IMapEditor.class);

        String[] mapItems = new String[mapEditor.getMapMaxCount()];
        for (int i = 0; i < mapItems.length; i++) {
            mapItems[i] = String.format("%02X", i);
        }
        mapList.setListData(mapItems);

        mapList.addListSelectionListener(e -> {
            treasures.setRowFilter(new RowFilter<TableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                    return Objects.equals(entry.getValue(0), mapList.getSelectedValue());
                }
            });
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
        DefaultTableModel treasureDataModel = new DefaultTableModel(
                treasureData,
                new String[]{"地图", "X", "Y", "物品"}
        );
        treasures.setModel(treasureDataModel);
        treasureDataModel.addTableModelListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) e.getSource();

            List<Treasure> treasures = treasureEditor.getTreasures();
            switch (e.getType()) {
                case TableModelEvent.INSERT -> {
                    // 插入数据
                    // TODO
                    Vector<?> vector = tableModel.getDataVector().get(e.getLastRow());
                    mapList.setSelectedIndex(Integer.parseInt(vector.get(0).toString(), 16));
                    treasures.add(new Treasure(
                            Integer.parseInt(vector.get(0).toString(), 16),
                            Integer.parseInt(vector.get(1).toString(), 16),
                            Integer.parseInt(vector.get(2).toString(), 16),
                            Integer.parseInt(vector.get(3).toString(), 16)
                    ));
                }
                case TableModelEvent.UPDATE -> {
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
                case TableModelEvent.DELETE -> {
                    // 过滤会影响索引，需要转换得到实际索引
                    int index = TreasureEditorFrame.this.treasures.convertRowIndexToModel(e.getLastRow());
                    treasures.remove(index);
                }
            }

            treasureCount.setText(String.format("%d/%d", treasures.size(), treasureEditor.getTreasureMaxCount()));
        });

        treasures.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                DefaultTableModel model = (DefaultTableModel) treasures.getModel();
                Vector<?> vector = model.getDataVector().get(e.getLastIndex());
                treasureMap.setValue(Integer.parseInt(vector.get(0).toString(), 16));
                treasureX.setValue(Integer.parseInt(vector.get(1).toString(), 16));
                treasureY.setValue(Integer.parseInt(vector.get(2).toString(), 16));
                treasureItem.setSelectedIndex(Integer.parseInt(vector.get(3).toString(), 16));
            }
        });

        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        IItemEditor iItemEditor = getMetalMaxRe().getEditorManager().getEditor(IItemEditor.class);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < iItemEditor.getItems().size(); i++) {
            items.add(String.format("%02X %s", i, textEditor.getItemName(i)));
        }
        String[] itemsObj = items.toArray(new String[0]);
        treasureItem.setModel(new DefaultComboBoxModel<>(itemsObj));

        treasureRemove.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) treasures.getModel();
            model.removeRow(treasures.getSelectedRow());
        });
        treasureAdd.addActionListener(e -> {
            ((DefaultTableModel) treasures.getModel()).addRow(
                    new String[]{
                            treasureMap.getValue().toString(),
                            treasureX.getValue().toString(),
                            treasureY.getValue().toString(),
                            String.format("%02X", treasureItem.getSelectedIndex())
                    }
            );
        });
    }
}
