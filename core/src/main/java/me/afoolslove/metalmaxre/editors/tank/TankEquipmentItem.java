package me.afoolslove.metalmaxre.editors.tank;

import me.afoolslove.metalmaxre.DataValues;
import me.afoolslove.metalmaxre.editors.items.Item;
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
     * @see DataValues#get2ByteValue()
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
        this.weight = (byte) (weight & 0xFF);
    }

    /**
     * 设置防御力
     *
     * @see DataValues#get2ByteValue()
     */
    public void setDefense(@Range(from = 0x00, to = 0xFF) int defense) {
        this.defense = (byte) (defense & 0xFF);
    }

    /**
     * @return 装备的重量，1:0.1t
     */
    public byte getWeight() {
        return weight;
    }

    /**
     * @return 防御力
     * @see DataValues#get2ByteValue()
     */
    public byte getDefense() {
        return defense;
    }

    /**
     * @return 指向的真实防御力值
     * @see DataValues#get2ByteValue()
     */
    public int getDefenseValue() {
        return DataValues.VALUES.get(defense & 0xFF);
    }
}
