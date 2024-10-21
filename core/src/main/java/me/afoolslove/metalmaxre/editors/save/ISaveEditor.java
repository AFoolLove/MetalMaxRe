package me.afoolslove.metalmaxre.editors.save;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.monster.MonsterType;
import me.afoolslove.metalmaxre.editors.player.Player;
import me.afoolslove.metalmaxre.editors.tank.Tank;
import me.afoolslove.metalmaxre.utils.GameMode;
import org.jetbrains.annotations.NotNull;

public interface ISaveEditor {
    MetalMaxRe getMetalMaxRe();

    SaveData getSaveData();

    default void setData(int address, byte[] data, int offset, int length) {
        getSaveData().getBuffer().put(address, data, offset, length);
    }

    default void setData(int address, byte[] data) {
        getSaveData().getBuffer().put(address, data);
    }

    default void setData(int address, byte data) {
        getSaveData().getBuffer().put(address, data);
    }

    default byte[] getData(int address, int length) {
        byte[] data = new byte[length];
        getSaveData().getBuffer().get(address, data);
        return data;
    }

    default byte getData(int address) {
        return getSaveData().getBuffer().get(address);
    }

    /**
     * 设置玩家名称
     * <p>
     * *$6400~$640E
     *
     * @param player 玩家
     * @param name   新的名称
     */
    void setPlayerName(@NotNull Player player, String name);

    /**
     * 设置坦克名称
     * <p>
     * *$640F~$6446
     *
     * @param tank 坦克
     * @param name 新的名称
     */
    void setTankName(@NotNull Tank tank, String name);

    /**
     * 设置游戏模式
     *
     * @param gameMode 模式
     */
    void setGameMode(@NotNull GameMode gameMode);

    /**
     * 设置金钱
     *
     * @param money 金钱
     */
    void setMoney(int money);

    /**
     * 设置玩家属性
     *
     * @param player    玩家
     * @param attribute 玩家属性
     */
    void setPlayerAttribute(@NotNull Player player, @NotNull SavePlayerAttribute attribute);

    /**
     * 设置坦克属性
     *
     * @param tank      坦克
     * @param attribute 坦克属性
     */
    void setTankAttribute(@NotNull Tank tank, @NotNull SaveTankAttribute attribute);

    /**
     * 设置击破赏金首时的等级
     *
     * @param wantedMonsterId 被击破的赏金首ID
     * @param breakLevel      击破赏金首时玩家的等级
     */
    void setWantedBreakLevel(int wantedMonsterId, int breakLevel);

    /**
     * 设置击破数量
     *
     * @param type   怪物类型
     * @param number 击破数量
     */
    void setBreakNumber(@NotNull MonsterType type, int number);

    /**
     * 设置金铃
     *
     * @param gold 到达多少金钱后金钟响起
     */
    void setGoldChime(int gold);

    /**
     * 设置是否需要进行检查强度才能开始移动
     *
     * @param checkCode 检查代码，非0为需要检查
     */
    void setCheckCode(int checkCode);

    /**
     * 设置后备箱保存的物品（保管所）
     *
     * @param items 物品
     */
    void setTrunkItems(byte[] items);

    /**
     * 设置拖动战车火物体
     *
     * @param object 被托走的物体
     */
    void setTowObject(byte object);

    /**
     * 设置出租战车的出租状态
     *
     * @param taxTank 出租战车
     * @param tankId  出租的战车ID，0xFF为未出租
     */
    void setTaxState(@NotNull Tank taxTank, int tankId);


    /**
     * 获取玩家名称
     *
     * @param player 玩家
     * @return 玩家名称
     */
    byte[] getPlayerName(@NotNull Player player);

    /**
     * 获取坦克名称
     *
     * @param tank 坦克
     */
    byte[] getTankName(@NotNull Tank tank);

    /**
     * 获取游戏模式
     *
     * @return 游戏模式
     */
    byte getGameMode();

    /**
     * 获取金钱
     *
     * @return 金钱
     */
    byte[] getMoney();

    /**
     * 获取玩家属性
     *
     * @param player 玩家
     * @return 玩家属性
     */
    SavePlayerAttribute getPlayerAttribute(@NotNull Player player);

    /**
     * 获取坦克属性
     *
     * @param tank 坦克
     * @return 坦克属性
     */
    SaveTankAttribute getTankAttribute(@NotNull Tank tank);

    /**
     * 获取击破赏金首时的等级
     *
     * @param wantedMonsterId 被击破的赏金首ID
     */
    byte getWantedBreakLevel(int wantedMonsterId);

    /**
     * 获取击破数量
     *
     * @param type 怪物类型
     */
    byte[] getBreakNumber(@NotNull MonsterType type);

    /**
     * 获取金铃
     *
     * @return 金铃
     */
    byte[] getGoldChime();

    /**
     * 获取是否需要进行检查强度才能开始移动
     *
     * @return 检查代码，非0为需要检查
     */
    byte getCheckCode();

    /**
     * 获取背包保存的物品（保管所）
     *
     * @return 保管的物品
     */
    byte[] getTrunkItems();

    /**
     * 获取拖动战车火物体
     *
     * @return 物体
     */
    byte getTowObject();

    /**
     * 获取出租战车的出租状态
     *
     * @param taxTank 出租战车
     * @return 出租的战车ID，0xFF为未出租
     */
    byte getTaxState(@NotNull Tank taxTank);
}
