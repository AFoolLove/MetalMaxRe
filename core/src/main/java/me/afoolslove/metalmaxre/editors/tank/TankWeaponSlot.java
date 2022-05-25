package me.afoolslove.metalmaxre.editors.tank;

/**
 * 坦克开洞状态
 *
 * @author AFoolLove
 */
public enum TankWeaponSlot {
    /**
     * 主炮开洞
     */
    MAIN_GUN(0B1000_0000),
    /**
     * 副炮开洞
     */
    SECONDARY_GUN(0B0100_0000),
    /**
     * S-E 开洞
     */
    SPECIAL_EQUIPMENT(0B0010_0000);

    private final byte slot;

    TankWeaponSlot(int slot) {
        this.slot = (byte) (slot & 0xFF);
    }

    public byte getSlot() {
        return slot;
    }

    /**
     * @return 所有部位开洞
     */
    public static byte all() {
        return (byte) 0B1110_0000;
    }
}
