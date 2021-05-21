package me.afoolslove.metalmaxre.editor.computer.vendor;

import java.util.Objects;

/**
 * 售货机商品
 *
 * @author AFoolLove
 */
public class VendorGood {
    public byte item;
    public byte count;

    public VendorGood(byte item, byte count) {
        this.item = item;
        this.count = count;
    }

    /**
     * 设置商品
     */
    public void setItem(int item) {
        this.item = (byte) (item & 0xFF);
    }

    /**
     * 设置商品数量，包含是否有货
     */
    public void setCount(int count) {
        this.count = (byte) (count & 0xFF);
    }

    /**
     * @return 商品
     */
    public byte getItem() {
        return item;
    }

    /**
     * @return 商品数量，最大0x7F(127)
     */
    public byte getCount() {
        return (byte) (count & 0x7F);
    }

    /**
     * 设置商品是否有货
     */
    public void setNoGoods(boolean goods) {
        if (goods) {
            count |= 0B1000_0000;
        } else {
            count &= 0B0111_1111;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VendorGood)) {
            return false;
        }
        VendorGood that = (VendorGood) o;
        return getItem() == that.getItem() && getCount() == that.getCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getCount());
    }
}
