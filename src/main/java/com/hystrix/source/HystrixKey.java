package com.hystrix.source;

/**
 * base class for hystrix keys
 *
 * @author spuerKun
 * @date 17/12/7.
 */
public interface HystrixKey {

    String name();

    abstract class HystrixKeyDefault implements HystrixKey {

        private final String name;

        public HystrixKeyDefault(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
