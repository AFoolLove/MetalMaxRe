package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.desktop.FormatterJSpinnerEditor;
import me.afoolslove.metalmaxre.desktop.HexFormatter;
import me.afoolslove.metalmaxre.desktop.Main;
import me.afoolslove.metalmaxre.desktop.ValueMouseWheelListener;
import me.afoolslove.metalmaxre.desktop.adapter.BoxSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.ComboBoxEnterSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.FocusSelectAllAdapter;
import me.afoolslove.metalmaxre.desktop.formatter.NumberFormatter;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.tank.ITankEditor;
import me.afoolslove.metalmaxre.editors.tank.Tank;
import me.afoolslove.metalmaxre.editors.tank.TankInitialAttribute;
import me.afoolslove.metalmaxre.editors.tank.TankWeaponSlot;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TankEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JComboBox<String> tanks;
    private JComboBox<String> equipment0;
    private JComboBox<String> equipment1;
    private JComboBox<String> equipment2;
    private JComboBox<String> equipment3;
    private JComboBox<String> equipment4;
    private JComboBox<String> equipment5;
    private JCheckBox equipmentState0;
    private JCheckBox equipmentState1;
    private JCheckBox equipmentState2;
    private JCheckBox equipmentState3;
    private JCheckBox equipmentState4;
    private JCheckBox equipmentState5;
    private JCheckBox slotSecondaryGun;
    private JCheckBox slotSpecialEquipment;
    private JSpinner weight;
    private JSpinner defense;
    private JSpinner maxShells;
    private JButton save;
    private JCheckBox slotMainGun;
    private JSpinner defenseUpStep;
    private JSpinner shellsUpStep;
    private JSpinner initMap;
    private JSpinner initX;
    private JSpinner initY;
    private JButton selectPoint;
    private JButton gotoPoint;

    public TankEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("坦克编辑器", contentPane);
    }

    @Override
    protected void createLayout() {
        ItemListener itemListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ITankEditor tankEditor = getMetalMaxRe().getEditorManager().getEditor(ITankEditor.class);

                TankInitialAttribute initialAttributes = tankEditor.getTankInitAttribute(tanks.getSelectedIndex());
                // 初始坐标
                initMap.setValue(initialAttributes.intMap());
                initX.setValue(initialAttributes.intX());
                initY.setValue(initialAttributes.intY());
                // 底盘重量
                weight.setValue(initialAttributes.getWeight() & 0xFFFF);
                // 底盘防御
                defense.setValue(initialAttributes.getDefense() & 0xFFFF);
                // 弹仓上限
                maxShells.setValue(initialAttributes.getMaxShells() & 0xFF);
                // 开洞状态
                slotMainGun.setSelected((initialAttributes.getSlot() & TankWeaponSlot.MAIN_GUN.getSlot()) != 0x00);
                slotSecondaryGun.setSelected((initialAttributes.getSlot() & TankWeaponSlot.SECONDARY_GUN.getSlot()) != 0x00);
                slotSpecialEquipment.setSelected((initialAttributes.getSlot() & TankWeaponSlot.SPECIAL_EQUIPMENT.getSlot()) != 0x00);

                // 装备的装备状态
                byte equipmentState = initialAttributes.getEquipmentState();
                equipmentState0.setSelected((equipmentState & 0B1000_0000) != 0x00);
                equipmentState1.setSelected((equipmentState & 0B0100_0000) != 0x00);
                equipmentState2.setSelected((equipmentState & 0B0010_0000) != 0x00);
                equipmentState3.setSelected((equipmentState & 0B0001_0000) != 0x00);
                equipmentState4.setSelected((equipmentState & 0B0000_1000) != 0x00);
                equipmentState5.setSelected((equipmentState & 0B0000_0100) != 0x00);

                // 装备
                equipment0.setSelectedIndex(initialAttributes.getEquipment(0) & 0xFF);
                equipment1.setSelectedIndex(initialAttributes.getEquipment(1) & 0xFF);
                equipment2.setSelectedIndex(initialAttributes.getEquipment(2) & 0xFF);
                equipment3.setSelectedIndex(initialAttributes.getEquipment(3) & 0xFF);
                equipment4.setSelectedIndex(initialAttributes.getEquipment(4) & 0xFF);
                equipment5.setSelectedIndex(initialAttributes.getEquipment(5) & 0xFF);
            }
        };
        tanks.addItemListener(itemListener);
        tanks.setModel(new DefaultComboBoxModel<>(Arrays.stream(Tank.values()).map(Tank::getName).toList().toArray(new String[0x00])));
        gotoPoint.addActionListener(e -> {
            int map = ((Number) initMap.getValue()).intValue();
            int x = ((Number) initX.getValue()).intValue();
            int y = ((Number) initY.getValue()).intValue();

            Main frame = (Main) getFrame();
            frame.gotoPoint(map, x, y);
        });


        selectPoint.addActionListener(e -> {
            selectPoint.setEnabled(false);
            Main frame = (Main) getFrame();
            frame.getSelectListeners().add(new BoxSelectedAdapter.SelectListener() {
                @Override
                public void select(int x, int y) {
                    if (!isVisible()) {
                        frame.getSelectListeners().remove(this);
                        selectPoint.setEnabled(true);
                    }
                }

                @Override
                public void selected(int x, int y) {
                    frame.getSelectListeners().remove(this);
                    selectPoint.setEnabled(true);
                    if (isVisible()) {
                        int option = JOptionPane.showConfirmDialog(getFrame(),
                                String.format("Map:%02X X:%02X Y:%02X", frame.getSelectedMap(), x, y),
                                String.format("%s 初始坐标设置", tanks.getSelectedItem()), JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            initMap.setValue(frame.getSelectedMap());
                            initX.setValue(x);
                            initY.setValue(y);
                        }
                    }
                }
            });
            if (!getFrame().isActive()) {
                getFrame().setState(JFrame.ICONIFIED);
                getFrame().setState(JFrame.NORMAL);
            }
        });
        save.addActionListener(e -> {
            ITankEditor tankEditor = getMetalMaxRe().getEditorManager().getEditor(ITankEditor.class);
            TankInitialAttribute initialAttributes = tankEditor.getTankInitAttribute(tanks.getSelectedIndex());
            // 设置初始坐标
            initialAttributes.setMap(((Number) initMap.getValue()).intValue());
            initialAttributes.setX(((Number) initX.getValue()).intValue());
            initialAttributes.setY(((Number) initY.getValue()).intValue());
            // 初始地盘重量
            initialAttributes.setWeight(((Number) weight.getValue()).intValue());
            // 初始防御力
            initialAttributes.setDefense(((Number) defense.getValue()).intValue());
            // 初始弹仓上限
            initialAttributes.setMaxShells(((Number) maxShells.getValue()).intValue());
            // 设置初始装备
            initialAttributes.setEquipment(0, equipment0.getSelectedIndex());
            initialAttributes.setEquipment(1, equipment1.getSelectedIndex());
            initialAttributes.setEquipment(2, equipment2.getSelectedIndex());
            initialAttributes.setEquipment(3, equipment3.getSelectedIndex());
            initialAttributes.setEquipment(4, equipment4.getSelectedIndex());
            initialAttributes.setEquipment(5, equipment5.getSelectedIndex());

            // 设置装备初始状态
            byte equipmentState = 0;
            if (equipmentState0.isSelected()) {
                equipmentState |= 0B1000_0000;
            }
            if (equipmentState1.isSelected()) {
                equipmentState |= 0B0100_0000;
            }
            if (equipmentState2.isSelected()) {
                equipmentState |= 0B0010_0000;
            }
            if (equipmentState3.isSelected()) {
                equipmentState |= 0B0001_0000;
            }
            if (equipmentState4.isSelected()) {
                equipmentState |= 0B0000_1000;
            }
            if (equipmentState5.isSelected()) {
                equipmentState |= 0B0000_0100;
            }
            initialAttributes.setEquipmentState(equipmentState);

            // 设置初始开洞状态
            byte slot = 0;
            if (slotMainGun.isSelected()) {
                slot |= TankWeaponSlot.MAIN_GUN.getSlot();
            }
            if (slotSecondaryGun.isSelected()) {
                slot |= TankWeaponSlot.SECONDARY_GUN.getSlot();
            }
            if (slotSpecialEquipment.isSelected()) {
                slot |= TankWeaponSlot.SPECIAL_EQUIPMENT.getSlot();
            }
            initialAttributes.setSlot(slot);
        });

        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        IItemEditor iItemEditor = getMetalMaxRe().getEditorManager().getEditor(IItemEditor.class);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < iItemEditor.getItems().size(); i++) {
            items.add(String.format("%02X %s", i, textEditor.getItemName(i)));
        }
        String[] itemsObj = items.toArray(new String[0]);

        equipment0.setModel(new DefaultComboBoxModel<>(itemsObj));
        equipment1.setModel(new DefaultComboBoxModel<>(itemsObj));
        equipment2.setModel(new DefaultComboBoxModel<>(itemsObj));
        equipment3.setModel(new DefaultComboBoxModel<>(itemsObj));
        equipment4.setModel(new DefaultComboBoxModel<>(itemsObj));
        equipment5.setModel(new DefaultComboBoxModel<>(itemsObj));
        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                maxShells,
                defenseUpStep, shellsUpStep
        }) {
            spinner.setModel(new SpinnerNumberModel(0, 0x00, 0xFF, 1));
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, NumberFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
        for (JSpinner spinner : new JSpinner[]{
                initMap,
                initX, initY
        }) {
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, HexFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
        // 对JSpinner添加两个十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                weight, defense
        }) {
            spinner.setModel(new SpinnerNumberModel(0, 0x00, 0xFFFF, 1));
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, NumberFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }

        // 为装备和道具可以输入id后直接设置
        for (JComboBox<?> comboBox : new JComboBox<?>[]{
                equipment0, equipment1,
                equipment2, equipment3,
                equipment4, equipment5
        }) {
            comboBox.getEditor().getEditorComponent().addKeyListener(new ComboBoxEnterSelectedAdapter(comboBox));
            FocusSelectAllAdapter.addAdapter(comboBox.getEditor().getEditorComponent());
        }
        itemListener.itemStateChanged(new ItemEvent(tanks, 0, null, ItemEvent.SELECTED));
    }
}
