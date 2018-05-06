package com.redis.redlock.lock;

/**
 * @author superKun
 * @date 2018/5/5.
 */
public interface RLock {

    boolean tryLock(long leaseTime);

    void lock(long leaseTime) throws InterruptedException;

    void unlock();

    void forceUnlock();

    int getHoldCount();

    boolean isLocked();

    default boolean tryLock() throws InterruptedException {
        return tryLock(0);
    }

    default void lock() throws InterruptedException {
        lock(0);
    }
}
