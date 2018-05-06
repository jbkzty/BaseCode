package com.jdk8.map;

/**
 * @author spuerKun
 * @date 2018/3/8.
 */
public class MapTransfer {

    public void transfer(Entry[] oldTable, Entry[] newTable) {

        // src引用旧的Entry数组
        Entry[] src = oldTable;

        int newCapacity = newTable.length;

        for (int j = 0; j < oldTable.length; j++) {
            // 取得旧Entry数组的每个元素
            Entry entry = src[j];
            if (entry != null) {
                // 释放旧Entry数组的对象引用
                src[j] = null;
                do {
                    Entry next = entry.next;

                } while (entry != null);
            }
        }
    }

    final static class Entry {
        final int key;
        final String value;
        Entry next;

        public Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
