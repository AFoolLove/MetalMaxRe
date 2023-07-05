package me.afoolslove.metalmaxre.editors.tank;

import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.editors.items.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 坦克的装备
 * 主炮、副炮、S-E
 *
 * @author AFoolLove
 */
public class TankEquipmentItem extends Item {
    /**
     * 值
     *
     * @see IDataValueEditor#get2ByteValues()
     */
    public byte value;

    /**
     * 重量
     * 1:0.1t
     */
    public byte weight;

    /**
     * 防御力
     * 1:0.1t
     */
    public byte defense;

    /**
     * 设置装备的重量
     * 1:0.1t
     */
    public void setWeight(@Range(from = 0x00, to = 0xFF) int weight) {
        setWeight((byte) (weight & 0xFF));
    }

    /**
     * 设置装备的重量
     * 1:0.1t
     */
    public void setWeight(byte weight) {
        this.weight = weight;
    }

    /**
     * 设置防御力
     *
     * @see IDataValueEditor#get2ByteValues()
     */
    public void setDefense(@Range(from = 0x00, to = 0xFF) int defense) {
        setDefense((byte) (defense & 0xFF));
    }

    /**
     * 设置防御力
     *
     * @see IDataValueEditor#get2ByteValues()
     */
    public void setDefense(byte defense) {
        this.defense = defense;
    }

    /**
     * @return 装备的重量，1:0.1t
     */
    public byte getWeight() {
        return weight;
    }

    /**
     * @return 装备的重量，1:0.1t
     */
    public int intWeight() {
        return getWeight() & 0xFF;
    }

    /**
     * @return 防御力
     * @see IDataValueEditor#get2ByteValues()
     */
    public byte getDefense() {
        return defense;
    }

    /**
     * @return 防御力
     * @see IDataValueEditor#get2ByteValues()
     */
    public int intDefense() {
        return getDefense() & 0xFF;
    }

    /**
     * @return 指向的真实防御力值
     * @see IDataValueEditor#get2ByteValues()
     */
    public int getDefenseValue(@NotNull IDataValueEditor dataValues) {
        return dataValues.getValue(defense & 0xFF).intValue();
    }
}
