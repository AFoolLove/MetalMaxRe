package me.afoolslove.metalmaxre.editor.items;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.tank.TankEngine;
import me.afoolslove.metalmaxre.editor.tank.TankEquipmentItem;
import me.afoolslove.metalmaxre.editor.tank.TankWeapon;
import me.afoolslove.metalmaxre.editor.text.TextEditor;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * 坦克物品
 * 装备、C装置、引擎、底盘
 *
 * @author AFoolLove
 */
public class TankItems {
    /**
     * 坦克武器（主炮、副炮、S-E）数量
     */
    public static final int TANK_WEAPON_MAX_COUNT = 0x34;
    /**
     * 坦克C装置数量
     */
    public static final int TANK_C_UNIT_MAX_COUNT = 0x04;
    /**
     * 坦克引擎数量
     */
    public static final int TANK_ENGINE_MAX_COUNT = 0x18;
    /**
     * 坦克底盘数量
     */
    public static final int TANK_CHASSIS_MAX_COUNT = 0x08;
    /**
     * 坦克的道具数量
     */
    public static final int TANK_ITEMS_MAX_COUNT = 0x13;


    public List<TankWeapon> weapons = new ArrayList<>();
    public List<TankEquipmentItem> cUnits = new ArrayList<>();
    public List<TankEngine> engines = new ArrayList<>();
    public List<Item> chassis = new ArrayList<>();


    /**
     * @return 所有武器（主炮、副炮、S-E）
     */
    public List<TankWeapon> getWeapons() {
        return weapons;
    }

    /**
     * @return 武器名称
     * @see TextEditor#getItemName(int)
     */
    public String getWeaponName(@Range(from = 0x00, to = 0xFF) int weapon) {
        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);

        return textEditor.getItemName(PlayerItems.PLAYER_ARMOR_MAX_COUNT + PlayerItems.PLAYER_WEAPON_MAX_COUNT
                + weapon);
    }

    /**
     * @return 所有C装置
     */
    public List<TankEquipmentItem> getCUnits() {
        return cUnits;
    }

    /**
     * @return C装置名称
     * @see TextEditor#getItemName(int)
     */
    public String getCUnitName(@Range(from = 0x00, to = 0xFF) int cUnit) {
        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);
        return textEditor.getItemName(PlayerItems.PLAYER_ARMOR_MAX_COUNT + PlayerItems.PLAYER_WEAPON_MAX_COUNT + TANK_WEAPON_MAX_COUNT
                + cUnit);
    }


    /**
     * @return 所有引擎
     */
    public List<TankEngine> getEngines() {
        return engines;
    }

    /**
     * @return 引擎名称
     * @see TextEditor#getItemName(int)
     */
    public String getEngineName(@Range(from = 0x00, to = 0xFF) int engine) {
        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);
        return textEditor.getItemName(PlayerItems.PLAYER_ARMOR_MAX_COUNT + PlayerItems.PLAYER_WEAPON_MAX_COUNT + TANK_WEAPON_MAX_COUNT + TANK_C_UNIT_MAX_COUNT
                + engine);
    }

    /**
     * @return 所有底盘
     */
    public List<Item> getChassis() {
        return chassis;
    }


    /**
     * @return 底盘名称
     * @see TextEditor#getItemName(int)
     */
    public String getChassisName(@Range(from = 0x00, to = 0xFF) int chassis) {
        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);
        return textEditor.getItemName(PlayerItems.PLAYER_ARMOR_MAX_COUNT + PlayerItems.PLAYER_WEAPON_MAX_COUNT + TANK_WEAPON_MAX_COUNT + TANK_C_UNIT_MAX_COUNT + TANK_ENGINE_MAX_COUNT
                + chassis);
    }
}
