package com.redis.redlock;

import com.redis.redlock.connection.RedisClient;
import com.redis.redlock.connection.RedisSingle;
import com.redis.redlock.lock.RLock;
import com.redis.redlock.lock.RLockImpl;

import java.util.UUID;

/**
 * @author superKun
 * @date 2018/5/6.
 */
public class RedLock {

    private static final String THREAD_UUID = UUID.randomUUID().toString();

    private RedisClient redisClient;

    public RedLock(String host, int port, String password) {
        this.redisClient = new RedisSingle(host, port, password);
    }

    public static RedLock create() {
        return create("127.0.0.1", 6379, null);
    }

    public static RedLock create(String host, int port, String password) {
        RedLock redLock = new RedLock(host, port, password);
        return redLock;
    }

    public RLock getLock(String name) {
        RLockImpl rLock = new RLockImpl(THREAD_UUID, name, redisClient);
        return rLock;
    }

    public void shutdown() {
        this.redisClient.close();
    }
}
