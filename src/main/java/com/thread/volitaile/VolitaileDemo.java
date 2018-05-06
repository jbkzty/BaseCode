package com.thread.volitaile;

/**
 * 发起20个线程，每个线程对race变量进行10000次自增操作，如果并发正确的话，结果应该是200000
 *
 * @author spuerKun
 * @date 18/1/1.
 */
public class VolitaileDemo {

    public static volatile int race = 0;

    public static void increase() {
        race++;
    }

    private static final int THREAD_NUM = 20;

    public static void main(String[] args) {

        Thread[] threads = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i] = new Thread(() -> {
                for (int x = 0; x < 10000; x++) {
                    increase();
                }
            });
            threads[i].start();
        }

        try {
            Thread.sleep(2000);
            System.out.println(race);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
