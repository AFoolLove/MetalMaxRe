package me.afoolslove.metalmaxre.editors.items;

import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import org.jetbrains.annotations.NotNull;
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
     * @return 是否为无价的物品
     */
    public boolean isPriceless() {
        return (getPrice() & 0xFF) == 0xFE || (getPrice() & 0xFF) == 0xFF;
    }

    /**
     * @return 是否可以丢弃
     */
    public boolean canDiscard() {
        return (getPrice() & 0xFF) != 0xFF;
    }

    /**
     * @return 真实的价格
     */
    public int getPriceValue(@NotNull IDataValueEditor dataValueEditor) {
        if (isPriceless()) {
            return getPrice();
        }
        return dataValueEditor.getValues().get(getPrice() & 0xFF).intValue();
    }
}
