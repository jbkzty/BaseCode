package com.base.redis;

import com.redis.redlock.RedLock;
import com.redis.redlock.lock.RLock;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author spuerKun
 * @date 2018/5/6.
 */
public class RLockTest extends TestCase {

    private RedLock redLock;

    private RLock lock;

    @Override
    public void setUp() {
        redLock = RedLock.create();
        lock = redLock.getLock("test");
    }

    @Test
    public void testLock() throws InterruptedException {
        Assert.assertFalse(lock.isLocked());
        lock.lock();
        lock.lock();
        Assert.assertTrue(lock.isLocked());
        lock.unlock();
        Assert.assertTrue(lock.isLocked());
        lock.unlock();
        Assert.assertFalse(lock.isLocked());
    }
}
