package com.thread.threadLocal;

/**
 * @author spuerKun
 * @date 18/1/13.
 */
public class Run {

    public static void main(String[] args) {
        try {
            ThreadA threadA = new ThreadA();
            threadA.start();

            Thread.sleep(1000);

            ThreadB threadB = new ThreadB();
            threadB.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
