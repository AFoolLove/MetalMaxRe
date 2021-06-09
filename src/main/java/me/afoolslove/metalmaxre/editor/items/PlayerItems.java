package me.afoolslove.metalmaxre.editor.items;

import me.afoolslove.metalmaxre.editor.player.PlayerArmor;
import me.afoolslove.metalmaxre.editor.player.PlayerWeapon;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家的武器、装备、道具
 *
 * @author AFoolLove
 */
public class PlayerItems {
    /**
     * 玩家的防具种类数量
     */
    public static final int PLAYER_ARMOR_MAX_COUNT = 0x22;
    /**
     * 玩家的武器种类数量
     */
    public static final int PLAYER_WEAPON_MAX_COUNT = 0x1E;
    /**
     * 玩家的道具种类数量
     */
    public static final int PLAYER_ITEMS_MAX_COUNT = 0x32;


    public List<PlayerArmor> armors = new ArrayList<>(PLAYER_ARMOR_MAX_COUNT);
    public List<PlayerWeapon> weapons = new ArrayList<>(PLAYER_WEAPON_MAX_COUNT);

    /**
     * @return 玩家所有防具
     */
    public List<PlayerArmor> getArmors() {
        return armors;
    }

    /**
     * @return 玩家所有武器
     */
    public List<PlayerWeapon> getWeapons() {
        return weapons;
    }
}
