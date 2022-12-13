package me.afoolslove.metalmaxre.editors.tank;

/**
 * 坦克武器的炮弹容量
 *
 * @author AFoolLove
 */

public enum TankShellCapacity {
    /**
     * 无限炮弹
     */
    INFINITE(0B0000_0111),
    /**
     * 2/4/8/16/32/48/64 有限炮弹
     */
    X2(0B0000_0000),
    X4(0B0000_0001),
    X8(0B0000_0010),
    X16(0B0000_0011),
    X32(0B0000_0100),
    X48(0B0000_0101),
    X64(0B0000_0110);

    private final byte value;

    TankShellCapacity(int value) {
        this.value = (byte) (value & 0B0000_0111);
    }

    public byte getValue() {
        return value;
    }

    public static TankShellCapacity fromValue(int value) {
        byte v = (byte) (value & 0B0000_0111);
        for (TankShellCapacity shellCapacity : values()) {
            if (shellCapacity.getValue() == v) {
                return shellCapacity;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name();
    }
}
