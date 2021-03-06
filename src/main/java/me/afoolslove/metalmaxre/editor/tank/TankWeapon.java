package me.afoolslove.metalmaxre.editor.tank;

import me.afoolslove.metalmaxre.AttackRange;
import me.afoolslove.metalmaxre.DataValues;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 坦克装备
 * 主炮、副炮、S-E
 *
 * @author AFoolLove
 */
public class TankWeapon extends TankEquipmentItem {
    /**
     * 攻击动画
     */
    public byte attackAnim;

    /**
     * 可装备的穴
     */
    public byte canEquipped;

    /**
     * 设置攻击动画
     */
    public void setAttackAnim(@Range(from = 0x00, to = 0xFF) int attackAnim) {
        this.attackAnim = (byte) (attackAnim & 0xFF);
    }

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
     * 设置改装备的炮弹容量
     */
    public void setShellCapacity(@NotNull TankShellCapacity capacity) {
        // 清除之前的容量
        canEquipped &= 0B1111_1000;
        // 设置容量
        canEquipped |= capacity.getValue();
    }

    /**
     * 设置可装备此装备的穴
     */
    public void setCanEquipped(@Range(from = 0x00, to = 0xFF) int canEquipped) {
        this.canEquipped = (byte) (canEquipped & 0xFF);
    }

    /**
     * 设置可装备此装备的穴
     */
    public void setCanEquipped(@NotNull TankWeaponSlot... tankWeaponSlots) {
        canEquipped &= 0B0001_1111;
        if (tankWeaponSlots.length == 0) {
            // 谁也不能装备
            // 为空可还行
            return;
        }
        int length = Math.min(3, tankWeaponSlots.length);
        for (int i = 0; i < length; i++) {
            switch (tankWeaponSlots[i]) {
                case MAIN_GUN:
                    canEquipped |= (byte) 0B1000_0000;
                    break;
                case SECONDARY_GUN:
                    canEquipped |= (byte) 0B0100_0000;
                    break;
                case SPECIAL_EQUIPMENT:
                    canEquipped |= (byte) 0B0010_0000;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @return 是否可以装备到某个穴上
     */
    public boolean hasEquipped(@NotNull TankWeaponSlot slot) {
        switch (slot) {
            case MAIN_GUN:
                return (canEquipped & 0B1000_0000) != 0;
            case SECONDARY_GUN:
                return (canEquipped & 0B0100_0000) != 0;
            case SPECIAL_EQUIPMENT:
                return (canEquipped & 0B0010_0000) != 0;
            default:
                break;
        }
        return false;
    }

    /**
     * @return 攻击动画
     */
    public byte getAttackAnim() {
        return attackAnim;
    }

    /**
     * @return 该武器的攻击范围和可装备的穴
     */
    public byte getCanEquipped() {
        return canEquipped;
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
        return DataValues.VALUES.get(value & 0xFF);
    }

    /**
     * @return 炮弹容量
     */
    public TankShellCapacity getShellCapacity() {
        return TankShellCapacity.values()[(canEquipped & 0B0000_0111)];
    }
}
