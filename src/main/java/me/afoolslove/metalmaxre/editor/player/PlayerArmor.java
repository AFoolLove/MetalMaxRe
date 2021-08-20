package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.DataValues;
import org.jetbrains.annotations.Range;

/**
 * 玩家的防具
 *
 * @author AFoolLove
 */
public class PlayerArmor extends PlayerEquipmentItem {

    /**
     * 设置防御力
     *
     * @see DataValues#get2ByteValue()
     */
    public void setDefense(@Range(from = 0x00, to = 0xFF) int defense) {
        value = (byte) (defense & 0xFF);
    }

    /**
     * @return 防御力
     * @see DataValues#get2ByteValue()
     */
    public byte getDefense() {
        return value;
    }

    /**
     * @return 指向的真实防御力值
     * @see DataValues#get2ByteValue()
     */
    public int getDefenseValue() {
        return DataValues.VALUES.get(value & 0xFF);
    }

}
