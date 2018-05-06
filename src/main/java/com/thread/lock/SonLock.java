package com.thread.lock;

/**
 * @author spuerKun
 * @date 17/11/30.
 */
public class SonLock extends SuperLock {

    @Override
    public synchronized void doTest() {
        System.out.println("子类的锁操作");
        super.doTest();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000000000; i++) {
            SonLock sonLock = new SonLock();
            sonLock.doTest();
        }
    }
}
