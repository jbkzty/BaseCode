package com.redis.redlock.lock;

import com.redis.redlock.connection.RedisClient;
import com.redis.redlock.pubsub.Pubsub;
import com.redis.redlock.pubsub.PubsubEntry;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author superKun
 * @date 2018/5/6.
 */
public class RLockImpl implements RLock {

    private static final String C_PREFIX = "REDLOCK.CHANNEL.";

    private static final String K_PREFIX = "REDLOCK.";

    private RedisClient redisClient;

    private static Pubsub PUBSUB = new Pubsub();

    private String key;

    private String id;

    private String channel;

    /**
     * 分布式锁的默认锁定时间为30s,确保未来一段时间内
     */
    private static final int INTERNAL_LOCK_LEASE_TIME = 300;

    private Map<String, Future<?>> EXPIRATION_RENEWAL = new ConcurrentHashMap<>();

    public RLockImpl(String id, String name, RedisClient client) {
        this.id = id;
        this.key = K_PREFIX + name;
        this.channel = C_PREFIX + name;
        this.redisClient = client;
    }

    @Override
    public boolean tryLock(long leaseTime) {
        Long ttl = tryAcuqire(leaseTime);
        return ttl == null;
    }

    /**
     * (1) 首先尝试获取锁，返回结果是已存在的锁的剩余存活时间，为null则说明没有已存在的锁，并且成功获得了锁
     * (2) 如果获取到了锁，直接返回，直接执行业务逻辑
     * (3) 如果没有获取到锁，便需要等到锁的释放，通过Redis的channel订阅锁释放的消息
     * (4) 订阅锁的释放消息成功之后，进入一个不断重试获取锁的循环，循环中每次都先试着获取锁，并且的到已存在锁的剩余存活时间
     * (5) 如果在重试中拿到了锁，便结束循环
     * (6) 如果锁当前是被占用的，那么等待释放锁的消息，具体实现使用了JDK并发的信号量工具Semaphore来阻塞线程
     * 当锁被释放并发布释放锁的消息之后，信号量release()方法会被调用，此时被信号量阻塞的等待队列中的一个线程就可以继续尝试获取锁了
     * (7) 在成功获取到锁之后，就没必要继续订阅锁的释放消息，因此要取消对Redis上相应的channel订阅
     *
     * @param leaseTime
     * @throws InterruptedException
     */
    @Override
    public void lock(long leaseTime) throws InterruptedException {
        // 1.尝试获取锁
        Long ttl = tryAcuqire(leaseTime);

        // 2. 获得锁成功
        if (ttl == null) {
            return;
        }

        /**
         * 3. 等待锁释放，并且订阅锁
         * 利用redis的pub/sub机制监听一个channel,在监听到锁被释放之后尝试重新加锁
         * 或者返回失败让客户端执行下一个操作
         */
        PubsubEntry pubsubEntry = PUBSUB.subscribe(channel);
        redisClient.subscribe(channel, pubsubEntry.getPubSub());

        try {
            while (true) {
                // 再一次尝试一个申请锁
                ttl = tryAcuqire(leaseTime);
                // 获得锁并且返回
                if (ttl == null) {
                    return;
                }
                if (ttl > 0) {
                    /**
                     * 通过信号量(共享锁)阻塞，等待解锁消息
                     */
                    pubsubEntry.getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS);
                } else {
                    pubsubEntry.getLatch().tryAcquire(100, TimeUnit.MILLISECONDS);
                }
            }
        } finally {
            // 无论是否获得锁,都要取消订阅解锁消息
            if (pubsubEntry.getPubSub().isSubscribed()) {
                PUBSUB.unsubscribe(pubsubEntry.getPubSub());
            }
        }
    }

    /**
     * 释放锁
     * (1) 如果key不存在，说明锁已经被释放，直接执行publish命令发布释放锁消息并且返回1
     * (2) 如果key存在，但是field在hash中不存在，说明自己不是锁持有者，无权释放锁，返回nil
     * (3) 因为锁可重入，所以释放的时候不能把所有的锁全部释放，一次只能释放一把锁，因此执行hincrby对锁的值减一
     * (4) 释放一把锁后，如果还有剩余的锁，则刷新锁的失效时间并返回0；如果刚才释放的已经是最后一把锁，则执行 del 命令删除锁的 key，并发布锁释放消息，返回 1。
     */
    @Override
    public void unlock() {
        Object ret = redisClient.eval("if (redis.call('exists', KEYS[1]) == 0) then " +
                        "redis.call('publish', KEYS[2], ARGV[1]); " +
                        "return 1; " +
                        "end;" +
                        "if (redis.call('hexists', KEYS[1], ARGV[2]) == 0) then " +
                        "return nil;" +
                        "end; " +
                        "local counter = redis.call('hincrby', KEYS[1], ARGV[2], -1); " +
                        "if (counter > 0) then " +
                        "return 0; " +
                        "else " +
                        "redis.call('del', KEYS[1]); " +
                        "redis.call('publish', KEYS[2], ARGV[1]); " +
                        "return 1; " +
                        "end; ",
                Arrays.asList(key, channel), Pubsub.UNLOCK_MESSAGE, getValue());
        if (ret == null) {
            throw new IllegalMonitorStateException("Not locked by current thread, node id: " + id + " thread-id: " + Thread.currentThread().getId());
        }
        cancelExpirationRenewal();
    }

    private void cancelExpirationRenewal() {
        Future<?> future = EXPIRATION_RENEWAL.remove(getValue());
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override
    public void forceUnlock() {
        Object ret = redisClient.eval("if (redis.call('exists', KEYS[1]) == 0) then " +
                        "redis.call('publish', KEYS[2], ARGV[1]); " +
                        "return 1; " +
                        "end;" +
                        "if (redis.call('hexists', KEYS[1], ARGV[2]) == 0) then " +
                        "return nil;" +
                        "end; " +
                        "redis.call('del', KEYS[1]); " +
                        "redis.call('publish', KEYS[2], ARGV[1]); " +
                        "return 1;",
                Arrays.asList(key, channel), Pubsub.UNLOCK_MESSAGE, getValue());
        if (ret == null) {
            throw new IllegalMonitorStateException("Not locked by current thread, node id: " + id + " thread-id: " + Thread.currentThread().getId());
        }
        cancelExpirationRenewal();
    }

    @Override
    public int getHoldCount() {
        String ret = redisClient.hGet(key, getValue());
        if (ret == null) {
            return 0;
        }
        return Integer.parseInt(ret);
    }

    @Override
    public boolean isLocked() {
        return redisClient.exists(key);
    }

    public String getValue() {
        return id + ":" + Thread.currentThread().getId();
    }

    /**
     * 尝试加锁
     * 支持锁的重入
     * KEYS[1] : 需要加锁的key,这边需要的是字符串类型
     * ARGV[1] : 锁的超时时间，防止死锁
     * ARGV[2] : 锁的唯一标识，uuid
     * step1: 检查是否key已经被占用，如果没有则设置超时时间和唯一标识，初始化value=1
     * step2: 如果锁重入,需要判断锁的key field 都一直情况下 value 加一
     * step3: 返回剩余的过期时间
     *
     * @param leaseTime
     * @return null - 表示成功获取到锁，Long - 表示需要等到的时间
     */
    private Long tryAcuqire(long leaseTime) {


        /**
         * (1) 如果通过 exists 命令发现当前 key 不存在，即锁没被占用，则执行 hset 写入 Hash 类型数据
         *     key:全局锁名称（例如共享资源ID）, field:锁实例名称（Redisson客户端ID:线程ID）, value:1，并执行 pexpire 对该 key 设置失效时间
         *     返回空值 nil，至此获取锁成功
         * (2) 如果通过 hexists 命令发现 Redis 中已经存在当前 key 和 field 的 Hash 数据
         *     说明当前线程之前已经获取到锁，因为这里的锁是可重入的，则执行 hincrby 对当前 key field 的值加一，并重新设置失效时间，返回空值，至此重入获取锁成功
         * (3) 最后是锁已被占用的情况，即当前 key 已经存在，但是 Hash 中的 Field 与当前值不同，则执行 pttl 获取锁的剩余存活时间并返回，至此获取锁失败。
         */
        String ret;
        if (leaseTime > 0) {
            ret = redisClient.eval("if (redis.call('exits',KEYS[1]) == 0) then " +
                    "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                    "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                    "return nil; " +
                    "end; " +
                    "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                    "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                    "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                    "return nil; " +
                    "end; " +
                    "return redis.call('pttl', KEYS[1]);", Arrays.asList(key), String.valueOf(leaseTime), getValue());
        } else {
            ret = redisClient.eval("if (redis.call('exits',KEYS[1]) == 0) then " +
                    "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                    "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                    "return nil; " +
                    "end; " +
                    "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                    "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                    "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                    "return nil; " +
                    "end; " +
                    "return redis.call('pttl', KEYS[1]);", Arrays.asList(key), String.valueOf(INTERNAL_LOCK_LEASE_TIME), getValue());

            // 成功申请到锁，开始一个调度任务
            scheduleExpirationRenewal();
        }
        return ret != null ? Long.parseLong(ret) : null;
    }

    /**
     * 在成功获取到锁的情况下，为了避免业务中对共享资源的操作未完成，锁就被释放掉了，需要定期(锁失效时间的三分之一)刷新锁失效的时间
     * 除非被unlock的cancleTask方法触发，否则会一直循环重置过期时间
     */
    private void scheduleExpirationRenewal() {
        // 保证任务不会被重复创建
        if (EXPIRATION_RENEWAL.containsKey(getValue())) {
            return;
        }

        // 添加一个netty的Timeout回调任务，每（internalLockLeaseTime / 3）毫秒执行一次
        // 这边使用的是调度线程池执行
        // 异步调用redis的pexpire命令，重新设置过期时间
        Future<?> future = redisClient.schedule(INTERNAL_LOCK_LEASE_TIME / 3, INTERNAL_LOCK_LEASE_TIME / 3, TimeUnit.MICROSECONDS,
                "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                        "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                        "return 1; " +
                        "end; " +
                        "return 0;",
                Arrays.asList(key), String.valueOf(INTERNAL_LOCK_LEASE_TIME), getValue());

        EXPIRATION_RENEWAL.put(getValue(), future);
    }

    @Override
    public String toString() {
        return "RLockImpl{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
