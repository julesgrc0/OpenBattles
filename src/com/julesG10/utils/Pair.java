package com.julesG10.utils;

import java.util.Map;

// https://stackoverflow.com/a/3110644
public class Pair<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
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

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    public K setKey(K key) {
        K old = this.key;
        this.key = key;
        return old;
    }
}
