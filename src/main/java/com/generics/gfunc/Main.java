package com.generics.gfunc;

/**
 * 泛型方法
 *
 * @author spuerKun
 * @date 17/11/15.
 */
public class Main {

    public static <T> void out(T t) {
        System.out.println(t);
    }

    public static <T> void print(T... args) {
        for (T t : args) {
            System.out.println(t);
        }
    }

    public static void main(String[] args) {
        out("findingsea");
        out(123);
        out(11.11);
        out(true);
        print("findingsea", 123, 11.11, true);
    }

}
