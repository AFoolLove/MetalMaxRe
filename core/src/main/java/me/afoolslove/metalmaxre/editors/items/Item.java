package me.afoolslove.metalmaxre.editors.items;

import org.jetbrains.annotations.Range;

/**
 * 所有物品
 * <p>
 * 包含人类和坦克的工具、装备等
 *
 * @author AFoolLove
 */
public class Item {
    /**
     * 物品的价格数据
     */
    public byte price;

    /**
     * 设置价格
     */
    public void setPrice(@Range(from = 0x00, to = 0xFF) int price) {
        this.price = (byte) (price & 0xFF);
    }

    /**
     * @return 价格数据
     */
    public byte getPrice() {
        return price;
    }

    /**
     * @return 真实的价格
     */
    public int getPriceValue() {
//        return DataValues.VALUES.get(price & 0xFF);
        return 0;
    }
}
