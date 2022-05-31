package me.afoolslove.metalmaxre.editors.computer.vendor;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;

public interface IVendorEditor extends IRomEditor {
    /**
     * @return 售货机商品的最大组合数量
     */
    default int getVendorMaxCount() {
        return 0x12;
    }

    /**
     * @return 售货机商品种类数量（可以重复
     */
    default int getVendorTypeCount() {
        return 0x06;
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
     * @return 售货机数据地址
     */
    DataAddress getVendorsAddress();

}
