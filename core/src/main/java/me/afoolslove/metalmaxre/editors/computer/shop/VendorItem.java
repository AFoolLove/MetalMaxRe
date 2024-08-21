package me.afoolslove.metalmaxre.editors.computer.shop;

import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 售货机商品
 *
 * @author AFoolLove
 */
public class VendorItem extends ShopItem {
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
     * @see #getItem()
     */
    public void setItem(@Range(from = 0x00, to = 0xFF) int item) {
        super.setItem(item);
    }

    /**
     * 设置商品
     *
     * @param item 商品
     * @see #setItem(int)
     * @see #getItem()
     */
    public void setItem(byte item) {
        super.setItem(item);
    }

    /**
     * 设置商品数量，包含是否有货
     *
     * @param count 商品数量
     * @see #setCount(byte)
     * @see #getCount()
     * @see #intCount()
     */
    public void setCount(@Range(from = 0x00, to = 0xFF) int count) {
        this.count = (byte) (count & 0xFF);
    }

    /**
     * 设置商品数量，包含是否有货
     *
     * @param count 商品数量
     * @see #setCount(int)
     * @see #getCount()
     * @see #intCount()
     */
    public void setCount(byte count) {
        this.count = count;
    }

    /**
     * @return 商品
     * @see #intItem()
     * @see #setItem(byte)
     * @see #setItem(int)
     */
    public byte getItem() {
        return super.getItem();
    }

    /**
     * @return 商品
     * @see #getItem()
     * @see #setItem(byte)
     * @see #setItem(int)
     */
    public int intItem() {
        return super.intItem();
    }

    /**
     * @return 商品数量，最大0x7F(127)
     * @see #intCount()
     * @see #setCount(byte)
     * @see #setCount(int)
     */
    public byte getCount() {
        return (byte) (count & 0x7F);
    }

    /**
     * @return 商品数量，最大0x7F(127)
     * @see #getCount()
     * @see #setCount(byte)
     * @see #setCount(int)
     */
    @Range(from = 0x00, to = 0x7F)
    public int intCount() {
        return (byte) (count & 0x7F);
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
        return String.format("VendorItem{item=%02X, count=%02X}", getItem(), getCount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VendorItem that)) {
            return false;
        }
        return getItem() == that.getItem() && getCount() == that.getCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getCount());
    }
}
