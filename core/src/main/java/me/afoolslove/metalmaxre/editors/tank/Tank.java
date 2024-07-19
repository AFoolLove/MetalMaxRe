package me.afoolslove.metalmaxre.editors.tank;

/**
 * 坦克
 *
 * @author AFoolLove
 */

public enum Tank {
    /**
     * 默认WEN式
     */
    NO_1(0x00, false),
    /**
     * 默认HOR式
     */
    NO_2(0x01, false),
    /**
     * 默认CAR式
     */
    NO_3(0x02, false),
    /**
     * 默认TAN式
     */
    NO_4(0x03, false),
    /**
     * 默认BAO式
     */
    NO_5(0x04, false),
    /**
     * 默认HU式
     */
    NO_6(0x05, false),
    /**
     * 默认RLA式
     */
    NO_7(0x06, false),
    /**
     * 默认KHU式
     */
    NO_8(0x07, false),

    /**
     * 出租坦克
     */
    TAX_1(0x08, true),
    TAX_2(0x09, true),
    TAX_3(0x0A, true),
    TAX_4(0x0B, true),
    TAX_5(0x0C, true),
    TAX_6(0x0D, true),
    TAX_7(0x0E, true),
    TAX_8(0x0F, true),
    TAX_9(0x10, true),
    TAX_A(0x11, true);

    /**
     * 所有坦克总数量
     */
    public static final int ALL_COUNT = 0x12;
    /**
     * 出租坦克总数量
     */
    public static final int TAX_TANK_COUNT = 0x0A;
    /**
     * 玩家可以拥有的坦克总数量
     */
    public static final int PLAYER_TANK_COUNT = 0x08;
    /**
     * 可以同时出现在地图上的坦克数量
     */
    public static final int AVAILABLE_COUNT = 0x0B;

    private final byte id;
    private final boolean isTax;

    Tank(int id, boolean isTax) {
        this.id = (byte) (id & 0xFF);
        this.isTax = isTax;
    }

    /**
     * 获取坦克ID
     *
     * @return 坦克ID
     */
    public byte getId() {
        return id;
    }

    /**
     * @return 是否为出租坦克
     */
    public boolean isTax() {
        return isTax;
    }

    /**
     * @return 坦克的名称
     */
    public String getName() {
        // 暂时直接显示枚举名称
        return this.name();
    }

    /**
     * 通过id获取枚举
     */
    public static Tank fromId(int tankId) {
        for (Tank value : values()) {
            if (value.getId() == tankId) {
                return value;
            }
        }
        return null;
    }
}
