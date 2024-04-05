package me.afoolslove.metalmaxre.utils;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;

/**
 * 物品清单
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
}
