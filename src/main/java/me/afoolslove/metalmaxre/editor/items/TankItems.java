package me.afoolslove.metalmaxre.editor.items;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.tank.TankEngine;
import me.afoolslove.metalmaxre.editor.tank.TankEquipmentItem;
import me.afoolslove.metalmaxre.editor.tank.TankWeapon;

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
     * @return 所有C装置
     */
    public List<TankEquipmentItem> getCUnits() {
        return cUnits;
    }

    /**
     * @return 所有引擎
     */
    public List<TankEngine> getEngines() {
        return engines;
    }

    /**
     * @return 所有底盘
     */
    public List<Item> getChassis() {
        return chassis;
    }
}
