package me.afoolslove.metalmaxre.editors.computer.vendor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 售货机商品编辑器
 * <p>
 * 0x00 固定值 0x0D
 * 0x01 - 0x06  商品类型
 * 0x07 - 0x0C  商品数量
 * 0x0D 中奖物品
 * 数量byte D7的值为1时，商品为无货
 *
 * @author AFoolLove
 */
public class VendorEditorImpl extends RomBufferWrapperAbstractEditor implements IVendorEditor {
    private final DataAddress vendorsAddress;

    /**
     * 所有售货机的商品组合
     */
    private final List<VendorItemList> vendorItemLists = new ArrayList<>();

    public VendorEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x23EC8 - 0x10, 0x23FC4 - 0x10));
    }

    public VendorEditorImpl(@NotNull MetalMaxRe metalMaxRe, DataAddress vendorsAddress) {
        super(metalMaxRe);
        this.vendorsAddress = vendorsAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getVendorItemLists().clear();

        byte[] items = new byte[getVendorTypeCount()];
        byte[] counts = new byte[getVendorTypeCount()];
        // 读取售货机的商品组合
        // 0x0D 为每组数据固定头字节，非头字节视为读取完毕
        position(getVendorsAddress());
        while (getBuffer().get() == 0x0D) {
            // 读取商品
            getBuffer().get(items);
            // 读取商品数量和是否有货
            getBuffer().get(counts);

            VendorItemList itemList = new VendorItemList();
            // 添加商品
            for (int i = 0; i < items.length; i++) {
                itemList.add(new VendorItem(items[i], counts[i]));
            }
            // 获取中奖物品
            itemList.setAward(getBuffer().get());
            getVendorItemLists().add(itemList);
        }
    }

    @Editor.Apply
    public void onApply() {
        // 优先储存后加入的
        ArrayList<VendorItemList> vendorItemLists = new ArrayList<>(getVendorItemLists());
        int fromIndex = Math.max(0, vendorItemLists.size() - getVendorTypeCount());
        byte[] items = new byte[getVendorTypeCount()];
        byte[] counts = new byte[getVendorTypeCount()];
        position(getVendorsAddress());
        for (int index = fromIndex, size = vendorItemLists.size(); index < size; index++) {
            VendorItemList vendorItemList = vendorItemLists.get(index);
            // 写入固定头
            getBuffer().put(0x0D);
            // 写入商品和数量
            for (int i = 0; i < items.length; i++) {
                VendorItem item = vendorItemList.get(i);
                items[i] = item.getItem();
                counts[i] = item.getCount();
            }
            getBuffer().put(items);
            getBuffer().put(counts);
            // 写入奖品
            getBuffer().put(vendorItemList.getAward());
        }
    }

    @Override
    public DataAddress getVendorsAddress() {
        return vendorsAddress;
    }

    @Override
    public List<VendorItemList> getVendorItemLists() {
        return vendorItemLists;
    }

    @Override
    public VendorItemList getVendorItemList(int vendor) {
        return vendorItemLists.get(vendor);
    }
}
