package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.AttackRange;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.items.Item;
import me.afoolslove.metalmaxre.editors.player.Player;
import me.afoolslove.metalmaxre.editors.player.PlayerArmor;
import me.afoolslove.metalmaxre.editors.player.PlayerItem;
import me.afoolslove.metalmaxre.editors.player.PlayerWeapon;
import me.afoolslove.metalmaxre.editors.tank.*;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.*;

public class ItemEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JXTable playerWeapons;
    private JXTable playerArmors;
    private JXTable playerItems;
    private JXTable tankWeapons;
    private JXTable tankCUnits;
    private JXTable tankEngines;
    private JXTable tankChassis;
    private JXTable tankItems;
    private JButton save;

    public ItemEditorFrame(@NotNull Frame frame, @Nullable MetalMaxRe metalMaxRe) {
        this(null, frame, metalMaxRe);
    }

    public ItemEditorFrame(Integer initMap, @NotNull Frame frame, @Nullable MetalMaxRe metalMaxRe) {
        super(initMap, frame, metalMaxRe);
        init("物品编辑器", contentPane);
    }

    @Override
    protected void createMenu(@NotNull JMenuBar menuBar) {
    }

    @Override
    protected void createLayout() {
        IItemEditor itemEditor = getMetalMaxRe().getEditorManager().getEditor(IItemEditor.class);
        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        IDataValueEditor dataValueEditor = getMetalMaxRe().getEditorManager().getEditor(IDataValueEditor.class);
        List<Item> items = itemEditor.getItems();

        List<Object[]> playerWeaponData = new ArrayList<>(itemEditor.getPlayerWeaponMaxCount());
        List<Object[]> playerArmorData = new ArrayList<>(itemEditor.getPlayerArmorMaxCount());
        List<Object[]> playerItemData = new ArrayList<>(itemEditor.getPlayerItemsMaxCount());

        List<Object[]> tankWeaponData = new ArrayList<>(itemEditor.getTankWeaponMaxCount());
        List<Object[]> tankCUnitData = new ArrayList<>(itemEditor.getTankCUnitMaxCount());
        List<Object[]> tankEngineData = new ArrayList<>(itemEditor.getTankEngineMaxCount());
        List<Object[]> tankChassisData = new ArrayList<>(itemEditor.getTankChassisMaxCount());
        List<Object[]> tankItemsData = new ArrayList<>(itemEditor.getTankItemsMaxCount());

        for (int id = 0; id < items.size(); id++) {
            int itemId = id + 1;
            Item item = items.get(id);
            if (item instanceof PlayerWeapon playerWeapon) {
                Object[] playerWeaponDatum = new Object[0x09];
                playerWeaponDatum[0] = String.format("%02X", itemId);
                playerWeaponDatum[1] = textEditor.getItemName(itemId);
                playerWeaponDatum[2] = playerWeapon.getAttackAnim();
                playerWeaponDatum[3] = playerWeapon.getAttackRange();
                playerWeaponDatum[4] = playerWeapon.getAttack();
                playerWeaponDatum[5] = playerWeapon.getPrice();
                playerWeaponDatum[6] = playerWeapon.hasEquipped(Player.PLAYER_0);
                playerWeaponDatum[7] = playerWeapon.hasEquipped(Player.PLAYER_1);
                playerWeaponDatum[8] = playerWeapon.hasEquipped(Player.PLAYER_2);
                playerWeaponData.add(playerWeaponDatum);
            } else if (item instanceof PlayerArmor playerArmor) {
                Object[] playerArmorDatum = new Object[0x07];
                playerArmorDatum[0] = String.format("%02X", itemId);
                playerArmorDatum[1] = textEditor.getItemName(itemId);
                playerArmorDatum[2] = playerArmor.getDefense();
                playerArmorDatum[3] = playerArmor.getPrice();
                playerArmorDatum[4] = playerArmor.hasEquipped(Player.PLAYER_0);
                playerArmorDatum[5] = playerArmor.hasEquipped(Player.PLAYER_1);
                playerArmorDatum[6] = playerArmor.hasEquipped(Player.PLAYER_2);
                playerArmorData.add(playerArmorDatum);
            } else if (item instanceof PlayerItem playerItem) {
                Object[] playerItemDatum = new Object[0x03];
                playerItemDatum[0] = String.format("%02X", itemId);
                playerItemDatum[1] = textEditor.getItemName(itemId);
                playerItemDatum[2] = playerItem.getPrice();
                playerItemData.add(playerItemDatum);
            } else if (item instanceof TankWeapon tankWeapon) {
                Object[] tankWeaponDatum = new Object[0x0C];
                tankWeaponDatum[0x00] = String.format("%02X", itemId);
                tankWeaponDatum[0x01] = textEditor.getItemName(itemId);
                tankWeaponDatum[0x02] = tankWeapon.getAttackAnim();
                tankWeaponDatum[0x03] = tankWeapon.getAttackRange();
                tankWeaponDatum[0x04] = tankWeapon.getAttack();
                tankWeaponDatum[0x05] = tankWeapon.getDefense();
                tankWeaponDatum[0x06] = tankWeapon.getShellCapacity();
                tankWeaponDatum[0x07] = tankWeapon.getWeight();
                tankWeaponDatum[0x08] = tankWeapon.getPrice();
                tankWeaponDatum[0x09] = tankWeapon.hasEquipped(TankWeaponSlot.MAIN_GUN);
                tankWeaponDatum[0x0A] = tankWeapon.hasEquipped(TankWeaponSlot.SECONDARY_GUN);
                tankWeaponDatum[0x0B] = tankWeapon.hasEquipped(TankWeaponSlot.SPECIAL_EQUIPMENT);
                tankWeaponData.add(tankWeaponDatum);
            } else if (item instanceof TankCUnit tankCUnit) {
                Object[] tankCUnitDatum = new Object[0x05];
                tankCUnitDatum[0] = String.format("%02X", itemId);
                tankCUnitDatum[1] = textEditor.getItemName(itemId);
                tankCUnitDatum[2] = tankCUnit.getDefense();
                tankCUnitDatum[3] = tankCUnit.getWeight();
                tankCUnitDatum[4] = tankCUnit.getPrice();
                tankCUnitData.add(tankCUnitDatum);
            } else if (item instanceof TankEngine tankEngine) {
                Object[] tankEngineDatum = new Object[0x06];
                tankEngineDatum[0] = String.format("%02X", itemId);
                tankEngineDatum[1] = textEditor.getItemName(itemId);
                tankEngineDatum[2] = tankEngine.getDefense();
                tankEngineDatum[3] = tankEngine.getCapacity();
                tankEngineDatum[4] = tankEngine.getWeight();
                tankEngineDatum[5] = tankEngine.getPrice();
                tankEngineData.add(tankEngineDatum);
            } else if (item instanceof TankChassis chassis) {
                Object[] tankChassisDatum = new Object[0x03];
                tankChassisDatum[0] = String.format("%02X", itemId);
                tankChassisDatum[1] = textEditor.getItemName(itemId);
                tankChassisDatum[2] = chassis.getPrice();
                tankChassisData.add(tankChassisDatum);
            } else if (item instanceof TankItem tankItem) {
                Object[] tankItemDatum = new Object[0x03];
                tankItemDatum[0] = String.format("%02X", itemId);
                tankItemDatum[1] = textEditor.getItemName(itemId);
                tankItemDatum[2] = tankItem.getPrice();
                tankItemsData.add(tankItemDatum);
            }
        }
        // 人类武器
        playerWeapons.setModel(new LockTableModel(playerWeaponData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "攻击动画", "攻击范围", "攻击力", "价格", "玩家一", "玩家二", "玩家三"}, 0, 1, 4, 5));
        defaultTableColumnSize(playerWeapons);
        playerWeapons.getColumn(0x05).setPreferredWidth(96);
        playerWeapons.addMouseListener(new MouseDataValue(this, playerWeapons, 0x04, 0x05));
        // 人类防具
        playerArmors.setModel(new LockTableModel(playerArmorData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "防御力", "价格", "玩家一", "玩家二", "玩家三"}, 0, 1, 2, 3));
        defaultTableColumnSize(playerArmors);
        playerArmors.getColumn(0x03).setPreferredWidth(96);
        playerArmors.addMouseListener(new MouseDataValue(this, playerArmors, 0x02, 0x03));
        // 人类道具
        playerItems.setModel(new LockTableModel(playerItemData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "价格"}, 0, 1, 2));
        defaultTableColumnSize(playerItems);
        playerItems.getColumn(0x02).setPreferredWidth(96);
        playerItems.addMouseListener(new MouseDataValue(this, playerItems, 0x02));

        // 坦克武器
        tankWeapons.setModel(new LockTableModel(tankWeaponData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "攻击动画", "攻击范围", "攻击力", "防御力", "弹药数量", "重量", "价格", "主炮", "副炮", "S-E"}, 0, 1, 4, 5, 8));
        defaultTableColumnSize(tankWeapons);
        tankWeapons.getColumn(0x08).setPreferredWidth(96);
        tankWeapons.addMouseListener(new MouseDataValue(this, tankWeapons, 0x04, 0x05, 0x08));
        // 坦克C装置
        tankCUnits.setModel(new LockTableModel(tankCUnitData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "防御力", "重量", "价格"}, 0, 1, 2, 4));
        defaultTableColumnSize(tankCUnits);
        tankCUnits.getColumn(0x04).setPreferredWidth(96);
        tankCUnits.addMouseListener(new MouseDataValue(this, tankCUnits, 0x02, 0x04));
        // 坦克引擎
        tankEngines.setModel(new LockTableModel(tankEngineData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "防御力", "载重", "重量", "价格"}, 0, 1, 2, 5));
        defaultTableColumnSize(tankEngines);
        tankEngines.getColumn(0x05).setPreferredWidth(96);
        tankEngines.addMouseListener(new MouseDataValue(this, tankEngines, 0x02, 0x05));
        // 坦克底盘
        tankChassis.setModel(new LockTableModel(tankChassisData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "价格"}, 0, 1, 2));
        defaultTableColumnSize(tankChassis);
        tankChassis.getColumn(0x02).setPreferredWidth(96);
        tankChassis.addMouseListener(new MouseDataValue(this, tankChassis, 0x02));
        // 坦克道具
        tankItems.setModel(new LockTableModel(tankItemsData.toArray(new Object[0x00][]), new String[]{"ID", "名称", "价格"}, 0, 1, 2));
        defaultTableColumnSize(tankItems);
        tankItems.getColumn(0x02).setPreferredWidth(96);
        tankItems.addMouseListener(new MouseDataValue(this, tankItems, 0x02));


        playerWeapons.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            private final JComboBox<String> attackRanges = new JComboBox<>(new String[]{"单体", "一组", "全体"});
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                switch (table.convertColumnIndexToView(column)) {
                    case 0x02:  // 攻击动画
                        value = String.format("%02X", (byte) value);
                        break;
                    case 0x03:  // 攻击范围
                        attackRanges.setSelectedIndex(switch ((AttackRange) value) {
                            case ONE -> 0;
                            case GROUP -> 1;
                            case ALL -> 2;
                        });
                        return attackRanges;
                    case 0x04:  // 攻击力
                    case 0x05:  // 价格
                        value = String.format("%02X", (byte) value);
                        break;
                    case 0x06:  // 可装备
                    case 0x07:
                    case 0x08:
                        checkBox.setSelected((boolean) value);
                        return checkBox;
                }
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }

            @Override
            public Object getCellEditorValue() {
                Object value = super.getCellEditorValue();
                if (playerWeapons.getEditingColumn() != -1) {
                    int editingColumn = playerWeapons.getEditingColumn();
                    switch (editingColumn) {
                        case 0x02:  // 攻击动画
                            value = (byte) Integer.parseInt(value.toString(), 16);
                            break;
                        case 0x03:  // 攻击范围
                            value = switch (attackRanges.getSelectedIndex()) {
                                case 0x00 -> AttackRange.ONE;
                                case 0x01 -> AttackRange.GROUP;
                                case 0x02 -> AttackRange.ALL;
                                default -> AttackRange.ONE;
                            };
                            break;
                        case 0x04:  // 攻击力
                        case 0x05:  // 价格
                            value = (byte) Integer.parseInt(value.toString(), 16);
                            break;
                        case 0x06:  // 可装备
                        case 0x07:
                        case 0x08:
                            value = checkBox.isSelected();
                            break;
                    }
                }
                return value;
            }
        });
        playerWeapons.setDefaultRenderer(Object.class, new PriceTableCellRenderer(textEditor, dataValueEditor, 0x05) {
            private final String[] attackRanges = {"单体", "一组", "全体"};
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                switch (table.convertRowIndexToView(column)) {
                    case 0x02:  // 攻击动画
                        value = String.format("%02X", (byte) value);
                        break;
                    case 0x03:  // 攻击范围
                        value = attackRanges[switch ((AttackRange) value) {
                            case ONE -> 0;
                            case GROUP -> 1;
                            case ALL -> 2;
                        }];
                        break;
                    case 0x04:  // 攻击力
                        value = dataValueEditor.getValues().get(((byte) value) & 0xFF);
                        break;
                    case 0x06:  // 可装备
                    case 0x07:
                    case 0x08:
                        checkBox.setSelected((boolean) value);
                        checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                        return checkBox;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        playerArmors.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                switch (table.convertColumnIndexToView(column)) {
                    case 0x03:  // 价格
                        break;
                    case 0x04:  // 可装备
                    case 0x05:
                    case 0x06:
                        checkBox.setSelected((boolean) value);
                        break;
                }
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }

            @Override
            public Object getCellEditorValue() {
                Object value = super.getCellEditorValue();
                if (playerArmors.getEditingColumn() != -1) {
                    int editingColumn = playerArmors.getEditingColumn();
                    switch (editingColumn) {
                        case 0x04:  // 可装备
                        case 0x05:
                        case 0x06:
                            value = checkBox.isSelected();
                            break;
                    }
                }
                return value;
            }
        });
        playerArmors.setDefaultRenderer(Object.class, new PriceTableCellRenderer(textEditor, dataValueEditor, 0x03) {
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                switch (table.convertColumnIndexToView(column)) {
                    case 0x04:  // 可装备
                    case 0x05:
                    case 0x06:
                        checkBox.setSelected((boolean) value);
                        checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                        return checkBox;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        playerItems.setDefaultEditor(Object.class, new PriceCellEditor(0x02));
        playerItems.setDefaultRenderer(Object.class, new PriceTableCellRenderer(textEditor, dataValueEditor, 0x02));


        tankWeapons.setDefaultEditor(Object.class, new TankEquipmentCellEditor(0x08, 0x07) {
            private final JComboBox<String> attackRanges = new JComboBox<>(new String[]{"单体", "一组", "全体"});
            private final JComboBox<String> shellCapacity = new JComboBox<>(new String[]{"无限弹药", "2", "4", "8", "16", "32", "48", "62"});
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                switch (table.convertRowIndexToView(column)) {
                    case 0x02:  // 攻击动画
                        value = String.format("%02X", (byte) value);
                        break;
                    case 0x03:  // 攻击范围
                        attackRanges.setSelectedIndex(switch ((AttackRange) value) {
                            case ONE -> 0;
                            case GROUP -> 1;
                            case ALL -> 2;
                        });
                        return attackRanges;
                    case 0x04:  // 攻击力
                        break;
                    case 0x05:  // 防御力
                        break;
                    case 0x06:  // 弹药数量
                        shellCapacity.setSelectedIndex(switch ((TankShellCapacity) value) {
                            case INFINITE -> 0;
                            case X2 -> 1;
                            case X4 -> 2;
                            case X8 -> 3;
                            case X16 -> 4;
                            case X32 -> 5;
                            case X48 -> 6;
                            case X64 -> 7;
                        });
                        return shellCapacity;
                    case 0x09:  // 可装备
                    case 0x0A:
                    case 0x0B:
                        checkBox.setSelected((boolean) value);
                        return checkBox;
                }
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }

            @Override
            public Object getCellEditorValue() {
                Object value = super.getCellEditorValue();
                if (tankWeapons.getEditingColumn() != -1) {
                    int editingColumn = tankWeapons.getEditingColumn();
                    switch (editingColumn) {
                        case 0x02:  // 攻击动画
                            value = (byte) Integer.parseInt(value.toString(), 16);
                            break;
                        case 0x03:  // 攻击范围
                            value = switch (attackRanges.getSelectedIndex()) {
                                case 0x00 -> AttackRange.ONE;
                                case 0x01 -> AttackRange.GROUP;
                                case 0x02 -> AttackRange.ALL;
                                default -> AttackRange.ONE;
                            };
                            break;
                        case 0x06:  // 弹药数量
                            value = switch (shellCapacity.getSelectedIndex()) {
                                case 0x00 -> TankShellCapacity.INFINITE;
                                case 0x01 -> TankShellCapacity.X2;
                                case 0x02 -> TankShellCapacity.X4;
                                case 0x03 -> TankShellCapacity.X8;
                                case 0x04 -> TankShellCapacity.X16;
                                case 0x05 -> TankShellCapacity.X32;
                                case 0x06 -> TankShellCapacity.X48;
                                case 0x07 -> TankShellCapacity.X64;
                                default -> TankShellCapacity.INFINITE;
                            };
                            break;
                        case 0x09:  // 可装备
                        case 0x0A:
                        case 0x0B:
                            value = checkBox.isSelected();
                            break;
                    }
                }
                return value;
            }
        });
        tankWeapons.setDefaultRenderer(Object.class, new TankEquipmentTableCellRenderer(textEditor, dataValueEditor, 0x08, 0x07) {
            private final String[] attackRanges = {"单体", "一组", "全体"};
            private final String[] shellCapacity = {"无限弹药", "2", "4", "8", "16", "32", "48", "62"};
            private final JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                switch (table.convertRowIndexToView(column)) {
                    case 0x02:  // 攻击动画
                        value = String.format("%02X", (byte) value);
                        break;
                    case 0x03:  // 攻击范围
                        value = attackRanges[switch ((AttackRange) value) {
                            case ONE -> 0;
                            case GROUP -> 1;
                            case ALL -> 2;
                        }];
                        break;
                    case 0x04:  // 攻击力
                        break;
                    case 0x06:  // 弹药数量
                        value = shellCapacity[switch ((TankShellCapacity) value) {
                            case INFINITE -> 0;
                            case X2 -> 1;
                            case X4 -> 2;
                            case X8 -> 3;
                            case X16 -> 4;
                            case X32 -> 5;
                            case X48 -> 6;
                            case X64 -> 7;
                        }];
                        break;
                    case 0x09:  // 可装备
                    case 0x0A:
                    case 0x0B:
                        checkBox.setSelected((boolean) value);
                        checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                        return checkBox;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        tankCUnits.setDefaultEditor(Object.class, new TankEquipmentCellEditor(0x04, 0x03));
        tankCUnits.setDefaultRenderer(Object.class, new TankEquipmentTableCellRenderer(textEditor, dataValueEditor, 0x04, 0x03));
        tankEngines.setDefaultEditor(Object.class, new TankEquipmentCellEditor(0x05, 0x04) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return super.getTableCellEditorComponent(table, value, isSelected, row, column);
            }
        });
        tankEngines.setDefaultRenderer(Object.class, new TankEquipmentTableCellRenderer(textEditor, dataValueEditor, 0x05, 0x04) {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (table.convertColumnIndexToView(column) == 0x03) {
                    // 载重
                    value = String.format("%2.2ft", Integer.parseInt(value.toString()) / 10.00);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        tankChassis.setDefaultEditor(Object.class, new PriceCellEditor(0x02));
        tankChassis.setDefaultRenderer(Object.class, new PriceTableCellRenderer(textEditor, dataValueEditor, 0x02));
        tankItems.setDefaultRenderer(Object.class, new PriceTableCellRenderer(textEditor, dataValueEditor, 0x02));
        tankItems.setDefaultRenderer(Object.class, new PriceTableCellRenderer(textEditor, dataValueEditor, 0x02));


        save.addActionListener(e -> {
            DefaultTableModel playerWeaponsModel = (DefaultTableModel) playerWeapons.getModel();
            DefaultTableModel playerArmorsModel = (DefaultTableModel) playerArmors.getModel();
            DefaultTableModel playerItemsModel = (DefaultTableModel) playerItems.getModel();

            DefaultTableModel tankWeaponsModel = (DefaultTableModel) tankWeapons.getModel();
            DefaultTableModel tankCUnitsModel = (DefaultTableModel) tankCUnits.getModel();
            DefaultTableModel tankEnginesModel = (DefaultTableModel) tankEngines.getModel();
            DefaultTableModel tankChassisModel = (DefaultTableModel) tankChassis.getModel();
            DefaultTableModel tankItemsModel = (DefaultTableModel) tankItems.getModel();

            // 设置人类武器数据
            List<PlayerWeapon> playerWeapons = itemEditor.getPlayerWeapons();
            playerWeapons.clear();
            for (Vector vector : playerWeaponsModel.getDataVector()) {
                PlayerWeapon weapon = new PlayerWeapon();
                // 设置攻击动画
                weapon.setAttackAnim((byte) vector.get(0x02));
                // 设置攻击范围
                weapon.setAttackRange((AttackRange) vector.get(0x03));
                // 设置攻击力
                weapon.setAttack((byte) vector.get(0x04));
                // 设置价格
                weapon.setPrice((byte) vector.get(0x05));
                // 设置可装备
                if ((boolean) vector.get(0x06)) {
                    weapon.addCanEquipped(Player.PLAYER_0);
                }
                if ((boolean) vector.get(0x07)) {
                    weapon.addCanEquipped(Player.PLAYER_1);
                }
                if ((boolean) vector.get(0x07)) {
                    weapon.addCanEquipped(Player.PLAYER_2);
                }
                playerWeapons.add(weapon);
            }
            // 设置人类防具数据
            List<PlayerArmor> playerArmors = itemEditor.getPlayerArmors();
            playerArmors.clear();
            for (Vector vector : playerArmorsModel.getDataVector()) {
                PlayerArmor armor = new PlayerArmor();
                // 设置防御力
                armor.setDefense((byte) vector.get(0x02));
                // 设置价格
                armor.setPrice((byte) vector.get(0x03));
                // 设置可装备
                if ((boolean) vector.get(0x04)) {
                    armor.addCanEquipped(Player.PLAYER_0);
                }
                if ((boolean) vector.get(0x05)) {
                    armor.addCanEquipped(Player.PLAYER_1);
                }
                if ((boolean) vector.get(0x06)) {
                    armor.addCanEquipped(Player.PLAYER_2);
                }
                playerArmors.add(armor);
            }
            // 设置人类道具
            List<PlayerItem> playerItems = itemEditor.getPlayerItems();
            playerItems.clear();
            for (Vector vector : playerItemsModel.getDataVector()) {
                PlayerItem item = new PlayerItem();
                // 设置价格
                item.setPrice((byte) vector.get(0x02));
                playerItems.add(item);
            }

            // 坦克武器
            List<TankWeapon> tankWeapons = itemEditor.getTankWeapons();
            tankWeapons.clear();
            for (Vector vector : tankWeaponsModel.getDataVector()) {
                TankWeapon weapon = new TankWeapon();
                // 设置攻击动画
                weapon.setAttackAnim((byte) vector.get(0x02));
                // 设置攻击范围
                weapon.setAttackRange((AttackRange) vector.get(0x03));
                // 设置攻击力
                weapon.setAttack((byte) vector.get(0x04));
                // 设置防御力
                weapon.setDefense((byte) vector.get(0x05));
                // 设置弹药数量
                weapon.setShellCapacity((TankShellCapacity) vector.get(0x06));
                // 设置重量
                weapon.setWeight((byte) vector.get(0x07));
                // 设置价格
                weapon.setPrice((byte) vector.get(0x08));
                // 设置可装备
                if ((boolean) vector.get(0x09)) {
                    weapon.addCanEquipped(TankWeaponSlot.MAIN_GUN);
                }
                if ((boolean) vector.get(0x0A)) {
                    weapon.addCanEquipped(TankWeaponSlot.SECONDARY_GUN);
                }
                if ((boolean) vector.get(0x0B)) {
                    weapon.addCanEquipped(TankWeaponSlot.SPECIAL_EQUIPMENT);
                }
                tankWeapons.add(weapon);
            }
            // 设置C装置
            List<TankCUnit> tankCUnits = itemEditor.getTankCUnits();
            tankCUnits.clear();
            for (Vector vector : tankCUnitsModel.getDataVector()) {
                TankCUnit cUnit = new TankCUnit();
                // 设置防御力
                cUnit.setDefense((byte) vector.get(0x02));
                // 设置重量
                cUnit.setWeight((byte) vector.get(0x03));
                // 设置价格
                cUnit.setPrice((byte) vector.get(0x04));
                tankCUnits.add(cUnit);
            }
            // 设置引擎
            List<TankEngine> tankEngines = itemEditor.getTankEngines();
            tankEngines.clear();
            for (Vector vector : tankEnginesModel.getDataVector()) {
                TankEngine engine = new TankEngine();
                // 设置防御力
                engine.setDefense((byte) vector.get(0x02));
                // 设置载重
                engine.setCapacity((byte) vector.get(0x03));
                // 设置重量
                engine.setWeight((byte) vector.get(0x04));
                // 设置价格
                engine.setPrice((byte) vector.get(0x05));
                tankEngines.add(engine);
            }
            // 设置底盘
            List<TankChassis> tankChassis = itemEditor.getTankChassis();
            tankChassis.clear();
            for (Vector vector : tankChassisModel.getDataVector()) {
                TankChassis chassis = new TankChassis();
                // 设置价格
                chassis.setPrice((byte) vector.get(0x02));
                tankChassis.add(chassis);
            }
            // 设置物品
            List<TankItem> tankItems = itemEditor.getTankItems();
            tankItems.clear();
            for (Vector vector : tankItemsModel.getDataVector()) {
                TankItem tankItem = new TankItem();
                // 设置价格
                tankItem.setPrice((byte) vector.get(0x02));
                tankItems.add(tankItem);
            }
        });
    }

    /**
     * 第二列宽度设置为128，其它设置为64
     */
    private void defaultTableColumnSize(JTable table) {
        int index = 0;
        Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn tableColumn = columns.nextElement();
            if (index == 1) {
                tableColumn.setPreferredWidth(128);
            } else {
                tableColumn.setPreferredWidth(64);
            }
            index++;
        }
    }


    private static class LockTableModel extends DefaultTableModel {
        private final int[] locks;

        public LockTableModel(int rowCount, int columnCount, int... locks) {
            super(rowCount, columnCount);
            this.locks = locks;
        }

        public LockTableModel(Vector<?> columnNames, int rowCount, int... locks) {
            super(columnNames, rowCount);
            this.locks = locks;
        }

        public LockTableModel(Object[] columnNames, int rowCount, int... locks) {
            super(columnNames, rowCount);
            this.locks = locks;
        }

        public LockTableModel(Vector<? extends Vector> data, Vector<?> columnNames, int... locks) {
            super(data, columnNames);
            this.locks = locks;
        }

        public LockTableModel(Object[][] data, Object[] columnNames, int... locks) {
            super(data, columnNames);
            this.locks = locks;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return Arrays.binarySearch(locks, column) < 0;
        }
    }

    private static class BaseTableCellRenderer extends DefaultTableCellRenderer {
        private final ITextEditor textEditor;

        public BaseTableCellRenderer(ITextEditor textEditor) {
            super();
            this.textEditor = textEditor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (table.convertColumnIndexToView(column) == 0x01) {
                int r = table.convertRowIndexToView(row);
                value = textEditor.getItemName(Integer.parseInt(table.getValueAt(r, 0x00).toString(), 16));
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class PriceCellEditor extends DefaultCellEditor {
        private int priceColumn;

        public PriceCellEditor(int priceColumn) {
            super(new JTextField());
            this.priceColumn = priceColumn;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (table.convertColumnIndexToView(column) == priceColumn) {
                // 金钱

            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }

    private static class PriceTableCellRenderer extends BaseTableCellRenderer {
        private final IDataValueEditor dataValueEditor;
        private int priceColumn;

        public PriceTableCellRenderer(ITextEditor textEditor, IDataValueEditor dataValueEditor, int priceColumn) {
            super(textEditor);
            this.dataValueEditor = dataValueEditor;
            this.priceColumn = priceColumn;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (table.convertColumnIndexToView(column) == priceColumn) {
                // 转换为真实数值
                value = switch (Integer.parseInt(value.toString(), 16)) {
                    case -1 -> "无价，不可丢弃";
                    case -2 -> "无价，可以丢弃";
                    default -> dataValueEditor.getValues().get(((byte) value) & 0xFF).intValue();
                };
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class TankEquipmentCellEditor extends PriceCellEditor {
        private final int weightColumn;

        public TankEquipmentCellEditor(int priceColumn, int weightColumn) {
            super(priceColumn);
            this.weightColumn = weightColumn;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (table.convertColumnIndexToView(column) == weightColumn) {
                // 重量
            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }

    private static class TankEquipmentTableCellRenderer extends PriceTableCellRenderer {
        private int weightColumn;

        public TankEquipmentTableCellRenderer(ITextEditor textEditor, IDataValueEditor dataValueEditor, int priceColumn, int weightColumn) {
            super(textEditor, dataValueEditor, priceColumn);
            this.weightColumn = weightColumn;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (table.convertColumnIndexToView(column) == weightColumn) {
                // 重量
                value = String.format("%2.2ft", Integer.parseInt(value.toString()) / 10.00);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }


    private static class MouseDataValue extends MouseAdapter {
        private final ItemEditorFrame frame;
        private final JTable table;

        private final int[] columns;

        private MouseDataValue(@NotNull ItemEditorFrame frame, @NotNull JTable table, int... columns) {
            this.frame = frame;
            this.table = table;
            this.columns = columns;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Point point = e.getPoint();
            int column = table.convertColumnIndexToModel(table.columnAtPoint(point));
            int row = table.convertRowIndexToModel(table.rowAtPoint(point));
            if (e.getClickCount() == 2) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // 双击
                    if (Arrays.binarySearch(columns, column) < 0) {
                        // 不是指定的数据值列
                        return;
                    }

                    Object name = table.getValueAt(row, 0x01);
                    Object action = table.getColumnName(column);

                    // 打开数据值编辑器
                    DataValueEditorFrame dataValueEditorFrame = new DataValueEditorFrame(String.format("%s %s 设置", name, action), frame, frame.getMetalMaxRe());
                    dataValueEditorFrame.pack();
                    dataValueEditorFrame.setVisible(true);
                    dataValueEditorFrame.addWindowFocusListener(new WindowAdapter() {
                        @Override
                        public void windowLostFocus(WindowEvent e) {
                            // 丢失焦点自动关闭
                            dataValueEditorFrame.setVisible(false);
                        }
                    });
                    dataValueEditorFrame.getSelectListeners().add(new DataValueEditorFrame.SelectListener() {
                        @Override
                        public void selected(int index, int value) {
                            // 关闭窗口
                            dataValueEditorFrame.setVisible(false);
                            // 移除监听器
                            dataValueEditorFrame.getSelectListeners().remove(this);
                            // 设置为选中的值
                            table.getModel().setValueAt((byte) index, row, column);
                        }
                    });
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    // 右键双击
                    if (column == 0x01) {
                        // 双击名称
                        boolean hasFrame = false;
                        for (Frame frame1 : getFrames()) {
                            // 文本编辑器，未被关闭
                            if (frame1 instanceof TextEditorFrame textEditorFrame && textEditorFrame.isVisible()) {
                                hasFrame = true;
                                textEditorFrame.getTextPages().setSelectedIndex(0x00);
                                textEditorFrame.getTextPage().setSelectedIndex(Integer.parseInt(table.getValueAt(row, 0x00).toString(), 16));
                                textEditorFrame.setState(ICONIFIED);
                                textEditorFrame.setState(NORMAL);
                                break;
                            }
                        }
                        if (!hasFrame) {
                            // 没有文本编辑器窗口，打开一个并选中
                            TextEditorFrame textEditorFrame = new TextEditorFrame(frame, frame.getMetalMaxRe());
                            textEditorFrame.pack();
                            textEditorFrame.setVisible(true);
                            SwingUtilities.invokeLater(() -> {
                                textEditorFrame.getTextPages().setSelectedIndex(0x00);
                                textEditorFrame.getTextPage().setSelectedIndex(Integer.parseInt(table.getValueAt(row, 0x00).toString(), 16));
                            });
                        }


                    }
                }
            }
        }
    }
}
