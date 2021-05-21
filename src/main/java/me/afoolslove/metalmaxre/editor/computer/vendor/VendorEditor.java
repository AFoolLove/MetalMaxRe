package me.afoolslove.metalmaxre.editor.computer.vendor;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
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
 *
 * TODO 商品价格
 *
 * @author AFoolLove
 */
public class VendorEditor extends AbstractEditor {
    /**
     * 售货机商品的最大组合数量
     */
    public static final int VENDOR_MAX_COUNT = 0x12;

    private final List<VendorGoods> vendorGoods = new ArrayList<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        buffer.position(0x23EC8);

        byte[] items = new byte[0x06];
        byte[] counts = new byte[0x06];
        while (buffer.get() == 0x0D) {
            // 读取商品
            buffer.get(items);
            // 读取商品数量和是否有货
            buffer.get(counts);

            VendorGoods goods = new VendorGoods();
            // 添加商品
            for (int i = 0; i < 0x06; i++) {
                goods.add(new VendorGood(items[i], counts[i]));
            }
            // 获取中奖物品
            goods.setAward(buffer.get());
            vendorGoods.add(goods);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        buffer.position(0x23EC8);

        // 移除多余的商品组合
        Iterator<VendorGoods> iterator = vendorGoods.iterator();
        while (vendorGoods.size() > VENDOR_MAX_COUNT) {
            if (iterator.hasNext()) {
                VendorGoods vendorGood = iterator.next();
                iterator.remove();
                System.out.printf("售货机编辑器：移除多余的售货机商品组 %s", vendorGood);
            }
        }

        byte[] items = new byte[0x06];
        byte[] counts = new byte[0x06];
        while (iterator.hasNext()) {
            VendorGoods vendorGood = iterator.next();

            // 写入固定头
            buffer.put((byte) 0x0D);
            // 写入商品和数量
            for (int i = 0; i < 0x06; i++) {
                VendorGood good = vendorGood.get(i);
                items[i] = good.item;
                counts[i] = good.count;
            }
            buffer.put(items);
            buffer.put(counts);
            // 写入奖品
            buffer.put(vendorGood.award);
        }
        return true;
    }

    public List<VendorGoods> getVendorGoods() {
        return vendorGoods;
    }

    public VendorGoods getVendorGood(int vendor) {
        return vendorGoods.get(vendor % 6);
    }
}
