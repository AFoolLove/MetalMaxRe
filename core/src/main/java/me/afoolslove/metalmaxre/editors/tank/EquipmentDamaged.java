package me.afoolslove.metalmaxre.editors.tank;

/**
 * 装备破损状态
 *
 * @author AFoolLove
 */
public enum EquipmentDamaged {
    /**
     * 正常
     */
    OK(0B0000_0000),
    /**
     * 小破
     */
    DAMAGED(0B1000_0000),
    /**
     * 大破
     */
    BROKEN(0B1100_0000);

    private final byte value;

    EquipmentDamaged(int value) {
        this.value = (byte) (value & 0xFF);
    }

    public byte getValue() {
        return value;
    }

    public static EquipmentDamaged fromId(int id) {
        for (EquipmentDamaged equipmentDamaged : values()) {
            if (equipmentDamaged.getValue() == (byte) id) {
                return equipmentDamaged;
            }
        }
        return null;
    }
}
