package me.afoolslove.metalmaxre.editors.computer.shop;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ItemList;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;

import java.util.List;

public interface IShopEditor extends IRomEditor {
    @Override
    default String getId() {
        return "shopEditor";
    }

    /**
     * 索引指向的 商品/点唱机曲目 列表位置
     */
    List<SingleMapEntry<Character, Integer>> getShopIndexes();

    /**
     * @return 获取所有商店的商品列表
     */
    List<ItemList<ShopItem>> getShopLists();

    /**
     * 通过索引获取商店的商品列表
     *
     * @param shop 商店索引
     * @return 某个商店的商品列表
     */
    default ItemList<ShopItem> getShopList(int shop) {
        return getShopLists().get(shop);
    }

    /**
     * @return 所有售货机的商品列表
     * @see #getVendorItemList(int)
     */
    List<VendorItemList> getVendorItemLists();

    /**
     * 通过索引获取售货机的商品列表
     *
     * @param vendor 售货机索引
     * @return 某个售货机的商品列表
     * @see #getVendorItemLists()
     */
    default VendorItemList getVendorItemList(int vendor) {
        return getVendorItemLists().get(vendor);
    }

    /**
     * @return 商店数据地址
     */
    DataAddress getShopAddress();

    /**
     * @return 商店数据索引地址
     */
    DataAddress getShopIndexAddress();

}
