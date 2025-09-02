package me.afoolslove.metalmaxre.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Entry由SingleMapEntry实现，可变更Key和Value的值
 *
 * @author AFoolLove
 */
public class ListSingleMap<K, V> extends ArrayList<SingleMapEntry<K, V>> {

    public ListSingleMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ListSingleMap(int initialCapacity, boolean create) {
        super(initialCapacity);
        if (create) {
            for (int i = 0; i < initialCapacity; i++) {
                add(SingleMapEntry.createEmpty());
                add(SingleMapEntry.createEmpty());
            }
        }
    }

    public ListSingleMap() {
    }

    public ListSingleMap(@NotNull Collection<? extends Map.Entry<K, V>> c) {
        super(c.stream().map(m -> {
                    if (m instanceof SingleMapEntry<K, V> e) {
                        return e;
                    } else {
                        return SingleMapEntry.create(m.getKey(), m.getValue());
                    }
                }
        ).toList());
    }

    public boolean add(K key, V value) {
        return add(SingleMapEntry.create(key, value));
    }
}
