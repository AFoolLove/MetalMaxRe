package me.afoolslove.metalmaxre.editor.tank;

import org.jetbrains.annotations.Range;

/**
 * 坦克引擎装备
 *
 * @author AFoolLove
 */
public class TankEngine extends TankEquipmentItem {

    /**
     * 设置引擎的最大载重量
     * 注：超过99后显示??:??
     * 1:1t
     */
    public void setCapacity(@Range(from = 0x00, to = 0xFF) int capacity) {
        value = (byte) (capacity & 0xFF);
    }

    /**
     * @return 引擎的最大载重量
     */
    public byte getCapacity() {
        return value;
    }
}
