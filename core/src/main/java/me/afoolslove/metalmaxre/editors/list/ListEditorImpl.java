package me.afoolslove.metalmaxre.editors.list;

import me.afoolslove.metalmaxre.InteractType;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ItemList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 清单编辑器
 *
 * @author AFoolLove
 */
public class ListEditorImpl extends RomBufferWrapperAbstractEditor implements IListEditor {
    public static final Logger LOGGER = LoggerFactory.getLogger(ListEditorImpl.class);
    /**
     * 清单数据索引
     */
    public static final String LIST_INDEX_ADDRESS = "listIndex";
    /**
     * 清单数据
     */
    public static final String LIST_ADDRESS = "list";

    private final Map<InteractType, List<ItemList<ItemValue>>> typeLists = new HashMap<>();

    public ListEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x23CA5 - 0x10, 0x23CC4 - 0x10),
                DataAddress.fromPRG(0x23CC5 - 0x10, 0x2400F - 0x10));
    }

    public ListEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                          @NotNull DataAddress listIndexAddress,
                          @NotNull DataAddress listAddress) {
        super(metalMaxRe);
        putDataAddress(LIST_INDEX_ADDRESS, listIndexAddress);
        putDataAddress(LIST_ADDRESS, listAddress);
    }

    @Editor.Load
    public void onLoad() {
        getTypeLists().clear();

        Map<Integer, byte[]> listData = new HashMap<>();
        loadIndexedData(LIST_INDEX_ADDRESS, LIST_ADDRESS, listData, 0x10);
        for (Map.Entry<Integer, byte[]> entry : listData.entrySet()) {
            InteractType interactType = InteractType.values()[entry.getKey()];
            byte[] value = entry.getValue();

            List<ItemList<ItemValue>> itemLists = getTypeLists().computeIfAbsent(interactType, k -> new ArrayList<>());
            itemLists.addAll(loadByteArray(value, interactType));
        }
    }


    /**
     * 从字节数组中加载多个物品清单
     * <p>
     * 格式：数量+数量*1字节
     */
    private static List<ItemList<ItemValue>> loadByteArray(byte[] bytes, @Nullable InteractType type) {
        if (type == null) {
            type = InteractType.TANK_EQUIPMENT; // 改成啥都行，只要不是下面特判的类型
        }
        List<ItemList<ItemValue>> list = new ArrayList<>();

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        while (buffer.hasRemaining()) {
            int len = buffer.get() & 0xFF;
            if (len == 0xFF) {
                break;
            }
            if (len == 0) {
                continue;
            }
            ItemList<ItemValue> objects = switch (type) {
                case VENDING_MACHINE_ITEM, VENDING_MACHINE_SHELL, VENDING_MACHINE_SP -> {
                    VendorItemList ls = new VendorItemList();
                    for (int j = 0; j < 6; j++) {
                        ls.add(new VendorItem(buffer.get(), (byte) 0));
                    }
                    for (int j = 0; j < 6; j++) {
                        ls.get(j).setRawCount(buffer.get());
                    }
                    ls.setAward(len == 0x0C ? null : buffer.get());
                    yield (ItemList) ls;
                }
                default -> {
                    ItemList<ItemValue> ls = new ItemList<>();
                    for (int j = 0; j < len; j++) {
                        ls.add(new ItemValue(buffer.get()));
                    }
                    yield ls;
                }
            };
            list.add(objects);
        }
        return list;
    }

    @Editor.Apply
    public void onApply() {
        Map<Integer, byte[]> listData = new HashMap<>();
        for (InteractType listType : InteractType.getListTypes()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<ItemList<ItemValue>> typeList = getTypeList(listType);
            for (ItemList<ItemValue> itemValues : typeList) {
                out.writeBytes(itemValues.toByteArray());
            }
            listData.put(listType.ordinal(), out.toByteArray());
        }
        int end = applyIndexedData(LIST_INDEX_ADDRESS, LIST_ADDRESS, listData, 0x10, (byte) 0xFF);
        if (end >= 0) {
            LOGGER.info("清单编辑器：剩余{}个空闲字节", end);
        } else {
            LOGGER.error("清单编辑器：数据错误！超出了数据上限{}字节", -end);
        }
    }

    @Override
    public Map<InteractType, List<ItemList<ItemValue>>> getTypeLists() {
        return this.typeLists;
    }
}
