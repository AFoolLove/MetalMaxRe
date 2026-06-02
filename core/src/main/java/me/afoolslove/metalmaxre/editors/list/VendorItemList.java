package me.afoolslove.metalmaxre.editors.list;

import me.afoolslove.metalmaxre.utils.ItemList;
import org.jetbrains.annotations.Range;

import java.io.ByteArrayOutputStream;

/**
 * 售货机商品清单
 * <p>
 * 长度 + 项目*数量 + 项目数量*数量 + [奖品]
 *
 * @author AFoolLove
 */
public class VendorItemList extends ItemList<VendorItem> {
    /**
     * 售货机中奖后给予的奖品
     * <p>
     * 为null时无奖品
     */
    private Byte award = 0x00;

    /**
     * @return 售货机中奖后给予的奖品
     */
    public Byte getAward() {
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
     * @see #setAward(Byte)
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
    public void setAward(Byte award) {
        this.award = award;
    }

    public boolean hasAward() {
        return this.award != null;
    }

    @Override
    public byte[] toByteArray() {
        boolean hasAward = hasAward();
        int len = size() * 2 + (hasAward ? 1 : 0);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 + len);
        // 写入长度
        outputStream.write(len);
        // 写入项目
        for (VendorItem item : this) {
            outputStream.write(item.getItem());
        }
        // 写入项目数量
        for (VendorItem item : this) {
            // 无奖品时，将项目强制为无货模式
            outputStream.write(item.getRawCount() | (hasAward ? 0 : 0B1000_0000));
        }
        // 写入奖品
        if (hasAward) {
            outputStream.write(getAward());
        }
        return outputStream.toByteArray();
    }

    @Override
    public String toString() {
        return String.format("VendorItemList{award=%02X}", award);
    }
}
