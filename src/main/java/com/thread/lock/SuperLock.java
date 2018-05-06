package com.thread.lock;

/**
 * @author spuerKun
 * @date 17/11/30.
 */
public abstract class SuperLock {

    public synchronized void doTest() {
        System.out.println("父类的锁操作");
    }

}
