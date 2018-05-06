package com.hystrix.source;

/**
 * @author spuerKun
 * @date 17/12/7.
 */
public interface HystrixCommandGroupKey extends HystrixKey {

    class Factory {

        private Factory() {
        }

        private static final InternMap<String, HystrixCommandGroupDefault> intern = new InternMap<>(new InternMap.ValueConstructor<String, HystrixCommandGroupDefault>() {
            @Override
            public HystrixCommandGroupDefault create(String key) {
                return new HystrixCommandGroupDefault(key);
            }
        });

        public static HystrixCommandGroupKey asKey(String name) {
            return intern.interned(name);
        }

        private static class HystrixCommandGroupDefault extends HystrixKey.HystrixKeyDefault implements HystrixCommandGroupKey {
            public HystrixCommandGroupDefault(String name) {
                super(name);
            }
        }

        static int getGroupCount() {
            return intern.size();
        }
    }
}
