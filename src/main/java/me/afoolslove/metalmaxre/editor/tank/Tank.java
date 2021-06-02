package me.afoolslove.metalmaxre.editor.tank;

/**
 * 坦克
 *
 * @author AFoolLove
 */

public enum Tank {
    /**
     * 默认WEN式
     */
    NO_1(0x00),
    /**
     * 默认HOR式
     */
    NO_2(0x01),
    /**
     * 默认CAR式
     */
    NO_3(0x02),
    /**
     * 默认TAN式
     */
    NO_4(0x03),
    /**
     * 默认BAO式
     */
    NO_5(0x04),
    /**
     * 默认HU式
     */
    NO_6(0x05),
    /**
     * 默认RLA式
     */
    NO_7(0x06),
    /**
     * 默认KHU式
     */
    NO_8(0x07),

    /**
     * 出租坦克
     */
    TAX_1(0x08),
    TAX_2(0x09),
    TAX_3(0x0A),
    TAX_4(0x0B),
    TAX_5(0x0C),
    TAX_6(0x0D),
    TAX_7(0x0E),
    TAX_8(0x0F),
    TAX_9(0x10),
    TAX_A(0x11);

    public static final int COUNT = 0x12;
    public static final int TAX_COUNT = 0x0A;
    public static final int NO_COUNT = 0x08;

    private final byte id;

    Tank(int id) {
        this.id = (byte) (id & 0xFF);
    }

    public byte getId() {
        return id;
    }

    /**
     * @return 坦克的名称
     */
    public String getName() {
        // 暂时直接显示枚举名称
        return this.name();
    }
}
