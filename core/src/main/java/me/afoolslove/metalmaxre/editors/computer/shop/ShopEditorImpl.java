package me.afoolslove.metalmaxre.editors.computer.shop;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 售货机商品编辑器
 * <p>
 * 0x00 固定值 0x0D
 * 0x01 - 0x06  商品类型
 * 0x07 - 0x0C  商品数量
 * 0x0D 中奖物品
 * 数量byte D7的值为1时，商品为无货
 * <p>
 * TODO 需要重新验证数据
 *
 * @author AFoolLove
 */
public class ShopEditorImpl extends RomBufferWrapperAbstractEditor implements IShopEditor {
    private final DataAddress shopAddress;
    private final DataAddress vendorsAddress;

    /**
     * 所有售货机的商品组合
     */
    private final List<VendorItemList> vendorItemLists = new ArrayList<>();
    private final List<List<Byte>> shopLists = new ArrayList<>();

    public ShopEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x23CC5 - 0x10, 0x23EC7 - 0x10),
                DataAddress.fromPRG(0x23EC8 - 0x10, 0x23FC4 - 0x10));
    }

    public ShopEditorImpl(@NotNull MetalMaxRe metalMaxRe, DataAddress shopAddress, DataAddress vendorsAddress) {
        super(metalMaxRe);
        this.shopAddress = shopAddress;
        this.vendorsAddress = vendorsAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getShopLists().clear();
        getVendorItemLists().clear();

        // 读取商品清单
        position(getShopAddress());
        while (getShopAddress().range(position())) {
            // 读取商品数量
            int count = getBuffer().getToInt();
            // 读取商品
            byte[] items = new byte[count];
            getBuffer().get(items);
            // 将商品添加到list
            List<Byte> list = new ArrayList<>();
            for (byte item : items) {
                list.add(item);
            }
            getShopLists().add(list);
        }

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
//        // 写入商品清单
//        position(getShopAddress());
//
//        List<byte[]> shopItems = new ArrayList<>();
//        for (List<Byte> shopList : getShopLists()) {
//            if (shopList.isEmpty()) {
//                // 空的清单
//                continue;
//            }
//            ByteArrayOutputStream shopItem = new ByteArrayOutputStream(shopList.size() + 1);
//            shopItem.write(shopList.size() & 0xFF);
//            for (Byte aByte : shopList) {
//                shopItem.write(aByte);
//            }
//            shopItems.add(shopItem.toByteArray());
//        }
//
//        position(getShopAddress());
//        for (byte[] shopItem : shopItems) {
//            if (position() == getShopAddress().getEndAddress() || !getShopAddress().range(position() + 0x02)) {
//                // 至少剩余2字节
//                System.err.printf("商品编辑器：没有多余的空间存放商品清单：%s\n", Arrays.toString(shopItem));
//                continue;
//            }
//
//            if (getShopAddress().range(position() + shopItem.length)) {
//                // 可以完整写入
//                getBuffer().put(shopItem);
//            } else {
//                // 剩余的空间不足以写入这张清单
//                // 尽量写入部分
//                int length = getShopAddress().getEndAddress() - position();
//
//                // 调整数量裁剪写入
//                shopItem[0] = (byte) (length - 1);
//                getBuffer().put(shopItem, 0x00, length);
//                System.err.printf("商品编辑器：商品清单部分未写入：%s\n", Arrays.toString(shopItem));
//            }
//        }


        // 写入商品清单
        position(getShopAddress());
        ByteArrayOutputStream shopItems = new ByteArrayOutputStream(getShopAddress().length());
        for (List<Byte> shopList : getShopLists()) {
            if (shopList.isEmpty()) {
                // 空的清单
                continue;
            }
            shopItems.write(shopList.size());
            for (Byte aByte : shopList) {
                shopItems.write(aByte);
            }
        }
        getBuffer().put(shopItems.toByteArray(), 0, Math.min(shopItems.size(), getShopAddress().length()));
        if (shopItems.size() > getShopAddress().length()) {
            System.err.printf("商品编辑器：错误！超出了数据上限%d字节\n", getShopAddress().length() - shopItems.size());
        }

        // 优先储存后加入的
        byte[] items = new byte[getVendorTypeCount()];
        byte[] counts = new byte[getVendorTypeCount()];
        position(getVendorsAddress());
        for (VendorItemList vendorItemList : getVendorItemLists().subList(0x00, Math.min(getVendorMaxCount(), getVendorItemLists().size()))) {
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

        // 使用FF填充未使用的数据
        int end = getVendorsAddress().getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            System.out.printf("售货机编辑器：剩余%d个空闲字节\n", end);
            if (end != 0) {
                byte[] fill = new byte[end];
                Arrays.fill(fill, (byte) 0xFF);
                getBuffer().put(fill);
            }
        } else {
            System.err.printf("售货机编辑器：错误！超出了数据上限%d字节\n", -end);
        }
    }

    @Override
    public List<List<Byte>> getShopLists() {
        return shopLists;
    }

    @Override
    public List<VendorItemList> getVendorItemLists() {
        return vendorItemLists;
    }

    @Override
    public VendorItemList getVendorItemList(int vendor) {
        return vendorItemLists.get(vendor);
    }

    @Override
    public DataAddress getVendorsAddress() {
        return vendorsAddress;
    }

    @Override
    public DataAddress getShopAddress() {
        return shopAddress;
    }
}
