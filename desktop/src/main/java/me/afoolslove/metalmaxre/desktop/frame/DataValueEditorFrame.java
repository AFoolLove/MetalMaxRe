package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class DataValueEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JXTable dataValueTable;
    private JXSearchField dataValueFilter;

    public DataValueEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("数据值编辑器", contentPane);
    }

    @Override
    protected void createLayout() {
        dataValueFilter.addActionListener(e -> {
            if (e.getActionCommand().isEmpty()) {
                dataValueTable.setRowFilter(null);
            } else {
                dataValueTable.setRowFilter(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
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

        dataValueTable.setModel(new DefaultTableModel() {
            private final IDataValueEditor dataValueEditor = getMetalMaxRe().getEditorManager().getEditor(IDataValueEditor.class);
            private final String[] columnNames = {"索引", "数据", "数值"};
            private int rowCount;

            {
                rowCount += dataValueEditor.get1ByteMaxCount();
                rowCount += dataValueEditor.get2ByteMaxCount();
                rowCount += dataValueEditor.get3ByteMaxCount();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0x00 || column == 0x01) {
                    // 索引和数据不能更改
                    return false;
                }
                return super.isCellEditable(row, column);
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public int getRowCount() {
                return rowCount;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                fireTableCellUpdated(row, column);

                if (column == 0x00 || column == 0x01) {
                    return;
                }
                if (column == 0x02) {
                    dataValueEditor.getValues().put(row, Integer.parseInt(aValue.toString()));
                }
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 0x00) {
                    // 索引
                    return String.format("%02X", row);
                }

                if (row < dataValueEditor.get1ByteMaxCount()) {
                    switch (column) {
                        case 0x01:
                            return String.format("%02X", dataValueEditor.getValues().get(row).intValue());
                        case 0x02:
                            return dataValueEditor.getValues().get(row).intValue();
                    }
                } else if (row < (dataValueEditor.get1ByteMaxCount() + dataValueEditor.get2ByteMaxCount())) {
                    switch (column) {
                        case 0x01:
                            return String.format("%04X", dataValueEditor.getValues().get(row).intValue());
                        case 0x02:
                            return dataValueEditor.getValues().get(row).intValue();
                    }
                } else {
                    switch (column) {
                        case 0x01:
                            final int A = 0x0A;  // 程序中写死的值
                            var value = dataValueEditor.getValues().get(row).intValue();
                            int x09 = value / (0x100 * A); // 得到 x09
                            int x08 = (value - (x09 * 0x100 * A)) / A; // 得到 x08

                            return String.format("%04X", (x08 << 8) + x09);
                        case 0x02:
                            return dataValueEditor.getValues().get(row).intValue();
                    }
                }
                return -1;
            }
        });
//        dataValueTable.getModel().addTableModelListener(e -> {
//            if (e.getType() == TableModelEvent.UPDATE) {
//                if (e.getColumn() == 0x00 || e.getColumn() == 0x01) {
//                    // 忽略索引和数据更新
//                    return;
//                }
//
//                if (e.getColumn() == 0x02) {
//                    // 数值更新（int）
//                    Object valueAt = dataValueTable.getValueAt(e.getLastRow(), 0x02);
//                    System.out.println();
//                }
//            }
//        });
    }
}
