package me.afoolslove.metalmaxre.editors.items;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.player.PlayerArmor;
import me.afoolslove.metalmaxre.editors.player.PlayerItem;
import me.afoolslove.metalmaxre.editors.player.PlayerWeapon;
import me.afoolslove.metalmaxre.editors.tank.*;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.Range;

import java.util.List;

/**
 * 物品编辑器
 *
 * @author AFoolLove
 */
public interface IItemEditor extends IRomEditor {

    /**
     * 获取玩家所有防具的数量
     *
     * @return 玩家所有防具数量
     */
    default int getPlayerArmorMaxCount() {
        return 0x22;
    }

    /**
     * 获取玩家所有武器的数量
     *
     * @return 玩家所有武器数量
     */
    default int getPlayerWeaponMaxCount() {
        return 0x1E;
    }

    /**
     * 获取玩家所有道具的数量
     *
     * @return 玩家所有道具数量
     */
    default int getPlayerItemsMaxCount() {
        return 0x32;
    }

    /**
     * 坦克武器（主炮、副炮、S-E）数量
     */
    default int getTankWeaponMaxCount() {
        return 0x34;
    }

    /**
     * 坦克C装置数量
     */
    default int getTankCUnitMaxCount() {
        return 0x04;
    }

    /**
     * 坦克引擎数量
     */
    default int getTankEngineMaxCount() {
        return 0x18;
    }

    /**
     * 坦克底盘数量
     */
    default int getTankChassisMaxCount() {
        return 0x08;
    }

    /**
     * 坦克的道具数量
     */
    default int getTankItemsMaxCount() {
        return 0x13;
    }

    /**
     * 获取一个物品
     *
     * @param id 物品id
     * @return 物品
     */
    Item getItem(@Range(from = 0x00, to = 0xFF) int id);

    /**
     * 获取一个物品
     *
     * @param id 物品id
     * @return 物品
     */
    Item getItem(byte id);

    /**
     * 获取玩家所有防具
     *
     * @return 玩家所有防具
     */
    List<PlayerArmor> getPlayerArmors();

    /**
     * 获取玩家所有武器
     *
     * @return 玩家所有武器
     */
    List<PlayerWeapon> getPlayerWeapons();

    /**
     * 获取玩家所有道具
     *
     * @return 玩家所有道具
     */
    List<PlayerItem> getPlayerItems();


    /**
     * 获取坦克所有武器（主炮、副炮、S-E）
     *
     * @return 坦克所有武器（主炮、副炮、S-E）
     */
    List<TankWeapon> getTankWeapons();

    /**
     * 获取坦克所有C装置
     *
     * @return 坦克所有C装置
     */
    List<TankCUnit> getTankCUnits();

    /**
     * 获取坦克所有引擎
     *
     * @return 坦克所有引擎
     */
    List<TankEngine> getTankEngines();

    /**
     * 获取坦克所有地盘
     *
     * @return 坦克所有地盘
     */
    List<TankChassis> getTankChassis();

    /**
     * 获取坦克所有道具
     *
     * @return 坦克所有道具
     */
    List<TankItem> getTankItems();

    /**
     * 获取所有物品
     *
     * @return 所有物品
     */
    List<Item> getItems();


    /**
     * 获取坦克引擎装备的最大载重地址
     *
     * @return 所有坦克引擎装备的最大载重地址
     */
    DataAddress getTankEnginesMaxCapacityAddress();

    /**
     * 获取玩家装备可穿戴状态地址
     *
     * @return 玩家装备可穿戴状态地址
     */
    DataAddress getPlayerEquipmentCanEquippedStartAddress();
}
