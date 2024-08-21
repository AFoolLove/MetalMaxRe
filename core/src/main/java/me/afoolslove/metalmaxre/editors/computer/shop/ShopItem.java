package me.afoolslove.metalmaxre.editors.computer.shop;

/**
 * 商店基本物品
 *
 * @author AFoolLove
 */
public class ShopItem {
    private byte item;

    public ShopItem() {
        this.item = 0x00;
    }

    public ShopItem(byte item) {
        this.item = item;
    }

    public byte getItem() {
        return item;
    }

    public int intItem() {
        return getItem() & 0xFF;
    }

    public void setItem(byte item) {
        this.item = item;
    }

    public void setItem(int item) {
        setItem((byte) (item & 0xFF));
    }

}
