package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.Range;

/**
 * 所有物品
 * 包含人类和战车的工具、装备
 *
 * @author AFoolLove
 */
public class Item {
    /**
     * 所有物品共计 0xDD 个
     */
    public static final int ITEMS_MAX_COUNT = 0xDD;

    /**
     * 物品的售价
     *
     * @see DataValues
     */
    public byte price;

    /**
     * 设置价格
     *
     * @see DataValues
     */
    public void setPrice(@Range(from = 0x00, to = 0xFF) int price) {
        this.price = (byte) (price & 0xFF);
    }

    /**
     * @return 价格
     * @see DataValues
     */
    public byte getPrice() {
        return price;
    }

    /**
     * @return 指向的真实价格
     * @see DataValues
     */
    public int getPriceValue() {
        return DataValues.VALUES.get(price & 0xFF);
    }
}
