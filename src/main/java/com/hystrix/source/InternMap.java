package com.hystrix.source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 每一个Key对应一个单例
 *
 * @author spuerKun
 * @date 17/12/7.
 */
public class InternMap<K, V> {

    private final ConcurrentMap<K, V> storage = new ConcurrentHashMap<K, V>();
    private final ValueConstructor<K, V> valueConstructor;

    public interface ValueConstructor<K, V> {
        V create(K key);
    }

    public InternMap(ValueConstructor<K, V> valueConstructor) {
        this.valueConstructor = valueConstructor;
    }

    public V interned(K key) {
        V existingKey = storage.get(key);
        V newKey = null;
        if (existingKey == null) {
            newKey = valueConstructor.create(key);
            existingKey = storage.putIfAbsent(key, newKey);
        }
        return existingKey != null ? existingKey : newKey;
    }

    public int size() {
        return storage.size();
    }


}
