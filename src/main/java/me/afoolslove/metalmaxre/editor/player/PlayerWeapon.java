package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.AttackRange;
import me.afoolslove.metalmaxre.DataValues;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 玩家的武器
 *
 * @author AFoolLove
 */
public class PlayerWeapon extends PlayerEquipmentItem {
    /**
     * 设置攻击力
     *
     * @see DataValues#get2ByteValue()
     */
    public void setAttack(@Range(from = 0x00, to = 0xFF) int attack) {
        value = (byte) (attack & 0xFF);
    }

    /**
     * 设置攻击的范围
     */
    public void setAttackRange(@NotNull AttackRange range) {
        // 清除当前攻击范围数据
        canEquipped &= 0B1110_0111;
        // 设置攻击范围
        canEquipped |= range.getValue();
    }

    /**
     * @return 该武器的攻击范围和可装备的玩家
     */
    @Override
    public byte getCanEquipped() {
        return super.getCanEquipped();
    }

    /**
     * @return 攻击力
     * @see DataValues#get2ByteValue()
     */
    public byte getAttack() {
        return value;
    }

    /**
     * @return 指向的真实攻击力值
     * @see DataValues#get2ByteValue()
     */
    public int getAttackValue() {
        return DataValues.VALUES.get(value);
    }
}
