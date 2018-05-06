package com.netty.chapter02.server;

/**
 * @author spuerKun
 * @date 2018/3/11.
 */
public class ClassDemo {

    static class Parent {
        static {
            A = 2;
        }

        static int A = 1;
    }

    static class Sub extends Parent {
        public static int B = A;
    }

    public static void main(String[] args) {
        System.out.println(Sub.B);
    }
}
