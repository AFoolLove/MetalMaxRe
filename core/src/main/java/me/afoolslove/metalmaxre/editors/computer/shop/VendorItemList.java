package me.afoolslove.metalmaxre.editors.computer.shop;

import me.afoolslove.metalmaxre.utils.ItemList;
import org.jetbrains.annotations.Range;

/**
 * 售货机全部商品
 *
 * @author AFoolLove
 */
public class VendorItemList extends ItemList<VendorItem> {
    /**
     * 售货机中奖后给予的奖品
     */
    private byte award = 0x00;

    /**
     * @return 售货机中奖后给予的奖品
     */
    public byte getAward() {
        return award;
    }

    /**
     * @return 售货机中奖后给予的奖品
     */
    public int intAward() {
        return getAward() & 0xFF;
    }

    /**
     * 设置售货机中奖后给予的奖品
     *
     * @param award 奖品
     * @see #setAward(byte)
     * @see #getAward()
     * @see #intAward()
     */
    public void setAward(@Range(from = 0x00, to = 0xFF) int award) {
        this.award = (byte) (award & 0xFF);
    }

    /**
     * 设置售货机中奖后给予的奖品
     *
     * @param award 奖品
     * @see #setAward(int)
     * @see #getAward()
     * @see #intAward()
     */
    public void setAward(byte award) {
        this.award = award;
    }

    @Override
    public String toString() {
//        return String.format("VendorItemList{items.txt={{%02X:%02X},{%02X:%02X},{%02X:%02X},{%02X:%02X},{%02X:%02X},{%02X:%02X}},award=%02X}", award);
        return String.format("VendorItemList{award=%02X}", award);
    }
}
