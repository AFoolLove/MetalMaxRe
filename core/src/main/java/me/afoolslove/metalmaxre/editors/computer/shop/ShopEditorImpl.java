package me.afoolslove.metalmaxre.editors.computer.shop;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ItemList;
import me.afoolslove.metalmaxre.utils.ListSingleMap;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 售货机商品编辑器
 * <p>
 * 0x00 固定值 0x0D 为数量
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
    private final DataAddress shopIndexAddress;
    private final DataAddress shopAddress;

    /**
     * 索引指向的 商品/点唱机曲目/售货机 列表位置
     */
    private final ListSingleMap<Character, Integer> shopIndexes = new ListSingleMap<>();
    /**
     * 所有 商品/点唱机曲目/售货机 列表
     */
    private final List<ItemList<Object>> lists = new ArrayList<>();

    public ShopEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x23CA5 - 0x10, 0x23CC4 - 0x10),
                DataAddress.fromPRG(0x23CC5 - 0x10, 0x2400F - 0x10));
    }

    public ShopEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                          @NotNull DataAddress shopIndexAddress,
                          @NotNull DataAddress shopAddress) {
        super(metalMaxRe);
        this.shopIndexAddress = shopIndexAddress;
        this.shopAddress = shopAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getShopIndexes().clear();
        getShopLists().clear();

        // 读取商店索引
        position(getShopIndexAddress());
        for (int i = 0; i < 0x10; i++) {
            // 占位
            getShopIndexes().add(SingleMapEntry.create(getBuffer().getChar(), 0));
        }

        // 读取商品清单
        position(getShopAddress());
        List<ItemList<Object>> ut = new ArrayList<>();
        while (getShopAddress().range(position() - 0x10)) {
            // 读取商品数量
            int count = getBuffer().getToInt();
            if (count == 0xFF) {
                // 读取结束
                break;
            }

            // 判断索引是否为此
            int index = (((position() - 1) - 0x10) % 0x2000) + 0x8000;
            for (int i = 0; i < 0x10; i++) {
                SingleMapEntry<Character, Integer> entry = getShopIndexes().get(i);
                if (index == entry.getKey()) {
                    entry.setValue(ut.size());
                }
            }
            // 读取商品
            byte[] items = new byte[count];
            getBuffer().get(items);
            // 将商品添加到list
            ItemList<Object> list = new ItemList<>();
            for (byte item : items) {
                list.add(item);
            }
            ut.add(list);
        }
        getShopLists().addAll(ut);

        // 将ItemList转换为VendorItemList
        int vendorListOffset = -1;
        // 将ItemList转换为JukeBoxItemList
        int jukeBoxListOffset = -1;
        for (SingleMapEntry<Character, Integer> entry : getShopIndexes()) {
            switch (entry.getKey()) {
                case 0x9EB8: // 售货机
                    vendorListOffset = entry.getValue();
                    break;
                case 0x9EAB: // 点唱机
                    jukeBoxListOffset = entry.getValue();
                    break;
            }
        }

        if (vendorListOffset != -1) {
            // 转换
            for (int i = vendorListOffset, size = getShopLists().size(); i < size; i++) {
                ItemList<Object> itemList = getShopLists().get(i);
                if (itemList.size() != 0x0D) {
                    continue;
                }
                VendorItemList vendorItemList = new VendorItemList();
                getShopLists().set(i, (ItemList) vendorItemList);

                for (int j = 0; j < 0x06; j++) {
                    // 商品
                    Byte item = (Byte) itemList.getItem(j);
                    // 商品数量和是否有货
                    Byte count = (Byte) itemList.getItem(0x06 + j);
                    vendorItemList.add(new VendorItem(item, count));
                    // 中奖物品
                    vendorItemList.setAward((Byte) itemList.getLast());
                }
            }
        }

        if (jukeBoxListOffset != -1) {
            // 转换
            for (int i = jukeBoxListOffset, size = getShopLists().size(); i < size; i++) {
                ItemList<?> itemList = getShopLists().get(i);
                if (itemList instanceof VendorItemList) {
                    continue;
                }
                JukeBoxItemList jukeBoxItemList = new JukeBoxItemList();
                for (Object o : itemList) {
                    if (o instanceof Byte b) {
                        jukeBoxItemList.add(b);
                    }
                }
                getShopLists().set(i, (ItemList) jukeBoxItemList);
            }
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
//                System.err.printf("商品编辑器：没有多余的空间存放商品清单：%s\n", NumberR.toHexString(shopItem));
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
//                System.err.printf("商品编辑器：商品清单部分未写入：%s\n", NumberR.toHexString(shopItem));
//            }
//        }

//
//        // 写入商品清单
//        position(getShopAddress());
//        ByteArrayOutputStream shopItems = new ByteArrayOutputStream(getShopAddress().length());
//        for (List<Byte> shopList : getShopLists()) {
//            if (shopList.isEmpty()) {
//                // 空的清单
//                continue;
//            }
//            shopItems.write(shopList.size());
//            for (Byte aByte : shopList) {
//                shopItems.write(aByte);
//            }
//        }
//        getBuffer().put(shopItems.toByteArray(), 0, Math.min(shopItems.size(), getShopAddress().length()));
//        if (shopItems.size() > getShopAddress().length()) {
//            System.err.printf("商品编辑器：错误！超出了数据上限%d字节\n", getShopAddress().length() - shopItems.size());
//        }

//        // 优先储存后加入的
//        byte[] items = new byte[getVendorTypeCount()];
//        byte[] counts = new byte[getVendorTypeCount()];
//        position(getVendorsAddress());
//        for (VendorItemList vendorItemList : getVendorItemLists().subList(0x00, Math.min(getVendorMaxCount(), getVendorItemLists().size()))) {
//            // 写入固定头
//            getBuffer().put(0x0D);
//            // 写入商品和数量
//            for (int i = 0; i < items.length; i++) {
//                VendorItem item = vendorItemList.get(i);
//                items[i] = item.getItem();
//                counts[i] = item.getCount();
//            }
//            getBuffer().put(items);
//            getBuffer().put(counts);
//            // 写入奖品
//            getBuffer().put(vendorItemList.getAward());
//        }
//
//        // 使用FF填充未使用的数据
//        int end = getVendorsAddress().getEndAddress(-position() + 0x10 + 1);
//        if (end >= 0) {
//            System.out.printf("售货机编辑器：剩余%d个空闲字节\n", end);
//            if (end != 0) {
//                byte[] fill = new byte[end];
//                Arrays.fill(fill, (byte) 0xFF);
//                getBuffer().put(fill);
//            }
//        } else {
//            System.err.printf("售货机编辑器：错误！超出了数据上限%d字节\n", -end);
//        }
    }

    @Override
    public List<SingleMapEntry<Character, Integer>> getShopIndexes() {
        return shopIndexes;
    }

    @Override
    public List<ItemList<Object>> getShopLists() {
        return lists;
    }

    @Override
    public List<VendorItemList> getVendorItemLists() {
        return getShopLists().stream()
                .filter(VendorItemList.class::isInstance)
                .map(VendorItemList.class::cast)
                .toList();
    }

    @Override
    public DataAddress getShopAddress() {
        return shopAddress;
    }

    @Override
    public DataAddress getShopIndexAddress() {
        return shopIndexAddress;
    }
}
