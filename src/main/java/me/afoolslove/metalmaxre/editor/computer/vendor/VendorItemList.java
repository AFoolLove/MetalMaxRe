package me.afoolslove.metalmaxre.editor.computer.vendor;

import java.util.LinkedList;

/**
 * 售货机全部商品
 *
 * @author AFoolLove
 */
public class VendorItemList extends LinkedList<VendorItem> {
    public byte award;

    /**
     * @return 奖品
     */
    public byte getAward() {
        return award;
    }

    /**
     * 设置奖品
     */
    public void setAward(int award) {
        this.award = (byte) (award & 0xFF);
    }

    @Override
    public String toString() {
//        return String.format("VendorItemList{items={{%02X:%02X},{%02X:%02X},{%02X:%02X},{%02X:%02X},{%02X:%02X},{%02X:%02X}},award=%02X}", award);
        return String.format("VendorItemList{award=%02X}", award);
    }
}
