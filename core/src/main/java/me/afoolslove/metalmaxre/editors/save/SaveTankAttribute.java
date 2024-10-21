package me.afoolslove.metalmaxre.editors.save;

import me.afoolslove.metalmaxre.editors.tank.EquipmentDamaged;
import me.afoolslove.metalmaxre.editors.tank.TankInitialAttribute;
import me.afoolslove.metalmaxre.utils.ListSingleMap;

/**
 * 存档中的坦克属性
 *
 * @author AFoolLove
 */
public class SaveTankAttribute extends TankInitialAttribute {
    /**
     * 异常状态
     */
    public byte deBuff;
    /**
     * 武器的剩余弹药
     */
    public byte[] residualShells;
    /**
     * 装备破损状态
     */
    public EquipmentDamaged[] equipmentsDamaged;
    /**
     * 特殊炮弹
     * <p>
     * K: 特殊炮弹ID
     * <p>
     * V: 特殊炮弹数量
     */
    public ListSingleMap<Byte, Byte> specialShells;
    /**
     * 道具，8byte
     */
    public byte[] inventory;


    public byte getDeBuff() {
        return deBuff;
    }

    public byte[] getResidualShells() {
        return residualShells;
    }

    public EquipmentDamaged[] getEquipmentsDamaged() {
        return equipmentsDamaged;
    }

    public ListSingleMap<Byte, Byte> getSpecialShells() {
        return specialShells;
    }

    /**
     * @return 道具
     */
    public byte[] getInventory() {
        return inventory;
    }

    public void setDeBuff(byte deBuff) {
        this.deBuff = deBuff;
    }

    public void setResidualShells(byte[] residualShells) {
        this.residualShells = residualShells;
    }

    public void setEquipmentsDamaged(EquipmentDamaged[] equipmentsDamaged) {
        this.equipmentsDamaged = equipmentsDamaged;
    }

    public void setSpecialShells(ListSingleMap<Byte, Byte> specialShells) {
        this.specialShells = specialShells;
    }

    public void setInventory(byte[] inventory) {
        this.inventory = inventory;
    }
}
