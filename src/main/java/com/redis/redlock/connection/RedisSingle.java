package com.redis.redlock.connection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author superKun
 * @date 2018/5/5.
 */
public class RedisSingle implements RedisClient {

    private JedisPool jedisPool1;

    private JedisPool jedisPool2;

    private ExecutorService executorService;

    private ScheduledExecutorService scheduledExecutorService;

    public RedisSingle(String host, int port, String password) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(255);
        jedisPool1 = new JedisPool(config, host, port, 2000, password);
        jedisPool2 = new JedisPool(config, host, port, 2000, password);
        executorService = new ThreadPoolExecutor(10, 100, 10, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<>());
        scheduledExecutorService = new ScheduledThreadPoolExecutor(20);
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        try (Jedis jedis = jedisPool1.getResource()) {
            return jedis.set(key, value, nxxx, expx, time);
        }
    }

    @Override
    public String set(String key, String value, String nxxx) {
        try (Jedis jedis = jedisPool1.getResource()) {
            return jedis.set(key, value, nxxx);
        }
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = jedisPool1.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public boolean exists(String key) {
        try (Jedis jedis = jedisPool1.getResource()) {
            return jedis.exists(key).booleanValue();
        }
    }

    @Override
    public String hGet(String key, String field) {
        try (Jedis jedis = jedisPool1.getResource()) {
            return jedis.hget(key, field);
        }
    }

    @Override
    public String eval(String script, List<String> keys, String... params) {
        try (Jedis jedis = jedisPool1.getResource()) {
            Object ret = jedis.eval(script, keys, Arrays.asList(params));
            if (ret == null) return null;
            return ret.toString();
        }
    }

    @Override
    public Future<?> schedule(long initDelay, long delay, TimeUnit timeUnit, String script, List<String> keys, String... params) {
        return scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try (Jedis jedis = jedisPool1.getResource()) {
                jedis.eval(script, keys, Arrays.asList(params));
            }
        }, initDelay, delay, timeUnit);
    }

    @Override
    public void subscribe(String channel, JedisPubSub listener) {
        executorService.execute(() -> {
            try (Jedis jedis = jedisPool2.getResource()) {
                jedis.subscribe(listener, channel);
            }
        });
    }

    @Override
    public void publish(String channel, String message) {
        try (Jedis jedis = jedisPool1.getResource()) {
            jedis.publish(channel, message);
        }
    }

    @Override
    public void close() {
        this.executorService.shutdown();
        this.scheduledExecutorService.shutdown();
        this.jedisPool1.close();
        this.jedisPool2.close();
    }
}
