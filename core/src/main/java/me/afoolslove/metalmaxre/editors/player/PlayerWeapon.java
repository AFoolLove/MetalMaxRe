package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.AttackRange;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 玩家的武器
 *
 * @author AFoolLove
 */
public class PlayerWeapon extends PlayerEquipmentItem {
    /**
     * 攻击动画
     */
    public byte attackAnim;

    /**
     * 设置攻击动画
     */
    public void setAttackAnim(@Range(from = 0x00, to = 0xFF) int attackAnim) {
        this.attackAnim = (byte) (attackAnim & 0xFF);
    }

    /**
     * 设置攻击力
     *
     * @see IDataValueEditor#get2ByteValues()
     */
    public void setAttack(@Range(from = 0x00, to = 0xFF) int attack) {
        setAttack((byte) (attack & 0xFF));
    }

    /**
     * 设置攻击力
     *
     * @see IDataValueEditor#get2ByteValues()
     */
    public void setAttack(byte attack) {
        value = attack;
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
     * @return 攻击动画
     */
    public byte getAttackAnim() {
        return attackAnim;
    }

    /**
     * @return 攻击动画
     */
    public int intAttackAnim() {
        return getAttackAnim() & 0xFF;
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
     * @see IDataValueEditor#get2ByteValues()
     */
    public byte getAttack() {
        return value;
    }

    /**
     * @return 攻击力
     * @see IDataValueEditor#get2ByteValues()
     */
    public int intAttack() {
        return getAttack() & 0xFF;
    }

    /**
     * @return 攻击范围
     */
    public AttackRange getAttackRange() {
        return AttackRange.fromValue(canEquipped & 0B0001_1000);
    }

    /**
     * @return 指向的真实攻击力值
     * @see IDataValueEditor#get2ByteValues()
     */
    public int getAttackValue(@NotNull IDataValueEditor dataValues) {
        return dataValues.getValue(intAttack()).intValue();
    }
}
