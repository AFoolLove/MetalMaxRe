package me.afoolslove.metalmaxre.editor.tank;

/**
 * 坦克装备
 *
 * @author AFoolLove
 */

public enum TankEquipment {
    /**
     * 主炮
     */
    MAIN_GUN(0x00, 0B1000_0000),
    /**
     * 副炮
     */
    SECONDARY_GUN(0x01, 0B0100_0000),
    /**
     * S-E
     */
    SPECIAL_EQUIPMENT(0x02, 0B0010_0000),
    /**
     * C装置
     */
    C_UNIT(0x03, 0B0001_0000),
    /**
     * 引擎
     */
    ENGINE(0x04, 0B0000_1000),
    /**
     * 底盘
     * 注：修改底盘在地图上不会有变化，但战斗时和详细界面会变更为响应的样式
     */
    CHASSIS(0x05, 0B0000_0100);

    private final byte slot;
    private final byte states;

    TankEquipment(int slot, int states) {
        this.slot = (byte) (slot & 0xFF);
        this.states = (byte) (states & 0xFF);
    }

    /**
     * @return 装备所在位置
     */
    public byte getSlot() {
        return slot;
    }

    /**
     * @return 装备的已装备的状态值
     */
    public byte getStates() {
        return states;
    }
}
