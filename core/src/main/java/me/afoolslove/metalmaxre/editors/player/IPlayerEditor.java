package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.Range;

import java.util.Map;

public interface IPlayerEditor extends IRomEditor {

    /**
     * @return 所有玩家的初始属性
     */
    Map<Player, PlayerInitialAttributes> getInitialAttributes();

    /**
     * @return 指定玩家的初始属性
     */
    PlayerInitialAttributes getInitialAttributes(Player player);

    /**
     * @return 指定玩家的初始属性
     */
    default PlayerInitialAttributes getInitialAttributes(int playerId) {
        return getInitialAttributes(Player.formId(playerId));
    }

    /**
     * 获取玩家的最大等级（包含）
     *
     * @return 玩家的最大等级（包含）
     */
    default int getMaxLevel() {
        return 99;
    }

    /**
     * 设置初始金钱
     */
    void setMoney(@Range(from = 0x00, to = 0xFFFFFF) int money);

    /**
     * @return 金钱
     */
    @Range(from = 0x00, to = 0xFFFFFF)
    int getMoney();

    /**
     * @return 数组形式的金钱
     */
    byte[] getMoneyByteArray();

    DataAddress getPlayerAddress();
}
