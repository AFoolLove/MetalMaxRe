package me.afoolslove.metalmaxre.editor.computer.vendor;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 售货机商品编辑器
 * <p>
 * 起始：0x23EC8
 * 结束：0x23FC4
 * <p>
 * 0x00 固定值 0x0D
 * 0x01 - 0x06  商品类型
 * 0x07 - 0x0C  商品数量
 * 0x0D 中奖物品
 * 数量byte D7的值为1时，商品为无货
 * <p>
 * <p>
 * *兼容多版本
 *
 * @author AFoolLove
 */
public class VendorEditor extends AbstractEditor<VendorEditor> {
    /**
     * 售货机商品的最大组合数量
     */
    public static final int VENDOR_MAX_COUNT = 0x12;

    /**
     * 售货机商品种类数量
     */
    public static final int VENDOR_ITEM_COUNT = 0x06;

    /**
     * 售货机数据地址
     */
    public static final int VENDOR_START_OFFSET = 0x23EC8 - 0x10;
    public static final int VENDOR_END_OFFSET = 0x23FC4 - 0x10;

    /**
     * 所有售货机的商品组合
     */
    private final List<VendorItemList> vendorItemLists = new ArrayList<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        vendorItemLists.clear();

        byte[] items = new byte[VENDOR_ITEM_COUNT];
        byte[] counts = new byte[VENDOR_ITEM_COUNT];
        // 读取售货机的商品组合
        // 0x0D 为每组数据固定头字节，非头字节视为读取完毕
        setPrgRomPosition(VENDOR_START_OFFSET);
        while (get(buffer) == 0x0D) {
            // 读取商品
            get(buffer, items);
            // 读取商品数量和是否有货
            get(buffer, counts);

            VendorItemList itemList = new VendorItemList();
            // 添加商品
            for (int i = 0; i < VENDOR_ITEM_COUNT; i++) {
                itemList.add(new VendorItem(items[i], counts[i]));
            }
            // 获取中奖物品
            itemList.setAward(get(buffer));
            vendorItemLists.add(itemList);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 优先储存后加入的
        ArrayList<VendorItemList> vendorItemLists = new ArrayList<>(getVendorItemLists());
        int fromIndex = Math.max(0, vendorItemLists.size() - VENDOR_MAX_COUNT);
        byte[] items = new byte[VENDOR_ITEM_COUNT];
        byte[] counts = new byte[VENDOR_ITEM_COUNT];
        setPrgRomPosition(VENDOR_START_OFFSET);
        for (int index = fromIndex, size = vendorItemLists.size(); index < size; index++) {
            VendorItemList vendorItemList = vendorItemLists.get(index);
            // 写入固定头
            put(buffer, 0x0D);
            // 写入商品和数量
            for (int i = 0; i < VENDOR_ITEM_COUNT; i++) {
                VendorItem item = vendorItemList.get(i);
                items[i] = item.getItem();
                counts[i] = item.getCount();
            }
            put(buffer, items);
            put(buffer, counts);
            // 写入奖品
            put(buffer, vendorItemList.getAward());
        }
        return true;
    }

    /**
     * @return 所有售货机的商品列表
     * @see #getVendorItemList(int)
     */
    public List<VendorItemList> getVendorItemLists() {
        return vendorItemLists;
    }

    /**
     * 通过索引获取售货机的商品列表
     *
     * @param vendor 售货机索引
     * @return 某个售货机的商品列表
     * @see #getVendorItemLists()
     */
    public VendorItemList getVendorItemList(int vendor) {
        return vendorItemLists.get(vendor);
    }
}
