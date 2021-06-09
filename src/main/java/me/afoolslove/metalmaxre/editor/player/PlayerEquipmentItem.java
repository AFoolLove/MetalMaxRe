package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.DataValues;
import me.afoolslove.metalmaxre.Item;
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
     * @see DataValues#get2ByteValue()
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
        int length = Math.min(3, players.length);
        for (int i = 0; i < length; i++) {
            switch (players[i]) {
                case HANTA:
                    canEquipped |= (byte) 0B1000_0000;
                    break;
                case TAMPER:
                    canEquipped |= (byte) 0B0100_0000;
                    break;
                case ANNE:
                    canEquipped |= (byte) 0B0010_0000;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @return 玩家是否可以装备该装备
     */
    public boolean hasEquipped(@NotNull Player player) {
        switch (player) {
            case HANTA:
                return (canEquipped & 0B1000_0000) != 0;
            case TAMPER:
                return (canEquipped & 0B0100_0000) != 0;
            case ANNE:
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
