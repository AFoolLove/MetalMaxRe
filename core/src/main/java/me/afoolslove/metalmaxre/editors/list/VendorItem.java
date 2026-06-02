package me.afoolslove.metalmaxre.editors.list;

import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 售货机商品
 *
 * @author AFoolLove
 */
public class VendorItem extends ItemValue {
    private byte count;

    public VendorItem(byte item, byte count) {
        super(item);
        this.count = count;
    }

    /**
     * 设置商品
     *
     * @param item 商品
     * @see #setItem(byte)
     * @see #getRawItem()
     */
    public void setItem(@Range(from = 0x00, to = 0xFF) int item) {
        super.setItem(item);
    }

    /**
     * 设置商品
     *
     * @param item 商品
     * @see #setItem(int)
     * @see #getRawItem()
     */
    public void setItem(byte item) {
        super.setItem(item);
    }

    /**
     * 设置商品数量，包含是否有货
     *
     * @param count 商品数量
     * @see #setRawCount(byte)
     * @see #getCount()
     * @see #intCount()
     */
    public void setRawCount(@Range(from = 0x00, to = 0xFF) int count) {
        this.count = (byte) (count & 0xFF);
    }

    /**
     * 设置商品数量，包含是否有货
     *
     * @param count 商品数量
     * @see #setRawCount(int)
     * @see #getCount()
     * @see #intCount()
     */
    public void setRawCount(byte count) {
        this.count = count;
    }

    /**
     * @return 商品
     * @see #intRawItem()
     * @see #setItem(byte)
     * @see #setItem(int)
     */
    public byte getRawItem() {
        return super.getItem();
    }

    /**
     * @return 商品
     * @see #getRawItem()
     * @see #setItem(byte)
     * @see #setItem(int)
     */
    public int intRawItem() {
        return super.intItem();
    }

    /**
     * @return 商品数量，最大0x7F(127)
     * @see #intCount()
     * @see #setRawCount(byte)
     * @see #setRawCount(int)
     */
    public byte getCount() {
        return (byte) (getRawCount() & 0x7F);
    }

    /**
     * @return 商品数量，最大0x7F(127)
     * @see #getCount()
     * @see #setRawCount(byte)
     * @see #setRawCount(int)
     */
    @Range(from = 0x00, to = 0x7F)
    public int intCount() {
        return getRawCount() & 0x7F;
    }

    public byte getRawCount() {
        return this.count;
    }

    public int intRawCount() {
        return getRawCount() & 0xFF;
    }

    /**
     * 设置商品是否有货
     *
     * @param hasItems true 为有货，false 为无货
     * @see #hasItems()
     */
    public void hasItems(boolean hasItems) {
        if (hasItems) {
            count &= 0B0111_1111;
        } else {
            count |= (byte) 0B1000_0000;
        }
    }

    /**
     * @return 商品是否有货
     * @see #hasItems(boolean)
     */
    public boolean hasItems() {
        return (count & 0B1000_0000) == 0x00;
    }

    @Override
    public String toString() {
        return String.format("VendorItem{item=%02X, count=%02X}", getRawItem(), getCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VendorItem that)) {
            return false;
        }
        return getRawItem() == that.getRawItem() && getRawCount() == that.getRawCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRawItem(), getRawCount());
    }
}
