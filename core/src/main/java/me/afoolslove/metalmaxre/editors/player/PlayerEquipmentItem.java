package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.editors.items.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 玩家的装备
 * 武器、防具
 *
 * @author AFoolLove
 */
public class PlayerEquipmentItem extends Item {
    /**
     * 值
     *
     * @see IDataValueEditor#get1ByteValues()
     */
    public byte value;

    /**
     * 可装备的玩家
     */
    public byte canEquipped;

    /**
     * 设置玩家可装备该装备
     */
    public void setCanEquipped(@Range(from = 0x00, to = 0xFF) int canEquipped) {
        this.canEquipped = (byte) (canEquipped & 0xFF);
    }

    /**
     * 设置玩家可装备该装备
     */
    public void setCanEquipped(@NotNull Player... players) {
        if (players.length == 0) {
            // 谁也不能装备
            // 为空可还行
            canEquipped &= 0B0001_1111;
            return;
        }
        addCanEquipped(players);
    }

    /**
     * 添加可装备改装备的玩家
     */
    public void addCanEquipped(@NotNull Player... players) {
        for (Player player : players) {
            switch (player) {
                case PLAYER_0 -> canEquipped |= (byte) 0B1000_0000;
                case PLAYER_1 -> canEquipped |= (byte) 0B0100_0000;
                case PLAYER_2 -> canEquipped |= (byte) 0B0010_0000;
            }
        }
    }

    /**
     * 移除可装备改装备的玩家
     */
    public void removeCanEquipped(@NotNull Player... players) {
        for (Player player : players) {
            switch (player) {
                case PLAYER_0 -> canEquipped |= (byte) 0B0111_1111;
                case PLAYER_1 -> canEquipped |= (byte) 0B1011_1111;
                case PLAYER_2 -> canEquipped |= (byte) 0B1101_1111;
            }
        }
    }

    /**
     * @return 玩家是否可以装备该装备
     */
    public boolean hasEquipped(@NotNull Player player) {
        return hasEquipped(getCanEquipped(), player);
    }

    /**
     * @return 玩家是否可以装备该装备
     */
    public static boolean hasEquipped(byte canEquipped, @NotNull Player player) {
        switch (player) {
            case PLAYER_0:
                return (canEquipped & 0B1000_0000) != 0;
            case PLAYER_1:
                return (canEquipped & 0B0100_0000) != 0;
            case PLAYER_2:
                return (canEquipped & 0B0010_0000) != 0;
            default:
                break;
        }
        return false;
    }

    /**
     * @return 玩家是否可装备该装备
     */
    public byte getCanEquipped() {
        return canEquipped;
    }
}
