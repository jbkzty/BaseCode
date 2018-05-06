package com.thread.threadLocal;

/**
 * @author spuerKun
 * @date 18/1/15.
 */
public class Demo {

    private static ThreadLocal<String> threadLocal1 = new ThreadLocal<>();
    private static ThreadLocal<String> threadLocal2 = new ThreadLocal<>();

    public static void main(String[] args) {
        threadLocal1.set("aaa");
        threadLocal2.set("ccc");

        System.out.println(threadLocal1.get());
        System.out.println(threadLocal2.get());
    }
}
