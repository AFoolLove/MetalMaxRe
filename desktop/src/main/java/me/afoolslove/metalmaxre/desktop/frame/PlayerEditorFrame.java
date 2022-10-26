package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.desktop.FormatterJSpinnerEditor;
import me.afoolslove.metalmaxre.desktop.ValueMouseWheelListener;
import me.afoolslove.metalmaxre.desktop.adapter.ComboBoxEnterSelectedAdapter;
import me.afoolslove.metalmaxre.desktop.adapter.FocusSelectAllAdapter;
import me.afoolslove.metalmaxre.desktop.formatter.NumberFormatter;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.player.IPlayerEditor;
import me.afoolslove.metalmaxre.editors.player.Player;
import me.afoolslove.metalmaxre.editors.player.PlayerInitialAttributes;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JComboBox<String> players;
    private JComboBox<String> equipment0;
    private JComboBox<String> equipment1;
    private JComboBox<String> equipment2;
    private JComboBox<String> equipment3;
    private JComboBox<String> equipment4;
    private JComboBox<String> equipment5;
    private JComboBox<String> equipment6;
    private JComboBox<String> equipment7;
    private JCheckBox equipmentState0;
    private JCheckBox equipmentState1;
    private JCheckBox equipmentState2;
    private JCheckBox equipmentState3;
    private JCheckBox equipmentState4;
    private JCheckBox equipmentState5;
    private JCheckBox equipmentState6;
    private JCheckBox equipmentState7;
    private JComboBox<String> item0;
    private JComboBox<String> item1;
    private JComboBox<String> item2;
    private JComboBox<String> item3;
    private JComboBox<String> item4;
    private JComboBox<String> item5;
    private JComboBox<String> item6;
    private JComboBox<String> item7;
    private JSpinner money;
    private JSpinner level;
    private JSpinner health;
    private JSpinner maxHealth;
    private JSpinner battleSkill;
    private JSpinner repairSkill;
    private JSpinner attack;
    private JSpinner defense;
    private JSpinner strength;
    private JSpinner wisdom;
    private JSpinner vitality;
    private JSpinner speed;
    private JCheckBox buff0;
    private JCheckBox buff2;
    private JCheckBox buff4;
    private JCheckBox buff6;
    private JCheckBox buff5;
    private JCheckBox buff3;
    private JCheckBox buff7;
    private JCheckBox buff1;
    private JSpinner exp;
    private JButton save;
    private JSpinner drivingSkill;

    public PlayerEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("玩家编辑器", contentPane);
    }

    @Override
    protected void createLayout() {
        ItemListener itemListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                IPlayerEditor playerEditor = getMetalMaxRe().getEditorManager().getEditor(IPlayerEditor.class);

                int selectedIndex = players.getSelectedIndex();
                PlayerInitialAttributes initialAttributes = playerEditor.getInitialAttributes(selectedIndex);

                // 经验值
                exp.setValue(initialAttributes.getExperience());
                // 等级
                level.setValue(initialAttributes.getLevel() & 0xFF);
                // 当前生命值
                health.setValue(initialAttributes.getHealth() & 0xFFFF);
                // 最大生命值
                maxHealth.setValue(initialAttributes.getMaxHealth() & 0xFFFF);
                // 金钱
                money.setValue(playerEditor.getMoney());
                // 战斗等级
                battleSkill.setValue(initialAttributes.getBattleSkill() & 0xFF);
                // 驾驶等级
                drivingSkill.setValue(initialAttributes.getDrivingSkill() & 0xFF);
                // 修理等级
                repairSkill.setValue(initialAttributes.getRepairSkill() & 0xFF);

                // 攻击力
                attack.setValue(initialAttributes.getAttack() & 0xFF);
                // 防御力
                defense.setValue(initialAttributes.getDefense() & 0xFF);
                // 强度
                strength.setValue(initialAttributes.getStrength() & 0xFF);
                // 智力
                wisdom.setValue(initialAttributes.getWisdom() & 0xFF);
                // 体力
                vitality.setValue(initialAttributes.getVitality() & 0xFF);
                // 速度
                speed.setValue(initialAttributes.getSpeed() & 0xFF);

                // 装备的装备状态
                byte equipmentState = initialAttributes.getEquipmentState();
                equipmentState0.setSelected((equipmentState & 0B1000_0000) != 0x00);
                equipmentState1.setSelected((equipmentState & 0B0100_0000) != 0x00);
                equipmentState2.setSelected((equipmentState & 0B0010_0000) != 0x00);
                equipmentState3.setSelected((equipmentState & 0B0001_0000) != 0x00);
                equipmentState4.setSelected((equipmentState & 0B0000_1000) != 0x00);
                equipmentState5.setSelected((equipmentState & 0B0000_0100) != 0x00);
                equipmentState6.setSelected((equipmentState & 0B0000_0010) != 0x00);
                equipmentState7.setSelected((equipmentState & 0B0000_0001) != 0x00);

                // 装备
                equipment0.setSelectedIndex(initialAttributes.getEquipment(0) & 0xFF);
                equipment1.setSelectedIndex(initialAttributes.getEquipment(1) & 0xFF);
                equipment2.setSelectedIndex(initialAttributes.getEquipment(2) & 0xFF);
                equipment3.setSelectedIndex(initialAttributes.getEquipment(3) & 0xFF);
                equipment4.setSelectedIndex(initialAttributes.getEquipment(4) & 0xFF);
                equipment5.setSelectedIndex(initialAttributes.getEquipment(5) & 0xFF);
                equipment6.setSelectedIndex(initialAttributes.getEquipment(6) & 0xFF);
                equipment7.setSelectedIndex(initialAttributes.getEquipment(7) & 0xFF);

                // 道具
                item0.setSelectedIndex(initialAttributes.getInventory(0) & 0xFF);
                item1.setSelectedIndex(initialAttributes.getInventory(1) & 0xFF);
                item2.setSelectedIndex(initialAttributes.getInventory(2) & 0xFF);
                item3.setSelectedIndex(initialAttributes.getInventory(3) & 0xFF);
                item4.setSelectedIndex(initialAttributes.getInventory(4) & 0xFF);
                item5.setSelectedIndex(initialAttributes.getInventory(5) & 0xFF);
                item6.setSelectedIndex(initialAttributes.getInventory(6) & 0xFF);
                item7.setSelectedIndex(initialAttributes.getInventory(7) & 0xFF);
            }
        };
        players.addItemListener(itemListener);
        players.setModel(new DefaultComboBoxModel<>(Arrays.stream(Player.values()).map(Player::getName).toList().toArray(new String[0x00])));

        save.addActionListener(e -> {
            IPlayerEditor playerEditor = getMetalMaxRe().getEditorManager().getEditor(IPlayerEditor.class);
            PlayerInitialAttributes initialAttributes = playerEditor.getInitialAttributes(players.getSelectedIndex());
            // 设置初始金钱
            playerEditor.setMoney(((Number) money.getValue()).intValue());
            // 设置初始经验值
            initialAttributes.setExperience(((Number) exp.getValue()).intValue());
            // 设置初始等级
            initialAttributes.setLevel(((Number) level.getValue()).intValue());
            // 设置初始生命值
            initialAttributes.setHealth(((Number) health.getValue()).intValue());
            // 设置初始最大生命值
            initialAttributes.setMaxHealth(((Number) maxHealth.getValue()).intValue());
            // 设置初始装备
            initialAttributes.setEquipment(0, equipment0.getSelectedIndex());
            initialAttributes.setEquipment(1, equipment1.getSelectedIndex());
            initialAttributes.setEquipment(2, equipment2.getSelectedIndex());
            initialAttributes.setEquipment(3, equipment3.getSelectedIndex());
            initialAttributes.setEquipment(4, equipment4.getSelectedIndex());
            initialAttributes.setEquipment(5, equipment5.getSelectedIndex());
            initialAttributes.setEquipment(6, equipment6.getSelectedIndex());
            initialAttributes.setEquipment(7, equipment7.getSelectedIndex());
            // 设置初始道具
            initialAttributes.setInventory(0, item0.getSelectedIndex());
            initialAttributes.setInventory(1, item1.getSelectedIndex());
            initialAttributes.setInventory(2, item2.getSelectedIndex());
            initialAttributes.setInventory(3, item3.getSelectedIndex());
            initialAttributes.setInventory(4, item4.getSelectedIndex());
            initialAttributes.setInventory(5, item5.getSelectedIndex());
            initialAttributes.setInventory(6, item6.getSelectedIndex());
            initialAttributes.setInventory(7, item7.getSelectedIndex());

            // 初始战斗等级
            initialAttributes.setBattleSkill(((Number) battleSkill.getValue()).intValue());
            // 初始驾驶等级
            initialAttributes.setDrivingSkill(((Number) drivingSkill.getValue()).intValue());
            // 初始修理等级
            initialAttributes.setRepairSkill(((Number) repairSkill.getValue()).intValue());

            // 初始攻击力
            initialAttributes.setAttack(((Number) attack.getValue()).intValue());
            // 初始防御力
            initialAttributes.setDefense(((Number) defense.getValue()).intValue());
            // 初始强度
            initialAttributes.setStrength(((Number) strength.getValue()).intValue());
            // 初始智力
            initialAttributes.setWisdom(((Number) wisdom.getValue()).intValue());
            // 初始体力
            initialAttributes.setVitality(((Number) vitality.getValue()).intValue());
            // 初始速度
            initialAttributes.setSpeed(((Number) speed.getValue()).intValue());

            // 设置初始状态
            byte buff = 0;
            if (buff0.isSelected()) {
                buff |= 0B0000_0001;
            }
            if (buff1.isSelected()) {
                buff |= 0B0000_0010;
            }
            if (buff2.isSelected()) {
                buff |= 0B0000_0100;
            }
            if (buff3.isSelected()) {
                buff |= 0B0000_1000;
            }
            if (buff4.isSelected()) {
                buff |= 0B0001_0000;
            }
            if (buff5.isSelected()) {
                buff |= 0B0010_0000;
            }
            if (buff6.isSelected()) {
                buff |= 0B0100_0000;
            }
            if (buff7.isSelected()) {
                buff |= 0B1000_0000;
            }
            initialAttributes.setDeBuff(buff);

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
            if (equipmentState6.isSelected()) {
                equipmentState |= 0B0000_0010;
            }
            if (equipmentState7.isSelected()) {
                equipmentState |= 0B0000_0001;
            }
            initialAttributes.setEquipmentState(equipmentState);

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
        equipment6.setModel(new DefaultComboBoxModel<>(itemsObj));
        equipment7.setModel(new DefaultComboBoxModel<>(itemsObj));

        item0.setModel(new DefaultComboBoxModel<>(itemsObj));
        item1.setModel(new DefaultComboBoxModel<>(itemsObj));
        item2.setModel(new DefaultComboBoxModel<>(itemsObj));
        item3.setModel(new DefaultComboBoxModel<>(itemsObj));
        item4.setModel(new DefaultComboBoxModel<>(itemsObj));
        item5.setModel(new DefaultComboBoxModel<>(itemsObj));
        item6.setModel(new DefaultComboBoxModel<>(itemsObj));
        item7.setModel(new DefaultComboBoxModel<>(itemsObj));

        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                level,
                battleSkill, drivingSkill, repairSkill,
                strength, wisdom, vitality, speed
        }) {
            spinner.setModel(new SpinnerNumberModel(0, 0x00, 0xFF, 1));
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, NumberFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
        // 对JSpinner添加两个十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                attack, defense,
                health, maxHealth
        }) {
            spinner.setModel(new SpinnerNumberModel(0, 0x00, 0xFFFF, 1));
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, NumberFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
        // 对JSpinner添加鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                exp, money
        }) {
            spinner.setModel(new SpinnerNumberModel(0, 0x00, 0xFFFFFF, 1));
            spinner.setEditor(new FormatterJSpinnerEditor(spinner, NumberFormatter.getInstance()));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }

        // 为装备和道具可以输入id后直接设置
        for (JComboBox<?> comboBox : new JComboBox<?>[]{
                equipment0, equipment1,
                equipment2, equipment3,
                equipment4, equipment5,
                equipment6, equipment7,
                item0, item1,
                item2, item3,
                item4, item5,
                item6, item7}) {
            comboBox.getEditor().getEditorComponent().addKeyListener(new ComboBoxEnterSelectedAdapter(comboBox));
            FocusSelectAllAdapter.addAdapter(comboBox.getEditor().getEditorComponent());
        }

        // 选中一次玩家，更新数据
        itemListener.itemStateChanged(new ItemEvent(players, 0, null, ItemEvent.SELECTED));
    }
}
