package com.generics.gclass;

/**
 * 泛型类
 * <p>
 * 在编译期间，是无法知道K,V具体是什么类型的，只有在运行的时候才会真正根据类型来构造和分配内存
 *
 * @author spuerKun
 * @date 17/11/15.
 */
public class Container<K, V> {

    private K key;

    private V value;

    public Container(K k, V v) {
        this.key = k;
        this.value = v;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
