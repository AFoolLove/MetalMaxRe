package me.afoolslove.metalmaxre.editors.tank;

import org.jetbrains.annotations.Range;

/**
 * 坦克引擎装备
 *
 * @author AFoolLove
 */
public class TankEngine extends TankEquipmentItem {
    private boolean improvable = true;

    /**
     * 设置引擎的最大载重量
     * <p>
     * 注：超过99后显示??:??
     * <p>
     * 单位：1:1t
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

    /**
     * @return 引擎的最大载重量
     */
    public int intCapacity() {
        return getCapacity() & 0xFF;
    }

    /**
     * 设置是否能够被改造
     */
    public void setImprovable(boolean improvable) {
        this.improvable = improvable;
    }

    /**
     * @return 引擎是否可以被改造
     */
    public boolean isImprovable() {
        return improvable;
    }
}
