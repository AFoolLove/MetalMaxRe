package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import org.jetbrains.annotations.NotNull;
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
        value = defense;
    }

    /**
     * @return 防御力
     * @see IDataValueEditor#get2ByteValues()
     */
    public byte getDefense() {
        return value;
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
        return dataValues.getValues().get(value & 0xFF).intValue();
    }

}
