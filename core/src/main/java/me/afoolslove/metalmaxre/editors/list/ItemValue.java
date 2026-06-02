package me.afoolslove.metalmaxre.editors.list;

/**
 * 清单基本项目
 *
 * @author AFoolLove
 */
public class ItemValue implements Cloneable {
    private byte item;

    public ItemValue() {
        this.item = 0x00;
    }

    public ItemValue(byte item) {
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

    public ItemValue copy() {
        return new ItemValue(item);
    }
}
