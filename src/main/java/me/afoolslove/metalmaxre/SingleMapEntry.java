package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * 单例Entry
 */
public class SingleMapEntry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public SingleMapEntry(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    public K setKey(K key) {
        K oldKey = this.key;
        this.key = key;
        return oldKey;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SingleMapEntry<?, ?> that)) {
            return false;
        }
        return Objects.equals(getKey(), that.getKey()) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getValue());
    }

    public static <K, V> SingleMapEntry<K, V> create(@Nullable K key, @Nullable V value) {
        return new SingleMapEntry<>(key, value);
    }
}
