package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.editors.list.ItemValue;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;

/**
 * 项目清单
 * <p>
 * 长度 + 项目*数量
 *
 * @author AFoolLove
 */
public class ItemList<I> extends LinkedList<I> {

    public I getItem(int index) {
        return get(index);
    }

    public Class<I> getItemClass() {
        @SuppressWarnings("unchecked")
        Class<I> genericType = (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return genericType;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 + size());
        // 写入长度
        outputStream.write((byte) size());
        // 写入项目
        for (I i : this) {
            if (i instanceof ItemValue itemValue) {
                outputStream.write(itemValue.getItem());
            }
        }
        return outputStream.toByteArray();
    }
}
